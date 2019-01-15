package app.direction;

import app.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectionPathServiceImpl implements DirectionPathService {

    @Autowired
    private DirectionBasePathModel directionBasePathModel;
    @Autowired
    private Controller controller;

    private ObservableList<String> itemsWithAllFoldersFromPath = FXCollections.observableArrayList();
    private ObservableList<String> itemsWithMavenProject = FXCollections.observableArrayList();
    private ObservableList<String> itemsWithGradleProject = FXCollections.observableArrayList();
    private ObservableList<String> candidateToMavenBuild = FXCollections.observableArrayList();


    private ObservableList<String> candidateToGradleBuild = FXCollections.observableArrayList();

    @Override
    public void saveDirectionBasePath(TextField basePathInput, ListView<String> projectsFromPathMaven, ListView<String> projectsFromPathGradle) {
        directionBasePathModel.setBasePath(basePathInput.getText());

        File currentDir = new File(directionBasePathModel.getBasePath());
        List<Path> subfolder = new LinkedList<>();
        try {
            subfolder = Files.walk(currentDir.toPath(), 2)
                    .filter(Files::isRegularFile)
                    .filter(folder -> folder.getFileName().toString().contains("build.gradle"))
                    .map(Path::getParent)
                    .distinct()
                    .map(Path::getFileName)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Path[] stockArr = new Path[subfolder.size()];
        stockArr = subfolder.toArray(stockArr);

        for(Path s : stockArr){
            itemsWithMavenProject.add(s.toFile().getName());
        }

        projectsFromPathMaven.setItems(itemsWithMavenProject);
  //      itemsWithAllFoldersFromPath.sorted();
    }


    @Override
    public void chooseProject(ListView<String> projectsFromPath, ListView<String> projectsCandidateToMaven) {

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
