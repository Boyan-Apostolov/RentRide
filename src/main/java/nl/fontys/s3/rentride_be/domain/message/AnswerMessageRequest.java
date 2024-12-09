package nl.fontys.s3.rentride_be.domain.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerMessageRequest {
    private String answerContent;
}
