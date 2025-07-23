package han.org.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for all API responses providing a consistent structure
 * @param <T> the type of data being returned
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ApiResponse<T> {

    @JsonProperty("success")
    private final boolean success;

    @JsonProperty("timestamp")
    private final long timestamp;

    protected ApiResponse(boolean success) {
        this.success = success;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isSuccess() {
        return success;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
