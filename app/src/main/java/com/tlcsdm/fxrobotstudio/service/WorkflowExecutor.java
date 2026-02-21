package com.tlcsdm.fxrobotstudio.service;

import com.tlcsdm.fxrobotstudio.model.ActionType;
import com.tlcsdm.fxrobotstudio.model.Workflow;
import com.tlcsdm.fxrobotstudio.model.WorkflowStep;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * Executes a {@link Workflow} using JavaFX {@link Robot} to perform
 * mouse and keyboard actions defined in the workflow steps.
 */
public class WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(WorkflowExecutor.class);

    private volatile boolean running;
    private volatile boolean stopped;
    private Thread executionThread;
    private Consumer<String> onStatusUpdate;
    private Runnable onComplete;
    private Robot robot;

    public void setOnStatusUpdate(Consumer<String> onStatusUpdate) {
        this.onStatusUpdate = onStatusUpdate;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public void execute(Workflow workflow) {
        if (running) {
            log.warn("Execution already in progress");
            return;
        }

        stopped = false;
        running = true;

        Platform.runLater(() -> robot = new Robot());

        executionThread = new Thread(() -> {
            try {
                // Small delay to allow Robot initialization
                Thread.sleep(200);
                int loopCount = workflow.getLoopCount();
                List<WorkflowStep> steps = workflow.getSteps();
                log.info("Executing workflow '{}' with {} loops and {} steps",
                        workflow.getName(), loopCount, steps.size());

                for (int loop = 0; loop < loopCount && !stopped; loop++) {
                    int currentLoop = loop + 1;
                    updateStatus("Loop " + currentLoop + "/" + loopCount);

                    for (WorkflowStep step : steps) {
                        if (stopped) {
                            break;
                        }

                        if (step.getDelayMs() > 0) {
                            Thread.sleep(step.getDelayMs());
                        }

                        executeStep(step);
                    }
                }

                log.info("Workflow execution completed");
            } catch (InterruptedException e) {
                log.info("Workflow execution interrupted");
                Thread.currentThread().interrupt();
            } finally {
                running = false;
                if (onComplete != null) {
                    Platform.runLater(onComplete);
                }
            }
        }, "workflow-executor");
        executionThread.setDaemon(true);
        executionThread.start();
    }

    public void stop() {
        stopped = true;
        running = false;
        if (executionThread != null) {
            executionThread.interrupt();
        }
        log.info("Workflow execution stopped");
    }

    public boolean isRunning() {
        return running;
    }

    private void executeStep(WorkflowStep step) {
        if (robot == null || step.getActionType() == null) {
            return;
        }

        Platform.runLater(() -> {
            try {
                switch (step.getActionType()) {
                    case MOUSE_CLICK -> {
                        robot.mouseMove(step.getX(), step.getY());
                        robot.mouseClick(MouseButton.PRIMARY);
                    }
                    case MOUSE_DOUBLE_CLICK -> {
                        robot.mouseMove(step.getX(), step.getY());
                        robot.mouseClick(MouseButton.PRIMARY);
                        robot.mouseClick(MouseButton.PRIMARY);
                    }
                    case MOUSE_RIGHT_CLICK -> {
                        robot.mouseMove(step.getX(), step.getY());
                        robot.mouseClick(MouseButton.SECONDARY);
                    }
                    case MOUSE_MOVE -> robot.mouseMove(step.getX(), step.getY());
                    case KEY_PRESS -> {
                        String keyCode = step.getKeyCode();
                        if (keyCode != null) {
                            try {
                                KeyCode code = KeyCode.valueOf(keyCode.toUpperCase());
                                robot.keyPress(code);
                                robot.keyRelease(code);
                            } catch (IllegalArgumentException e) {
                                log.warn("Unknown key code: {}", keyCode);
                            }
                        }
                    }
                    case KEY_TYPE -> {
                        String text = step.getText();
                        if (text != null) {
                            for (char c : text.toCharArray()) {
                                robot.keyType(KeyCode.getKeyCode(String.valueOf(c)));
                            }
                        }
                    }
                    case SCROLL -> robot.mouseWheel(step.getY());
                    case DELAY -> { /* delay handled above */ }
                    case IMAGE_RECOGNITION -> {
                        log.debug("Image recognition step - imagePath: {}", step.getImagePath());
                    }
                    default -> log.warn("Unknown action type: {}", step.getActionType());
                }
            } catch (Exception e) {
                log.error("Error executing step {}: {}", step.getOrder(), e.getMessage(), e);
            }
        });
    }

    private void updateStatus(String message) {
        if (onStatusUpdate != null) {
            Platform.runLater(() -> onStatusUpdate.accept(message));
        }
    }

}
