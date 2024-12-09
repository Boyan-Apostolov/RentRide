package nl.fontys.s3.rentride_be.business.impl.message;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.message.AnswerMessageUseCase;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.MessageRepositoy;
import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AnswerMessageUseCaseImpl implements AnswerMessageUseCase {
    private MessageRepositoy messageRepositoy;
    private EmailerUseCase emailerUseCase;

    @Override
    public void answerMessage(Long messageId, String answerContent) {
        Optional<MessageEntity> optionalMessage = messageRepositoy.findById(messageId);
        if(optionalMessage.isEmpty())
            throw new NotFoundException("AnswerMessage->Message");
        MessageEntity messageEntity = optionalMessage.get();

        messageEntity.setAnswer(answerContent);

        emailerUseCase.send(messageEntity.getUser().getEmail(), "Support answer received!",
                String.format( "Your have recently submitted the question: '%s'. One of our support executives has replied with the following message %s",
                        messageEntity.getMessage(), messageEntity.getAnswer()),
                EmailType.SUPPORT);

    }
}
