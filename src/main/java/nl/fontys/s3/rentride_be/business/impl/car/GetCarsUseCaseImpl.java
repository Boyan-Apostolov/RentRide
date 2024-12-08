package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.car.GetCarsUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCarsUseCaseImpl implements GetCarsUseCase {
    @Value("${DEFAULT_PAGE_SIZE}")
    private int DefaultPageSize;

    private final CarRepository carRepository;

    @Override
    public List<Car> getCars() {
        return this.carRepository.findAll()
                .stream()
                .map(CarConverter::convert)
                .toList();
    }

    @Override
    public List<Car> getCars(int page) {
        Pageable pageable = PageRequest.of(page, DefaultPageSize);
        return this.carRepository
                .findAll(pageable)
                .stream()
                .map(CarConverter::convert)
                .toList();
    }

    @Override
    public Long getCount() {
        return this.carRepository.count();
    }
}
