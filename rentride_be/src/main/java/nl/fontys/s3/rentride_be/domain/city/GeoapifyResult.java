package nl.fontys.s3.rentride_be.domain.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoapifyResult {
    private String city;
    private Double lon;
    private Double lat;
}
