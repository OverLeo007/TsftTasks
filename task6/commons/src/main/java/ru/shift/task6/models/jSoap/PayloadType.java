package ru.shift.task6.models.jSoap;

public enum PayloadType {
    LEAVE,       // Клиент выходит из чата
    JOIN,        // Клиент успешно зашел
    MESSAGE,     // Сообщение в чат
    USER_LIST,   // Список участников
    ERROR,       // Ошибка на уровне клиента или сервера
    SUCCESS,     // Успешный ответ на какой-то запрос
    AUTH,        // Запрос аутентификации (вход с ником)
    SHUTDOWN    // Запрос на завершение работы отправителя
}

