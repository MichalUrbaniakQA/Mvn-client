package app.gradle;

import app.util.FileRead;
import org.gradle.api.GradleException;
import org.gradle.internal.impldep.org.apache.commons.io.output.ByteArrayOutputStream;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GradleInvokerServiceImpl implements GradleInvokerService{

    @Override
    public void gradleBuild(final StringBuilder resultAppendString, final String detailsPath, final String commandFinal){
        //  System.out.println(commandFinal);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            BuildLauncher build = setProjectConnection(detailsPath).newBuild();
            build.forTasks(commandFinal);
            build.setStandardOutput(System.out);

            build.setStandardOutput(outputStream);
            build.setStandardError(outputStream);

            build.run();
            //  System.out.println(outputStream);
        }

        catch (GradleException e) {
            //  System.out.println(e.getMessage());
        }
        finally {
            setProjectConnection(detailsPath).close();
        }
    }

    private ProjectConnection setProjectConnection(final String detailsPath){
        return GradleConnector
                .newConnector()
                .forProjectDirectory(new File(FileRead.PROJECTS_PATH + "/" + detailsPath))
                .connect();
    }
}
