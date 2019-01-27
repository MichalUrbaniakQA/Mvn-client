package app.direction;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface DirectionPathService {

    void saveDirectionBasePath(final TextField basePathInput, final ListView<String> projectsFromPathMaven,
                               final ListView<String> projectsFromPathGradle, final String value, final TextArea resultOutput);
}
