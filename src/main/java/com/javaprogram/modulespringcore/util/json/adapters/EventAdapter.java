package com.javaprogram.modulespringcore.util.json.adapters;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.models.impl.EventImpl;

public class EventAdapter implements JsonDeserializer<Event> {

    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Event deserialize(JsonElement jsonElement,
                             Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        return new EventImpl(object.get(TITLE).getAsString(),
                parseDate(object));
    }

    private Date parseDate(JsonObject object) {
        try {
            return FORMAT.parse(object.get(DATE).getAsString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
