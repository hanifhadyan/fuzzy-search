package han.org.repository;

import han.org.entity.BookSearchView;
import han.org.dto.BookSearchResponseDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookSearchRepository {

    private static final String BASE_QUERY = """
        FROM book_search_view
        WHERE 1=1
    """;

    private static final String FUZZY_SEARCH_QUERY = """
        (
            CASE
                WHEN book_code = :keyword THEN 10
                WHEN book_code ILIKE CONCAT('%', :keyword, '%') THEN 0.9
                ELSE SIMILARITY(book_code, :keyword)
            END +
            CASE
                WHEN CAST(release_year AS VARCHAR) = :keyword THEN 10
                WHEN CAST(release_year AS VARCHAR) ILIKE CONCAT('%', :keyword, '%') THEN 0.9
                ELSE SIMILARITY(CAST(release_year AS VARCHAR), :keyword)
            END +
            CASE
                WHEN book_title ILIKE CONCAT('%', :keyword, '%') THEN 0.9
                ELSE SIMILARITY(book_title, :keyword)
            END * 3 +
            CASE
                WHEN author_name ILIKE CONCAT('%', :keyword, '%') THEN 0.9
                ELSE SIMILARITY(author_name, :keyword)
            END * 2 +
            CASE
                WHEN publisher_name ILIKE CONCAT('%', :keyword, '%') THEN 0.9
                ELSE SIMILARITY(publisher_name, :keyword)
            END * 2 +
        ) AS ranking
    """;

    private static final String FUZZY_SEARCH_CONDITION = """
        SELECT * FROM ranked_books
        WHERE (
            :keyword IS NULL
            OR ranking >= 10
            OR (
                ranking > 1
                AND NOT EXISTS (
                    SELECT 1 FROM ranked_books rb
                    WHERE rb.ranking >= 10
                )
            )
        )
        ORDER BY
            CASE WHEN :keyword IS NOT NULL THEN ranking END DESC,
            book_title ASC
    """;

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public List<BookSearchResponseDto> searchBooks(String keyword, List<String> publishers, int limit, int offset) {
        Map<String, Object> params = prepareQueryParameters(keyword);
        String queryString = buildSearchQuery(publishers, params);

        Query query = createAndConfigureQuery(queryString, "BookNativeQueryMapping", params, limit, offset);

        return (List<BookSearchResponseDto>) query.getResultList();
    }

    @Transactional
    public long countBooks(String keyword, List<String> publishers) {
        Map<String, Object> params = prepareQueryParameters(keyword);
        String queryString = buildCountQuery(publishers, params);

        Query query = createAndConfigureQuery(queryString, "long", params, 0, 0);
        return (long) query.getSingleResult();
    }

    private Query createAndConfigureQuery(String queryString, String resultSetMapping, Map<String, Object> params, int limit, int offset) {
        Query query;
        if (resultSetMapping.equals("long")) {
            query = entityManager.createNativeQuery(queryString, long.class);
        } else {
            query = entityManager.createNativeQuery(queryString, resultSetMapping);
        }

        if (limit > 0 && offset >= 0) {
            query.setMaxResults(limit);
            query.setFirstResult(offset);
        }

        if (!params.isEmpty()) {
            params.forEach(query::setParameter);
        }

        return query;
    }

    private String buildSearchQuery(List<String> publishers, Map<String, Object> params) {
        StringBuilder queryBuilder = new StringBuilder("""
            WITH ranked_books AS(
                SELECT *,
        """)
        .append(FUZZY_SEARCH_QUERY)
        .append(BASE_QUERY);

        appendPublisherFilter(queryBuilder, publishers, params);
        return queryBuilder.append(")")
                .append(FUZZY_SEARCH_CONDITION)
                .toString();
    }

    private String buildCountQuery(List<String> publishers, Map<String, Object> params) {
        StringBuilder queryBuilder = new StringBuilder("""
            WITH ranked_books AS (
                SELECT *,
        """)
        .append(FUZZY_SEARCH_QUERY)
        .append(BASE_QUERY);;

        appendPublisherFilter(queryBuilder, publishers, params);
        return queryBuilder
                .append("""
                    )
                    SELECT COUNT(*) FROM ranked_books
                    WHERE (
                        :keyword IS NULL
                        OR ranking >= 10
                        OR (
                            ranking > 1
                            AND NOT EXISTS (
                                SELECT 1 FROM ranked_books rb
                                WHERE rb.ranking >= 10
                            )
                        )
                    )
                """)
                .toString();
    }

    private static Map<String, Object> prepareQueryParameters(String keyword) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isNotBlank(keyword)) {
            keyword = StringUtils.EMPTY;
        }
        params.put("keyword", keyword);
        return params;
    }

    private void appendPublisherFilter(StringBuilder queryBuilder, List<String> publishers, Map<String, Object> params) {
        if (publishers != null && !publishers.isEmpty()) {
            queryBuilder.append(" AND publisher_name IN :publishers");
            params.put("publishers", publishers);
        }
    }

    public void refreshMaterializedView() {
        entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW book_search_view").executeUpdate();
    }
}
