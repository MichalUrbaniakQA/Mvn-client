package app.direction;

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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectionPathServiceImpl implements DirectionPathService {

    @Autowired
    private DirectionBasePathModel directionBasePathModel;

    private ObservableList<String> itemsWithMavenProject = FXCollections.observableArrayList();
    private ObservableList<String> candidateToMavenBuild = FXCollections.observableArrayList();

    private ObservableList<String> itemsWithGradleProject = FXCollections.observableArrayList();
    private ObservableList<String> candidateToGradleBuild = FXCollections.observableArrayList();

    @Override
    public void saveDirectionBasePath(TextField basePathInput, ListView<String> projectsFromPathMaven,
                                      ListView<String> projectsFromPathGradle, final String value) {

        directionBasePathModel.setBasePath(basePathInput.getText());

        File currentDir = new File(directionBasePathModel.getBasePath());
        List<Path> subfolder = new LinkedList<>();
        try {
            subfolder = Files.walk(currentDir.toPath(), 2)
                    .filter(Files::isRegularFile)
                    .filter(folder -> folder.getFileName().toString().contains(value))
                    .map(Path::getParent)
                    .distinct()
                    .map(Path::getFileName)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Path[] stockArr = new Path[subfolder.size()];
        stockArr = subfolder.toArray(stockArr);

        switch (value) {
            case "pom.xml":
                for (Path s : stockArr) {
                    itemsWithMavenProject.add(s.toFile().getName());
                }
                projectsFromPathMaven.setItems(itemsWithMavenProject);
                break;
            case "build.gradle":
                for (Path s : stockArr) {
                    itemsWithGradleProject.add(s.toFile().getName());
                }
                projectsFromPathGradle.setItems(itemsWithGradleProject);
                break;
        }
    }


    @Override
    public void chooseProject(ListView<String> projectsFromPath, ListView<String> projectsCandidateToMaven) {

        ObservableList<String> elementFromClickOnTheListWithProjectFromPath = projectsFromPath.getSelectionModel().getSelectedItems();

        if (!(candidateToMavenBuild.containsAll(elementFromClickOnTheListWithProjectFromPath)))
            candidateToMavenBuild.addAll(elementFromClickOnTheListWithProjectFromPath);

        projectsCandidateToMaven.setItems(candidateToMavenBuild);
        candidateToMavenBuild.sorted();
    }

    @Override
    public void chooseProject1(ListView<String> projectsFromPath, ListView<String> projectsCandidateToGradle) {

        ObservableList<String> elementFromClickOnTheListWithProjectFromPath = projectsFromPath.getSelectionModel().getSelectedItems();

        if (!(candidateToGradleBuild.containsAll(elementFromClickOnTheListWithProjectFromPath)))
            candidateToGradleBuild.addAll(elementFromClickOnTheListWithProjectFromPath);

        projectsCandidateToGradle.setItems(candidateToGradleBuild);
        candidateToGradleBuild.sorted();
    }
}
