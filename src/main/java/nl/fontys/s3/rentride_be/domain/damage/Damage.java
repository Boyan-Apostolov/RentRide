package nl.fontys.s3.rentride_be.domain.damage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class Damage {
    private Long id;

    private String name;

    private double cost;

    private String iconUrl;
}
