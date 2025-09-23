import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.codec.digest.DigestUtils;

import components.Git;
import components.Head;
import components.Index;
import components.Objects;

public class IlleGITimate {

    private boolean compress = false;

    // Using String instead of Path because of empty behavior
    private String pathname = "";

    // Remember, these are directories
    private Git git;
    private Objects objects;

    // Both of these are files
    private Index index;
    private Head HEAD;

    // CONSTRUCTORS

    /*
     * If the repository already exists, this will not overwrite anything
     */
    public IlleGITimate() throws IOException {

        constructRepositoryPaths();

        if (isRepositoryHealthy()) {
            System.out.println("Git Repository Already Exists");
        } else {
            initializeRepository();
            System.out.println("Git Repository Created");
        }

        index.sync();

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

        constructRepositoryPaths();

        if (isRepositoryHealthy()) {
            System.out.println("Git Repository Already Exists");
        } else {
            initializeRepository();
            System.out.println("Git Repository Created");
        }

        index.sync();

    }

    // METHODS

    /*
     * Using apache library, which is gitignored. If this is not working for
     * someone, download the jar files from Google
     */
    private String generateSha1Hex(File file) throws IOException {
        return DigestUtils.sha1Hex(Files.readString(file.toPath()));
    }

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

    // TODO: put a try-catch here?
    public void commitFile(File file) throws IOException {

        String hash = generateSha1Hex(file);
        String pathname = file.getPath();

        // Case 1:
        if (index.containsPath(pathname) && index.containsHash(pathname, hash)) {
            return;
        }

        // Cases 2, 3, 4:
        index.addFile(file);
        objects.addFile(file);

    }

    /*
     * Builds the paths to each important file. If someone uses the default
     * constructor, pathname will be empty and these paths will simply point to
     * usual location of where git should be (one layer within the overarching
     * repository folder)
     */
    private void constructRepositoryPaths() throws IOException {
        git = new Git(pathname + "git");
        objects = new Objects(pathname + "git/objects");
        index = new Index(pathname + "git/index");
        HEAD = new Head(pathname + "git/HEAD");
    }

    /*
     * Builds the paths to each important file. If someone uses the default
     * constructor, pathname will be empty and these paths will simply point to
     * usual location of where git should be (one layer within the overarching
     * repository folder)
     */
    private void initializeRepository() throws IOException {
        if (!git.exists()) {
            git.initialize();
        }
        if (!objects.exists()) {
            objects.initialize();
        }
        if (!index.exists()) {
            index.initialize();
        }
        if (!HEAD.exists()) {
            HEAD.initialize();
        }
    }

    // Stretch Goal #2: Include a way to reset test files (3%)
    public void clearRepository() throws IOException {
        index.clear();
        objects.deleteContents();
    }

    /*
     * Tries to delete git, objects, and index and everything contained within.
     * Everything within objects (and then index) must be deleted first, since
     * directories can only be deleted by Java if they are empty.
     */
    public boolean deleteRepository() {
        index.delete();
        objects.delete();
        HEAD.delete();
        return git.delete();
    }

    /*
     * Checks to see if all the components of the repository exist. If any of them
     * do not, then this method returns false, indicating the repository is
     * unhealthy.
     */
    public boolean isRepositoryHealthy() {
        return git.exists() && objects.exists() && index.exists() && HEAD.exists();
    }

    /*
     * This is for testing purposes and should NOT be used otherwise.
     */
    public Objects getObjects() {
        return objects;
    }

    /*
     * This is for testing purposes and should NOT be used otherwise.
     */
    public Index getIndex() {
        return index;
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
}
