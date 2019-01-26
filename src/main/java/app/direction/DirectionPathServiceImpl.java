package app.direction;

import app.util.FileRead;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    private ObservableList<String> itemsWithMavenProject = FXCollections.observableArrayList();
    private ObservableList<String> itemsWithGradleProject = FXCollections.observableArrayList();

    @Override
    public void saveDirectionBasePath(TextField basePathInput, ListView<String> projectsFromPathMaven,
                                      ListView<String> projectsFromPathGradle, final String value, TextArea resultOutput) {

        FileRead.PROJECTS_PATH = basePathInput.getText();

        File currentDir = new File(FileRead.PROJECTS_PATH);
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
            resultOutput.setText(e.getMessage());
        }

        Path[] stockArr = new Path[subfolder.size()];
        stockArr = subfolder.toArray(stockArr);

        setProjects(value, stockArr, projectsFromPathMaven, projectsFromPathGradle);
    }

    private void setProjects(final String value, Path[] stockArr, ListView<String> projectsFromPathMaven, ListView<String> projectsFromPathGradle){
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
}
