package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class Controller implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    @FXML
    private ListView<String> projectsFromPath, projectsCandidateToMaven, mavenOrderCandidate, mavenOrderReadyList;
    @FXML
    private TextField basePathInput, mavenHomePath;
    @FXML
    private Button basePathSaveButton, mvnBuildButton;

    private String basePath;
    private String mavenHome;
    private File[] files;
    private ObservableList<String> itemsWithAllFoldersFromPath = FXCollections.observableArrayList();
    private ObservableList<String> candidateToMavenBuild = FXCollections.observableArrayList();
    private ObservableList<String> mavenOrderList = FXCollections.observableArrayList();
    private ObservableList<String> mavenOrderListCandidate = FXCollections.observableArrayList();


    @FXML
    void saveBasePath(ActionEvent event) {
        this.basePath = basePathInput.getText();
        files = new File(basePath).listFiles();

        for (File file : files) {
            if (!(itemsWithAllFoldersFromPath.contains(file.getName())))
                itemsWithAllFoldersFromPath.add(file.getName());
        }

        projectsFromPath.setItems(itemsWithAllFoldersFromPath);
        itemsWithAllFoldersFromPath.sorted();
    }

    @FXML
    void mouseClick(MouseEvent event) {
        ObservableList<String> elementFromClickOnTheListWithProjectFromPath = projectsFromPath.getSelectionModel().getSelectedItems();
        int index = projectsFromPath.getSelectionModel().getSelectedIndex();

        if (!(candidateToMavenBuild.containsAll(elementFromClickOnTheListWithProjectFromPath)))
            candidateToMavenBuild.addAll(elementFromClickOnTheListWithProjectFromPath);

        projectsCandidateToMaven.setItems(candidateToMavenBuild);
        projectsFromPath.getItems().remove(index);
        candidateToMavenBuild.sorted();
    }

    @FXML
    void mavenOrderSelectCandidate(MouseEvent event) {
        ObservableList<String> mavenOrder = mavenOrderCandidate.getSelectionModel().getSelectedItems();
        int index = mavenOrderCandidate.getSelectionModel().getSelectedIndex();

        if (!(mavenOrderListCandidate.containsAll(mavenOrder)))
            mavenOrderListCandidate.addAll(mavenOrder);

        mavenOrderReadyList.setItems(mavenOrderListCandidate);
        mavenOrderCandidate.getItems().remove(index);
    }

    @FXML
    void mavenOrderSelectReady(MouseEvent event) {
        ObservableList<String> elementFromClickOnTheListWithCandidateToMavenOrder = mavenOrderReadyList.getSelectionModel().getSelectedItems();
        int index = mavenOrderReadyList.getSelectionModel().getSelectedIndex();

        if (!(mavenOrderList.containsAll(elementFromClickOnTheListWithCandidateToMavenOrder)))
            mavenOrderList.addAll(elementFromClickOnTheListWithCandidateToMavenOrder);

        mavenOrderCandidate.setItems(mavenOrderList);
        mavenOrderReadyList.getItems().remove(index);
    }

    @FXML
    void mouseClickRemove(MouseEvent event) {
        ObservableList<String> elementFromClickOnTheListWithCandidateToMaven = projectsCandidateToMaven.getSelectionModel().getSelectedItems();
        int index = projectsCandidateToMaven.getSelectionModel().getSelectedIndex();

        if (!(itemsWithAllFoldersFromPath.containsAll(elementFromClickOnTheListWithCandidateToMaven)))
            itemsWithAllFoldersFromPath.addAll(elementFromClickOnTheListWithCandidateToMaven);

        projectsFromPath.setItems(itemsWithAllFoldersFromPath);
        projectsCandidateToMaven.getItems().remove(index);
    }

    @FXML
    void mvnBuildButton(ActionEvent event) {
        this.mavenHome = mavenHomePath.getText();
        ArrayList<String> completeListOfCandidate = new ArrayList<String>(projectsCandidateToMaven.getItems());

        for (String iterator : completeListOfCandidate) {
            mvnBuild(iterator);
        }
    }

    private void mvnBuild(final String detailsPath){
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(basePath + "/" + detailsPath));
        request.setGoals(Collections.singletonList("clean install -DskipTests"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv(mavenHome)));
        try {
            InvocationResult result = invoker.execute(request);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }

    @Override // D:/Workspace/intelij/qsg1/
    public void initialize(URL location, ResourceBundle resources) {
        addMavenOrder();

        basePathSaveButton.setOnAction(this::saveBasePath);
        projectsFromPath.setOnMouseClicked(this::mouseClick);
        projectsCandidateToMaven.setOnMouseClicked(this::mouseClickRemove);
        mvnBuildButton.setOnAction(this::mvnBuildButton);
        mavenOrderCandidate.setOnMouseClicked(this::mavenOrderSelectCandidate);
        mavenOrderReadyList.setOnMouseClicked(this::mavenOrderSelectReady);
    }

    private void addMavenOrder(){
        mavenOrderList.addAll("clean", "validate", "compile", "test", "-DskipTests", "package", "verify", "install", "site", "deploy");
        mavenOrderCandidate.setItems(mavenOrderList);
    }
}


