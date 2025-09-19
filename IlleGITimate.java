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

    /*
     * This is an HashMap of all the files that are in the objects directory.
     * Specifically, it contains the paths of all the added Files. I want this to
     * have an easy way of looking up whether a file exists (with good efficiency)
     * and it helps me not have to repeatedly iterate over the index when I can just
     * initialize this and iterate through it for all my needs.
     * 
     * TLDR: storedFiles is a HashMap that represents index that will be tinkered
     * with at run-time and the result will be written back to index
     * 
     * REMEMBER: <path, unique hash> since an index can only hold a path once,
     * whereas an index can hold the same hash many times
     * 
     * could always refactor the hash to be an IndexEntry or some other custom
     * object...
     */
    private HashMap<String, String> storedFiles = new HashMap<String, String>();

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

    // This makes the BLOB
    private void saveFileToObjectsDirectory(File file) throws IOException {
        String hash = generateSha1Hex(file);
        File objectsFile = new File(objects.getPath() + "/" + hash);

        // logic to copy stuff from file to objectsFile
        // sleek!
        FileOutputStream fos = new FileOutputStream(objectsFile);
        Files.copy(file.toPath(), fos);
        fos.close();
    }

    /*
     * Rebuilds the index from the storedFiles memory copy. This is done after every
     * commit
     */
    private void initializeIndexFromStoredFiles() throws IOException {
        // Erases and rebirths the index file
        index.delete();
        index.createNewFile();

        for (String pathname : storedFiles.keySet()) {
            appendFileToIndex(new File(pathname));
        }
    }

    private void appendFileToIndex(File file) throws IOException {
        // Checking if index exists
        if (!indexExists()) {
            throw new FileNotFoundException("appendFileToIndex(File file): Index file does not exist");
        }

        String hash = generateSha1Hex(file);
        String pathname = file.getPath();

        // True means the FileWriter is appending the text
        BufferedWriter bw = new BufferedWriter(new FileWriter(index, true));

        // First line doesn't need a new line, subsequent edits do
        if (!Files.readString(index.toPath()).isEmpty()) {
            bw.newLine();
        }

        bw.write(hash + " " + pathname);
        bw.close();
    }

    private void addFileToStoredFiles(File file) throws IOException {
        storedFiles.put(file.getPath(), generateSha1Hex(file));
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
    private void initializeStoredFilesFromIndex() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(index));
        while (br.ready()) {
            String line = br.readLine();
            String hash = line.substring(0, 40);
            String pathname = line.substring(41, line.length());
            storedFiles.put(pathname, hash);
        }
        br.close();
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
     * first before finally deleting the directory objects.
     */
    public boolean deleteObjects() {
        for (File file : objects.listFiles()) {
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
