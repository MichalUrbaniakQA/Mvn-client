package app.util;

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

    private void setValueToPojoFromConfigFile() {
        MAVEN_PATH = records.get(0);
        GRADLE_PATH = records.get(1);
        PROJECTS_PATH = records.get(2);
        EMAIL = records.get(3);
        PASS = records.get(4);
    }

    public void setConfigFromFile(String filename) {
        readFile(filename);
    }

    private String removeUnnesseseryPartFromLine(String removeFromTo) {
        StringBuilder str = new StringBuilder(removeFromTo);

        return str.delete(0, removeFromTo.lastIndexOf("-") + 1).toString();
    }

    private void readFile(String filename) {

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
