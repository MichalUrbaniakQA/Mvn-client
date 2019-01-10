package sample.maven;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class MvnPojo {

    private String errorMessage;
    private String mavenHome;
    private ObservableList<String> candidateToMavenBuild = FXCollections.observableArrayList();
    private ObservableList<String> mavenOrderList = FXCollections.observableArrayList();
    private ObservableList<String> mavenOrderListCandidate = FXCollections.observableArrayList();
}
