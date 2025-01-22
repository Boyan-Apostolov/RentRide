package nl.fontys.s3.rentride_be.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.domain.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private Long id;

    private String messageContent;

    private String answerContent;

    private User user;
}
