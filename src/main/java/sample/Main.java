package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sample.maven.MvnController;
import sample.maven.MvnOrderEnum;
import sample.maven.MvnPojo;
import sample.model.DirectionProjectPath;
import sample.projects.ProjectsPathController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@SpringBootApplication
public class Main extends Application// implements Initializable
{

    @Autowired
    private MvnController mvnController;
    @Autowired
    private ProjectsPathController projectsPathController;
    @Autowired
    private MvnPojo mvnPojo;
    @Autowired
    private DirectionProjectPath directionProjectPath;

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    // -Dmaven.multiModuleProjectDirectory=$MAVEN_HOME
    // D:/Workspace/intelij/qsg1/
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        addMavenOrder();
//        projectsPathController.initProjectPathController();
//
////        mvnController.getMvnBuildButton().setOnAction(mvnController.getMvnBuildButton());
////        mvnController.getMavenOrderCandidate().setOnMouseClicked(this::mavenOrderSelectCandidate);
////        mvnController.getMavenOrderReadyList().setOnMouseClicked(this::mavenOrderSelectReady);
//
////        basePathSaveButton.setOnAction(this::saveBasePath);
////        projectsFromPath.setOnMouseClicked(this::mouseClick);
////        projectsCandidateToMaven.setOnMouseClicked(this::mouseClickRemove);
//
////        mvnBuildButton.setOnAction(this::mvnBuildButton);
////        mavenOrderCandidate.setOnMouseClicked(this::mavenOrderSelectCandidate);
////        mavenOrderReadyList.setOnMouseClicked(this::mavenOrderSelectReady);
//    }

    private void addMavenOrder() {
        ObservableList<String>
                items = FXCollections.observableArrayList("clean", "validate", "compile", "test", "-DskipTests", "package", "verify", "install", "site", "deploy");
    items.forEach(System.out::println);
        ListView<String> list = new ListView<>(items);
        System.out.println(list.getItems());



    //    mvnPojo.getMavenOrderList().addAll(MvnOrderEnum.CLEAN.toString());
     //   mvnController.getMavenOrderCandidate().setItems(mvnPojo.getMavenOrderList());
        mvnController
                .setMavenOrderCandidate
                        (list);



//    //    mvnPojo.getDsds();
//         ObservableList<String> dsds = FXCollections.observableArrayList();
//        dsds.addAll("clean", "validate", "compile", "test", "-DskipTests", "package", "verify", "install", "site", "deploy");
//
//
//      //  mvnPojo.setMavenOrderList();
//   //     mvnPojo.setMavenOrderListCandidate(mvnPojo.getMavenOrderList());
//        mvnController.setMavenOrderCandidate(dsds.toString());
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(Main.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/test.fxml"));
        rootNode = fxmlLoader.load();

        primaryStage.setTitle("Awesome app");
        primaryStage.setScene(new Scene(rootNode));
        primaryStage.show();
        addMavenOrder();
    }

    @Override
    public void stop() {
        springContext.stop();
    }
}
