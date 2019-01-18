package app.maven;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface MavenService {

    void removeFromMavenList(ListView<String> projectsCandidateToMaven);
    void chooseProjectToBuild(ListView<String> mavenOrderCandidate, ListView<String> mavenOrderReadyList);
    void finalListToBuildMaven(ListView<String> mavenOrderCandidate, ListView<String> mavenOrderReadyList);
    void addMavenOrder(ListView<String> mavenOrderCandidate);
    void chooseProject(ListView<String> projectsFromPath, ListView<String> projectsCandidateTo);

    void mavenBuildButton(TextField mavenHomePath, ListView<String> projectsCandidateToMaven, ListView<String> mavenOrderReadyList,
                          TextArea resultOutput);

}
