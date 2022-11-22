package com.javaprogram.modulespringcore.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.repositories.EventRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Optional<Event> getById(long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventRepository.getEventsByTitle(title, pageSize, pageNum);
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventRepository.getEventsForDay(day, pageSize, pageNum);
    }

    public Event createEvent(Event event) {
        return eventRepository.create(event);
    }

    public Optional<Event> updateEvent(Event event) {
        return eventRepository.update(event);
    }

    public boolean deleteEvent(long eventId) {
        return eventRepository.deleteById(eventId);
    }
}