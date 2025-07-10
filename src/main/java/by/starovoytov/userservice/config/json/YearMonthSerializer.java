package by.starovoytov.userservice.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class YearMonthSerializer extends JsonSerializer<YearMonth> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public void serialize(YearMonth value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.format(FORMATTER));
        }
    }
}