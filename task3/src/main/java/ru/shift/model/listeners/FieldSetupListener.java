package ru.shift.model.listeners;

import ru.shift.model.events.FieldSetupEvent;

@FunctionalInterface
public interface FieldSetupListener {
    void onFieldSetup(FieldSetupEvent evt);
}
