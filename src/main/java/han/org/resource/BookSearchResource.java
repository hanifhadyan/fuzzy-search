package han.org.resource;

import han.org.dto.BookSearchRequestDto;
import han.org.dto.BookSearchResponseDto;
import han.org.Response.ErrorResponse;
import han.org.Response.SuccessResponse;
import han.org.service.BookSearchService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.data.domain.Page;

/**
 * REST Resource for book search operations
 * Provides both GET and POST endpoints for searching books
 * Follows REST API best practices with proper error handling
 */
@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookSearchResource {

    @Inject
    BookSearchService bookSearchService;

    /**
     * Search books using query parameters (GET)
     * Suitable for simple searches with basic parameters
     */
    @GET
    @Path("/search")
    public Response searchBooks(
            @QueryParam("q") String keyword,
            @QueryParam("publisher") String publisher,
            @QueryParam("offset") @Min(value = 0, message = "Offset must be non-negative") Integer offset,
            @QueryParam("limit") @Min(value = 1, message = "Limit must be at least 1")
                                  @Max(value = 100, message = "Limit cannot exceed 100") Integer limit) {
        try {
            BookSearchRequestDto request = buildSearchRequest(keyword, publisher, offset, limit);
            Page<BookSearchResponseDto> books = bookSearchService.search(request);

            return Response.ok(new SuccessResponse<>(books, "Books retrieved successfully")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                          .entity(new ErrorResponse("INVALID_PARAMETERS", "Invalid search parameters", e.getMessage()))
                          .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(new ErrorResponse("SEARCH_ERROR", "Error searching books", "An unexpected error occurred"))
                          .build();
        }
    }

    /**
     * Refresh the search index
     * Updates the search index with latest book data
     */
    @POST
    @Path("/refresh-index")
    public Response refreshSearchIndex() {
        try {
            bookSearchService.refreshSearchIndex();
            return Response.ok(new SuccessResponse<>("Search index refreshed successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(new ErrorResponse("INDEX_REFRESH_ERROR", "Failed to refresh index",
                                                  "An unexpected error occurred while refreshing the search index"))
                          .build();
        }
    }

    /**
     * Helper method to build search request from query parameters
     * Applies default values and validates input
     */
    private BookSearchRequestDto buildSearchRequest(String keyword, String publisher, Integer offset, Integer limit) {
        BookSearchRequestDto request = new BookSearchRequestDto();
        request.setKeyword(keyword);
        request.setPublisher(publisher);
        request.setOffset(offset != null ? offset : 0);
        request.setLimit(limit != null ? limit : 20);
        return request;
    }
}
