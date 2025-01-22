package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.rentride_be.persistance.MessageRepositoy;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MessageRepositoy messageRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity saveUser() {
        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .name("Test User")
                .password("password")
                .birthDate(LocalDate.now())
                .build();
        return userRepository.save(user);
    }

    @Test
    void save_ShouldSaveMessageWithAllFields() {
        UserEntity user = saveUser();
        MessageEntity initialMessage = MessageEntity.builder()
                .message("Where is my car?")
                .answer("Your car will arrive shortly.")
                .user(user)
                .build();

        MessageEntity savedMessage = messageRepository.save(initialMessage);
        assertNotNull(savedMessage.getId());

        savedMessage = entityManager.find(MessageEntity.class, savedMessage.getId());
        initialMessage.setId(savedMessage.getId());

        assertEquals(initialMessage, savedMessage);
    }

    @Test
    void findById_ShouldFindMessage() {
        UserEntity user = saveUser();
        MessageEntity initialMessage = MessageEntity.builder()
                .message("Where is my car?")
                .answer("Your car will arrive shortly.")
                .user(user)
                .build();

        MessageEntity savedMessage = messageRepository.save(initialMessage);

        Optional<MessageEntity> foundMessage = messageRepository.findById(savedMessage.getId());

        assertTrue(foundMessage.isPresent());
        assertEquals(savedMessage, foundMessage.get());
    }

    @Test
    void findById_ShouldReturnEmptyForNonExistingMessage() {
        Optional<MessageEntity> foundMessage = messageRepository.findById(999L);

        assertFalse(foundMessage.isPresent());
    }
}