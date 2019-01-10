package sample.maven;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.Data;
import org.apache.maven.shared.invoker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.model.DirectionProjectPath;
import sample.model.MvnPath;
import sample.projects.ProjectsPathController;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

@Controller
@Data
public class MvnController {

    @Autowired
    private ProjectsPathController projectsPathController;
    @Autowired
    private DirectionProjectPath directionProjectPath;
    @Autowired
    private MvnPojo mvnPojo;
    @Autowired
    private MvnPath mvnPath;

    private StringBuilder orderMaven = new StringBuilder();
    private StringBuilder resultAppendString = new StringBuilder();

    @FXML
    private ListView<String> projectsCandidateToMaven, mavenOrderCandidate, mavenOrderReadyList;
    @FXML
    private TextField mavenHomePath;
    @FXML
    private Button  mvnBuildButton;
    @FXML
    private TextArea mavenBuildResultOutput;

    @FXML
    public void mavenOrderSelectCandidate(MouseEvent event) {
        ObservableList<String> mavenOrder = mavenOrderCandidate.getSelectionModel().getSelectedItems();
        int index = mavenOrderCandidate.getSelectionModel().getSelectedIndex();

        if (!(mvnPojo.getMavenOrderListCandidate().containsAll(mavenOrder)))
            mvnPojo.setMavenOrderListCandidate(mavenOrder);
        //    mavenOrderListCandidate.addAll(mavenOrder);

        mavenOrderReadyList.setItems(mvnPojo.getMavenOrderListCandidate());
//        mavenOrderReadyList.setItems(mavenOrderListCandidate);
        mavenOrderCandidate.getItems().remove(index);
    }

    @FXML
    public void mavenOrderSelectReady(MouseEvent event) {
        ObservableList<String> elementFromClickOnTheListWithCandidateToMavenOrder = mavenOrderReadyList.getSelectionModel().getSelectedItems();
        int index = mavenOrderReadyList.getSelectionModel().getSelectedIndex();

//        if (!(mavenOrderList.containsAll(elementFromClickOnTheListWithCandidateToMavenOrder)))
//            mavenOrderList.addAll(elementFromClickOnTheListWithCandidateToMavenOrder);

        if (!(mvnPojo.getMavenOrderList().containsAll(elementFromClickOnTheListWithCandidateToMavenOrder)))
            mvnPojo.setMavenOrderList(elementFromClickOnTheListWithCandidateToMavenOrder);

        mavenOrderCandidate.setItems(mvnPojo.getMavenOrderList());
//        mavenOrderCandidate.setItems(mavenOrderList);
        mavenOrderReadyList.getItems().remove(index);
    }

    @FXML
    public void mvnBuildButton(ActionEvent event) {
    //    this.mavenHome = mavenHomePath.getText();
        ArrayList<String> completeListOfCandidate = new ArrayList<String>(projectsCandidateToMaven.getItems());
        ArrayList<String> completeListOfMavenOrder = new ArrayList<String>(mavenOrderReadyList.getItems());

        for (String s : completeListOfMavenOrder) {
            orderMaven.append(s);
            orderMaven.append(" ");
        }

        for (String iterator : completeListOfCandidate) {
            mvnBuild(iterator, orderMaven.toString());
        }

//        for (String s : completeListOfMavenOrder) {
//            orderMaven.append(s);
//            orderMaven.append(" ");
//        }
//
//        for (String iterator : completeListOfCandidate) {
//            mvnBuild(iterator, orderMaven.toString());
//        }
    }

    private void mvnBuild(final String detailsPath, final String readyOrderMaven) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(directionProjectPath.getBasePath() + "/" + detailsPath));
        request.setGoals(Collections.singletonList(readyOrderMaven));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv(mvnPath.getMavenHome())));

        try {
            InvocationResult result = invoker.execute(request);
            invoker.execute(request);
            if (result.getExitCode() != 0) {
                if (result.getExecutionException() != null) {
//                    errorMessage = result.getExecutionException().getMessage();
                    mvnPojo.setErrorMessage(result.getExecutionException().getMessage());
                    resultAppendString
                            .append("Project - ")
                            .append(detailsPath)
                            .append(mvnPojo.getErrorMessage())
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
//            errorMessage = e.getMessage();
            mvnPojo.setErrorMessage(e.getMessage());
            resultAppendString
                    .append("Project - ")
                    .append(detailsPath)
                    .append(mvnPojo.getErrorMessage())
                    .append("\n");
        }
        addInvokerResult();
    }

    private void addInvokerResult() {
        mavenBuildResultOutput.setText(resultAppendString.toString());
    }
}
