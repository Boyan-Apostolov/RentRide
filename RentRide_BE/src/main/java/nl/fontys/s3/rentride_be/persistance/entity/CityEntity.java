package nl.fontys.s3.rentride_be.persistance.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityEntity {
    private Long id;
    private String name;
    private Double lat;
    private Double lon;
}
