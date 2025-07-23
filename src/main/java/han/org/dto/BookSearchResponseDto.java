package han.org.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BookSearchResponseDto {

    @JsonProperty("book_code")
    private String bookCode;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("release_year")
    private Integer releaseYear;

    private String summary;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("publisher_id")
    private Long publisherId;

    @JsonProperty("publisher_name")
    private String publisherName;

    private String categories;

    // Constructors
    public BookSearchResponseDto() {}

    public BookSearchResponseDto(String bookCode, String bookTitle, Integer releaseYear,
                                String summary, Long authorId, String authorName,
                                Long publisherId, String publisherName, String categories) {
        this.bookCode = bookCode;
        this.bookTitle = bookTitle;
        this.releaseYear = releaseYear;
        this.summary = summary;
        this.authorId = authorId;
        this.authorName = authorName;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.categories = categories;
    }

    // Getters and Setters
    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
