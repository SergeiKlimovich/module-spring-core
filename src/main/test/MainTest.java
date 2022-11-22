import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.javaprogram.modulespringcore.models.Event;
import com.javaprogram.modulespringcore.models.Ticket;
import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.models.impl.EventImpl;
import com.javaprogram.modulespringcore.models.impl.UserImpl;
import com.javaprogram.modulespringcore.services.facade.BookingFacadeImpl;

public class MainTest {

    @Test
    void realTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        BookingFacadeImpl bookingFacadeImpl = context.getBean("bookingFacade", BookingFacadeImpl.class);
        User user = new UserImpl("Petr", "petr555@mail.com");
        User createdUser = bookingFacadeImpl.createUser(user);

        assertThat(createdUser.getId(), is(5L));

        Event event = new EventImpl("NewEvent", new Date(333));

        Event createdEvent = bookingFacadeImpl.createEvent(event);

        assertThat(createdEvent.getId(), is(1L));

        Ticket ticket =
                bookingFacadeImpl.bookTicket(createdUser.getId(), createdEvent.getId(), 5, Ticket.Category.STANDARD);

        assertThat(ticket.getId(), is(1L));
        assertThat(ticket.getUserId(), is(5L));
        assertThat(ticket.getEventId(), is(1L));
        assertThat(ticket.getCategory(), is(Ticket.Category.STANDARD));
        assertThat(ticket.getPlace(), is(5));

        List<Ticket> bookedTickets = bookingFacadeImpl.getBookedTickets(createdUser, 10, 1);

        assertThat(bookedTickets, is(Collections.singletonList(ticket)));

        boolean isCanceled = bookingFacadeImpl.cancelTicket(ticket.getId());
        List<Ticket> bookedTicketsAfterCancellation = bookingFacadeImpl.getBookedTickets(createdUser, 10, 1);

        assertTrue(isCanceled);
        assertThat(bookedTicketsAfterCancellation, is(Collections.emptyList()));

    }

}
