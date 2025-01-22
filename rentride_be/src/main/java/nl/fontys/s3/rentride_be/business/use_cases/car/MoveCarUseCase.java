package nl.fontys.s3.rentride_be.business.use_cases.car;

public interface MoveCarUseCase {
    void moveCar(Long carId, Long cityId);
}
