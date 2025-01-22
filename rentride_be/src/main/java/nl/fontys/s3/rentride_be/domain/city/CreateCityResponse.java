package nl.fontys.s3.rentride_be.domain.city;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCityResponse {
    private Long cityId;
}
