package app.jgit;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface GitService {

    void gitBuild(final TextField branchName, ListView<String> listOfBranch, TextArea resultOutput);
    void getLocalBranches(ListView<String> listOfBranch, ListView<String> project, TextArea resultOutput);
}
