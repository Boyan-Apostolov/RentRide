package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.CreateAuctionUseCase;
import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionRequest;
import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionResponse;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateAuctionUseCaseImpl  implements CreateAuctionUseCase {
    private AuctionRepository auctionRepository;
    private CarRepository carRepository;
    @Override
    public CreateAuctionResponse createAuction(CreateAuctionRequest createAuctionRequest) {
        if(createAuctionRequest.getEndDateTime().isBefore(LocalDateTime.now()))
            throw new InvalidOperationException("End date cannot be before start date");

        Optional<CarEntity> optionalCar = carRepository.findById(createAuctionRequest.getCar());
        if(optionalCar.isEmpty()) throw new NotFoundException("CreateAuction->Car");
        CarEntity car = optionalCar.get();

        if(!car.isExclusive()) throw new InvalidOperationException("Only exclusive cars can be used in auctions");

        AuctionEntity newAuctionEntity = saveNewAuction(createAuctionRequest, car);

        return CreateAuctionResponse.builder()
                .auctionId(newAuctionEntity.getId())
                .build();
    }

    private AuctionEntity saveNewAuction(CreateAuctionRequest createAuctionRequest, CarEntity car) {
        AuctionEntity newAuctionEntity = AuctionEntity.builder()
                .endDateTime(createAuctionRequest.getEndDateTime())
                .car(car)
                .minBidAmount(createAuctionRequest.getMinBidAmount())
                .description(createAuctionRequest.getDescription())
                .unlockCode(createAuctionRequest.getUnlockCode())
                .build();

        return this.auctionRepository.save(newAuctionEntity);
    }
}
