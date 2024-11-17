package nl.fontys.s3.rentride_be.business.impl.complex_queries;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexDiscountPlanRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ComplexDiscountPlanRepositoryQueriesUseCaseImpl implements ComplexDiscountPlanRepositoryQueriesUseCase {
private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    public List<GroupingDto> getMostBoughtDiscountPlans() {
        return this.discountPlanPurchaseRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getDiscountPlan().getTitle(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new GroupingDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
