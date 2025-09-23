package components;

import java.io.File;

public class Git {

    private File git;

    public Git(String pathname) {
        initializePath(pathname);
    }

    // GETTERS

    public boolean exists() {
        return git.exists();
    }

    // METHODS

    private void initializePath(String pathname) {
        git = new File(pathname);
    }

    public void initialize() {
        git.mkdir();
    }

    public boolean delete() {
        return git.delete();
    }

}
