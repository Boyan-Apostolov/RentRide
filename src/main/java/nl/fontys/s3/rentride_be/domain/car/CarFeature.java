package nl.fontys.s3.rentride_be.domain.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CarFeature {
    private Long id;
    private CarFeatureType featureType;
    private String featureText;
}
