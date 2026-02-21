package com.tlcsdm.fxrobotstudio.service;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.tlcsdm.fxrobotstudio.model.ActionType;
import com.tlcsdm.fxrobotstudio.model.WorkflowStep;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Records global keyboard and mouse events using jNativeHook
 * and converts them into {@link WorkflowStep} entries.
 */
public class WorkflowRecorder implements NativeKeyListener, NativeMouseInputListener {

    private static final Logger log = LoggerFactory.getLogger(WorkflowRecorder.class);

    private final List<WorkflowStep> recordedSteps = new ArrayList<>();
    private long lastEventTime;
    private int stepOrder;
    private boolean recording;
    private Consumer<WorkflowStep> onStepRecorded;

    public void setOnStepRecorded(Consumer<WorkflowStep> onStepRecorded) {
        this.onStepRecorded = onStepRecorded;
    }

    public void startRecording() {
        recordedSteps.clear();
        stepOrder = 0;
        lastEventTime = System.currentTimeMillis();
        recording = true;

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.addNativeMouseMotionListener(this);
            log.info("Recording started");
        } catch (NativeHookException e) {
            log.error("Failed to register native hook", e);
            recording = false;
        }
    }

    public void stopRecording() {
        recording = false;
        GlobalScreen.removeNativeKeyListener(this);
        GlobalScreen.removeNativeMouseListener(this);
        GlobalScreen.removeNativeMouseMotionListener(this);

        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            log.error("Failed to unregister native hook", e);
        }
        log.info("Recording stopped, {} steps recorded", recordedSteps.size());
    }

    public List<WorkflowStep> getRecordedSteps() {
        return new ArrayList<>(recordedSteps);
    }

    public boolean isRecording() {
        return recording;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (!recording) {
            return;
        }
        long now = System.currentTimeMillis();
        long delay = now - lastEventTime;
        lastEventTime = now;

        WorkflowStep step = new WorkflowStep();
        step.setOrder(++stepOrder);
        step.setActionType(ActionType.KEY_PRESS);
        step.setKeyCode(NativeKeyEvent.getKeyText(e.getKeyCode()));
        step.setDelayMs(delay);
        step.setDescription("Key press: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        recordedSteps.add(step);
        notifyStepRecorded(step);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        if (!recording) {
            return;
        }
        long now = System.currentTimeMillis();
        long delay = now - lastEventTime;
        lastEventTime = now;

        WorkflowStep step = new WorkflowStep();
        step.setOrder(++stepOrder);
        step.setX(e.getX());
        step.setY(e.getY());
        step.setDelayMs(delay);

        if (e.getButton() == NativeMouseEvent.BUTTON1) {
            if (e.getClickCount() == 2) {
                step.setActionType(ActionType.MOUSE_DOUBLE_CLICK);
                step.setDescription("Double click at (" + e.getX() + "," + e.getY() + ")");
            } else {
                step.setActionType(ActionType.MOUSE_CLICK);
                step.setDescription("Click at (" + e.getX() + "," + e.getY() + ")");
            }
        } else if (e.getButton() == NativeMouseEvent.BUTTON2) {
            step.setActionType(ActionType.MOUSE_RIGHT_CLICK);
            step.setDescription("Right click at (" + e.getX() + "," + e.getY() + ")");
        } else {
            step.setActionType(ActionType.MOUSE_CLICK);
            step.setDescription("Click at (" + e.getX() + "," + e.getY() + ")");
        }

        recordedSteps.add(step);
        notifyStepRecorded(step);
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        // handled in nativeMouseClicked
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        // handled in nativeMouseClicked
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        // not recording mouse movement by default to avoid flooding
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        // not recording mouse drag by default to avoid flooding
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // not recording key release
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // not recording key typed
    }

    private void notifyStepRecorded(WorkflowStep step) {
        if (onStepRecorded != null) {
            Platform.runLater(() -> onStepRecorded.accept(step));
        }
    }

}
