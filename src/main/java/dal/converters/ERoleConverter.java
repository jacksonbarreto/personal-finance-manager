package dal.converters;

import bll.enumerators.ERole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ERoleConverter implements AttributeConverter<ERole, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ERole role) {
        return role.getID();
    }

    @Override
    public ERole convertToEntityAttribute(Integer integer) {
        return Stream.of(ERole.values())
                .filter((e) -> e.getID().equals(integer))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
