-- Create materialized view for book search with all related information
CREATE MATERIALIZED VIEW book_search_view AS
SELECT
    b.book_code,
    b.book_title,
    b.release_year,
    b.summary,
    a.author_id,
    a.author_name,
    p.publisher_id,
    p.publisher_name,
    STRING_AGG(c.category_name, ', ') AS categories,
    -- Create searchable text for fuzzy search
    CONCAT(
        b.book_code, ' ',
        b.book_title, ' ',
        COALESCE(a.author_name, ''), ' ',
        COALESCE(p.publisher_name, ''), ' ',
        COALESCE(b.release_year::text, ''), ' ',
        COALESCE(STRING_AGG(c.category_name, ' '), '')
    ) AS searchable_text
FROM books b
LEFT JOIN authors a ON b.author_id = a.author_id
LEFT JOIN publishers p ON b.publisher_id = p.publisher_id
LEFT JOIN book_categories bc ON b.book_code = bc.book_code
LEFT JOIN categories c ON bc.category_id = c.category_id
GROUP BY b.book_code, b.book_title, b.release_year, b.summary,
         a.author_id, a.author_name, p.publisher_id, p.publisher_name;

-- Create index for the materialized view
CREATE INDEX idx_book_search_view_searchable_text ON book_search_view USING GIN (searchable_text gin_trgm_ops);
CREATE INDEX idx_book_search_view_title ON book_search_view USING GIN (book_title gin_trgm_ops);
CREATE INDEX idx_book_search_view_code ON book_search_view USING GIN (book_code gin_trgm_ops);
CREATE INDEX idx_book_search_view_author ON book_search_view USING GIN (author_name gin_trgm_ops);
CREATE INDEX idx_book_search_view_publisher ON book_search_view USING GIN (publisher_name gin_trgm_ops);
