package nl.fontys.s3.rentride_be.domain.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private Long id;
    private String name;
    private Double lat;
    private Double lon;
    private String depoAddress;
}
