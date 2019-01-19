package app.maven;

import app.util.FileRead;
import javafx.scene.control.TextArea;
import org.apache.maven.shared.invoker.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;

@Service
class MavenInvokerServiceImpl implements MavenInvokerService {

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
        request.setPomFile(new File(FileRead.PROJECTS_PATH + "/" + detailsPath));
        request.setGoals(Collections.singletonList(commandFinal));

        return request;
    }

    private Invoker invoker(){
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv(FileRead.MAVEN_PATH)));

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
