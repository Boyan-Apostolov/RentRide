package nl.fontys.s3.rentride_be.business.impl.damage;

import nl.fontys.s3.rentride_be.domain.damage.Damage;
import nl.fontys.s3.rentride_be.persistance.entity.DamageEntity;

public class DamageConverter {
    private DamageConverter() {}

    public static Damage convert(DamageEntity damage) {
        if(damage == null) return null;

        return Damage
                .builder()
                .id(damage.getId())
                .cost(damage.getCost())
                .name(damage.getName())
                .iconUrl(damage.getIconUrl())
                .build();
    }
}
