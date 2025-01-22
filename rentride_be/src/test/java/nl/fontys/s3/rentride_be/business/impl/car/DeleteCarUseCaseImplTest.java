package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCarUseCaseImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private DeleteCarUseCaseImpl deleteCarUseCase;

    @Test
    void deleteCar_shouldThrowErrorWhenCarDoesNotExist() {
        assertThrows(NotFoundException.class, () -> deleteCarUseCase.deleteCar(1L));

        Mockito.verify(this.carRepository).existsById(1L);
    }

    @Test
    void deleteCar_shouldCorrectlyRemoveTheCarWhenItExists(){
        when(this.carRepository.existsById(1L)).thenReturn(true);

        this.deleteCarUseCase.deleteCar(1L);

        verify(this.carRepository).deleteById(1L);
    }
}