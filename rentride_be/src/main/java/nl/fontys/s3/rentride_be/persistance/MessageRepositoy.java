package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepositoy extends JpaRepository<MessageEntity, Long> {
}
