package com.tlcsdm.fxrobotstudio;

import com.tlcsdm.fxrobotstudio.model.ActionType;
import com.tlcsdm.fxrobotstudio.model.Workflow;
import com.tlcsdm.fxrobotstudio.model.WorkflowStep;
import com.tlcsdm.fxrobotstudio.util.WorkflowXmlHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowXmlTest {

    @TempDir
    Path tempDir;

    @Test
    void testExportAndImportWorkflow() {
        Workflow workflow = new Workflow("Test Workflow");
        workflow.setDescription("A test workflow");
        workflow.setLoopCount(3);

        WorkflowStep step1 = new WorkflowStep();
        step1.setOrder(1);
        step1.setActionType(ActionType.MOUSE_CLICK);
        step1.setX(100);
        step1.setY(200);
        step1.setDelayMs(500);
        step1.setDescription("Click at (100,200)");

        WorkflowStep step2 = new WorkflowStep();
        step2.setOrder(2);
        step2.setActionType(ActionType.KEY_PRESS);
        step2.setKeyCode("ENTER");
        step2.setDelayMs(200);
        step2.setDescription("Press Enter");

        workflow.setSteps(List.of(step1, step2));

        File file = tempDir.resolve("test.fxr").toFile();
        WorkflowXmlHandler.exportWorkflow(workflow, file);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        Workflow imported = WorkflowXmlHandler.importWorkflow(file);

        assertNotNull(imported);
        assertEquals("Test Workflow", imported.getName());
        assertEquals("A test workflow", imported.getDescription());
        assertEquals(3, imported.getLoopCount());
        assertEquals(2, imported.getSteps().size());

        WorkflowStep importedStep1 = imported.getSteps().get(0);
        assertEquals(1, importedStep1.getOrder());
        assertEquals(ActionType.MOUSE_CLICK, importedStep1.getActionType());
        assertEquals(100, importedStep1.getX());
        assertEquals(200, importedStep1.getY());

        WorkflowStep importedStep2 = imported.getSteps().get(1);
        assertEquals(2, importedStep2.getOrder());
        assertEquals(ActionType.KEY_PRESS, importedStep2.getActionType());
        assertEquals("ENTER", importedStep2.getKeyCode());
    }

    @Test
    void testImportNonExistentFile() {
        File file = tempDir.resolve("nonexistent.fxr").toFile();
        Workflow result = WorkflowXmlHandler.importWorkflow(file);
        assertNull(result);
    }

    @Test
    void testWorkflowDefaultValues() {
        Workflow workflow = new Workflow();
        assertEquals("1.0", workflow.getVersion());
        assertEquals(1, workflow.getLoopCount());
        assertNotNull(workflow.getSteps());
        assertTrue(workflow.getSteps().isEmpty());
    }

    @Test
    void testI18nBundlesExist() {
        ResourceBundle enBundle = ResourceBundle.getBundle(
                FxRobotStudio.BUNDLE_BASE_NAME, Locale.ENGLISH);
        assertNotNull(enBundle);
        assertEquals("fxRobotStudio", enBundle.getString("app.title"));

        ResourceBundle zhBundle = ResourceBundle.getBundle(
                FxRobotStudio.BUNDLE_BASE_NAME, Locale.forLanguageTag("zh"));
        assertNotNull(zhBundle);
        assertEquals("fxRobotStudio", zhBundle.getString("app.title"));

        ResourceBundle jaBundle = ResourceBundle.getBundle(
                FxRobotStudio.BUNDLE_BASE_NAME, Locale.forLanguageTag("ja"));
        assertNotNull(jaBundle);
        assertEquals("fxRobotStudio", jaBundle.getString("app.title"));
    }

    @Test
    void testActionTypeEnumValues() {
        assertEquals(10, ActionType.values().length);
        assertNotNull(ActionType.MOUSE_CLICK);
        assertNotNull(ActionType.KEY_PRESS);
        assertNotNull(ActionType.IMAGE_RECOGNITION);
        assertNotNull(ActionType.DELAY);
    }

    @Test
    void testImageRecognitionStep() {
        WorkflowStep step = new WorkflowStep();
        step.setOrder(1);
        step.setActionType(ActionType.IMAGE_RECOGNITION);
        step.setImagePath("/path/to/target.png");
        step.setStopImagePath("/path/to/stop.png");
        step.setDescription("Find image and click");

        assertEquals(ActionType.IMAGE_RECOGNITION, step.getActionType());
        assertEquals("/path/to/target.png", step.getImagePath());
        assertEquals("/path/to/stop.png", step.getStopImagePath());
    }

}
