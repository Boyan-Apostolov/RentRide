package nl.fontys.s3.rentride_be.business.use_cases.auth;

import nl.fontys.s3.rentride_be.domain.user.EmailType;

public interface EmailerUseCase {
    void send(String to, String subject, String body, EmailType emailType);
}
