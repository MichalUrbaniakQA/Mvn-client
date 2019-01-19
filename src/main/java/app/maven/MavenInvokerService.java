package app.maven;

import javafx.scene.control.TextArea;

public interface MavenInvokerService {

    void mvnBuild(StringBuilder resultAppendString, final String detailsPath, final String commandFinal, TextArea resultOutput, String errorMessage);
}
