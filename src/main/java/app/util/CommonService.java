package app.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface CommonService {

    void finalProjectToBuild(ListView<String> projectsFinal);
    void candidateProjectToBuild(ListView<String> projectsCandidate, ListView<String> projectsFinal);

    void finalCommandToBuild(ListView<String> commandCandidate, ListView<String> commandFinal);
    void candidateCommandToBuild(ListView<String> commandCandidate, ListView<String> commandFinal);

    void addCommand(ListView<String> commandCandidate);
    void buildButton(TextField homePath, ListView<String> projectsCandidate, ListView<String> commandFinal, TextArea resultOutput);
}
