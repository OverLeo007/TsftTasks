package ru.shift.task6.alt.commons.protocol;

/**
 * NF - notification
 * RQ - request
 * RS - response
 */
public enum MessageType {
    DISCONNECT_NF,
    LEAVE_NF,
    JOIN_NF,
    MESSAGE_NF,
    MESSAGE_RQ,
    MESSAGE_RS,
    JOIN_RQ,
    JOIN_RS,
    USER_LIST_RQ,
    USER_LIST_RS,
    AUTH_RQ,
    AUTH_RS,
    ERROR_RS
}

