package app.maven;

import app.direction.DirectionBasePathModel;
import app.util.CommonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.maven.shared.invoker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class MavenServiceImpl implements CommonService {

    @Autowired
    private MavenHomeModel mavenHomeModel;
    @Autowired
    private DirectionBasePathModel directionBasePathModel;

    private ObservableList<String> finalCommandList = FXCollections.observableArrayList();
    private ObservableList<String> candidateCommandList = FXCollections.observableArrayList();

    private ObservableList<String> finalProjectList = FXCollections.observableArrayList();

    private StringBuilder commandMaven = new StringBuilder();
    private StringBuilder resultAppendString = new StringBuilder();

    private String errorMessage;

    @Override
    public void finalProjectToBuild(ListView<String> projectsFinal) {
        removeElement(projectsFinal, selectedIndex(projectsFinal));
    }

    @Override
    public void candidateProjectToBuild(ListView<String> projectsCandidate, ListView<String> projectsFinal) {
        addElementToList(finalProjectList, projectsCandidate);
        projectsFinal.setItems(finalProjectList);
    }

    @Override
    public void candidateCommandToBuild(ListView<String> commandCandidate, ListView<String> commandFinal) {
        addElementToList(finalCommandList, commandCandidate);
        commandFinal.setItems(finalCommandList);
        removeElement(commandCandidate, selectedIndex(commandCandidate));
    }

    @Override
    public void finalCommandToBuild(ListView<String> commandCandidate, ListView<String> commandFinal) {
        addElementToList(candidateCommandList, commandFinal);
        commandCandidate.setItems(candidateCommandList);
        removeElement(commandFinal, selectedIndex(commandFinal));
    }

    @Override
    public void addCommand(ListView<String> commandCandidate) {
        candidateCommandList.addAll("clean", "validate", "compile", "test", "-DskipTests", "package", "verify", "install", "site", "deploy");
        commandCandidate.setItems(candidateCommandList);
    }

    @Override
    public void buildButton(TextField homePath, ListView<String> projectsCandidate, ListView<String> commandFinal, TextArea resultOutput) {
        mavenHomeModel.setMavenHome(homePath.getText());

        appendMavenCommand(commandFinal);
        executeMavenCommand(projectsCandidate, resultOutput);
    }

    private void executeMavenCommand(ListView<String> projectsCandidate, TextArea resultOutput) {
        ArrayList<String> completeListOfCandidate = new ArrayList<>(projectsCandidate.getItems());

        for (String iterator : completeListOfCandidate) {
            mvnBuild(iterator, commandMaven.toString(), resultOutput);
        }
    }

    private void appendMavenCommand(ListView<String> commandFinal) {
        ArrayList<String> completeListOfMavenCommand = new ArrayList<>(commandFinal.getItems());

        for (String s : completeListOfMavenCommand) {
            commandMaven.append(s);
            commandMaven.append(" ");
        }
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


    private void mvnBuild(final String detailsPath, final String commandFinal, TextArea resultOutput) {
        try {
            invoker().execute(setInvocationRequest(detailsPath, commandFinal));

            exitCodeStatus(resultAppendString, invocationResult(detailsPath, commandFinal), detailsPath);

        } catch (MavenInvocationException e) {
            errorMessage = e.getMessage();
            resultAppendString
                    .append("Project - ")
                    .append(detailsPath)
                    .append(errorMessage)
                    .append("\n");
        }
        addInvokerResult(resultOutput);
    }

    private void exitCodeStatus(StringBuilder resultAppendString, InvocationResult result, String detailsPath){
        switch (result.getExitCode()){
            case 1:
                exitCodeFail(resultAppendString, result, detailsPath);
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

    private void exitCodeFail(StringBuilder resultAppendString, InvocationResult result, String detailsPath) {
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

    private void addInvokerResult(TextArea resultOutput) {
        resultOutput.setText(resultAppendString.toString());
    }

    private int selectedIndex(ListView<String> list) {
        return list.getSelectionModel().getSelectedIndex();
    }

    private ObservableList<String> getSelectedItems(ListView<String> list) {
        return list.getSelectionModel().getSelectedItems();
    }

    private void addElementToList(ObservableList<String> storeList, ListView<String> guiList) {
        if (!(storeList.containsAll(getSelectedItems(guiList))))
            storeList.addAll(getSelectedItems(guiList));
    }

    private void removeElement(ListView<String> listWithElement, int elementToRemove) {
        listWithElement.getItems().remove(elementToRemove);
    }
}
