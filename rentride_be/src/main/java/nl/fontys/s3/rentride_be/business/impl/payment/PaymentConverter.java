package nl.fontys.s3.rentride_be.business.impl.payment;

import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;

public class PaymentConverter {
    private PaymentConverter() {}

    public static Payment convert(PaymentEntity paymentEntity) {
        if(paymentEntity == null) return null;

        return Payment
                .builder()
                .id(paymentEntity.getId())
                .description(paymentEntity.getDescription())
                .amount(paymentEntity.getAmount())
                .user(UserConverter.convert(paymentEntity.getUser()))
                .stripeLink(paymentEntity.getStripeLink())
                .isPaid(paymentEntity.isPaid())
                .createdOn(paymentEntity.getCreatedOn())
                .build();
    }
}
