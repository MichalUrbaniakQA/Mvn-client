package app;

import app.direction.DirectionPathService;
import app.gradle.GradleService;
import app.util.CommonService;
import app.util.FileRead;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@org.springframework.stereotype.Controller
public class Controller implements Initializable {

    @Autowired
    private DirectionPathService directionPathService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private GradleService gradleService;
    @Autowired
    private FileRead fileRead;

    @FXML
    private ListView<String> projectsFromPathMaven, projectsCandidateToMaven, mavenOrderCandidate, mavenOrderReadyList;
    @FXML
    private ListView<String> projectsFromPathGradle, projectsCandidateToGradle, gradleOrderCandidate, gradleOrderReadyList;
    @FXML
    private TextField basePathInput, mavenHomePath, gradleHomePath;
    @FXML
    private Button basePathSaveButton, mvnBuildButton, gradleBuildButton;
    @FXML
    private TextArea resultOutput;

    @FXML
    void saveBasePath(ActionEvent event) {
        directionPathService.saveDirectionBasePath(basePathInput, projectsFromPathMaven, projectsFromPathGradle, "pom.xml");
        directionPathService.saveDirectionBasePath(basePathInput, projectsFromPathMaven, projectsFromPathGradle, "build.gradle");
    }

    @FXML
    void addMavenProjectToListCandidate(MouseEvent event) {
        commonService.candidateProjectToBuild(projectsFromPathMaven, projectsCandidateToMaven);
    }

    @FXML
    void removeMavenProjectCandidate(MouseEvent event) {
        commonService.finalProjectToBuild(projectsCandidateToMaven);
    }

    @FXML
    void mavenOrderSelectCandidate(MouseEvent event) {
        commonService.candidateCommandToBuild(mavenOrderCandidate, mavenOrderReadyList);
    }

    @FXML
    void mavenOrderSelectReady(MouseEvent event) {
        commonService.finalCommandToBuild(mavenOrderCandidate, mavenOrderReadyList);
    }

    @FXML
    void mouseClickGradle(MouseEvent event) {
        gradleService.chooseProject(projectsFromPathGradle, projectsCandidateToGradle);
    }

    @FXML
    void gradleOrderSelectCandidate(MouseEvent event) {
    }

    @FXML
    void gradleOrderSelectReady(MouseEvent event) {
    }


    @FXML
    void mouseClickRemoveGradle(MouseEvent event) {
        gradleService.removeFromGradleList(projectsCandidateToGradle);
    }

    @FXML
    void mvnBuildButton(ActionEvent event) {
        commonService.buildButton(mavenHomePath, projectsCandidateToMaven, mavenOrderReadyList, resultOutput);
    }

    @FXML
    void gradleBuildButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setValueTo();
        commonService.addCommand(mavenOrderCandidate);

        basePathSaveButton.setOnAction(this::saveBasePath);

        projectsFromPathMaven.setOnMouseClicked(this::addMavenProjectToListCandidate);
        projectsFromPathGradle.setOnMouseClicked(this::mouseClickGradle);
        projectsCandidateToMaven.setOnMouseClicked(this::removeMavenProjectCandidate);
        projectsCandidateToGradle.setOnMouseClicked(this::mouseClickRemoveGradle);
        mvnBuildButton.setOnAction(this::mvnBuildButton);
        mavenOrderCandidate.setOnMouseClicked(this::mavenOrderSelectCandidate);
        mavenOrderReadyList.setOnMouseClicked(this::mavenOrderSelectReady);
    }

    private void setValueTo() {
        fileRead.setConfigFromFile("config.txt");
        basePathInput.setText(FileRead.PROJECTS_PATH);
        mavenHomePath.setText(FileRead.MAVEN_PATH);
        gradleHomePath.setText(FileRead.GRADLE_PATH);
    }
}
