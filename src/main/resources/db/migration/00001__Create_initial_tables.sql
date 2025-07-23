-- Enable pg_trgm extension for fuzzy search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Create authors table
CREATE TABLE authors (
    author_id SERIAL PRIMARY KEY,
    author_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create publishers table
CREATE TABLE publishers (
    publisher_id SERIAL PRIMARY KEY,
    publisher_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create books table
CREATE TABLE books (
    book_code VARCHAR(50) PRIMARY KEY,
    book_title VARCHAR(500) NOT NULL,
    release_year INTEGER,
    publisher_id INTEGER REFERENCES publishers(publisher_id),
    author_id INTEGER REFERENCES authors(author_id),
    summary TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create book_categories junction table
CREATE TABLE book_categories (
    book_code VARCHAR(50) REFERENCES books(book_code),
    category_id INTEGER REFERENCES categories(category_id),
    PRIMARY KEY (book_code, category_id)
);

-- Create indexes for better performance
CREATE INDEX idx_books_title_gin ON books USING GIN (book_title gin_trgm_ops);
CREATE INDEX idx_books_code_gin ON books USING GIN (book_code gin_trgm_ops);
CREATE INDEX idx_authors_name_gin ON authors USING GIN (author_name gin_trgm_ops);
CREATE INDEX idx_publishers_name_gin ON publishers USING GIN (publisher_name gin_trgm_ops);
CREATE INDEX idx_books_year ON books (release_year);
CREATE INDEX idx_books_publisher ON books (publisher_id);
CREATE INDEX idx_books_author ON books (author_id);
