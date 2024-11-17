package nl.fontys.s3.rentride_be.business.use_cases.complex_queries;

import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;

import java.util.List;

public interface ComplexDiscountPlanRepositoryQueriesUseCase {
    List<GroupingDto> getMostBoughtDiscountPlans();
}
