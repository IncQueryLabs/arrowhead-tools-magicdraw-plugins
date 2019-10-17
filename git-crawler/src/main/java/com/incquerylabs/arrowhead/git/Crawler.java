package com.incquerylabs.arrowhead.git;

import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Crawler {

    private static void crawl(Path dotGit) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = builder.setGitDir(dotGit.toFile()).readEnvironment().findGitDir().setMustExist(true).build();
        //git.fetch().setTagOpt(TagOpt.FETCH_TAGS).call();
        RefDatabase database = repo.getRefDatabase();
        List<Ref> branches = database.getRefs();
        /*
        for(Ref ref : branches){
            System.out.println(ref.getName());
        }*/
        RevWalk walker = new RevWalk(repo);
        for (Ref ref : branches) {
            walker.markStart(walker.parseCommit(ref.getObjectId()));
        }
        RevCommit commit = walker.next();
        //System.out.println(commit.getFullMessage());
        RevTree tree = commit.getTree();
        TreeWalk tralk = new TreeWalk(repo);
        tralk.addTree(tree);
        tralk.setRecursive(true);
        String xml = ".xml";
        byte[] p = xml.getBytes();
        while (tralk.next()) {
            String path = tralk.getPathString();
           // System.out.println(path);
            if (path.endsWith(".iqs")) {
                ObjectId id = tralk.getObjectId(0);
                String t = id.toString();
                String t2 = t.substring(t.indexOf("[") + 1, t.length() - 1);
                MutableObjectId mu = new MutableObjectId();
                mu.fromString(t2);
                ObjectDatabase objs = repo.getObjectDatabase();
                ObjectLoader loader = objs.open(mu);
                loader.copyTo(System.out);
            }
        }
        /*
        for (RevCommit commit : walker) {
            System.out.println(commit.getFullMessage());
            RevTree tree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(repo);
            treeWalk.addTree(tree);
            while (treeWalk.next()){
                if(!treeWalk.isSubtree()){
                    treeWalk.
                } else {
                    treeWalk.enterSubtree();
                }
            }
        }*/
    }

    public static void main(String[] args) {
        Path iqs = Paths.get("C:/Users/Koltai Kadosa/Desktop/trip/.git");
        try {
            Crawler.crawl(iqs);
        } catch (Exception i) {
            System.out.println(i.getMessage());
            i.printStackTrace();
        }
    }
}
