package han.org.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a successful API response with optional data payload
 * @param <T> the type of data being returned
 */
public class SuccessResponse<T> extends ApiResponse<T> {

    @JsonProperty("data")
    private final T data;

    @JsonProperty("message")
    private final String message;

    public SuccessResponse(T data) {
        super(true);
        this.data = data;
        this.message = null;
    }

    public SuccessResponse(T data, String message) {
        super(true);
        this.data = data;
        this.message = message;
    }

    public SuccessResponse(String message) {
        super(true);
        this.data = null;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
