package han.org.service;

import han.org.dto.BookSearchRequestDto;
import han.org.dto.BookSearchResponseDto;
import han.org.repository.BookSearchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class BookSearchService {

    @Inject
    BookSearchRepository bookSearchRepository;

    public PageImpl<BookSearchResponseDto> search(BookSearchRequestDto request) {
        try {
            String keyword = (StringUtils.isEmpty(request.getKeyword()) ? sanitizeKeyword(request.getKeyword()) : null);
            List<String> publishers = request.getPublisher() != null ? List.of(request.getPublisher().trim().split(",")) : List.of();
            int limit = request.getLimit();
            int offset = request.getOffset();

            return getBooks(keyword, publishers, limit, offset);
        } catch (Exception e) {
            throw new RuntimeException("Error while searching for books", e);
        }
    }

    private PageImpl<BookSearchResponseDto> getBooks(String keyword, List<String> publishers, int limit, int offset) {
        Pageable pageable = Pageable.ofSize(limit).withPage(offset / limit);

        List<BookSearchResponseDto> listBooks = bookSearchRepository.searchBooks(keyword, publishers, limit, offset);
        if (listBooks == null || listBooks.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        long totalCount = bookSearchRepository.countBooks(keyword, publishers);

        return new PageImpl<>(listBooks, pageable, totalCount);
    }

    private String sanitizeKeyword(String keyword) {
        String trimmed = keyword.trim();
        return trimmed.replaceAll("[';\"\\\\]", "");
    }

    @Transactional
    public void refreshSearchIndex() {
        bookSearchRepository.refreshMaterializedView();
    }
}
