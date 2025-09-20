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

    // METHODS

    private void initializePath(String pathname){
        HEAD = new File(pathname);
    }

    public void initialize() throws IOException {
        HEAD.createNewFile();
    }

    public boolean delete() {
        return HEAD.delete();
    }

}
