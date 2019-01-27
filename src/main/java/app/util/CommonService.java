package app.util;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface CommonService {

    void finalProjectToBuild(final ListView<String> projectsFinal, final ListView<String> listOfBranches);
    void candidateProjectToBuild(final ListView<String> projectsCandidate, final ListView<String> projectsFinal);

    void finalCommandToBuild(final ListView<String> commandCandidate, final ListView<String> commandFinal);
    void candidateCommandToBuild(final ListView<String> commandCandidate, final ListView<String> commandFinal);

    void addCommand(final ListView<String> commandCandidate);
    void buildButton(final TextField homePath, final ListView<String> projectsCandidate, final ListView<String> commandFinal,
                     final TextArea resultOutput, final TextField basePathInput, final TextField mavenHomePath);
}
