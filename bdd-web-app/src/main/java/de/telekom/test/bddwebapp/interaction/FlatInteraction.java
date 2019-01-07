package de.telekom.test.bddwebapp.interaction;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toMap;

/**
 * Save the data flat for the test execution.
 * <p>
 * If data is stored in this interaction, the values from maps and lists are stored recursively too.
 * The following examples illustrate this:
 * - remember("key", "value") = ["key", "value"]
 * - remember("key.key", "value") = ["key.key", "value"]
 * - remember("key", ["key": "value"]) = ["key": ["key": "value"], "key.key", "value"]
 * - remember("key", [["key": "value"]] = ["key": ["value"], "key[0].key", "value"]
 * Further examples can be found in @see FlatInteractionSpec.
 * <p>
 * The advantage of this approach is the safe and fast availability of the test data, but be careful when saving large and deep maps.
 *
 * @author Daniel Keiss {@literal <daniel.keiss@telekom.de>}
 * <p>
 * Copyright (c) 2018 Daniel Keiss, Deutsche Telekom AG
 * This file is distributed under the conditions of the Apache License, Version 2.0.
 * For details see the file license on the toplevel.
 */
public abstract class FlatInteraction implements Interaction {

    private static String LIST_ITEM_FORMAT = "[%d]";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Getter
    protected Map<String, Object> context = Maps.newHashMap();

    public void startInteraction() {
        context = Maps.newHashMap();
    }

    public void stopInteraction() {
        startInteraction();
    }

    /**
     * Store some test data in the interaction context for later use.
     * Converts maps and list so that they are flat.
     * Also saves the maps and lists as individual objects.
     * Be careful when saving large and deep maps as this approach increases the amount of data exponentially!
     */
    public void remember(String key, Object value) {
        if (value instanceof Map) {
            ((Map) value).forEach((entryKey, entryValue) -> remember(key + OBJECT_KEY_SEPARATOR + entryKey.toString(), entryValue));
        }
        if (value instanceof List) {
            List list = ((List) value);
            for (int i = 0; i < list.size(); i++) {
                remember(key + String.format(LIST_ITEM_FORMAT, i), list.get(i));
            }
        }
        context.put(key, value);
    }

    // -------------------------------------------------------------------------
    // DEBUGGING
    // -------------------------------------------------------------------------

    /**
     * Be careful with this method. The log can get very large. Use only for debugging purposes!
     */
    public void logAllPossibleKeysWithType() {
        carefulHint("logAllPossibleKeysWithType()");
        Map<String, Class> keysWithType = context.entrySet().stream()
                .collect(toMap(Entry::getKey, entry -> entry.getValue().getClass()));
        logger.info("\n" + mapToString(keysWithType));
    }

    /**
     * Be careful with this method. The log can get very large. Use only for debugging purposes!
     */
    public void logAllPossibleKeysWithValue() {
        carefulHint("logAllPossibleKeysWithValue()");
        logger.info("\n" + mapToString(context));
    }

    private void carefulHint(String method) {
        logger.info("\n==============================================================================================\n" +
                "Be careful with " + method + ". The log can get very large. Use only for debugging purposes!\n" +
                "==============================================================================================");
    }

    private String mapToString(Map<String, ?> map) {
        return Joiner.on(",\n").withKeyValueSeparator("=").join(map);
    }

}
