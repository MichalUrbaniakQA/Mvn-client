package app.maven;

import app.direction.DirectionBasePathModel;
import javafx.scene.control.TextArea;
import org.apache.maven.shared.invoker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;

@Service
class MavenInvokerServiceImpl implements MavenInvokerService {

    @Autowired
    private DirectionBasePathModel directionBasePathModel;
    @Autowired
    private MavenHomeModel mavenHomeModel;

    @Override
    public void mvnBuild(StringBuilder resultAppendString, final String detailsPath, final String commandFinal, TextArea resultOutput, String errorMessage) {
        try {
            invoker().execute(setInvocationRequest(detailsPath, commandFinal));

            exitCodeStatus(resultAppendString, invocationResult(detailsPath, commandFinal), detailsPath, errorMessage);

        } catch (MavenInvocationException e) {
            errorMessage = e.getMessage();
            resultAppendString
                    .append("Project - ")
                    .append(detailsPath)
                    .append(errorMessage)
                    .append("\n");
        }
        addInvokerResult(resultOutput, resultAppendString);
    }

    private InvocationRequest setInvocationRequest(final String detailsPath, final String commandFinal){
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(directionBasePathModel.getBasePath() + "/" + detailsPath));
        request.setGoals(Collections.singletonList(commandFinal));

        return request;
    }

    private Invoker invoker(){
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv(mavenHomeModel.getMavenHome())));

        return invoker;
    }

    private InvocationResult invocationResult(final String detailsPath, final String commandFinal) throws MavenInvocationException {
        return invoker().execute(setInvocationRequest(detailsPath, commandFinal));
    }

    private void exitCodeStatus(StringBuilder resultAppendString, InvocationResult result, String detailsPath, String errorMessage){
        switch (result.getExitCode()){
            case 1:
                exitCodeFail(resultAppendString, result, detailsPath, errorMessage);
                break;
            case 0:
                exitCodeSuccess(resultAppendString, result, detailsPath);
                break;
        }
    }

    private void exitCodeSuccess(StringBuilder resultAppendString, InvocationResult result, String detailsPath) {
        resultAppendString
                .append("Project - ")
                .append(detailsPath)
                .append(". Build success. ")
                .append("Exit code: ")
                .append(result.getExitCode())
                .append("\n");
    }

    private void exitCodeFail(StringBuilder resultAppendString, InvocationResult result, String detailsPath, String errorMessage) {
        if (result.getExecutionException() != null) {
            errorMessage = result.getExecutionException().getMessage();
            resultAppendString
                    .append("Project - ")
                    .append(detailsPath)
                    .append(errorMessage)
                    .append(". Build failed. ")
                    .append("Exit code: ")
                    .append(result.getExitCode())
                    .append("\n");
        } else {
            resultAppendString
                    .append("Project - ")
                    .append(detailsPath)
                    .append(errorMessage)
                    .append(". Build failed. ")
                    .append("Exit code: ")
                    .append(result.getExitCode())
                    .append("\n");
        }
    }

    private void addInvokerResult(TextArea resultOutput, StringBuilder resultAppendString) {
        resultOutput.setText(resultAppendString.toString());
    }
}
