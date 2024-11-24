package nl.fontys.s3.rentride_be.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupingDto {
    private String key;
    private Long value;
}


