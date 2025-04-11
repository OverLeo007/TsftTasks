package ru.shift.model.listeners;

import java.util.Map;
import ru.shift.model.GameDifficulty;
import ru.shift.model.scores.Score;

/**
 * Интерфейс сценария обновления списка рекордов
 * Префикс "MV" обозначает, что ивент вызывается во Model и ловится в View.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.controller.listeners.CM_ScoreRecordListener}<br>
 * - {@link ru.shift.view.listeners.VC_ScoreRecordListener}<br>
 * Однако onScoresLoaded не инициализируется действиями пользователя,
 * а происходит при загрузке приложения.
 */
public interface MV_OnScoresUpdatedListener {
    void onScoresLoaded(Map<GameDifficulty, Score> scores);
    void onScoreUpdated(GameDifficulty difficulty, Score score);
}
