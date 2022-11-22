package com.javaprogram.modulespringcore.repositories;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.models.impl.EventImpl;
import com.javaprogram.modulespringcore.util.IdGenerator;
import com.javaprogram.modulespringcore.util.Paginator;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class EventRepository {

    private final Map<Long, Event> events = new HashMap<>();
    private Paginator<Event> paginator;
    private IdGenerator generator;

    public Optional<Event> findById(long eventId) {
        LOG.info("Getting an event by id {}.", eventId);
        return Optional.ofNullable(events.get(eventId));
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        LOG.info("Getting events by title {}. Passed page size - {}, page number - {}", title, pageSize, pageNum);
        return filter(event -> event.getTitle().contains(title), pageSize, pageNum);
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOG.info("Getting events by day {}. Passed page size - {}, page number - {}", day, pageSize, pageNum);
        return filter(event -> event.getDate().equals(day), pageSize, pageNum);
    }

    public Event create(Event event) {
        event.setId(generator.generateId(EventImpl.class));
        LOG.info("Adding a new event: title - {}, date - {}...", event.getTitle(), event.getDate());
        events.put(event.getId(), event);
        LOG.info("The event was added successfully");
        return events.get(event.getId());
    }

    public Optional<Event> update(Event event) {
        if (events.containsKey(event.getId())) {
            LOG.info("Updating an event by id {} with the following data: title - {}, date - {}...", event.getId(),
                    event.getTitle(), event.getDate());
            events.put(event.getId(), event);
            LOG.info("The event was updated successfully");
            return Optional.of(events.get(event.getId()));
        }
        LOG.warn("Such event was not found while updating");
        return Optional.empty();
    }

    public boolean deleteById(long eventId) {
        LOG.info("Deleting an event by id {}.", eventId);
        if (!events.containsKey(eventId)) {
            LOG.warn("The event was not found with such id");
            return false;
        }
        events.remove(eventId);
        LOG.info("The event was deleted successfully");
        return true;
    }

    private List<Event> filter(Predicate<Event> predicate, int pageSize, int pageNum) {
        return paginator.paginate(events
                .values()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList()), pageSize, pageNum);
    }
}
