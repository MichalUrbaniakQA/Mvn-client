package app.maven;

import app.util.MvnIncorrectPathException;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.maven.shared.invoker.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;

@Service
class MavenInvokerServiceImpl  implements MavenInvokerService {

    @Override
    public void mvnBuild(final StringBuilder resultAppendString, final String detailsPath, final String commandFinal,
                         final TextArea resultOutput, String errorMessage, final TextField basePathInput, final TextField mavenHomePath) {
        try {
            invoker(mavenHomePath, resultOutput).execute(setInvocationRequest(detailsPath, commandFinal, basePathInput));

            exitCodeStatus(resultAppendString, invocationResult(detailsPath, commandFinal, basePathInput, mavenHomePath, resultOutput), detailsPath, errorMessage);

        } catch (MavenInvocationException | MvnIncorrectPathException e) {
            errorMessage = e.getMessage();
            resultAppendString
                    .append("Project - ")
                    .append(detailsPath)
                    .append(errorMessage)
                    .append("\n");
        }
        addInvokerResult(resultOutput, resultAppendString);
    }

    private InvocationRequest setInvocationRequest(final String detailsPath, final String commandFinal, final TextField basePathInput){
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(basePathInput.getText()+ "/" + detailsPath));
        request.setGoals(Collections.singletonList(commandFinal));

        return request;
    }

    private Invoker invoker(final TextField mavenHomePath, final TextArea resultOutput)  {
        Invoker invoker = new DefaultInvoker();
        try {
            invoker.setMavenHome(new File(System.getenv(mavenHomePath.getText())));
        } catch (Exception e) {
            e.getStackTrace();
            resultOutput.setText(new MvnIncorrectPathException("Incorrect maven path.").toString());
        }


        return invoker;
    }

    private InvocationResult invocationResult(final String detailsPath, final String commandFinal, final TextField basePathInput,
                                              final TextField mavenHomePath, final TextArea resultOutput) throws MavenInvocationException, MvnIncorrectPathException {

        return invoker(mavenHomePath, resultOutput).execute(setInvocationRequest(detailsPath, commandFinal, basePathInput));
    }

    private void exitCodeStatus(final StringBuilder resultAppendString, final InvocationResult result, final String detailsPath, final String errorMessage){
        switch (result.getExitCode()){
            case 1:
                exitCodeFail(resultAppendString, result, detailsPath, errorMessage);
                break;
            case 0:
                exitCodeSuccess(resultAppendString, result, detailsPath);
                break;
        }
    }

    private void exitCodeSuccess(final StringBuilder resultAppendString, final InvocationResult result, final String detailsPath) {
        resultAppendString
                .append("Project - ")
                .append(detailsPath)
                .append(". Build success. ")
                .append("Exit code: ")
                .append(result.getExitCode())
                .append("\n");
    }

    private void exitCodeFail(final StringBuilder resultAppendString, final InvocationResult result, final String detailsPath, String errorMessage) {
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

    private void addInvokerResult(final TextArea resultOutput, final StringBuilder resultAppendString) {
        resultOutput.setText(resultAppendString.toString());
    }
}
