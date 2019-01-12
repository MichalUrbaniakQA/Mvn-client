package app;

import app.service.DirectionPathService;
import app.service.MavenService;
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
    private MavenService mavenService;

    @FXML
    private ListView<String> projectsFromPath, projectsCandidateToMaven, mavenOrderCandidate, mavenOrderReadyList;
    @FXML
    private TextField basePathInput, mavenHomePath;
    @FXML
    private Button basePathSaveButton, mvnBuildButton;
    @FXML
    private TextArea mavenBuildResultOutput;

    @FXML
    void saveBasePath(ActionEvent event) {
        directionPathService.saveDirectionBasePath(basePathInput, projectsFromPath);
    }

    //   D:/Workspace/intelij

    @FXML
    void mouseClick(MouseEvent event) {
        directionPathService.chooseProject(projectsFromPath, projectsCandidateToMaven);
    }

    @FXML
    void mavenOrderSelectCandidate(MouseEvent event) {
        mavenService.chooseProjectToBuild(mavenOrderCandidate, mavenOrderReadyList);
    }

    @FXML
    void mavenOrderSelectReady(MouseEvent event) {
        mavenService.finalListToBuildMaven(mavenOrderCandidate, mavenOrderReadyList);
    }

    @FXML
    void mouseClickRemove(MouseEvent event) {
        directionPathService.removeFromMavenList(projectsCandidateToMaven, projectsFromPath);
    }

    @FXML
    void mvnBuildButton(ActionEvent event) {
        mavenService.mavenBuildButton(mavenHomePath, projectsCandidateToMaven, mavenOrderReadyList, mavenBuildResultOutput);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mavenService.addMavenOrder(mavenOrderCandidate);

        basePathSaveButton.setOnAction(this::saveBasePath);
        projectsFromPath.setOnMouseClicked(this::mouseClick);
        projectsCandidateToMaven.setOnMouseClicked(this::mouseClickRemove);
        mvnBuildButton.setOnAction(this::mvnBuildButton);
        mavenOrderCandidate.setOnMouseClicked(this::mavenOrderSelectCandidate);
        mavenOrderReadyList.setOnMouseClicked(this::mavenOrderSelectReady);
    }
}

// -Dmaven.multiModuleProjectDirectory=$MAVEN_HOME



