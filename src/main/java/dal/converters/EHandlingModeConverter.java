package dal.converters;

import bll.enumerators.EHandlingMode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class EHandlingModeConverter implements AttributeConverter<EHandlingMode, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EHandlingMode handlingMode) {
        return handlingMode.getID();
    }

    @Override
    public EHandlingMode convertToEntityAttribute(Integer integer) {
        return Stream.of(EHandlingMode.values())
                .filter((e) -> e.getID().equals(integer))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
