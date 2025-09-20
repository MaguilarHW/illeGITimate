package components;

import java.io.File;
import java.io.IOException;

public class Head {
    
    private File HEAD;
    
    public Head(String pathname) throws IOException {
        initializePath(pathname);
    }
    
    // GETTERS
    
    public boolean exists() {
        return HEAD.exists();
    }

    public String getPath() {
        return HEAD.getPath();
    }

    // METHODS

    private void initializePath(String pathname){
        HEAD = new File(pathname + "git/HEAD");
    }

    public void initialize() throws IOException {
        HEAD.createNewFile();
    }

    public boolean delete() {
        return HEAD.delete();
    }

}
