package nl.fontys.s3.rentride_be.domain.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoapifyResponse {
    private List<GeoapifyResult> results;
}
