package com.incquerylabs.arrowhead.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.TagOpt;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sage {

    private static void crawl(Path dotGit) throws IOException, GitAPIException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = builder.setGitDir(dotGit.toFile()).readEnvironment().findGitDir().setMustExist(true).build();
        Git git = Git.wrap(repo);
        git.fetch().setTagOpt(TagOpt.FETCH_TAGS).call();

    }

    public static void main(String[] args) {
        Path iqs = Paths.get("C:/Users/Koltai Kadosa/Desktop/incquery-server/.git");
        try {
            Sage.crawl(iqs);
        } catch (Exception i) {
            System.out.println(i.getMessage());
            i.printStackTrace();
        }
    }
}
