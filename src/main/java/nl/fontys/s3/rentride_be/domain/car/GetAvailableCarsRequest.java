package nl.fontys.s3.rentride_be.domain.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAvailableCarsRequest {
    @NotBlank
    private String fromCity;

    @NotNull
    private LocalDateTime fromDateTime;

    @NotBlank
    private String toCity;

    @NotNull
    private LocalDateTime toDateTime;
}
