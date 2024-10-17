package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;

@Entity
@Table(name="car_feature")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarFeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "feature_type")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private CarFeatureType featureType;

    @Column(name = "feature_text")
    @NotNull
    private String featureText;
}
