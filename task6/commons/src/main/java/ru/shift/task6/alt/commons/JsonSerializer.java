package ru.shift.task6.alt.commons;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.ProtocolException;
import ru.shift.task6.alt.commons.protocol.abstracts.AbstractMessageImpl;
import ru.shift.task6.alt.commons.protocol.abstracts.Message;
import ru.shift.task6.alt.commons.protocol.impl.notifications.DisconnectNotification;
import ru.shift.task6.alt.commons.protocol.impl.notifications.JoinNotification;
import ru.shift.task6.alt.commons.protocol.impl.notifications.LeaveNotification;
import ru.shift.task6.alt.commons.protocol.impl.notifications.MessageNotification;
import ru.shift.task6.alt.commons.protocol.impl.requests.AuthRequest;
import ru.shift.task6.alt.commons.protocol.impl.requests.JoinRequest;
import ru.shift.task6.alt.commons.protocol.impl.requests.MessageRequest;
import ru.shift.task6.alt.commons.protocol.impl.requests.UserListRequest;
import ru.shift.task6.alt.commons.protocol.impl.responses.AuthResponse;
import ru.shift.task6.alt.commons.protocol.impl.responses.ErrorResponse;
import ru.shift.task6.alt.commons.protocol.impl.responses.JoinResponse;
import ru.shift.task6.alt.commons.protocol.impl.responses.MessageResponse;
import ru.shift.task6.alt.commons.protocol.impl.responses.UserListResponse;

public class JsonSerializer {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper()
                .findAndRegisterModules()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(Include.NON_NULL)
                .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
                .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
        mapper.registerSubtypes(
                // Auth
                named(AuthRequest.class, MessageType.AUTH_RQ),
                named(AuthResponse.class, MessageType.AUTH_RS),

                // Join
                named(JoinRequest.class, MessageType.JOIN_RQ),
                named(JoinResponse.class, MessageType.JOIN_RS),

                // Users List
                named(UserListRequest.class, MessageType.USER_LIST_RQ),
                named(UserListResponse.class, MessageType.USER_LIST_RS),

                // Message
                named(MessageRequest.class, MessageType.MESSAGE_RQ),
                named(MessageResponse.class, MessageType.MESSAGE_RS),

                // Notifications
                named(JoinNotification.class, MessageType.JOIN_NF),
                named(MessageNotification.class, MessageType.MESSAGE_NF),
                named(LeaveNotification.class, MessageType.LEAVE_NF),

                named(DisconnectNotification.class, MessageType.DISCONNECT_NF),

                // Error
                named(ErrorResponse.class, MessageType.ERROR_RS)
        );
    }

    private static NamedType named(Class<?> clazz, MessageType type) {
        return new NamedType(clazz, type.name());
    }

    public static String serialize(Message message) throws ProtocolException {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new ProtocolException(message.getType()+ " serialization failed", e);
        }
    }

    public static Message deserialize(String data) throws ProtocolException {
        if (data == null) {
            return null;
        }
        try {
            return mapper.readValue(data, AbstractMessageImpl.class);
        } catch (JsonProcessingException e) {
            throw new ProtocolException("Deserialization failed on: " + data, e);
        }
    }
}
