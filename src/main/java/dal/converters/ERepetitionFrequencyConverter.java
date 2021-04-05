package dal.converters;

import bll.enumerators.ERepetitionFrequency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ERepetitionFrequencyConverter implements AttributeConverter<ERepetitionFrequency, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ERepetitionFrequency frequency) {
        return frequency.getID();
    }

    @Override
    public ERepetitionFrequency convertToEntityAttribute(Integer integer) {
        return Stream.of(ERepetitionFrequency.values())
                .filter((e) -> e.getID().equals(integer))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
