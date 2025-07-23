import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

class BookSearchService {
  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  async searchBooks(searchParams) {
    try {
      const response = await this.api.get('/api/books/search', {
        params: searchParams
      });
      return response.data;
    } catch (error) {
      console.error('Search API error:', error);
      throw error;
    }
  }

  async searchBooksPost(searchRequest) {
    try {
      const response = await this.api.post('/api/books/search', searchRequest);
      return response.data;
    } catch (error) {
      console.error('Search POST API error:', error);
      throw error;
    }
  }

  async refreshSearchIndex() {
    try {
      const response = await this.api.post('/api/books/refresh-index');
      return response.data;
    } catch (error) {
      console.error('Refresh index API error:', error);
      throw error;
    }
  }

  // Health check endpoint
  async healthCheck() {
    try {
      const response = await this.api.get('/q/health');
      return response.data;
    } catch (error) {
      console.error('Health check API error:', error);
      throw error;
    }
  }
}

export default new BookSearchService();
