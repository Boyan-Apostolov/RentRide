package nl.fontys.s3.rentride_be.business.impl.message;

import nl.fontys.s3.rentride_be.domain.payment.Message;
import nl.fontys.s3.rentride_be.persistance.MessageRepositoy;
import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllMessagesUseCaseImplTest {

    @Mock
    private MessageRepositoy messageRepositoy;

    @InjectMocks
    private GetAllMessagesUseCaseImpl getAllMessagesUseCase;

    private MessageEntity messageEntity1;
    private MessageEntity messageEntity2;

    @BeforeEach
    void setUp() {
        messageEntity1 = MessageEntity.builder()
                .id(1L)
                .message("First message")
                .answer("First answer")
                .build();

        messageEntity2 = MessageEntity.builder()
                .id(2L)
                .message("Second message")
                .answer("Second answer")
                .build();
    }

    @Test
    void getAllMessages_ShouldReturnListOfMessages() {
        when(messageRepositoy.findAll()).thenReturn(List.of(messageEntity1, messageEntity2));

        List<Message> result = getAllMessagesUseCase.getAllMessages();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMessageContent()).isEqualTo("First message");
        assertThat(result.get(1).getMessageContent()).isEqualTo("Second message");

        verify(messageRepositoy, times(1)).findAll();
    }

    @Test
    void getAllMessages_ShouldReturnEmptyListWhenNoMessagesExist() {
        when(messageRepositoy.findAll()).thenReturn(List.of());

        List<Message> result = getAllMessagesUseCase.getAllMessages();

        assertThat(result).isEmpty();

        verify(messageRepositoy, times(1)).findAll();
    }
}