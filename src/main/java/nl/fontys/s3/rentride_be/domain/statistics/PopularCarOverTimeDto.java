package nl.fontys.s3.rentride_be.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PopularCarOverTimeDto {
    private String car;
    private int year;
    private String month;
    private long count;
}