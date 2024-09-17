package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.DeleteCarUseCase;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteCarUseCaseImpl implements DeleteCarUseCase {
    private CarRepository carRepository;

    @Override
    public void deleteCar(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new NotFoundException("Delete->Car");
        }

        this.carRepository.deleteById(carId);
    }
}
