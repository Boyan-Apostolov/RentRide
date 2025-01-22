package nl.fontys.s3.rentride_be.business.impl.damage;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.damage.GetAllDamageUseCase;
import nl.fontys.s3.rentride_be.domain.damage.Damage;
import nl.fontys.s3.rentride_be.persistance.DamageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllDamageUseCaseImpl implements GetAllDamageUseCase {
    private DamageRepository damageRepository;

    @Override
    public List<Damage> getAllDamage() {
        return this.damageRepository
                .findAll()
                .stream().map(DamageConverter::convert)
                .toList();
    }
}
