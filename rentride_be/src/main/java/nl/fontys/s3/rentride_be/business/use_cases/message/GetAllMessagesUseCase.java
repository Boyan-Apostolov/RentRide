package nl.fontys.s3.rentride_be.business.use_cases.message;

import nl.fontys.s3.rentride_be.domain.payment.Message;

import java.util.List;

public interface GetAllMessagesUseCase {
    List<Message> getAllMessages();
}
