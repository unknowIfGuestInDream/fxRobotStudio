package com.tlcsdm.fxrobotstudio.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Represents a single step in an automated workflow.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowStep {

    @XmlAttribute
    private int order;

    @XmlElement(name = "actionType")
    private ActionType actionType;

    @XmlElement(name = "x")
    private int x;

    @XmlElement(name = "y")
    private int y;

    @XmlElement(name = "delayMs")
    private long delayMs;

    @XmlElement(name = "keyCode")
    private String keyCode;

    @XmlElement(name = "text")
    private String text;

    @XmlElement(name = "imagePath")
    private String imagePath;

    @XmlElement(name = "stopImagePath")
    private String stopImagePath;

    @XmlElement(name = "description")
    private String description;

    public WorkflowStep() {
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = delayMs;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStopImagePath() {
        return stopImagePath;
    }

    public void setStopImagePath(String stopImagePath) {
        this.stopImagePath = stopImagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
