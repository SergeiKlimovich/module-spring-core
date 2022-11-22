package com.javaprogram.modulespringcore.util.json.adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.javaprogram.modulespringcore.models.impl.UserImpl;

public class UserAdapter implements JsonDeserializer<UserImpl> {

    private static final String NAME = "name";
    private static final String EMAIL = "email";

    @Override
    public UserImpl deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        return new UserImpl(object.get(NAME).getAsString(),
                object.get(EMAIL).getAsString());
    }
}