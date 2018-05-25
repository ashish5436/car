package de.telekom.jbehave.webapp.interaction;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

interface Interaction {

    /**
     * Store some data in the interaction context for later use.
     */
    void remember(String key, Object value);

    /**
     * Get some data in the interaction context.
     */
    default <S> S recall(String key) {
        return (S) getContext().get(key);
    }

    default <S> S recallNotNull(String key) {
        S value = recall(key);
        assertNotNull(String.format("Recalled '%s' for story interaction value '%s'", value, key), value);
        return value;
    }

    Map<String, Object> getContext();

}
