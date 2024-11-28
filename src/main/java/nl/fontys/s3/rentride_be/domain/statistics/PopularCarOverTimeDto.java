package nl.fontys.s3.rentride_be.domain.statistics;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularCarOverTimeDto {
    private String car;
    private int year;
    private String month;
    private long count;
}