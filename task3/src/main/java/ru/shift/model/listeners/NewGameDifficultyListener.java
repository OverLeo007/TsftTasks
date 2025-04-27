package ru.shift.model.listeners;

import ru.shift.model.events.NewGameDifficultyEvent;

public interface NewGameDifficultyListener {
    void onNewGameDifficulty(NewGameDifficultyEvent event);
}
