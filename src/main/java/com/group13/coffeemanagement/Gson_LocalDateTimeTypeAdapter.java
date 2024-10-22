package com.group13.coffeemanagement;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Gson_LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

    // Định dạng ISO-8601: yyyy-MM-dd'T'HH:mm:ss
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.nullValue();
        } else {
            // Ghi LocalDateTime dưới dạng chuỗi
            jsonWriter.value(localDateTime.format(FORMATTER));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String dateTimeString = jsonReader.nextString();
        // Chuyển chuỗi thành đối tượng LocalDateTime
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }
}
