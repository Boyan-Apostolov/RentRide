package nl.fontys.s3.rentride_be.business.use_cases.damage;

import nl.fontys.s3.rentride_be.domain.damage.Damage;

import java.util.List;

public interface GetAllDamageUseCase {
    List<Damage> getAllDamage();
}
