package app.maven;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class MavenHomeModel {

    private String mavenHome;
}
