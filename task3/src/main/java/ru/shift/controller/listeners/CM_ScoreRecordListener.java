package ru.shift.controller.listeners;

/**
 * Интерфейс сценария ввода имени рекордсмена.
 * Префикс "CM" обозначает, что ивент вызывается во Controller и ловится в Model.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.view.listeners.VC_ScoreRecordListener}<br>
 * - {@link ru.shift.model.listeners.MV_OnScoresUpdatedListener}
 */
public interface CM_ScoreRecordListener {
    void onNewScore(String name);

}
