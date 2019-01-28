package app.util;

import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileRead {

    private List<String> records = new ArrayList<>();

    public static String MAVEN_PATH = "";
    public static String GRADLE_PATH = "";
    public static String PROJECTS_PATH = "";
    public static String EMAIL= "";
    public static String PASS = "";
    public static String BRANCH_NAME = "";

    private void setValueToPojoFromConfigFile() {
        MAVEN_PATH = records.get(0);
        GRADLE_PATH = records.get(1);
        PROJECTS_PATH = records.get(2);
        EMAIL = records.get(3);
        PASS = records.get(4);
        BRANCH_NAME = records.get(5);
    }

    public void setConfigFromFile(final String filename, final TextArea resultOutput) {
        try {
            readFile(filename);
        }catch (Exception e){
            e.getStackTrace();
            resultOutput.setText(new FileNotFoundException("I don't see config file. \n Are you sure it is in good folder?").toString());
        }


    }

    private String removeUnnesseseryPartFromLine(final String removeFromTo) {
        StringBuilder str = new StringBuilder(removeFromTo);

        return str.delete(0, removeFromTo.lastIndexOf("-") + 1).toString();
    }

    private void readFile(final String filename) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                records.add(removeUnnesseseryPartFromLine(line));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setValueToPojoFromConfigFile();
    }
}
