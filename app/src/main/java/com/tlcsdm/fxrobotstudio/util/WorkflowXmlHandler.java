package com.tlcsdm.fxrobotstudio.util;

import com.tlcsdm.fxrobotstudio.model.Workflow;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Handles importing and exporting {@link Workflow} objects to/from XML files.
 * The exported files use the {@code .fxr} extension.
 */
public final class WorkflowXmlHandler {

    private static final Logger log = LoggerFactory.getLogger(WorkflowXmlHandler.class);

    private WorkflowXmlHandler() {
    }

    /**
     * Exports a workflow to the given file as XML.
     */
    public static void exportWorkflow(Workflow workflow, File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(Workflow.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(workflow, file);
        } catch (JAXBException e) {
            log.error("Failed to export workflow", e);
        }
    }

    /**
     * Imports a workflow from the given XML file.
     */
    public static Workflow importWorkflow(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(Workflow.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Workflow) unmarshaller.unmarshal(file);
        } catch (JAXBException | IllegalArgumentException e) {
            log.error("Failed to import workflow", e);
            return null;
        }
    }

}
