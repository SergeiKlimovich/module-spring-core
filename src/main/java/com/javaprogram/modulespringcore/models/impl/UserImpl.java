package com.javaprogram.modulespringcore.models.impl;

import com.javaprogram.modulespringcore.models.Identifiable;
import com.javaprogram.modulespringcore.models.User;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserImpl implements User, Identifiable {

    private long id;
    private String name;
    private String email;

    public UserImpl(String name, String email) {
        this.name = name;
        this.email = email;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}

