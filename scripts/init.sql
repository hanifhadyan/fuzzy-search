-- Enable pg_trgm extension for fuzzy search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Create user and database
CREATE USER fuzzy_user WITH PASSWORD 'fuzzy_password';
CREATE DATABASE fuzzy_test_db OWNER fuzzy_user;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE fuzzy_test_db TO fuzzy_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO fuzzy_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO fuzzy_user;
