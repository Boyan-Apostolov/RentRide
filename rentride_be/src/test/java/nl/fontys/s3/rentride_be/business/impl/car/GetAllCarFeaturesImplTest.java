package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.business.impl.user.GetAllCarFeaturesImpl;
import nl.fontys.s3.rentride_be.domain.car.CarFeature;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.CarFeatureRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
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
class GetAllCarFeaturesImplTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarFeatureRepository carFeatureRepository;

    @InjectMocks
    private GetAllCarFeaturesImpl getAllCarFeatures;

    @Test
    void getAllFeatures_shouldReturnEmptyListWhenThereAreNoFeatures() {
        List<CarFeature> actualFeatures = this.getAllCarFeatures.getAllCarFeatures();

        assertEquals(actualFeatures, List.of());
        verify(this.carFeatureRepository).findAll();
    }

    @Test
    void getAllFeatures_shouldReturnTheFeaturesWhenTheyArePresent() {
        List<CarFeatureEntity> features = new ArrayList<>();
        features.add(
                CarFeatureEntity.builder()
                        .featureText("2")
                        .featureType(CarFeatureType.Seats)
                        .build()
        );
        features.add(
                CarFeatureEntity.builder()
                        .featureText("test")
                        .featureType(CarFeatureType.Bonus)
                        .build()
        );
        when(this.carFeatureRepository.findAll()).thenReturn(features);

        List<CarFeature> actualFeatures = this.getAllCarFeatures.getAllCarFeatures();
        List<CarFeature> expectedFeatures = features.stream().map(CarFeatureConverter::convert).toList();

        assertEquals(expectedFeatures,actualFeatures );
        verify(this.carFeatureRepository).findAll();
    }
}
