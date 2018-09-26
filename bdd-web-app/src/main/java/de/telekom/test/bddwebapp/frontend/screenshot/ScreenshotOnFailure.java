package de.telekom.test.bddwebapp.frontend.screenshot;

import de.telekom.test.bddwebapp.frontend.lifecycle.WebDriverWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterScenario.Outcome;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.failures.PendingStepFound;
import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static java.text.MessageFormat.format;

/**
 * Add failure screenshot to report
 *
 * @author Daniel Keiss {@literal <daniel.keiss@telekom.de>}
 * <p>
 * Copyright (c) 2018 Daniel Keiss, Deutsche Telekom AG
 * This file is distributed under the conditions of the Apache License, Version 2.0.
 * For details see the file license on the toplevel.
 */
@AllArgsConstructor
public class ScreenshotOnFailure {

    protected final StoryReporterBuilder reporterBuilder;
    protected final WebDriverWrapper webDriverWrapper;
    private final Logger logger = LoggerFactory.getLogger(ScreenshotOnFailure.class);

    @AfterScenario(uponType = ScenarioType.EXAMPLE, uponOutcome = Outcome.FAILURE)
    public void afterScenarioWithExamplesFailure(UUIDExceptionWrapper uuidWrappedFailure) {
        afterScenarioFailure(uuidWrappedFailure);
    }

    @AfterScenario(uponType = ScenarioType.NORMAL, uponOutcome = Outcome.FAILURE)
    public void afterScenarioFailure(UUIDExceptionWrapper uuidWrappedFailure) {
        if (uuidWrappedFailure instanceof PendingStepFound) {
            return;
        }
        String screenshotPath = screenshotPath(uuidWrappedFailure.getUUID());
        String currentUrl = "[unknown page title]";
        try {
            currentUrl = webDriverWrapper.getDriver().getCurrentUrl();
        } catch (Exception ignored) {
        }
        try {
            screenshotPath = webDriverWrapper.saveScreenshotTo(screenshotPath);
        } catch (Exception e) {
            logger.error(format("Screenshot of page '{0}' failed", currentUrl), e);
        }
        if (StringUtils.isNoneBlank(screenshotPath)) {
            logger.info(format("Screenshot of page '{0}' has been saved to '{1}'", currentUrl, screenshotPath));
        } else {
            logger.error(format("Screenshot of page '{0}' has **NOT** been saved'", currentUrl));
        }
    }

    protected String screenshotPath(UUID uuid) {
        return format("{0}/screenshots/failed-scenario-{1}.png", reporterBuilder.outputDirectory(), uuid);
    }

}