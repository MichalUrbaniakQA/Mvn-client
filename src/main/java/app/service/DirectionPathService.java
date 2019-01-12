package app.service;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public interface DirectionPathService {

    void saveDirectionBasePath(TextField basePathInput, ListView<String> projectsFromPath);
    void chooseProject(ListView<String> projectsFromPath, ListView<String> projectsCandidateToMaven);
    void removeFromMavenList(ListView<String> projectsCandidateToMaven, ListView<String> projectsFromPath);
}
