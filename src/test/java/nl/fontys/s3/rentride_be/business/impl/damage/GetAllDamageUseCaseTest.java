package nl.fontys.s3.rentride_be.business.impl.damage;

import nl.fontys.s3.rentride_be.domain.damage.Damage;
import nl.fontys.s3.rentride_be.persistance.DamageRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DamageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllDamageUseCaseTest {

    @Mock
    private DamageRepository damageRepository;

    @InjectMocks
    private GetAllDamageUseCaseImpl getAllDamageUseCase;

    @Test
    void getAllDamageUseCase_shouldReturnEmptyListWhenNoItems() {
        List<Damage> actual = getAllDamageUseCase.getAllDamage();
        assertEquals(List.of(), actual);
    }

    @Test
    void getAllDamageUseCase_shouldReturnItemsWhenPresent() {
        List<DamageEntity> damages = new ArrayList<>();
        damages.add(
                DamageEntity.builder()
                        .id(1L)
                        .cost(12.12)
                        .iconUrl("test")
                        .name("test")
                        .build()
        );

        when(this.damageRepository.findAll()).thenReturn(damages);

        List<Damage> actualDamages = this.getAllDamageUseCase.getAllDamage();
        List<Damage> expectedCities = damages.stream().map(DamageConverter::convert).toList();

        assertEquals(expectedCities, actualDamages);
        verify(this.damageRepository).findAll();
    }
}
