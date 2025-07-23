import React, { useState } from 'react';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
  Card,
  CardContent,
  Chip,
  Box,
  CircularProgress,
  Alert,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Pagination,
  Slider
} from '@mui/material';
import { Search as SearchIcon, Refresh as RefreshIcon } from '@mui/icons-material';
import axios from 'axios';

const BookSearch = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchType, setSearchType] = useState('all');
  const [minSimilarity, setMinSimilarity] = useState(0.1);
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState([]);
  const [pagination, setPagination] = useState({
    currentPage: 1,
    totalPages: 1,
    totalCount: 0,
    pageSize: 10
  });
  const [error, setError] = useState('');

  const searchTypes = [
    { value: 'all', label: 'All Fields' },
    { value: 'title', label: 'Title' },
    { value: 'code', label: 'Code' },
    { value: 'author', label: 'Author' },
    { value: 'publisher', label: 'Publisher' },
    { value: 'year', label: 'Year' }
  ];

  const handleSearch = async (page = 1) => {
    if (!searchQuery.trim()) {
      setError('Please enter a search query');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await axios.get('/api/books/search', {
        params: {
          q: searchQuery,
          type: searchType,
          minSimilarity: minSimilarity,
          page: page,
          size: pagination.pageSize
        }
      });

      setResults(response.data.data);
      setPagination({
        currentPage: response.data.currentPage,
        totalPages: response.data.totalPages,
        totalCount: response.data.totalCount,
        pageSize: response.data.pageSize
      });
    } catch (err) {
      setError('Search failed. Please try again.');
      console.error('Search error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleRefreshIndex = async () => {
    setLoading(true);
    try {
      await axios.post('/api/books/refresh-index');
      setError('');
      // Show success message
      setTimeout(() => setError(''), 3000);
    } catch (err) {
      setError('Failed to refresh index');
      console.error('Refresh error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (event, value) => {
    handleSearch(value);
  };

  const getSimilarityColor = (similarity) => {
    if (similarity >= 0.8) return 'success';
    if (similarity >= 0.5) return 'warning';
    return 'error';
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom align="center">
        📚 Fuzzy Book Search
      </Typography>

      <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Search Books"
              variant="outlined"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              placeholder="Try typing with typos: 'harry poter', 'game throne'..."
            />
          </Grid>

          <Grid item xs={12} md={3}>
            <FormControl fullWidth>
              <InputLabel>Search Type</InputLabel>
              <Select
                value={searchType}
                label="Search Type"
                onChange={(e) => setSearchType(e.target.value)}
              >
                {searchTypes.map((type) => (
                  <MenuItem key={type.value} value={type.value}>
                    {type.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>

          <Grid item xs={12} md={3}>
            <Button
              fullWidth
              variant="contained"
              startIcon={<SearchIcon />}
              onClick={() => handleSearch()}
              disabled={loading}
              sx={{ height: '56px' }}
            >
              Search
            </Button>
          </Grid>

          <Grid item xs={12} md={9}>
            <Box sx={{ px: 2 }}>
              <Typography gutterBottom>
                Minimum Similarity: {minSimilarity}
              </Typography>
              <Slider
                value={minSimilarity}
                onChange={(e, newValue) => setMinSimilarity(newValue)}
                min={0.1}
                max={1.0}
                step={0.1}
                marks
                valueLabelDisplay="auto"
              />
            </Box>
          </Grid>

          <Grid item xs={12} md={3}>
            <Button
              fullWidth
              variant="outlined"
              startIcon={<RefreshIcon />}
              onClick={handleRefreshIndex}
              disabled={loading}
            >
              Refresh Index
            </Button>
          </Grid>
        </Grid>
      </Paper>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {loading && (
        <Box display="flex" justifyContent="center" my={4}>
          <CircularProgress />
        </Box>
      )}

      {results.length > 0 && (
        <>
          <Typography variant="h5" gutterBottom>
            Found {pagination.totalCount} results
          </Typography>

          <Grid container spacing={3}>
            {results.map((book, index) => (
              <Grid item xs={12} md={6} key={book.book_code}>
                <Card elevation={2} sx={{ height: '100%' }}>
                  <CardContent>
                    <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={2}>
                      <Typography variant="h6" component="h2">
                        {book.book_title}
                      </Typography>
                      <Chip
                        label={`${(book.similarity * 100).toFixed(0)}%`}
                        color={getSimilarityColor(book.similarity)}
                        size="small"
                      />
                    </Box>

                    <Typography color="text.secondary" gutterBottom>
                      Code: {book.book_code}
                    </Typography>

                    <Typography variant="body2" gutterBottom>
                      <strong>Author:</strong> {book.author_name}
                    </Typography>

                    <Typography variant="body2" gutterBottom>
                      <strong>Publisher:</strong> {book.publisher_name}
                    </Typography>

                    <Typography variant="body2" gutterBottom>
                      <strong>Year:</strong> {book.release_year}
                    </Typography>

                    {book.categories && (
                      <Box mt={2}>
                        <Typography variant="body2" gutterBottom>
                          <strong>Categories:</strong>
                        </Typography>
                        <Box display="flex" flexWrap="wrap" gap={1}>
                          {book.categories.split(', ').map((category, idx) => (
                            <Chip
                              key={idx}
                              label={category}
                              size="small"
                              variant="outlined"
                            />
                          ))}
                        </Box>
                      </Box>
                    )}

                    {book.summary && (
                      <Typography variant="body2" color="text.secondary" mt={2}>
                        {book.summary.substring(0, 200)}...
                      </Typography>
                    )}
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>

          {pagination.totalPages > 1 && (
            <Box display="flex" justifyContent="center" mt={4}>
              <Pagination
                count={pagination.totalPages}
                page={pagination.currentPage}
                onChange={handlePageChange}
                color="primary"
              />
            </Box>
          )}
        </>
      )}
    </Container>
  );
};

export default BookSearch;
