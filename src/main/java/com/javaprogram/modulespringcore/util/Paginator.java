package com.javaprogram.modulespringcore.util;

import java.util.Collections;
import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class Paginator<T> {

    private String warnMessage;

    public List<T> paginate(List<T> entities, int pageSize, int pageNumber) {
        if (pageSize <= 0 || pageNumber <= 0) {
            LOG.warn(warnMessage, pageSize, pageNumber);
            throw new IllegalArgumentException();
        }

        int startIndex = (pageNumber - 1) * pageSize;

        if (entities.size() <= startIndex) {
            return Collections.emptyList();
        }
        return entities.subList(startIndex, Math.min(startIndex + pageSize, entities.size()));
    }
}
