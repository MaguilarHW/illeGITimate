import java.io.File;
import java.io.IOException;

public class IlleGITimate {

    // Using String instead of Path because of empty behavior
    private String pathname = "";

    // Remember, these are directories
    private File git;
    private File objects;

    // This is the index file
    private File index;

    /*
     * If the repository already exists, this implementation will not overwrite anything 
     */
    public IlleGITimate() throws IOException {
        initializePaths();

        if (isRepositoryHealthy()) {
            System.out.println("Git Repository Already Exists");
        }
        else {
            initializeRepository();
        }
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
        }
        else {
            initializeRepository();
        }
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
     * Returns the path of what
     */
    // public boolean remove

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
