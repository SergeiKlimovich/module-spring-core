package com.javaprogram.modulespringcore.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.models.Ticket;
import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.models.impl.TicketImpl;
import com.javaprogram.modulespringcore.util.IdGenerator;
import com.javaprogram.modulespringcore.util.Paginator;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class TicketRepository {
    private final Map<Long, Ticket> tickets = new HashMap<>();
    private Paginator<Ticket> paginator;
    private IdGenerator generator;

    public boolean doesEventExistByUserIdAndEventId(long userId, long eventId) {
        LOG.info("Getting tickets by user id {} and event id {}.", userId, eventId);
        return this.tickets
                .values()
                .stream()
                .anyMatch(ticket -> ticket.getUserId() == userId && ticket.getEventId() == eventId);
    }

    public Ticket create(Ticket ticket) {
        ticket.setId(generator.generateId(TicketImpl.class));
        LOG.info("Adding a new ticket: userId - {}, eventId - {}, place - {}, category - {}.", ticket.getUserId(),
                ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
        tickets.put(ticket.getId(), ticket);
        LOG.info("The event was added successfully");
        return tickets.get(ticket.getId());
    }

    public List<Ticket> getByUser(User user, int pageSize, int pageNum) {
        LOG.info("Getting tickets by user id {}. Passed page size - {}, page number - {}", user.getId(), pageSize,
                pageNum);
        return filter(ticket -> ticket.getUserId() == user.getId(), pageSize, pageNum);
    }

    public List<Ticket> getByEvent(Event event, int pageSize, int pageNum) {
        LOG.info("Getting tickets by event id {}. Passed page size - {}, page number - {}", event.getId(),
                pageSize, pageNum);
        return filter(ticket -> ticket.getEventId() == event.getId(), pageSize, pageNum);
    }

    public boolean deleteById(long ticketId) {
        LOG.info("Deleting a ticket by id {}.", ticketId);
        if (!tickets.containsKey(ticketId)) {
            LOG.warn("The ticket was not found with such id");
            return false;
        }
        tickets.remove(ticketId);
        LOG.info("The ticket was deleted successfully");
        return true;
    }

    private List<Ticket> filter(Predicate<Ticket> predicate, int pageSize, int pageNum) {
        return paginator.paginate(this.tickets
                .values()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList()), pageSize, pageNum);
    }
}
