package nl.fontys.s3.rentride_be.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class City {
    private String name;
    private Double Lat;
    private Double Lon;
}
