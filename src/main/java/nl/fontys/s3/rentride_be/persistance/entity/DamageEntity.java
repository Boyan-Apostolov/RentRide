package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "damage")

@Data
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class DamageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "cost")
    @NotNull
    private double cost;


    @Column(name = "icon_url")
    @NotNull
    private String iconUrl;
}
