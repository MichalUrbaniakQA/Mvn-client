package app.direction;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public interface DirectionPathService {

    void saveDirectionBasePath(TextField basePathInput, ListView<String> projectsFromPathMaven, ListView<String> projectsFromPathGradle);
    void chooseProject(ListView<String> projectsFromPathMaven, ListView<String> projectsCandidateToMaven);
    void removeFromMavenList(ListView<String> projectsCandidateToMaven, ListView<String> projectsFromPathMaven);
}
