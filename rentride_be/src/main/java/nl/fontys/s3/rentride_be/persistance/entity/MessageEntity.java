package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "answer")
    private String answer;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;
}
