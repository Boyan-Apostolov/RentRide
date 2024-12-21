package nl.fontys.s3.rentride_be.business.impl.message;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.MessageRepositoy;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMessageUseCaseImplTest {

    @Mock
    private MessageRepositoy messageRepositoy;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken accessToken;

    @Mock
    private EmailerUseCase emailerUseCase;

    @InjectMocks
    private CreateMessageUseCaseImpl createMessageUseCase;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .email("user@test.com")
                .build();

        when(accessToken.getUserId()).thenReturn(1L);
    }

    @Test
    void createMessage_ShouldSaveMessageAndSendEmail() {
        String messageContent = "I need help with my booking.";

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        createMessageUseCase.createMessage(messageContent);

        verify(messageRepositoy, times(1)).save(argThat((MessageEntity msg) ->
                msg.getMessage().equals(messageContent) &&
                        msg.getUser().equals(userEntity)
        ));

        verify(emailerUseCase, times(1)).send(
                "user@test.com",
                "Support message received!",
                "Your message 'I need help with my booking.' was received and will be answered as soon as possible",
                EmailType.SUPPORT
        );
    }

    @Test
    void createMessage_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class,
                () -> createMessageUseCase.createMessage("Hello, I have a problem."));

        verify(messageRepositoy, never()).save(any(MessageEntity.class));
        verify(emailerUseCase, never()).send(anyString(), anyString(), anyString(), any(EmailType.class));
    }
}