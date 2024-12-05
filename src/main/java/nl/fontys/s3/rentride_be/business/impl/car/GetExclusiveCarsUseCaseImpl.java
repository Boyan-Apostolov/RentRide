package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.car.GetExclusiveCarsUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetExclusiveCarsUseCaseImpl implements GetExclusiveCarsUseCase{
    private CarRepository carRepository;

    @Override
    public List<Car> getCars() {
        return this.carRepository.findAll()
                .stream()
                .filter(CarEntity::isExclusive)
                .map(CarConverter::convert)
                .toList();
    }
}
