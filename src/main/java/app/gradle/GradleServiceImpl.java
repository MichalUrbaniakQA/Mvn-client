package app.gradle;

import app.util.CommonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service(value="gradleServiceImpl")
public class GradleServiceImpl implements CommonService {

    @Autowired
    private GradleInvokerService gradleInvokerService;

    private ObservableList<String> finalCommandList = FXCollections.observableArrayList();
    private ObservableList<String> candidateCommandList = FXCollections.observableArrayList();

    private ObservableList<String> finalProjectList = FXCollections.observableArrayList();

    private StringBuilder commandGradle = new StringBuilder();
    private StringBuilder resultAppendString = new StringBuilder();

    @Override
    public void finalProjectToBuild(final ListView<String> projectsFinal, final ListView<String> listOfBranches) {
        removeElement(projectsFinal, selectedIndex(projectsFinal));
        listOfBranches.getItems().clear();
    }

    @Override
    public void candidateProjectToBuild(final ListView<String> projectsCandidate, final ListView<String> projectsFinal) {
        addElementToList(finalProjectList, projectsCandidate);
        projectsFinal.setItems(finalProjectList);
    }

    @Override
    public void finalCommandToBuild(final ListView<String> commandCandidate, final ListView<String> commandFinal) {
        addElementToList(candidateCommandList, commandFinal);
        commandCandidate.setItems(candidateCommandList);
        removeElement(commandFinal, selectedIndex(commandFinal));
    }

    @Override
    public void candidateCommandToBuild(final ListView<String> commandCandidate, final ListView<String> commandFinal) {
        addElementToList(finalCommandList, commandCandidate);
        commandFinal.setItems(finalCommandList);
        removeElement(commandCandidate, selectedIndex(commandCandidate));
    }

    @Override
    public void addCommand(final ListView<String> commandCandidate) {
        candidateCommandList.addAll("assemble", "build", "buildDependents", "buildNeeded", "classes",
                "clean", "jar", "libs", "testClasses", "-x test", "test", "jettyRun", "jettyRunWar");
        commandCandidate.setItems(candidateCommandList);
    }

    @Override
    public void buildButton(final TextField homePath, final ListView<String> projectsCandidate, final ListView<String> commandFinal,
                            final TextArea resultOutput, final TextField basePathInput, final TextField mavenHomePath) {
        executeGradleCommand(projectsCandidate, commandFinal);
    }

    private void executeGradleCommand(final ListView<String> projectsCandidate, final ListView<String> commandFinal) {
        ArrayList<String> completeListOfCandidate = new ArrayList<>(projectsCandidate.getItems());

        for (String iterator : completeListOfCandidate) {
            appendGradleCommand(commandFinal);
            gradleInvokerService.gradleBuild(resultAppendString, iterator, commandGradle.toString());
        }
    }

    private void appendGradleCommand(final ListView<String> commandFinal) {
        ArrayList<String> completeListOfMavenCommand = new ArrayList<>(commandFinal.getItems());

        for (String s : completeListOfMavenCommand) {
            commandGradle.append(s);
        }
    }

    private void removeElement(final ListView<String> listWithElement, final int elementToRemove) {
        listWithElement.getItems().remove(elementToRemove);
    }

    private int selectedIndex(final ListView<String> list) {
        return list.getSelectionModel().getSelectedIndex();
    }

    private ObservableList<String> getSelectedItems(final ListView<String> list) {
        return list.getSelectionModel().getSelectedItems();
    }

    private void addElementToList(final ObservableList<String> storeList, final ListView<String> guiList) {
        if (!(storeList.containsAll(getSelectedItems(guiList))))
            storeList.addAll(getSelectedItems(guiList));
    }
}
