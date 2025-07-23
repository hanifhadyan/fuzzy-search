# Fuzzy Search Book Application - Full Stack

A full-stack application with Quarkus backend and React frontend that provides fuzzy search functionality for books using PostgreSQL's trigram extension. The application allows you to search for books by title, code, author, publisher, and year with typo tolerance and similarity scoring.

## Features

- **Fuzzy Search**: Search with typo tolerance using PostgreSQL's pg_trgm extension
- **Multiple Search Types**: Search by title, code, author, publisher, year, or all fields
- **Similarity Scoring**: Shows how similar the search results are to your query
- **Modern UI**: React frontend with Material-UI components
- **Real-time Search**: Interactive search with instant results
- **Materialized View**: Optimized search performance with pre-computed search data
- **REST API**: RESTful endpoints for integration with other applications
- **CLI Interface**: Command-line interface for testing and direct interaction
- **Full Stack**: Complete solution with backend API and frontend interface

## Technology Stack

### Backend
- **Java 21**: Modern Java with latest features
- **Quarkus**: Supersonic Subatomic Java framework
- **PostgreSQL**: Database with trigram extension for fuzzy search
- **Flyway**: Database migration management
- **Hibernate ORM**: Object-relational mapping

### Frontend
- **React 18**: Modern React with hooks
- **Material-UI**: Beautiful and responsive UI components
- **Axios**: HTTP client for API calls
- **React Router**: Client-side routing

### Infrastructure
- **Docker**: Containerization for easy deployment
- **Docker Compose**: Multi-container orchestration

## Project Structure

```
├── build.gradle                    # Gradle build configuration
├── docker-compose.yml              # Docker services configuration
├── .env                            # Environment variables
├── scripts/                        # Utility scripts
│   ├── init.sql                   # Database initialization
│   ├── start-dev.sh               # Start backend only
│   ├── start-cli.sh               # Start CLI application
│   ├── start-fullstack.sh         # Start full stack application
│   ├── stop-fullstack.sh          # Stop full stack application
│   └── stop.sh                    # Stop services
├── src/main/java/han/org/          # Backend Java code
│   ├── entity/                    # JPA entities
│   ├── dto/                       # Data Transfer Objects
│   ├── repository/                # Data access layer
│   ├── service/                   # Business logic layer
│   ├── resource/                  # REST endpoints
│   └── cmd/                       # CLI application
├── src/main/resources/             # Backend resources
│   ├── application.properties     # Application configuration
│   └── db/migration/              # Flyway migrations
└── frontend/                      # React frontend
    ├── package.json               # Frontend dependencies
    ├── public/                    # Static assets
    └── src/                       # React source code
        ├── components/            # React components
        └── services/              # API services
```

## Quick Start - Full Stack

### Prerequisites

- Java 21 or higher
- Node.js 18 or higher
- Docker and Docker Compose
- Git

### Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd fuzzy-search
   ```

2. **Start the full stack application**
   ```bash
   ./scripts/start-fullstack.sh
   ```

   This will:
   - Start PostgreSQL container with trigram extension
   - Run database migrations
   - Install frontend dependencies
   - Start the Quarkus backend on port 8080
   - Start the React frontend on port 3000

3. **Access the application**
   - **Frontend UI**: http://localhost:3000
   - **Backend API**: http://localhost:8080
   - **Dev UI**: http://localhost:8080/q/dev

### Alternative: Backend Only

To run only the backend:
```bash
./scripts/start-dev.sh
```

### Alternative: CLI Mode

To run the command-line interface:
```bash
./scripts/start-cli.sh
```

## Frontend Features

The React frontend provides:

- **Interactive Search Interface**: Beautiful Material-UI components
- **Real-time Results**: Instant search as you type
- **Similarity Visualization**: Color-coded similarity scores
- **Advanced Filters**: Search type selection and similarity threshold
- **Responsive Design**: Works on desktop and mobile devices
- **Pagination**: Navigate through large result sets
- **Category Tags**: Visual representation of book categories

## API Documentation

### Search Books

**POST** `/api/books/search`

Request body:
```json
{
  "search_query": "harry potter",
  "search_type": "all",
  "min_similarity": 0.1,
  "page": 1,
  "size": 10
}
```

**GET** `/api/books/search?q=harry%20potter&type=title&minSimilarity=0.1&page=1&size=10`

Response:
```json
{
  "data": [
    {
      "book_code": "HP001",
      "book_title": "Harry Potter and the Philosopher's Stone",
      "release_year": 1997,
      "author_name": "J.K. Rowling",
      "publisher_name": "Bloomsbury Publishing",
      "categories": "Fantasy, Young Adult",
      "similarity": 0.85
    }
  ],
  "totalCount": 1,
  "totalPages": 1,
  "currentPage": 1,
  "pageSize": 10,
  "hasNext": false,
  "hasPrevious": false
}
```

### Search Types

- `all`: Search across all fields (default)
- `title`: Search by book title only
- `code`: Search by book code only
- `author`: Search by author name only
- `publisher`: Search by publisher name only
- `year`: Search by release year only

### Refresh Search Index

**POST** `/api/books/refresh-index`

Refreshes the materialized view for updated search results.

## Database Schema

The application uses the following tables:

- **books**: Main book information
- **authors**: Author details
- **publishers**: Publisher information
- **categories**: Book categories
- **book_categories**: Many-to-many relationship between books and categories
- **book_search_view**: Materialized view for optimized search

## Development

### Frontend Development

```bash
cd frontend
npm install
npm start
```

### Backend Development

```bash
./gradlew quarkusDev
```

### Running Tests

```bash
./gradlew test
cd frontend && npm test
```

### Building for Production

```bash
# Backend
./gradlew build

# Frontend
cd frontend
npm run build
```

## Configuration

### Environment Variables

```properties
# Database Configuration
DB_USER=fuzzy_user
DB_PASSWORD=fuzzy_password
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fuzzy_test_db
DB_URL=jdbc:postgresql://localhost:5432/fuzzy_test_db

# Frontend (optional)
REACT_APP_API_URL=http://localhost:8080
```

## Stopping the Application

### Stop Full Stack
```bash
./scripts/stop-fullstack.sh
```

### Stop with cleanup
```bash
./scripts/stop-fullstack.sh --clean-logs
./scripts/stop-fullstack.sh --clean-volumes
```

## Sample Data

The application comes with sample data including:
- 10 books from various genres
- 10 authors
- 10 publishers
- 10 categories

## Fuzzy Search Examples

Try these search queries to see the fuzzy search in action:

- "harry poter" (typo in "potter")
- "game throne" (partial match)
- "stephn king" (typo in "stephen")
- "sci-fi" (category search)
- "1997" (year search)

## Screenshots

### Main Search Interface
- Clean, modern interface with Material-UI
- Search type selector and similarity threshold slider
- Real-time search results with similarity scoring

### Search Results
- Card-based layout with book information
- Color-coded similarity scores
- Category tags and pagination

## Troubleshooting

### PostgreSQL Connection Issues

If you encounter database connection issues:

1. Check if Docker is running
2. Verify PostgreSQL container is up: `docker ps`
3. Check logs: `docker logs fuzzy_search_postgres`

### Frontend Not Loading

If the frontend doesn't start:

1. Check if Node.js is installed: `node --version`
2. Install dependencies: `cd frontend && npm install`
3. Check if port 3000 is available

### CORS Issues

If you encounter CORS errors:

1. Verify backend is running on port 8080
2. Check application.properties for CORS configuration
3. Ensure frontend is accessing the correct backend URL

### Migration Issues

If migrations fail:

1. Stop the application: `./scripts/stop-fullstack.sh`
2. Clean up: `./scripts/stop-fullstack.sh --clean-volumes`
3. Restart: `./scripts/start-fullstack.sh`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for both backend and frontend
5. Submit a pull request

## License

This project is licensed under the MIT License.
