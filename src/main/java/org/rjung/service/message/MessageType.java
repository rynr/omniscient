package org.rjung.service.message;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link Message} has a {@link MessageType} and can even be superseded with a
 * new {@link MessageType}. So the {@link MessageType} declares some kind of
 * state-machine. Every {@link MessageType} can allow the transition to a
 * different {@link MessageType} via an event. An event is defined as a
 * {@link String}, all available events can be requested by calling
 * {@link #getActions()}. The new {@link MessageType} after an event is returned
 * by {@link #event(String)}.
 */
public enum MessageType {

    LOG("≣", Pattern.compile("\\A\\s*(≣|-)\\s*")) {
        @Override
        public Map<String, MessageType> getTransitions() {
            return Collections.emptyMap();
        }
    },
    INFO("ℹ", Pattern.compile("\\A\\s*(ℹ|i|I)\\s*")) {
        @Override
        public Map<String, MessageType> getTransitions() {
            return Collections.emptyMap();
        }
    },
    BOOKMARK("❯", Pattern.compile("\\A\\s*(❯|>)\\s*")) {
        @Override
        public Map<String, MessageType> getTransitions() {
            return Collections.emptyMap();
        }
    },
    SNIPPET("¶", Pattern.compile("\\A\\s*(¶|\\$)\\s*")) {
        @Override
        public Map<String, MessageType> getTransitions() {
            return Collections.emptyMap();
        }
    },
    TODO("☐", Pattern.compile("\\A\\s*(☐|\\[\\s*\\])\\s*")) {
        @Override
        public Map<String, MessageType> getTransitions() {
            return Stream.of(new SimpleEntry<>("close", MessageType.DONE))
                    .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
        }
    },
    DONE("☑", Pattern.compile("\\A\\s*(☑|\\[[xX]\\])\\s*")) {
        @Override
        public Map<String, MessageType> getTransitions() {
            return Stream.of(new SimpleEntry<>("open", MessageType.TODO))
                    .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
        }
    };

    private final String identifier;
    private final Pattern pattern;

    private MessageType(final String identifier, final Pattern pattern) {
        this.identifier = identifier;
        this.pattern = pattern;
    }

    /**
     * Retrieve the identifier of this {@link MessageType}.
     *
     * @return Identifier of this {@link MessageType}
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Clean source-message from identification of {@link MessageType}.
     *
     * @param source
     *            Source-message.
     * @return Source-message without {@link MessageType} specific identifier.
     */
    public String clearMessage(String source) {
        Matcher matcher = pattern.matcher(source);
        return matcher.find() ? source.substring(matcher.end()) : source;
    }

    /**
     * Retrieve a {@link Set} of all available actions that can be called.
     *
     * @return {@link Set} of all available actions that can be called.
     */
    public Set<String> getActions() {
        return getTransitions().keySet();
    }

    /**
     * Retrieve the new resulting {@link MessageType} after a action was called.
     *
     * @param action
     *            The action from {@link #getActions()} that shall be called.
     * @return The new {@link MessageType} after the action was called.
     */
    public MessageType event(final String action) {
        return getTransitions().containsKey(action) ? getTransitions().get(action) : this;
    }

    /**
     * Verify if a source-message defines the {@link MessageType}.
     *
     * @param source
     *            The message to be checked.
     * @return true if the messages defines the {@link MessageType}.
     */
    public boolean match(final String source) {
        return pattern.matcher(source).find();
    }

    /**
     * Retrieve the possible transitions.
     *
     * @return {@link Map} of actions for a {@link MessageType} and it's next
     *         {@link MessageType}.
     */
    public abstract Map<String, MessageType> getTransitions();

    /**
     * Identify the {@link MessageType} of a source-message. If no
     * {@link MessageType} fits, the default is {@link MessageType#LOG}.
     *
     * @param source
     *            The source-message to check.
     * @return The identified {@link MessageType}.
     */
    public static final MessageType getMessageType(String source) {
        for (MessageType type : values()) {
            if (type.match(source)) {
                return type;
            }
        }
        return null;
    }
}
