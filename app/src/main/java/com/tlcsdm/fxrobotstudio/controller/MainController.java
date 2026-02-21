package com.tlcsdm.fxrobotstudio.controller;

import com.tlcsdm.fxrobotstudio.FxRobotStudio;
import com.tlcsdm.fxrobotstudio.model.Workflow;
import com.tlcsdm.fxrobotstudio.model.WorkflowStep;
import com.tlcsdm.fxrobotstudio.preferences.AppPreferences;
import com.tlcsdm.fxrobotstudio.util.WorkflowXmlHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ResourceBundle;

public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @FXML
    private TextField workflowNameField;

    @FXML
    private Spinner<Integer> loopCountSpinner;

    @FXML
    private TableView<WorkflowStep> stepsTable;

    @FXML
    private TableColumn<WorkflowStep, Integer> orderColumn;

    @FXML
    private TableColumn<WorkflowStep, String> actionColumn;

    @FXML
    private TableColumn<WorkflowStep, String> descriptionColumn;

    @FXML
    private Button importButton;

    @FXML
    private Button exportButton;

    @FXML
    private Label statusLabel;

    private final ObservableList<WorkflowStep> steps = FXCollections.observableArrayList();
    private Workflow currentWorkflow;

    @FXML
    public void initialize() {
        currentWorkflow = new Workflow("New Workflow");

        loopCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999, 1));

        orderColumn.setCellValueFactory(new PropertyValueFactory<>("order"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        stepsTable.setItems(steps);

        ResourceBundle bundle = FxRobotStudio.getBundle();
        if (bundle != null) {
            statusLabel.setText(bundle.getString("status.ready"));
        }
    }

    @FXML
    public void onImport() {
        ResourceBundle bundle = FxRobotStudio.getBundle();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle != null ? bundle.getString("dialog.import.title") : "Import Workflow");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("fxRobotStudio Workflow (*.fxr)", "*.fxr"));
        File file = fileChooser.showOpenDialog(stepsTable.getScene().getWindow());
        if (file != null) {
            Workflow workflow = WorkflowXmlHandler.importWorkflow(file);
            if (workflow != null) {
                currentWorkflow = workflow;
                workflowNameField.setText(workflow.getName());
                loopCountSpinner.getValueFactory().setValue(workflow.getLoopCount());
                steps.setAll(workflow.getSteps());
                statusLabel.setText(bundle != null ? bundle.getString("status.imported") : "Workflow imported");
                log.info("Imported workflow: {}", file.getName());
            }
        }
    }

    @FXML
    public void onExport() {
        ResourceBundle bundle = FxRobotStudio.getBundle();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle != null ? bundle.getString("dialog.export.title") : "Export Workflow");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("fxRobotStudio Workflow (*.fxr)", "*.fxr"));
        fileChooser.setInitialFileName(currentWorkflow.getName() + ".fxr");
        File file = fileChooser.showSaveDialog(stepsTable.getScene().getWindow());
        if (file != null) {
            currentWorkflow.setName(workflowNameField.getText());
            currentWorkflow.setLoopCount(loopCountSpinner.getValue());
            currentWorkflow.setSteps(steps.stream().toList());
            WorkflowXmlHandler.exportWorkflow(currentWorkflow, file);
            statusLabel.setText(bundle != null ? bundle.getString("status.exported") : "Workflow exported");
            log.info("Exported workflow: {}", file.getName());
        }
    }

    @FXML
    public void onSettings() {
        log.debug("Open settings dialog");
    }

}
