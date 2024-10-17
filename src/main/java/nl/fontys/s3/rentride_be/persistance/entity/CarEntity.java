package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "car")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "make")
    private String make;

    @NotNull
    @Column(name = "model")
    private String model;

    @NotNull
    @Column(name = "registration_number")
    private String registrationNumber;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "car_feature_mapping",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CarFeatureEntity> features;

    @NotNull
    @Column(name = "fuel_consumption")
    private Double fuelConsumption;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "city_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CityEntity city;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "car_photos", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "photo_base64")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<String> photosBase64;
}
