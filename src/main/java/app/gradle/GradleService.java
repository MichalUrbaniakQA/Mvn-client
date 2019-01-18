package app.gradle;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface GradleService {

    void removeFromGradleList(ListView<String> projectsCandidateToGradle);
    void chooseProjectToBuild(ListView<String> gradleOrderCandidate, ListView<String> gradleOrderReadyList);
    void finalListToBuildGradle(ListView<String> gradleOrderCandidate, ListView<String> gradleOrderReadyList);
    void addGradleOrder(ListView<String> gradleOrderCandidate);
    void chooseProject(ListView<String> projectsFromPath, ListView<String> projectsCandidateTo);

    void gradleBuildButton(TextField gradleHomePath, ListView<String> projectsCandidateToGradle, ListView<String> gradleOrderReadyList,
                          TextArea gradleBuildResultOutput);
}
