package nl.fontys.s3.rentride_be.business.impl.message;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.MessageRepositoy;
import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AnswerMessageUseCaseImplTest {

    @Mock
    private MessageRepositoy messageRepositoy;

    @Mock
    private EmailerUseCase emailerUseCase;

    @InjectMocks
    private AnswerMessageUseCaseImpl answerMessageUseCase;

    private MessageEntity messageEntity;

    @BeforeEach
    void setUp() {
        messageEntity = MessageEntity.builder()
                .id(1L)
                .message("Where is my car?")
                .answer(null)
                .user(UserEntity.builder().email("user@example.com").build())
                .build();
    }

    @Test
    void answerMessage_ShouldThrowNotFoundException_WhenMessageNotFound() {
        Long messageId = 1L;
        when(messageRepositoy.findById(messageId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> answerMessageUseCase.answerMessage(messageId, "Your car is arriving soon."))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"AnswerMessage->Message_NOT_FOUND\"");

        verify(messageRepositoy, times(1)).findById(messageId);
        verify(emailerUseCase, never()).send(any(), any(), any(), any());
        verify(messageRepositoy, never()).save(any());
    }

    @Test
    void answerMessage_ShouldSendEmailAndSaveMessage_WhenMessageExists() {
        Long messageId = 1L;
        String answerContent = "Your car will arrive within 24 hours.";
        when(messageRepositoy.findById(messageId)).thenReturn(Optional.of(messageEntity));

        answerMessageUseCase.answerMessage(messageId, answerContent);

        verify(messageRepositoy, times(1)).findById(messageId);
        verify(emailerUseCase, times(1)).send(
                "user@example.com",
                "Support answer received!",
                "Your have recently submitted the question: 'Where is my car?'. One of our support executives has replied with the following message Your car will arrive within 24 hours.",
                EmailType.SUPPORT
        );
        verify(messageRepositoy, times(1)).save(messageEntity);
        assertThat(messageEntity.getAnswer()).isEqualTo(answerContent);
    }
}