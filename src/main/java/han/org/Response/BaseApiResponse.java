package han.org.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

/**
 * Base class for all API responses providing a consistent structure
 * Follows best practices for REST API response design
 *
 * @param <T> the type of data being returned
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Base API response structure")
public abstract class BaseApiResponse<T> {

    @JsonProperty("success")
    @Schema(description = "Indicates if the request was successful", example = "true")
    private final boolean success;

    @JsonProperty("timestamp")
    @Schema(description = "ISO 8601 timestamp when the response was generated", example = "2025-07-18T10:30:00Z")
    private final String timestamp;

    @JsonProperty("version")
    @Schema(description = "API version", example = "1.0")
    private final String version;

    protected BaseApiResponse(boolean success) {
        this.success = success;
        this.timestamp = Instant.now().toString();
        this.version = "1.0";
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getVersion() {
        return version;
    }
}
