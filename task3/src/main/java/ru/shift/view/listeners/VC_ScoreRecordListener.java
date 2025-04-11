package ru.shift.view.listeners;

/**
 * Интерфейс сценария ввода имени рекордсмена.
 * Префикс "VC" обозначает, что ивент вызывается во View и ловится в Controller.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.controller.listeners.CM_ScoreRecordListener}<br>
 * - {@link ru.shift.model.listeners.MV_OnScoresUpdatedListener}
 */
public interface VC_ScoreRecordListener {

    void onRecordNameEntered(String name);
}
