package han.org.mapping;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.SqlResultSetMapping;

@Entity
@SqlResultSetMapping(
        name = "BookNativeQueryMapping",
        classes = {
                @ConstructorResult(
                        targetClass = han.org.dto.BookSearchResponseDto.class,
                        columns = {
                                @ColumnResult(name = "book_code", type = String.class),
                                @ColumnResult(name = "book_title", type = String.class),
                                @ColumnResult(name = "release_year", type = Integer.class),
                                @ColumnResult(name = "summary", type = String.class),
                                @ColumnResult(name = "author_id", type = Long.class),
                                @ColumnResult(name = "author_name", type = String.class),
                                @ColumnResult(name = "publisher_id", type = Long.class),
                                @ColumnResult(name = "publisher_name", type = String.class),
                                @ColumnResult(name = "categories", type = String.class)
                        }
                )
        }
)
public class BookNativeQueryMapper {
}
