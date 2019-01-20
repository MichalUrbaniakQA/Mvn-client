package app.direction;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;

public class Test {

    private GradleConnector connector;

    public Test(String gradleInstallationDir, String projectDir) {
        connector = GradleConnector.newConnector();
        connector.useInstallation(new File(gradleInstallationDir));
        connector.forProjectDirectory(new File(projectDir));
    }

    public void executeTask(String... tasks) {
        ProjectConnection connection = connector.connect();
        BuildLauncher build = connection.newBuild();
        build.forTasks(tasks);

        build.run();
        connection.close();
    }
}
