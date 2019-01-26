package app.maven;

import app.util.CommonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service(value="mavenServiceImpl")
public class MavenServiceImpl implements CommonService {

    @Autowired
    private MavenInvokerService mavenInvokerService;

    private ObservableList<String> finalCommandList = FXCollections.observableArrayList();
    private ObservableList<String> candidateCommandList = FXCollections.observableArrayList();

    private ObservableList<String> finalProjectList = FXCollections.observableArrayList();
    private ObservableList<String> candidateProjectList = FXCollections.observableArrayList();

    private StringBuilder commandMaven = new StringBuilder();
    private StringBuilder resultAppendString = new StringBuilder();

    private String errorMessage;

    @Override
    public void finalProjectToBuild(ListView<String> projectsFinal, ListView<String> listOfBranches) {
        removeElement(projectsFinal, selectedIndex(projectsFinal));
        listOfBranches.getItems().clear();
    }

    @Override
    public void candidateProjectToBuild(ListView<String> projectsCandidate, ListView<String> projectsFinal) {
        addElementToList(finalProjectList, projectsCandidate);
        projectsFinal.setItems(finalProjectList);

    }

    @Override
    public void finalCommandToBuild(ListView<String> commandCandidate, ListView<String> commandFinal) {
        addElementToList(candidateCommandList, commandFinal);
        commandCandidate.setItems(candidateCommandList);
        removeElement(commandFinal, selectedIndex(commandFinal));
    }

    @Override
    public void candidateCommandToBuild(ListView<String> commandCandidate, ListView<String> commandFinal) {
        addElementToList(finalCommandList, commandCandidate);
        commandFinal.setItems(finalCommandList);
        removeElement(commandCandidate, selectedIndex(commandCandidate));
    }

    @Override
    public void addCommand(ListView<String> commandCandidate) {
        candidateCommandList.addAll("clean", "validate", "compile", "test", "-DskipTests", "package", "verify", "install", "site",
                "deploy", "jar");
        commandCandidate.setItems(candidateCommandList);
    }

    @Override
    public void buildButton(TextField homePath, ListView<String> projectsCandidate, ListView<String> commandFinal, TextArea resultOutput) {
        appendMavenCommand(commandFinal);
        executeMavenCommand(projectsCandidate, resultOutput);
    }

    private void executeMavenCommand(ListView<String> projectsCandidate, TextArea resultOutput) {
        ArrayList<String> completeListOfCandidate = new ArrayList<>(projectsCandidate.getItems());

        for (String iterator : completeListOfCandidate) {
            mavenInvokerService.mvnBuild(resultAppendString, iterator, commandMaven.toString(), resultOutput, errorMessage);
        }
    }

    private void appendMavenCommand(ListView<String> commandFinal) {
        ArrayList<String> completeListOfMavenCommand = new ArrayList<>(commandFinal.getItems());

        for (String s : completeListOfMavenCommand) {
            commandMaven.append(s);
            commandMaven.append(" ");
        }
    }

    private void removeElement(ListView<String> listWithElement, int elementToRemove) {
        listWithElement.getItems().remove(elementToRemove);
    }

    private int selectedIndex(ListView<String> list) {
        return list.getSelectionModel().getSelectedIndex();
    }

    private ObservableList<String> getSelectedItems(ListView<String> list) {
        return list.getSelectionModel().getSelectedItems();
    }

    private void addElementToList(ObservableList<String> storeList, ListView<String> guiList) {
        if (!(storeList.containsAll(getSelectedItems(guiList))))
            storeList.addAll(getSelectedItems(guiList));
    }
}
