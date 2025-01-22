package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.message.AnswerMessageUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.message.CreateMessageUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.message.GetAllMessagesUseCase;
import nl.fontys.s3.rentride_be.domain.message.AnswerMessageRequest;
import nl.fontys.s3.rentride_be.domain.message.CreateMessageRequest;
import nl.fontys.s3.rentride_be.domain.payment.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("messages")
@AllArgsConstructor
public class MessageController {
    private GetAllMessagesUseCase getAllMessagesUseCase;
    private AnswerMessageUseCase answerMessageUseCase;
    private CreateMessageUseCase createMessageUseCase;

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(this.getAllMessagesUseCase.getAllMessages());
    }

    @PostMapping()
    @RolesAllowed({"ADMIN", "CUSTOMER"})

    public ResponseEntity<String> createMessage(@RequestBody @Valid CreateMessageRequest request) {
        createMessageUseCase.createMessage(request.getMessageContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<Void> answerMessage(@PathVariable("id") long id,
                                           @RequestBody @Valid AnswerMessageRequest request) {
        answerMessageUseCase.answerMessage(id, request.getAnswerContent());
        return ResponseEntity.noContent().build();
    }
}
