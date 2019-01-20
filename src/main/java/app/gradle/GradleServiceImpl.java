package app.gradle;

import app.util.CommonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.springframework.stereotype.Service;

import java.io.File;

@Service(value="gradleServiceImpl")
public class GradleServiceImpl implements CommonService {

    private ObservableList<String> finalCommandList = FXCollections.observableArrayList();
    private ObservableList<String> candidateCommandList = FXCollections.observableArrayList();

    private ObservableList<String> finalProjectList = FXCollections.observableArrayList();

    private StringBuilder commandGradle = new StringBuilder();
    private StringBuilder resultAppendString = new StringBuilder();

    @Override
    public void finalProjectToBuild(ListView<String> projectsFinal) {
        removeElement(projectsFinal, selectedIndex(projectsFinal));
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
        candidateCommandList.addAll("assemble", "build", "buildDependents", "buildNeeded", "classes",
                "clean", "jar", "libs", "testClasses", "-x test", "test", "jettyRun", "jettyRunWar");
        commandCandidate.setItems(candidateCommandList);
    }

    public void aaa(){
        String g1 = "C:/Gradle/gradle-5.1";
        String g2 = "GRADLE_HOME";
        String g3 = "clean build";
        String g4 = "D:/Workspace/intelij/springboot-auth-updated";

        ProjectConnection connection = GradleConnector.newConnector()
                .forProjectDirectory(new File(g4))
                .connect();
        try {
            BuildLauncher build = connection.newBuild();
            build.forTasks(g3);
            build.setStandardOutput(System.out);
            build.run();
        } finally {
            connection.close();
        }

    }

    @Override
    public void buildButton(TextField homePath, ListView<String> projectsCandidate, ListView<String> commandFinal, TextArea resultOutput) {


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
