package nl.fontys.s3.rentride_be.business.use_cases.user;

import nl.fontys.s3.rentride_be.domain.user.UpdateUserEmailSettingsRequest;

public interface UpdateUserEmailSettingsUseCase {
    public void updateUserEmails(UpdateUserEmailSettingsRequest request);
}
