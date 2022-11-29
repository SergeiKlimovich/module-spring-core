package com.javaprogram.modulespringcore.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.models.impl.UserImpl;
import com.javaprogram.modulespringcore.repositories.UserRepository;
import com.javaprogram.modulespringcore.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService sut;

    @Test
    void shouldReturnUserWhenExistIdPassed() {
        //given
        Optional<User> expected = Optional.of(new UserImpl("Sergei", "Sergei111@mail.com"));
        when(userRepository.findById(1)).thenReturn(expected);

        //when
        Optional<User> actual = sut.getUserById(1);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnEmptyWhenNotExistIdPassed() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Optional<User> actual = sut.getUserById(10);

        //then
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    void shouldReturnUserWhenExistEmailPassed() {
        //given
        Optional<User> expected = Optional.of(new UserImpl("Sergei", "Sergei111@mail.com"));
        when(userRepository.findByEmail("Sergei111@mail.com")).thenReturn(expected);

        //when
        Optional<User> actual = sut.getUserByEmail("Sergei111@mail.com");

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnEmptyWhenNotExistEmailPassed() {
        //given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        //when
        Optional<User> actual = sut.getUserByEmail("test");

        //then
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    void shouldDeleteUserWhenExistIdPassed() {
        //given
        when(userRepository.deleteById(1)).thenReturn(true);

        //when
        boolean actual = sut.deleteUser(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteUserWhenNotExistIdPassed() {
        //given
        when(userRepository.deleteById(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.deleteUser(10);

        //then
        assertFalse(actual);
    }

    @Test
    void shouldUpdateUserWhenExistIdPassed() {
        //given
        Optional<User> user = Optional.of(new UserImpl("Sergei", "Sergei111@mail.com"));
        Optional<User> expected = Optional.of(new UserImpl("SergeiUpdated", "Sergei111@mail.com"));
        when(userRepository.update(user.get())).thenReturn(expected);

        //when
        Optional<User> actual = sut.updateUser(user.get());

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnEmptyUserWhenNotExistIdPassedWhileUpdating() {
        //given
        Optional<User> user = Optional.of(new UserImpl("Sergei", "Sergei111@mail.com"));
        when(userRepository.update(any(User.class))).thenReturn(Optional.empty());

        //when
        Optional<User> actual = sut.updateUser(user.get());

        //then
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    void shouldCreateUser() {
        //given
        Optional<User> user = Optional.of(new UserImpl("Sergei", "Sergei111@mail.com"));
        User expected = new UserImpl("Sergei", "Sergei111@mail.com");
        when(userRepository.create(user.get())).thenReturn(expected);

        //when
        User actual = sut.createUser(user.get());

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnNonEmptyUsers() {
        //given
        int pageSize = 5;
        int pageNumber = 2;
        String name = "Sergei";
        List<User> expected = Arrays.asList(new UserImpl("Sergei", "Sergei111@mail.com"),
                new UserImpl("Sergei22", "Sergei@mail.ru"),
                new UserImpl("Sergei33", "Mimir@gmail.eu"));
        when(userRepository.findByName(name, pageSize, pageNumber)).thenReturn(expected);

        //when
        List<User> actual = sut.getUsersByName(name, pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }
}
