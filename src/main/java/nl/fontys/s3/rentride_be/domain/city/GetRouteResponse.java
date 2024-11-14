package nl.fontys.s3.rentride_be.domain.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRouteResponse {
    private String distance;
    private String time;
    private String imgUrl;
}
