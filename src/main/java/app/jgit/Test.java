package app.jgit;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

@Component
public class Test {

    private Git git;
    private Repository localRepo;

    String test = "D:/Workspace/intelij/gitTest";
    String ssh =  "git@bitbucket.org:ahmedprokebab/gittest.git";
    String http =  "https://ahmedprokebab@bitbucket.org/ahmedprokebab/gittest.git";
    String path = "D:/Workspace/test/test";


    public void test1(){
        try {
            git = Git.cloneRepository()
                    .setURI(ssh)
                    .setDirectory(new File("D:/Workspace/test/test"))
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }


        System.out.println("Created a new repository at " + git.getRepository().getDirectory().getAbsolutePath());

        System.out.println(git.branchList().toString());
    }

    public void pull3(){
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider( "cana123cana123@gmail.com","ziomek123");



        try {
            localRepo = new FileRepository(test + "/.git");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Git git = new Git(localRepo);
        try {
            git.checkout().setName("master").call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }


        PullCommand cmd = git.pull().setCredentialsProvider(cp);


        try {
            cmd.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void pull1(){
        try {
            localRepo = new FileRepository(path + "/.git");
        } catch (IOException e) {
            e.printStackTrace();
        }
        git = new Git(localRepo);

        PullCommand pullCmd = git.pull();
        try {
//            PullResult result = pullCmd.call();
//            FetchResult fetchResult = result.getFetchResult();
//            MergeResult mergeResult = result.getMergeResult();
//            mergeResult.getMergeStatus();
            pullCmd.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void test2() throws IOException {
        File dir = File.createTempFile("D:/Workspace/test/test", ".test");
        if(!dir.delete()) {
            throw new IOException("Could not delete file " + dir);
        }

        // The Git-object has a static method to initialize a new repository
        try (Git git = Git.init()
                .setDirectory(dir)
                .call()) {
            System.out.println("Created a new repository at " + git.getRepository().getDirectory());
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        // clean up here to not keep using more and more disk-space for these samples
        FileUtils.deleteDirectory(dir);

        dir = File.createTempFile("repoinit", ".test");
        if(!dir.delete()) {
            throw new IOException("Could not delete file " + dir);
        }

        // you can also create a Repository-object directly from the
        try (Repository repository = FileRepositoryBuilder.create(new File(dir.getAbsolutePath(), ".git"))) {
            System.out.println("Created a new repository at " + repository.getDirectory());
        }

        // clean up here to not keep using more and more disk-space for these samples
        FileUtils.deleteDirectory(dir);
    }
}
