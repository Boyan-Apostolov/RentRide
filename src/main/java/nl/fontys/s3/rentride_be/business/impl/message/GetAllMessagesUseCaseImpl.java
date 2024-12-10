package nl.fontys.s3.rentride_be.business.impl.message;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.message.GetAllMessagesUseCase;
import nl.fontys.s3.rentride_be.domain.payment.Message;
import nl.fontys.s3.rentride_be.persistance.MessageRepositoy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllMessagesUseCaseImpl implements GetAllMessagesUseCase {
    private MessageRepositoy messageRepositoy;

    @Override
    public List<Message> getAllMessages() {
        return messageRepositoy
                .findAll()
                .stream().map(MessageConverter::convert)
                .toList();
    }
}
