package com.javaprogram.modulespringcore.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.models.Ticket;
import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.models.impl.EventImpl;
import com.javaprogram.modulespringcore.models.impl.TicketImpl;
import com.javaprogram.modulespringcore.models.impl.UserImpl;
import com.javaprogram.modulespringcore.repositories.EventRepository;
import com.javaprogram.modulespringcore.repositories.TicketRepository;
import com.javaprogram.modulespringcore.repositories.UserRepository;
import com.javaprogram.modulespringcore.services.TicketService;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private TicketService sut;

    @Test
    void shouldBookWhenExistIdsPassedAndBookingNotAlreadyDone() {
        //given
        Optional<Event> event = createEvent(1, "New Year", new Date());
        Optional<User> user = createUser(1, "Sergei", "Sergei111@mail.com");
        Optional<Ticket> ticket = createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2);
        Ticket expected = ticket.get();
        when(userRepository.findById(1)).thenReturn(user);
        when(eventRepository.findById(1)).thenReturn(event);
        when(ticketRepository.doesEventExistByUserIdAndEventId(1, 1)).thenReturn(false);
        when(ticketRepository.create(any(TicketImpl.class))).thenReturn(expected);

        //when
        Optional<Ticket> actual = sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM);

        //then
        assertThat(actual.get(), is(expected));
    }

    @Test
    void shouldNotBookWhenNotExistUserIdPassed() {
        //given
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        //when
        Optional<Ticket> actual = sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM);

        //then
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    void shouldNotBookWhenNotExistEventIdPassed() {
        //given
        when(userRepository.findById(1)).thenReturn(createUser(1, "Sergei", "Sergei111@mail.com"));
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        //when
        Optional<Ticket> actual = sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM);

        //then
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenBookingAlreadyDone() {
        //when
        Optional<Event> event = createEvent(1, "New Year", new Date());
        Optional<User> user = createUser(1, "Sergei", "Sergei111@mail.com");
        when(userRepository.findById(1)).thenReturn(user);
        when(eventRepository.findById(1)).thenReturn(event);
        when(ticketRepository.doesEventExistByUserIdAndEventId(1, 1)).thenReturn(true);

        //then
        assertThrows(IllegalStateException.class, () -> sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM));
    }

    @Test
    void shouldGetBookingsByUser() {
        //given
        int pageSize = 5;
        int pageNumber = 2;

        Optional<User> user = createUser(1, "Sergei", "Sergei111@mail.com");

        List<Ticket> tickets = Arrays.asList(createTicket(1, 1, 2, Ticket.Category.PREMIUM, 2).get(),
                createTicket(3, 1, 1, Ticket.Category.BAR, 5).get(),
                createTicket(10, 1, 3, Ticket.Category.STANDARD, 10).get());

        List<Event> events = Arrays.asList(createEvent(1, "New Year", new Date(123)).get(),
                createEvent(2, "New Year After-party", new Date(233)).get(),
                createEvent(3, "New Year Pre_party", new Date(344)).get());

        List<Ticket> expected = Arrays.asList(createTicket(10, 1, 3, Ticket.Category.STANDARD, 10).get(),
                createTicket(1, 1, 2, Ticket.Category.PREMIUM, 2).get(),
                createTicket(3, 1, 1, Ticket.Category.BAR, 5).get());

        when(ticketRepository.getByUser(user.get(), pageSize, pageNumber)).thenReturn(tickets);
        when(eventRepository.findById(1)).thenReturn(Optional.of(events.get(0)));
        when(eventRepository.findById(2)).thenReturn(Optional.of(events.get(1)));
        when(eventRepository.findById(3)).thenReturn(Optional.of(events.get(2)));

        //when
        List<Ticket> actual = sut.getBookedTicketsByUser(user.get(), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldGetBookingsByEvent() {
        //given
        int pageSize = 5;
        int pageNumber = 2;

        Optional<Event> event = createEvent(1, "New Year", new Date(123));

        List<Ticket> tickets = Arrays.asList(createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2).get(),
                createTicket(3, 2, 1, Ticket.Category.BAR, 5).get(),
                createTicket(10, 3, 1, Ticket.Category.STANDARD, 10).get());

        List<User> users = Arrays.asList(createUser(1, "ASergei", "ASergei111@mail.com").get(),
                createUser(2, "Sergei22", "Mimir@gmail.eu").get(),
                createUser(3, "Sergei33", "BSergei111@mail.ru").get());

        List<Ticket> expected = Arrays.asList(createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2).get(),
                createTicket(10, 3, 1, Ticket.Category.STANDARD, 10).get(),
                createTicket(3, 2, 1, Ticket.Category.BAR, 5).get());

        when(ticketRepository.getByEvent(event.get(), pageSize, pageNumber)).thenReturn(tickets);
        when(userRepository.findById(1)).thenReturn(Optional.of(users.get(0)));
        when(userRepository.findById(2)).thenReturn(Optional.of(users.get(1)));
        when(userRepository.findById(3)).thenReturn(Optional.of(users.get(2)));

        //when
        List<Ticket> actual = sut.getBookedTicketsByEvent(event.get(), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));

    }

    @Test
    void shouldDeleteEventWhenExistIdPassed() {
        //given
        when(ticketRepository.deleteById(1)).thenReturn(true);

        //when
        boolean actual = sut.cancelTicket(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteEventWhenNotExistIdPassed() {
        //given
        when(ticketRepository.deleteById(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.cancelTicket(10);

        //then
        assertFalse(actual);
    }

    private Optional<Event> createEvent(long id, String title, Date date) {
        EventImpl event = new EventImpl(title, date);
        event.setId(id);
        return Optional.of(event);
    }

    private Optional<User> createUser(long id, String name, String email) {
        UserImpl user = new UserImpl(name, email);
        user.setId(id);
        return Optional.of(user);
    }

    private Optional<Ticket> createTicket(long id, long userId, long eventId, Ticket.Category category, int place) {
        Ticket ticket = new TicketImpl(userId, eventId, category, place);
        ticket.setId(id);
        return Optional.of(ticket);
    }
}