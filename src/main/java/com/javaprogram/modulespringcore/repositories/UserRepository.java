package com.javaprogram.modulespringcore.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.models.impl.UserImpl;
import com.javaprogram.modulespringcore.util.IdGenerator;
import com.javaprogram.modulespringcore.util.Paginator;
import com.javaprogram.modulespringcore.util.annotation.BindStaticData;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class UserRepository {

    @BindStaticData(fileLocation = "preparedUsers.json", castTo = UserImpl.class)
    private final Map<Long, User> users = new HashMap<>();
    private Paginator<User> paginator;
    private IdGenerator generator;

    public Optional<User> findById(long userId) {
        LOG.info("Get a user by id, {}", userId);
        return Optional.ofNullable(users.get(userId));
    }

    public Optional<User> findByEmail(String email) {
        LOG.info("Get a user by email, {}", email);
        for (Map.Entry<Long, User> user : users.entrySet()) {
            if (user.getValue().getEmail().equals(email)) {
                return Optional.of(user.getValue());
            }
        }
        LOG.warn("User was not found with email {}", email);
        return Optional.empty();
    }

    public List<User> findByName(String name, int pageSize, int pageNum) {
        LOG.info("Get users by name {}. Passed page size - {}, page number - {}", name, pageSize, pageNum);
        return paginator.paginate(this.users
                .values()
                .stream()
                .filter(userEntry -> userEntry.getName().contains(name))
                .collect(Collectors.toList()), pageSize, pageNum);
    }

    public User create(User user) {
        user.setId(generator.generateId(UserImpl.class));
        LOG.info("A new user was created: name - {}, email - {}...", user.getName(), user.getEmail());
        users.put(user.getId(), user);
        LOG.info("The user was created successfully");
        return users.get(user.getId());
    }

    public Optional<User> update(User user) {
        if (users.containsKey(user.getId())) {
            LOG.info("Updating a user by id {}, with the following data: name - {}, email - {}.", user.getId(),
                    user.getName(), user.getEmail());
            users.put(user.getId(), user);
            LOG.info("The user was updated successfully");
            return Optional.of(users.get(user.getId()));
        }
        LOG.warn("Such user was not found.");
        return Optional.empty();
    }

    public boolean deleteById(long userId) {
        LOG.info("Deleting a user by {} id...", userId);
        if (!users.containsKey(userId)) {
            LOG.warn("A user was not found with such id");
            return false;
        }
        users.remove(userId);
        LOG.info("The user was deleted successfully");
        return true;
    }
}
