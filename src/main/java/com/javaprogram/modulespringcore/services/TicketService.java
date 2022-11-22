package com.javaprogram.modulespringcore.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.models.Ticket;
import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.models.impl.TicketImpl;
import com.javaprogram.modulespringcore.repositories.EventRepository;
import com.javaprogram.modulespringcore.repositories.TicketRepository;
import com.javaprogram.modulespringcore.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TicketService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public Optional<Ticket> bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        if (userRepository.findById(userId).isPresent() && eventRepository.findById(eventId).isPresent()) {
            if (ticketRepository.doesEventExistByUserIdAndEventId(userId, eventId)) {
                LOG.warn("An event with passed ids(userId - {}, eventId - {}) is added", userId, eventId);
                throw new IllegalStateException();
            }
            Ticket ticket = new TicketImpl(userId, eventId, category, place);
            return Optional.of(ticketRepository.create(ticket));
        }
        LOG.warn("Either a user or an event does not exist with the passed ids: userId - {}, eventId - {}", userId,
                eventId);
        return Optional.empty();
    }

    public List<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNum) {
        return ticketRepository.getByUser(user, pageSize, pageNum)
                .stream()
                .map(ticket -> ImmutablePair.of(eventRepository.findById(ticket.getEventId()).get(), ticket))
                .sorted(Comparator.comparing(pair -> pair.getLeft().getDate(), Comparator.reverseOrder()))
                .map(Pair::getRight)
                .collect(Collectors.toList());
    }

    public List<Ticket> getBookedTicketsByEvent(Event event, int pageSize, int pageNum) {
        return ticketRepository.getByEvent(event, pageSize, pageNum)
                .stream()
                .map(ticket -> ImmutablePair.of(userRepository.findById(ticket.getUserId()).get(), ticket))
                .sorted(Comparator.comparing(pair -> pair.getLeft().getEmail()))
                .map(Pair::getRight)
                .collect(Collectors.toList());
    }

    public boolean cancelTicket(long ticketId) {
        return ticketRepository.deleteById(ticketId);
    }
}
