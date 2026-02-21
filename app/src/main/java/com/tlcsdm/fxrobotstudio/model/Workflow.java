package com.tlcsdm.fxrobotstudio.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Root element for a workflow definition.
 * Exported files use the {@code .fxr} extension.
 */
@XmlRootElement(name = "workflow")
@XmlAccessorType(XmlAccessType.FIELD)
public class Workflow {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String version = "1.0";

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "loopCount")
    private int loopCount = 1;

    @XmlElementWrapper(name = "steps")
    @XmlElement(name = "step")
    private List<WorkflowStep> steps = new ArrayList<>();

    public Workflow() {
    }

    public Workflow(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public List<WorkflowStep> getSteps() {
        return steps;
    }

    public void setSteps(List<WorkflowStep> steps) {
        this.steps = steps;
    }

}
