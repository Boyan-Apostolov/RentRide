package nl.fontys.s3.rentride_be.business;

import nl.fontys.s3.rentride_be.domain.City;
import org.junit.jupiter.api.Test;

class CityServiceTest {

    private CityService getNewCityService() {
        CityRepo cityRepo = new CityRepoImpl();
        CityService cityService = new CityServiceImp(cityRepo);
        return cityService;
    }

    @Test
    void getCitiesShouldReturnCorrectSeededCities() {
        CityService cityService = getNewCityService();

        assertEquals(3, cityService.getCities().size());
        assertEquals("Eindhoven", cityService.getCities().get(0).getName());
    }

    @Test
    void addCityShouldCorrectlyAddTheCity() {
        CityService cityService = getNewCityService();

        cityService.addCity(new City("Test", 123.123,123123.0));

        assertEquals(4, cityService.getCities().size());
        assertEquals("Test", cityService.getCities().get(3).getName());
    }
}