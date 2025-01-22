package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;

public final class CityConverter {
    private CityConverter() {}

    public static City convert(CityEntity cityEntity){
        if(cityEntity == null) return null;
        return City.builder()
                .id(cityEntity.getId())
                .name(cityEntity.getName())
                .lat(cityEntity.getLat())
                .lon(cityEntity.getLon())
                .depoAddress(cityEntity.getDepoAdress())
                .build();
    }
}
