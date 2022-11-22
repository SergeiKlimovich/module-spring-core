package com.javaprogram.modulespringcore.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.models.Identifiable;
import com.javaprogram.modulespringcore.models.Ticket;
import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.util.json.adapters.EventAdapter;
import com.javaprogram.modulespringcore.util.json.adapters.TicketAdapter;
import com.javaprogram.modulespringcore.util.json.adapters.UserAdapter;

public class JsonMapper {
    private final Gson delegate;

    private static JsonMapper withTypeAdapters() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .disableHtmlEscaping()
                .registerTypeAdapter(Event.class, new EventAdapter())
                .registerTypeAdapter(Ticket.class, new TicketAdapter())
                .registerTypeAdapter(User.class, new UserAdapter());
        return new JsonMapper(gsonBuilder.create());
    }

    public JsonMapper(Gson delegate) {
        this.delegate = delegate;
    }

    public JsonMapper() {
        this.delegate = withTypeAdapters().delegate;
    }

    public <T extends Identifiable> T fromJson(String json, Class<T> clazz) {
        return delegate.fromJson(json, clazz);
    }
}
