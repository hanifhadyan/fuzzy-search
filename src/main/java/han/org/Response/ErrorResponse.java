package han.org.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

/**
 * Represents an error API response with detailed error information
 * Follows REST API best practices for error handling
 */
@Schema(description = "Error API response")
public class ErrorResponse extends BaseApiResponse<Void> {

    @JsonProperty("error")
    @Schema(description = "Error information")
    private final ErrorInfo error;

    /**
     * Creates an error response with basic error message
     */
    public ErrorResponse(String message) {
        super(false);
        this.error = new ErrorInfo("GENERAL_ERROR", message, null, null);
    }

    /**
     * Creates an error response with error message and details
     */
    public ErrorResponse(String message, String details) {
        super(false);
        this.error = new ErrorInfo("GENERAL_ERROR", message, details, null);
    }

    /**
     * Creates an error response with error code, message and details
     */
    public ErrorResponse(String code, String message, String details) {
        super(false);
        this.error = new ErrorInfo(code, message, details, null);
    }

    /**
     * Creates an error response with validation errors
     */
    public ErrorResponse(String message, List<ValidationError> validationErrors) {
        super(false);
        this.error = new ErrorInfo("VALIDATION_ERROR", message, null, validationErrors);
    }

    public ErrorInfo getError() {
        return error;
    }

    /**
     * Detailed error information
     */
    @Schema(description = "Detailed error information")
    public static class ErrorInfo {
        @JsonProperty("code")
        @Schema(description = "Error code", example = "VALIDATION_ERROR")
        private final String code;

        @JsonProperty("message")
        @Schema(description = "Human-readable error message", example = "Invalid input provided")
        private final String message;

        @JsonProperty("details")
        @Schema(description = "Additional error details", example = "Field 'name' is required")
        private final String details;

        @JsonProperty("validationErrors")
        @Schema(description = "List of validation errors")
        private final List<ValidationError> validationErrors;

        public ErrorInfo(String code, String message, String details, List<ValidationError> validationErrors) {
            this.code = code;
            this.message = message;
            this.details = details;
            this.validationErrors = validationErrors;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getDetails() {
            return details;
        }

        public List<ValidationError> getValidationErrors() {
            return validationErrors;
        }
    }

    /**
     * Represents a validation error
     */
    @Schema(description = "Validation error details")
    public static class ValidationError {
        @JsonProperty("field")
        @Schema(description = "Field name that failed validation", example = "email")
        private final String field;

        @JsonProperty("message")
        @Schema(description = "Validation error message", example = "Invalid email format")
        private final String message;

        @JsonProperty("rejectedValue")
        @Schema(description = "Value that was rejected", example = "invalid-email")
        private final Object rejectedValue;

        public ValidationError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }
    }
}
