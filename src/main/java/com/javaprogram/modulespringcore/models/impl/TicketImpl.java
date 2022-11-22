package com.javaprogram.modulespringcore.models.impl;

import com.javaprogram.modulespringcore.models.Identifiable;
import com.javaprogram.modulespringcore.models.Ticket;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TicketImpl implements Ticket, Identifiable {

    private long id;
    private long userId;
    private long eventId;
    private Ticket.Category category;
    private int place;

    public TicketImpl(long userId, long eventId, Category category, int place) {
        this.userId = userId;
        this.eventId = eventId;
        this.category = category;
        this.place = place;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public long getEventId() {
        return eventId;
    }

    @Override
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getPlace() {
        return place;
    }

    @Override
    public void setPlace(int place) {
        this.place = place;
    }
}