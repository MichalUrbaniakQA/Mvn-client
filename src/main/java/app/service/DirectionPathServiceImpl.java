package app.service;

import app.Controller;
import app.model.DirectionBasePath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DirectionPathServiceImpl implements DirectionPathService {

    @Autowired
    private DirectionBasePath directionBasePath;
    @Autowired
    private Controller controller;

    private File[] files;
    private ObservableList<String> itemsWithAllFoldersFromPath = FXCollections.observableArrayList();
    private ObservableList<String> candidateToMavenBuild = FXCollections.observableArrayList();

    @Override
    public void saveDirectionBasePath(TextField basePathInput, ListView<String> projectsFromPath) {
        directionBasePath.setBasePath(basePathInput.getText());
        files = new File(directionBasePath.getBasePath()).listFiles();

        for (File file : files) {
            if (!(itemsWithAllFoldersFromPath.contains(file.getName())))
                itemsWithAllFoldersFromPath.add(file.getName());
        }

        projectsFromPath.setItems(itemsWithAllFoldersFromPath);
        itemsWithAllFoldersFromPath.sorted();
    }

    @Override
    public void chooseProject(ListView<String> projectsFromPath, ListView<String> projectsCandidateToMaven){

        ObservableList<String> elementFromClickOnTheListWithProjectFromPath = projectsFromPath.getSelectionModel().getSelectedItems();
        int index = projectsFromPath.getSelectionModel().getSelectedIndex();

        if (!(candidateToMavenBuild.containsAll(elementFromClickOnTheListWithProjectFromPath)))
            candidateToMavenBuild.addAll(elementFromClickOnTheListWithProjectFromPath);

        projectsCandidateToMaven.setItems(candidateToMavenBuild);
        projectsFromPath.getItems().remove(index);
        candidateToMavenBuild.sorted();
    }

    @Override
    public void removeFromMavenList(ListView<String> projectsCandidateToMaven, ListView<String> projectsFromPath) {

        ObservableList<String> elementFromClickOnTheListWithCandidateToMaven = projectsCandidateToMaven.getSelectionModel().getSelectedItems();
        int index = projectsCandidateToMaven.getSelectionModel().getSelectedIndex();

        if (!(itemsWithAllFoldersFromPath.containsAll(elementFromClickOnTheListWithCandidateToMaven)))
            itemsWithAllFoldersFromPath.addAll(elementFromClickOnTheListWithCandidateToMaven);

        projectsFromPath.setItems(itemsWithAllFoldersFromPath);
        projectsCandidateToMaven.getItems().remove(index);
    }
}
