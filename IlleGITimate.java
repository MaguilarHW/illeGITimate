import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.codec.digest.DigestUtils;

public class IlleGITimate {

    private boolean compress = false;

    // Using String instead of Path because of empty behavior
    private String pathname = "";

    // Remember, these are directories
    private File git;
    private File objects;

    // Both of these are files
    private File index;
    private File HEAD;

    // CONSTRUCTORS

    /*
     * If the repository already exists, this will not overwrite anything
     */
    public IlleGITimate() throws IOException {
        initializePaths();

        if (isRepositoryHealthy()) {
            System.out.println("Git Repository Already Exists");
        } else {
            initializeRepository();
            System.out.println("Git Repository Created");
        }

        initializeStoredFilesFromIndex();
    }

    /*
     * By allowing more than one instance of IlleGITimate, many directories
     * can host this imposter git. This means I can do more testing! Not much to
     * look at here. Note that if you want to redirect a copy of git to a subfolder,
     * it must already exist. If pathname = "test", this code will NOT create the
     * subfolder "test", for example.
     */
    public IlleGITimate(String pathname) throws IOException {
        this.pathname = pathname + "/";
        initializePaths();

        if (isRepositoryHealthy()) {
            System.out.println("Git Repository Already Exists");
        } else {
            initializeRepository();
            System.out.println("Git Repository Created");
        }

        initializeStoredFilesFromIndex();
    }

    // METHODS

    /*
     * The way I understand it, there are four scenarios when saving files using
     * git:
     * 
     * 1: The file has the same path and hash as a pair in storedFiles. Then it is
     * identical to that file and should not be stored.
     * 
     * 2: The file only shares a hash with something stored in storedFiles. Then the
     * contents are identical and storedFiles should be updated. Index should be
     * rewritten after.
     * 
     * 3: The file only shares a path with something stored in storedFiles. Then it
     * was a file that had been changed. We put these in just
     * objects and update the index by replacing the old entry.
     * 
     * 4: It shares nothing in common, it goes in both index and objects.
     * 
     * Note that 2, 3, 4 both involve rewriting the hashMap, which can be done in
     * one method since "put"ing also overwrites in a hashMap.
     */
    public void commitFile(File file) throws IOException {
        String hash = generateSha1Hex(file);
        String pathname = file.getPath();

        // Case 1:
        if (storedFiles.containsKey(pathname) && storedFiles.get(pathname).equals(hash)) {
            return;
        }

        // Cases 2, 3, 4:
        addFileToStoredFiles(file);
        saveFileToObjectsDirectory(file);

        // instead of trying to deal with in-place edits, just rewrite the entire index
        // every time we commit a file. could probably do this better tbh
        initializeIndexFromStoredFiles();
    }

    /*
     * Builds the paths to each important file. If someone uses the default
     * constructor, pathname will be empty and these paths will simply point to
     * usual location of where git should be (one layer within the overarching
     * repository folder)
     */
    private void initializePaths() {
        git = new File(pathname + "git");
        objects = new File(pathname + "git/objects");
        index = new File(pathname + "git/index");
        HEAD = new File(pathname + "git/HEAD");
    }

    // Initializes git, objects, and index.
    private void initializeRepository() throws IOException {
        if (!gitExists()) {
            git.mkdir();
        }
        if (!objectsExists()) {
            objects.mkdir();
        }
        if (!indexExists()) {
            index.createNewFile();
        }
        if (!headExists()) {
            HEAD.createNewFile();
        }
    }

    /*
     * Tries to delete git, objects, and index and everything contained within.
     * Everything within objects (and then index) must be deleted first, since
     * directories can only be deleted by Java if they are empty.
     */
    public boolean deleteRepository() {
        deleteIndex();
        deleteObjects();
        deleteHead();
        return deleteGit();
    }

    /*
     * Git is a directory containing objects. Objects must therefore be deleted
     * first. One might want to change this implementation later to handle the edge
     * case where git does not exist but objects and index do, whereby objects would
     * need to be moved within git...
     * 
     * Honestly, this is the same thing as deleteRepository. At least for now...
     */
    public boolean deleteGit() {
        deleteIndex();
        deleteObjects();
        deleteHead();
        return git.delete();
    }

    public boolean deleteIndex() {
        return index.delete();
    }

    public boolean deleteHead() {
        return HEAD.delete();
    }

    /*
     * Checks to see if all the components of the repository exist. If any of them
     * do not, then this method returns false, indicating the repository is
     * unhealthy.
     */
    public boolean isRepositoryHealthy() {
        return gitExists() && objectsExists() && indexExists();
    }

    public boolean gitExists() {
        return git.exists();
    }

    public boolean objectsExists() {
        return objects.exists();
    }

    public boolean indexExists() {
        return index.exists();
    }

    public boolean headExists() {
        return HEAD.exists();
    }

    // // Stretch Goal #2: Create a tester for blob creation and **verification**
    // (7%)
    // // Blob creation is handled using saveFileToObjectsDirectory()
    // public boolean verifySyncBetweenIndexAndObjectsDirectory() throws IOException
    // {
    // // Verifying sync means AT LEAST every file in index is in git/objects/
    // // Maybe this isn't good
    // initializeStoredFilesFromIndex();

    // List<File> filesInObjects = Arrays.asList(objects.listFiles());

    // for (String hash : storedFiles.values()) {
    // System.out.println("Checking existence of " + objects.getAbsolutePath() + "/"
    // + hash);
    // if (filesInObjects.contains(filesInObjects)) {

    // }
    // }
    // }

    // Stretch Goal #2: Include a way to reset test files (3%)
    public void clearRepositoryForTestingPurposes() throws IOException {
        index.delete();
        index.createNewFile();
        deleteObjects();
        objects.mkdir();
    }
}
