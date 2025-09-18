import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import org.apache.commons.codec.digest.DigestUtils;

public class IlleGITimate {

    // Using String instead of Path because of empty behavior
    private String pathname = "";

    // Remember, these are directories
    private File git;
    private File objects;

    /*
     * This is an HashSet of all the files that are in the objects directory.
     * Specifically, it contains the paths of all the added Files. Since I don't
     * know of a way to have objects be anything except a placeholder (I can't have
     * it actually point to files), I will need this to look up whether a file
     * exists and occasionally to rebuild the index if something bad happens.
     * Conversely, index can be used to rebuild this data.
     * 
     * To traverse all the files outside of git, simply iterate. To traverse them
     * all within git, hash and then iterate.
     */
    private HashSet<File> unhashedPathsToStoredFiles = new HashSet<File>();

    // This is the index file
    private File index;

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
        }

        initializeUnhashedPathsToStoredFiles();
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
        }

        initializeUnhashedPathsToStoredFiles();
    }

    // METHODS

    public void commitFile(File file) throws IOException {
        String sha1Hex = generateSha1Hex(file);
        String pathname = file.getPath();

        writeFileToIndex(file);
    }

    /*
     * Using apache library, which is gitignored. If this is not working for
     * someone, download the jar files from Google
     */
    private String generateSha1Hex(File file) throws IOException {
        return DigestUtils.sha1Hex(Files.readString(file.toPath()));
    }

    private void writeFileToIndex(File file) throws IOException {
        // TODO: should probably check if index exists here
        String sha1Hex = generateSha1Hex(file);
        String pathname = file.getPath();

        // True means the FileWriter is appending the text
        BufferedWriter bw = new BufferedWriter(new FileWriter(index, true));

        // First line doesn't need a new line, subsequent edits do
        if (!Files.readString(index.toPath()).isEmpty()) {
            bw.newLine();
        }

        bw.write(sha1Hex + " " + pathname);
        bw.close();
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
    }

    /*
     * This is useful when running the program when an index already exists. This
     * allows the HashSet to be filled with Files using the data within index. 41 is
     * the length of the hash.
     */
    private void initializeUnhashedPathsToStoredFiles() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(index));
        while (br.ready()) {
            String line = br.readLine();
            String pathname = line.substring(41, line.length());
            unhashedPathsToStoredFiles.add(new File(pathname));
        }
        br.close();
    }

    /*
     * Checks the contents of a file against the contents of all files within 
     */
    private boolean isFileAlreadyInObjects(File file) {
        
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
    }

    /*
     * Tries to delete git, objects, and index and everything contained within.
     * Everything within objects (and then index) must be deleted first, since
     * directories can only be deleted by Java if they are empty.
     */
    public boolean deleteRepository() {
        deleteIndex();
        deleteObjects();
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
        return git.delete();
    }

    /*
     * Objects is a directory containing many files. Java can only delete the
     * directory if it is empty, thus this method deletes everything within objects
     * (by accessing every file in the ArrayList hashedObjects) first before finally
     * deleting the directory objects.
     */

    // TODO: here, we need to hash before iterating
    public boolean deleteObjects() {
        for (File file : unhashedPathsToStoredFiles) {
            file.delete();
        }
        return objects.delete();
    }

    public boolean deleteIndex() {
        return index.delete();
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
}
