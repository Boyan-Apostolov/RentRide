package nl.fontys.s3.rentride_be.business.impl.auth;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.impl.booking.ScheduleBookingJobsUseCaseImpl;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailerUseCaseImpl implements EmailerUseCase {

    private final JavaMailSender javaMailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailerUseCaseImpl.class);

    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String sender;


    @Override
    public void send(String to, String subject, String body, EmailType emailType) {
        try{
            Optional<UserEntity> optionalUser = userRepository.findByEmail(to);
            if (optionalUser.isEmpty()) throw new NotFoundException("EmailSender->User");

            UserEntity user = optionalUser.get();
            if((user.isPromoEmails() && emailType.equals(EmailType.PROMO))
            || (user.isDamageEmails() && emailType.equals(EmailType.DAMAGE))
            ||(user.isBookingsEmails() && emailType.equals(EmailType.BOOKING))){
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(sender);
                mailMessage.setTo(to);
                mailMessage.setText(body);
                mailMessage.setSubject(subject);

                javaMailSender.send(mailMessage);
            }
        }
        catch (Exception e){
            logger.error(String.format("Could not send email to %s: %s", to, e.getMessage()));        }
    }
}
