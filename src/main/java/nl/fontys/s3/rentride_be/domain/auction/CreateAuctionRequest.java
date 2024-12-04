package nl.fontys.s3.rentride_be.domain.auction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuctionRequest {
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    @Min(1)
    private double minBidAmount;

    @NotNull
    private LocalDateTime endDateTime;

    @NotNull
    @Min(1)
    private Long car;
}
