package dev.cobblesword.converters;

import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

@ReadingConverter
public class BinaryToUUIDConverter implements Converter<Binary, UUID> {
    @Override
    public UUID convert(Binary source) {
        ByteBuffer bb = ByteBuffer.wrap(source.getData());
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }
}
