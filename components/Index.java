package components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

public class Index {

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
    private File index;
    private int numberOfEntries;

    public Index(String pathname) throws IOException {
        initializePath(pathname);
    }

    // GETTERS

    public HashMap<String, String> getStoredFiles() {
        return storedFiles;
    }

    public boolean exists() {
        return index.exists();
    }

    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    // METHODS

    private void initializePath(String pathname) {
        index = new File(pathname);
    }

    public void initialize() throws IOException {
        index.createNewFile();
    }

    /*
     * Using apache library, which is gitignored. If this is not working for
     * someone, download the jar files from Google
     */
    private String generateSha1Hex(File file) throws IOException {
        return DigestUtils.sha1Hex(Files.readString(file.toPath()));
    }

    /*
     * Remember that storedFiles is the run-time representation of the index file.
     * This is useful when running the program when an index already exists. This
     * also allows the HashMap to be filled with Files using the data within index.
     * 41 is the length of the hash.
     * 
     * TODO: technically this doesn't do anything if we try and remove things or
     * clear
     */
    public void sync() throws IOException {
        numberOfEntries = 0;

        BufferedReader br = new BufferedReader(new FileReader(index));
        while (br.ready()) {
            String line = br.readLine();
            String hash = line.substring(0, 40);
            String pathname = line.substring(41, line.length());
            storedFiles.put(pathname, hash);
            numberOfEntries += 1;
        }
        br.close();
    }

    /*
     * The same as above, but for adding a single file to storedFiles
     */
    public void addFile(File file) throws IOException {
        storedFiles.put(file.getPath(), generateSha1Hex(file));
        rewrite();
    }

    /*
     * Rebuilds the index by deleting the index file and then rewriting it from
     * what's stored in storedFiles. This needs to be done every time a commit is
     * made. I understand that there is probably a better way to do this.
     */
    private void rewrite() throws IOException {
        index.delete();
        index.createNewFile();
        numberOfEntries = 0;

        for (String pathname : storedFiles.keySet()) {
            numberOfEntries += 1;
            appendFile(new File(pathname));
        }
    }

    private void appendFile(File file) throws IOException {
        // Checking if index exists
        if (!this.exists()) {
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

    public boolean delete() {
        return index.delete();
    }

    public void clear() throws IOException {
        index.delete();
        index.createNewFile();
        storedFiles.clear();
        numberOfEntries = 0;
    }

    public boolean containsPath(String pathname) {
        return storedFiles.containsKey(pathname);
    }

    public boolean containsHash(String pathname, String hash) {
        return storedFiles.get(pathname).equals(hash);
    }
}
