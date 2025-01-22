package nl.fontys.s3.rentride_be.business.impl.message;

import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.domain.payment.Message;
import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;

public final class MessageConverter {

    private MessageConverter() {
    }

    public static Message convert(MessageEntity messageEntity) {
        if (messageEntity == null) return null;

        return Message.builder()
                .id(messageEntity.getId())
                .messageContent(messageEntity.getMessage())
                .user(UserConverter.convert(messageEntity.getUser()))
                .answerContent(messageEntity.getAnswer())
                .build();
    }
}
