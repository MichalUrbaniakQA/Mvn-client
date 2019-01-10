package sample.model;

import lombok.Data;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class DirectionProjectPath {

    private String basePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
