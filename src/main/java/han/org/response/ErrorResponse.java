package han.org.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an error API response with error details
 */
public class ErrorResponse extends ApiResponse<Void> {

    @JsonProperty("error")
    private final String error;

    @JsonProperty("details")
    private final String details;

    public ErrorResponse(String error) {
        super(false);
        this.error = error;
        this.details = null;
    }

    public ErrorResponse(String error, String details) {
        super(false);
        this.error = error;
        this.details = details;
    }

    public String getError() {
        return error;
    }

    public String getDetails() {
        return details;
    }
}
