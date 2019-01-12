package app.service;

import app.model.DirectionBasePath;
import app.model.MavenHome;
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
public class MavenServiceImpl implements MavenService {

    @Autowired
    private MavenHome mavenHome;
    @Autowired
    private DirectionBasePath directionBasePath;

    private ObservableList<String> mavenOrderListCandidate = FXCollections.observableArrayList();
    private ObservableList<String> mavenOrderList = FXCollections.observableArrayList();

    private StringBuilder orderMaven = new StringBuilder();
    private StringBuilder resultAppendString = new StringBuilder();
    private String errorMessage;

    @Override
    public void chooseProjectToBuild(ListView<String> mavenOrderCandidate, ListView<String> mavenOrderReadyList) {

        ObservableList<String> mavenOrder = mavenOrderCandidate.getSelectionModel().getSelectedItems();
        int index = mavenOrderCandidate.getSelectionModel().getSelectedIndex();

        if (!(mavenOrderListCandidate.containsAll(mavenOrder)))
            mavenOrderListCandidate.addAll(mavenOrder);

        mavenOrderReadyList.setItems(mavenOrderListCandidate);
        mavenOrderCandidate.getItems().remove(index);
    }

    @Override
    public void finalListToBuildMaven(ListView<String> mavenOrderCandidate, ListView<String> mavenOrderReadyList) {

        ObservableList<String> elementFromClickOnTheListWithCandidateToMavenOrder = mavenOrderReadyList.getSelectionModel().getSelectedItems();
        int index = mavenOrderReadyList.getSelectionModel().getSelectedIndex();

        if (!(mavenOrderList.containsAll(elementFromClickOnTheListWithCandidateToMavenOrder)))
            mavenOrderList.addAll(elementFromClickOnTheListWithCandidateToMavenOrder);

        mavenOrderCandidate.setItems(mavenOrderList);
        mavenOrderReadyList.getItems().remove(index);
    }

    @Override
    public void addMavenOrder(ListView<String> mavenOrderCandidate) {

        mavenOrderList.addAll("clean", "validate", "compile", "test", "-DskipTests", "package", "verify", "install", "site", "deploy");
        mavenOrderCandidate.setItems(mavenOrderList);
    }

    @Override
    public void mavenBuildButton(TextField mavenHomePath, ListView<String> projectsCandidateToMaven, ListView<String> mavenOrderReadyList,
                                 TextArea mavenBuildResultOutput){

        mavenHome.setMavenHome(mavenHomePath.getText());
        ArrayList<String> completeListOfCandidate = new ArrayList<String>(projectsCandidateToMaven.getItems());
        ArrayList<String> completeListOfMavenOrder = new ArrayList<String>(mavenOrderReadyList.getItems());

        for (String s : completeListOfMavenOrder) {
            orderMaven.append(s);
            orderMaven.append(" ");
        }

        for (String iterator : completeListOfCandidate) {
            mvnBuild(iterator, orderMaven.toString(), mavenBuildResultOutput);
        }

    }

    private void mvnBuild(final String detailsPath, final String readyOrderMaven, TextArea mavenBuildResultOutput) {

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(directionBasePath.getBasePath() + "/" + detailsPath));
        request.setGoals(Collections.singletonList(readyOrderMaven));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv(mavenHome.getMavenHome())));

        try {
            InvocationResult result = invoker.execute(request);
            invoker.execute(request);
            if (result.getExitCode() != 0) {
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
                            .append(", Build failed. ")
                            .append("Exit code: ")
                            .append(result.getExitCode())
                            .append("\n");
                }
            }
            if (result.getExitCode() != 1) {
                resultAppendString
                        .append("Project - ")
                        .append(detailsPath)
                        .append(". Build success. ")
                        .append("Exit code: ")
                        .append(result.getExitCode())
                        .append("\n");
            }
        } catch (MavenInvocationException e) {
            errorMessage = e.getMessage();
            resultAppendString
                    .append("Project - ")
                    .append(detailsPath)
                    .append(errorMessage)
                    .append("\n");
        }
        addInvokerResult(mavenBuildResultOutput);
    }

    private void addInvokerResult(TextArea mavenBuildResultOutput) {

        mavenBuildResultOutput.setText(resultAppendString.toString());
    }
}
