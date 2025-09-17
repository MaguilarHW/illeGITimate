import java.io.File;
import java.io.IOException;

public class IlleGITimate {

    // using String instead of Path because of empty behavior
    private String path = "";

    private File git = new File(path + "/git");
    private File objects = new File(path + "/git/objects");
    private File index = new File(path + "/git/index");

    public IlleGITimate(String path) {
        this.path = path;
    }

    // Initializing IlleGITimate
    // Makes git dir and index if they don't exist
    public void initialize() throws IOException {
        if (!git.exists()) {
            git.mkdir();
        }
        if (!objects.exists()) {
            objects.mkdir();
        }
        if (!index.exists()) {
            index.createNewFile();
        }
    }
}
