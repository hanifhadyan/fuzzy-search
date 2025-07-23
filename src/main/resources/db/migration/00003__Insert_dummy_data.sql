-- Insert dummy authors
INSERT INTO authors (author_name) VALUES
    ('J.K. Rowling'),
    ('George R.R. Martin'),
    ('Stephen King'),
    ('J.R.R. Tolkien'),
    ('Agatha Christie'),
    ('Dan Brown'),
    ('Isaac Asimov'),
    ('Arthur C. Clarke'),
    ('Terry Pratchett'),
    ('Neil Gaiman');

-- Insert dummy publishers
INSERT INTO publishers (publisher_name) VALUES
    ('Bloomsbury Publishing'),
    ('Bantam Books'),
    ('Scribner'),
    ('HarperCollins'),
    ('Penguin Random House'),
    ('Simon & Schuster'),
    ('Macmillan'),
    ('Oxford University Press'),
    ('Cambridge University Press'),
    ('Doubleday');

-- Insert dummy categories
INSERT INTO categories (category_name) VALUES
    ('Fantasy'),
    ('Science Fiction'),
    ('Mystery'),
    ('Horror'),
    ('Adventure'),
    ('Romance'),
    ('Thriller'),
    ('Classic Literature'),
    ('Young Adult'),
    ('Biography');

-- Insert dummy books
INSERT INTO books (book_code, book_title, release_year, publisher_id, author_id, summary) VALUES
    ('HP001', 'Harry Potter and the Philosopher''s Stone', 1997, 1, 1, 'A young wizard discovers his magical heritage and attends Hogwarts School of Witchcraft and Wizardry.'),
    ('GOT001', 'A Game of Thrones', 1996, 2, 2, 'Noble families vie for control of the Iron Throne in the Seven Kingdoms of Westeros.'),
    ('SK001', 'The Shining', 1977, 3, 3, 'A family becomes winter caretakers at an isolated hotel where supernatural forces influence the father.'),
    ('LOTR001', 'The Lord of the Rings: The Fellowship of the Ring', 1954, 4, 4, 'A hobbit embarks on a quest to destroy a powerful ring and save Middle-earth.'),
    ('AC001', 'Murder on the Orient Express', 1934, 5, 5, 'Detective Hercule Poirot investigates a murder aboard the famous Orient Express train.'),
    ('DB001', 'The Da Vinci Code', 2003, 6, 6, 'A symbologist uncovers a conspiracy involving the Holy Grail and secret societies.'),
    ('IA001', 'Foundation', 1951, 7, 7, 'A mathematician develops psychohistory to predict the future of galactic civilization.'),
    ('ACC001', 'Childhood''s End', 1953, 8, 8, 'Alien overlords arrive on Earth and guide humanity toward its next evolutionary step.'),
    ('TP001', 'The Colour of Magic', 1983, 9, 9, 'The first adventure in the Discworld series featuring the inept wizard Rincewind.'),
    ('NG001', 'Good Omens', 1990, 10, 10, 'An angel and a demon work together to prevent the apocalypse.');

-- Insert book categories relationships
INSERT INTO book_categories (book_code, category_id) VALUES
    ('HP001', 1), ('HP001', 9),  -- Fantasy, Young Adult
    ('GOT001', 1), ('GOT001', 5),  -- Fantasy, Adventure
    ('SK001', 4), ('SK001', 7),  -- Horror, Thriller
    ('LOTR001', 1), ('LOTR001', 8),  -- Fantasy, Classic Literature
    ('AC001', 3), ('AC001', 8),  -- Mystery, Classic Literature
    ('DB001', 3), ('DB001', 7),  -- Mystery, Thriller
    ('IA001', 2), ('IA001', 8),  -- Science Fiction, Classic Literature
    ('ACC001', 2), ('ACC001', 8),  -- Science Fiction, Classic Literature
    ('TP001', 1), ('TP001', 2),  -- Fantasy, Science Fiction
    ('NG001', 1), ('NG001', 2);  -- Fantasy, Science Fiction

-- Refresh the materialized view
REFRESH MATERIALIZED VIEW book_search_view;
