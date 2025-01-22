package nl.fontys.s3.rentride_be.domain.statistics;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
        name = "GroupingDtoMapping",
        classes = @ConstructorResult(
                targetClass = GroupingDto.class,
                columns = {
                        @ColumnResult(name = "key", type = String.class),
                        @ColumnResult(name = "value", type = Long.class)
                }
        )
)
public class GroupingDtoMapping {
    // No fields required; this class is only for mapping purposes.
}
