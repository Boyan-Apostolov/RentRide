package nl.fontys.s3.rentride_be.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UpdateUserEmailSettingsRequest {
    private boolean bookingsEmails;
    private boolean damageEmails;
    private boolean promoEmails;
}

