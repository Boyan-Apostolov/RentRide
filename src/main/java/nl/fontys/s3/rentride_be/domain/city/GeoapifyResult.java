package nl.fontys.s3.rentride_be.domain.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GeoapifyResult {
    private String city;
    private Double lon;
    private Double lat;
}
