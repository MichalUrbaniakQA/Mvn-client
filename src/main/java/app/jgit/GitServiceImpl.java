package app.jgit;

import app.util.FileRead;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GitServiceImpl implements GitService {

    private ObservableList<String> localBranches = FXCollections.observableArrayList();

    private Git git;
    private Repository localRepo;
    private CredentialsProvider cp;

    private List<Ref> local = null;

    private void setConfig(final ListView<String> project, final TextArea resultOutput) {
        this.cp = new UsernamePasswordCredentialsProvider(FileRead.EMAIL, FileRead.PASS);

        try {
            this.localRepo = new FileRepository(FileRead.PROJECTS_PATH + "/" + setProject(project) + "/.git");
        } catch (IOException e) {
            resultOutput.setText(e.getMessage());
        }
        this.git = new Git(localRepo);
    }

    @Override
    public void gitBuild(final TextField branchName, final ListView<String> listOfBranch, final TextArea resultOutput) {
        gitCheckout(listOfBranch, resultOutput);
        gitPull(branchName, resultOutput);
    }

    @Override
    public void getLocalBranches(final ListView<String> listOfBranch, final ListView<String> project, final TextArea resultOutput) {
        setConfig(project, resultOutput);
        returnBranchList(resultOutput);
        setBranchesShortNames();
        listOfBranch.setItems(localBranches);
    }

    private void gitCheckout(final ListView<String> listOfBranch, final TextArea resultOutput){
        try {
            git.checkout().setName(listOfBranch.getSelectionModel().getSelectedItem().replace("[", "").replace("]", "")).call();
        } catch (GitAPIException e) {
            resultOutput.setText(e.getMessage());
        }
    }

    private void gitPull(final TextField branchName, final TextArea resultOutput){
        PullCommand cmd = git.pull().setRemoteBranchName(branchName.getText()).setCredentialsProvider(cp);
        try {
            cmd.call();
            resultOutput.setText("Git command successful.");
        } catch (GitAPIException e) {
            resultOutput.setText(e.getMessage());
        }
    }

    private void returnBranchList(final TextArea resultOutput) {
        try {
            local = git.branchList().call();
        } catch (GitAPIException e) {
            resultOutput.setText(e.getMessage());
        }
    }

    private void setBranchesShortNames() {
        StringBuilder names;
        for (Ref ref : local) {
            names = new StringBuilder(ref.getName());
            names.delete(0, 11);
            localBranches.add(names.toString());
        }
        localBranches.add("---------------------------");
    }

    private String setProject(final ListView<String> project) {
        return project.getItems().toString().replace("[", "").replace("]", "");
    }
}
