package nl.fontys.s3.rentride_be.business.use_cases.message;

public interface AnswerMessageUseCase {
    void answerMessage(Long messageId, String answerContent);
}
