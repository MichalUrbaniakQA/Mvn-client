package app.jgit;

import app.util.FileRead;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
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
public class GitServiceImpl {

    private ObservableList<String> remoteBranches = FXCollections.observableArrayList();
    private ObservableList<String> localBranches = FXCollections.observableArrayList();

    private Git git;
    private Repository localRepo;

    String test = "D:/Workspace/intelij/gitTest";
//    String ssh =  "git@bitbucket.org:ahmedprokebab/gittest.git";
//    String http =  "https://ahmedprokebab@bitbucket.org/ahmedprokebab/gittest.git";
//    String path = "D:/Workspace/test/test";

    private List<Ref> remote = null;
    private List<Ref> local = null;

    private ObservableList<String> getSelectedItems(ListView<String> list) {
        return list.getSelectionModel().getSelectedItems();
    }

    private String selectItem(ListView<String> branches){
        return branches.getSelectionModel().getSelectedItems().toString().replace("[","").replace("]", "");
    }

    public void setBranchesOnGui(ListView<String> listOfRemoteBranches, ListView<String> listOfLocalBranches ){
        try {
            localRepo = new FileRepository(test + "/.git");
        } catch (IOException e) {
            e.printStackTrace();
        }
        git = new Git(localRepo);


        getAllRemoteBranches(listOfRemoteBranches, listOfLocalBranches);
    }

    public void pull3(ListView<String> listOfRemoteBranches, ListView<String> listOfLocalBranches) {
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(FileRead.EMAIL, FileRead.PASS);


        System.out.println(listOfLocalBranches.getSelectionModel().getSelectedItems());

        try {
            localRepo = new FileRepository(test + "/.git");
        } catch (IOException e) {
            e.printStackTrace();
        }
        git = new Git(localRepo);

//        getAllRemoteBranches(listOfRemoteBranches, listOfLocalBranches);

//        try {
//            git.checkout().setName("master").call();
//        } catch (GitAPIException e) {
//            e.printStackTrace();
//        }
//
//
        PullCommand cmd = git.pull().setRemoteBranchName(selectItem(listOfRemoteBranches)).setCredentialsProvider(cp);


        try {
            cmd.call();
   //         git.checkout().setName("feature/test1").call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void returnBranchList() {
        try {
            remote = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
            local = git.branchList().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void getAllRemoteBranches(ListView<String> listOfRemoteBranches, ListView<String> listOfLocalBranches) {
        returnBranchList();
        setBranchesShortNames();
        listOfRemoteBranches.setItems(remoteBranches);
        listOfLocalBranches.setItems(localBranches);
    }

    private void setBranchesShortNames() {
        for (Ref ref : remote) {
            remoteBranches.add(ref.getName().substring(ref.getName().lastIndexOf("/") + 1));
        }
        for (Ref ref : local) {
            localBranches.add(ref.getName().substring(ref.getName().lastIndexOf("/") + 1));
        }
    }
}
