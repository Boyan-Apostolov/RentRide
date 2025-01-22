package nl.fontys.s3.rentride_be.domain.car;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCarResponse {
    private Long carId;
}
