package ru.shift.task6.commons.protocol.abstracts;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.shift.task6.commons.protocol.MessageType;

@JsonTypeInfo(
        use = Id.NAME,
        include = As.EXISTING_PROPERTY,
        property = "type"
)
@JsonPropertyOrder({"type", "id"})
@Getter
@Setter
public abstract class AbstractMessageImpl implements Message {

    protected String id;

    public AbstractMessageImpl(@NonNull String id) {
        this.id = id;
    }

    public AbstractMessageImpl() {
        id = generateId();
    }

    @Override
    @JsonGetter
    public abstract MessageType getType();


    @Override
    public String toString() {
        return "Message{" + getType().name() + "}";
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
