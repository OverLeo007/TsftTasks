package ru.shift;

import lombok.Getter;

public class Resource {
    private static int lastId = 0;
    @Getter
    private final int id;

    public Resource() {
        synchronized (Resource.class) {
            this.id = lastId++;
        }
    }

}
