package han.org.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Represents a successful API response with optional data payload
 * Follows REST API best practices with consistent structure
 *
 * @param <T> the type of data being returned
 */
@Schema(description = "Successful API response")
public class SuccessResponse<T> extends BaseApiResponse<T> {

    @JsonProperty("data")
    @Schema(description = "The response data payload")
    private final T data;

    @JsonProperty("message")
    @Schema(description = "Optional success message", example = "Operation completed successfully")
    private final String message;

    @JsonProperty("meta")
    @Schema(description = "Optional metadata about the response")
    private final Object meta;

    /**
     * Creates a success response with data only
     */
    public SuccessResponse(T data) {
        super(true);
        this.data = data;
        this.message = null;
        this.meta = null;
    }

    /**
     * Creates a success response with data and message
     */
    public SuccessResponse(T data, String message) {
        super(true);
        this.data = data;
        this.message = message;
        this.meta = null;
    }

    /**
     * Creates a success response with data, message and metadata
     */
    public SuccessResponse(T data, String message, Object meta) {
        super(true);
        this.data = data;
        this.message = message;
        this.meta = meta;
    }

    /**
     * Creates a success response with message only (no data)
     */
    public SuccessResponse(String message) {
        super(true);
        this.data = null;
        this.message = message;
        this.meta = null;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Object getMeta() {
        return meta;
    }
}
