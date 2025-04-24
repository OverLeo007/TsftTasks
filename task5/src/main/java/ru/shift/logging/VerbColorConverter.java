package ru.shift.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import ru.shift.VerbColoring;

public class VerbColorConverter extends ClassicConverter {

    private static final boolean IS_COLORED = System.getProperty("colored") != null;

    private static final Pattern VERB_PATTERN = Pattern.compile(
            "\\b(" + String.join("|", VerbColoring.getColoredWords()) + ")\\b"
    );

    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        if (message == null || message.isBlank() || !IS_COLORED) return message;

        Function<String, String> verbColorFunc = getVerbColorSupplier(message);

        return VERB_PATTERN.matcher(message).replaceAll((MatchResult match) -> {
            String verb = match.group(1);
            return verbColorFunc.apply(verb);
        });
    }

    private Function<String, String> getVerbColorSupplier(String message) {
        boolean isProducer = message.startsWith("Производитель");
        boolean isConsumer = message.startsWith("Потребитель");

        if (isProducer) {
            return VerbColoring::getPV;
        } else if (isConsumer) {
            return VerbColoring::getCV;
        } else {
            return verb -> verb;
        }
    }

}