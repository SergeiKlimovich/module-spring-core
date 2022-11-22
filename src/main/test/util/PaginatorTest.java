package util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.javaprogram.modulespringcore.models.impl.UserImpl;
import com.javaprogram.modulespringcore.util.Paginator;

public class PaginatorTest {
    private final Paginator<UserImpl> sut = new Paginator<>();

    @Test
    void shouldPaginateEntities() {
        //given
        List<UserImpl> users = Arrays.asList(new UserImpl("Sergei1", "Sergei322@mail.ru"),
                new UserImpl("Sergei2", "Sergei322@mail.ru"),
                new UserImpl("Sergei3", "Sergei322@mail.ru"),
                new UserImpl("Sergei4", "Sergei322@mail.ru"),
                new UserImpl("Sergei5", "Sergei322@mail.ru"),
                new UserImpl("Sergei6", "Sergei322@mail.ru"),
                new UserImpl("Sergei7", "Sergei322@mail.ru"),
                new UserImpl("Sergei8", "Sergei322@mail.ru"),
                new UserImpl("Sergei9", "Sergei322@mail.ru"),
                new UserImpl("Sergei10", "Sergei322@mail.ru"));

        List<UserImpl> expected1 = Arrays.asList(new UserImpl("Sergei4", "Sergei322@mail.ru"),
                new UserImpl("Sergei5", "Sergei322@mail.ru"),
                new UserImpl("Sergei6", "Sergei322@mail.ru"));

        List<UserImpl> expected2 = Arrays.asList(new UserImpl("Sergei10", "Sergei322@mail.ru"));

        List<UserImpl> expected3 = Arrays.asList(new UserImpl("Sergei7", "Sergei322@mail.ru"),
                new UserImpl("Sergei8", "Sergei322@mail.ru"),
                new UserImpl("Sergei9", "Sergei322@mail.ru"),
                new UserImpl("Sergei10", "Sergei322@mail.ru"));

        //when
        List<UserImpl> actual1 = sut.paginate(users, 3, 2);
        List<UserImpl> actual2 = sut.paginate(users, 3, 4);
        List<UserImpl> actual3 = sut.paginate(users, 6, 2);
        List<UserImpl> actual4 = sut.paginate(users, 6, 3);

        //then
        assertThat(actual1, is(expected1));
        assertThat(actual2, is(expected2));
        assertThat(actual3, is(expected3));
        assertThat(actual4, is(Collections.emptyList()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInvalidPageSizePassed() {
        assertThrows(IllegalArgumentException.class, () -> sut.paginate(Collections.emptyList(), 0, 10));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInvalidPageNumberPassed() {
        assertThrows(IllegalArgumentException.class, () -> sut.paginate(Collections.emptyList(), 2, -2));
    }
}
