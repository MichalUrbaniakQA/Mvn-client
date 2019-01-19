package app.gradle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Service;

@Service
public class GradleServiceImpl implements GradleService {

    private ObservableList<String> candidateToGradleBuild = FXCollections.observableArrayList();

    @Override
    public void removeFromGradleList(ListView<String> projectsCandidateToGradle) {

        int index = projectsCandidateToGradle.getSelectionModel().getSelectedIndex();
        projectsCandidateToGradle.getItems().remove(index);
    }

    @Override
    public void chooseProject(ListView<String> projectsFromPath, ListView<String> projectsCandidateToMaven) {

        ObservableList<String> elementFromClickOnTheListWithProjectFromPath = projectsFromPath.getSelectionModel().getSelectedItems();

        if (!(candidateToGradleBuild.containsAll(elementFromClickOnTheListWithProjectFromPath)))
            candidateToGradleBuild.addAll(elementFromClickOnTheListWithProjectFromPath);

        projectsCandidateToMaven.setItems(candidateToGradleBuild);
        candidateToGradleBuild.sorted();
    }

    @Override
    public void chooseProjectToBuild(ListView<String> gradleOrderCandidate, ListView<String> gradleOrderReadyList) {

    }

    @Override
    public void finalListToBuildGradle(ListView<String> gradleOrderCandidate, ListView<String> gradleOrderReadyList) {

    }

    @Override
    public void addGradleOrder(ListView<String> gradleOrderCandidate) {

    }

    @Override
    public void gradleBuildButton(TextField gradleHomePath, ListView<String> projectsCandidateToGradle, ListView<String> gradleOrderReadyList, TextArea gradleBuildResultOutput) {

    }
}
