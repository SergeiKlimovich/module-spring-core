package com.javaprogram.modulespringcore.util;

import com.javaprogram.modulespringcore.models.Identifiable;
import com.javaprogram.modulespringcore.models.impl.EventImpl;
import com.javaprogram.modulespringcore.models.impl.TicketImpl;
import com.javaprogram.modulespringcore.models.impl.UserImpl;

public class IdGenerator {
    private static long EVENT_COUNTER = 1;
    private static long TICKET_COUNTER = 1;
    private static long USER_COUNTER = 1;

    public long generateId(Class<? extends Identifiable> clazz) {
        if (clazz.equals(EventImpl.class)) {
            return EVENT_COUNTER++;
        }
        if (clazz.equals(TicketImpl.class)) {
            return TICKET_COUNTER++;
        }
        if (clazz.equals(UserImpl.class)) {
            return USER_COUNTER++;
        }
        return 0;
    }
}
