package nl.fontys.s3.rentride_be.business.impl.complex_queries;

import nl.fontys.s3.rentride_be.business.impl.complex_queries.ComplexDiscountPlanRepositoryQueriesUseCaseImpl;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplexDiscountPlanRepositoryQueriesUseCaseImplTest {

    @Mock
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @InjectMocks
    private ComplexDiscountPlanRepositoryQueriesUseCaseImpl complexQueries;

    private List<DiscountPlanPurchaseEntity> mockPurchases;

    @BeforeEach
    void setUp() {
        DiscountPlanEntity plan1 = DiscountPlanEntity.builder().title("Plan A").build();
        DiscountPlanEntity plan2 = DiscountPlanEntity.builder().title("Plan B").build();
        DiscountPlanEntity plan3 = DiscountPlanEntity.builder().title("Plan C").build();

        mockPurchases = List.of(
                DiscountPlanPurchaseEntity.builder().discountPlan(plan1).build(),
                DiscountPlanPurchaseEntity.builder().discountPlan(plan1).build(),
                DiscountPlanPurchaseEntity.builder().discountPlan(plan2).build(),
                DiscountPlanPurchaseEntity.builder().discountPlan(plan3).build(),
                DiscountPlanPurchaseEntity.builder().discountPlan(plan3).build(),
                DiscountPlanPurchaseEntity.builder().discountPlan(plan3).build()
        );

        when(discountPlanPurchaseRepository.findAll()).thenReturn(mockPurchases);
    }

    @Test
    void getMostBoughtDiscountPlans_ShouldReturnCorrectGrouping() {
        List<GroupingDto> result = complexQueries.getMostBoughtDiscountPlans();

        List<GroupingDto> expected = List.of(
                new GroupingDto("Plan B", 1L),
                new GroupingDto("Plan A", 2L),
                new GroupingDto("Plan C", 3L)
        );

        assertEquals(expected, result);
    }
}