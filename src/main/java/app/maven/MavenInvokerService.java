package app.maven;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface MavenInvokerService {

    void mvnBuild(final StringBuilder resultAppendString, final String detailsPath, final String commandFinal,
                  final TextArea resultOutput, final String errorMessage, final TextField basePathInput, final TextField mavenHomePath);
}
