package com.martin.dayplanner.model.storage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue(); // Skriv en null-verdi til JSON hvis LocalDate er null
        } else {
            out.value(value.format(formatter)); // Skriv formaterte LocalDate-verdier
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek().name().equals("NULL")) { // Håndter null-verdi under lesing
            in.nextNull();
            return null;
        } else {
            return LocalDate.parse(in.nextString(), formatter); // Parse gyldige verdier
        }
    }
}
