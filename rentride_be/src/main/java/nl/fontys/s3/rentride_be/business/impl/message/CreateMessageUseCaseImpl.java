package nl.fontys.s3.rentride_be.business.impl.message;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.message.CreateMessageUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.MessageRepositoy;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateMessageUseCaseImpl  implements CreateMessageUseCase {
    private MessageRepositoy messageRepositoy;
    private UserRepository userRepository;
    private AccessToken accessToken;
    private EmailerUseCase emailerUseCase;

    @Override
    public void createMessage(String messageContent) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(accessToken.getUserId());
        if(optionalUserEntity.isEmpty())
            throw new NotFoundException("CreateMessage->User");
        UserEntity userEntity = optionalUserEntity.get();

        this.messageRepositoy.save(MessageEntity.builder()
                        .message(messageContent)
                        .user(userEntity)
                .build());

        emailerUseCase.send(userEntity.getEmail(), "Support message received!",
                String.format( "Your message '%s' was received and will be answered as soon as possible", messageContent),
                EmailType.SUPPORT);
    }
}
