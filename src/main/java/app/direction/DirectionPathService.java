package app.direction;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface DirectionPathService {

    void saveDirectionBasePath(TextField basePathInput, ListView<String> projectsFromPathMaven,
                               ListView<String> projectsFromPathGradle, final String value, TextArea resultOutput);
}
