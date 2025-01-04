package com.martin.dayplanner.model.storage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public void write(JsonWriter out, LocalTime value) throws IOException {
        if (value == null) {
            out.nullValue(); // Skriv null til JSON hvis verdien er null
        } else {
            out.value(value.format(formatter)); // Skriv formaterte verdier
        }
    }

    @Override
    public LocalTime read(JsonReader in) throws IOException {
        if (in.peek().name().equals("NULL")) { // Hvis JSON-verdien er null
            in.nextNull(); // Hopp over verdien
            return null; // Returner null
        } else {
            return LocalTime.parse(in.nextString(), formatter); // Parse gyldig LocalTime
        }
    }
}
