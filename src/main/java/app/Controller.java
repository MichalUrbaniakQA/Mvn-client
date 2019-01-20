package app;

import app.direction.DirectionPathService;
import app.gradle.GradleServiceImpl;
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
import org.springframework.beans.factory.annotation.Qualifier;

import java.net.URL;
import java.util.ResourceBundle;

@org.springframework.stereotype.Controller
public class Controller implements Initializable {

    @Autowired
    private DirectionPathService directionPathService;
    @Autowired
    private FileRead fileRead;

    @Qualifier("mavenServiceImpl")
    @Autowired
    private CommonService mavenService;
    @Qualifier("gradleServiceImpl")
    @Autowired
    private CommonService gradleService;

    @Autowired
    private GradleServiceImpl test;

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
        mavenService.candidateProjectToBuild(projectsFromPathMaven, projectsCandidateToMaven);
    }

    @FXML
    void addGradleProjectToListCandidate(MouseEvent event) {
        gradleService.candidateProjectToBuild(projectsFromPathGradle, projectsCandidateToGradle);
    }
    ///////////////////////////////////////////////////////////////////////
    @FXML
    void removeMavenProjectCandidate(MouseEvent event) {
        mavenService.finalProjectToBuild(projectsCandidateToMaven);
    }

    @FXML
    void removeGradleProjectCandidate(MouseEvent event) {
        gradleService.finalProjectToBuild(projectsCandidateToGradle);
    }
    //////////////////////////////////////////////////////////////////////

    @FXML
    void mavenOrderSelectCandidate(MouseEvent event) {
        mavenService.candidateCommandToBuild(mavenOrderCandidate, mavenOrderReadyList);
    }

    @FXML
    void gradleOrderSelectCandidate(MouseEvent event) {
        gradleService.candidateCommandToBuild(gradleOrderCandidate, gradleOrderReadyList);
    }
    //////////////////////////////////////////////////////////////////////////////////

    @FXML
    void mavenOrderSelectReady(MouseEvent event) {
        mavenService.finalCommandToBuild(mavenOrderCandidate, mavenOrderReadyList);
    }

    @FXML
    void gradleOrderSelectReady(MouseEvent event) {
        gradleService.finalCommandToBuild(gradleOrderCandidate, gradleOrderReadyList);
    }
    //////////////////////////////////////////////////////////////////////////////////////
    @FXML
    void mvnBuildButton(ActionEvent event) {
        mavenService.buildButton(mavenHomePath, projectsCandidateToMaven, mavenOrderReadyList, resultOutput);
    }

    @FXML
    void gradleBuildButton(ActionEvent event) {
      //  gradleService.buildButton(gradleHomePath, projectsCandidateToGradle, gradleOrderReadyList, resultOutput);
        test.aaa();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setValueTo();
        mavenService.addCommand(mavenOrderCandidate);
        gradleService.addCommand(gradleOrderCandidate);

        basePathSaveButton.setOnAction(this::saveBasePath);

        projectsFromPathMaven.setOnMouseClicked(this::addMavenProjectToListCandidate);
        projectsFromPathGradle.setOnMouseClicked(this::addGradleProjectToListCandidate);
        projectsCandidateToMaven.setOnMouseClicked(this::removeMavenProjectCandidate);
        projectsCandidateToGradle.setOnMouseClicked(this::removeGradleProjectCandidate);
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
