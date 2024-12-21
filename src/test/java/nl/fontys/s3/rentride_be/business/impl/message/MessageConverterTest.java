package nl.fontys.s3.rentride_be.business.impl.message;

import nl.fontys.s3.rentride_be.domain.payment.Message;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MessageConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain() {
        MessageEntity messageEntity = MessageEntity
                .builder()
                .user(UserEntity.builder().build())
                .message("test")
                .answer("test")
                .build();

        Message actual = MessageConverter.convert(messageEntity);

        Message expected = Message
                .builder()
                .user(User.builder().build())
                .messageContent("test")
                .answerContent("test")
                .build();


        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull() {
        assertNull(MessageConverter.convert(null));
    }
}

