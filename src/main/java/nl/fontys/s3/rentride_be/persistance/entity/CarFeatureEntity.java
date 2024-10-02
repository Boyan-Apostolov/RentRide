package nl.fontys.s3.rentride_be.persistance.entity;

import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;

@Data
@Builder
public class CarFeatureEntity {
    private Long id;
    private CarFeatureType featureType;
    private String featureText;
}
