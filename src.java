import java.io.File;

public class src {
    public static void main(String[] args) throws Exception {
        IlleGITimate git = new IlleGITimate();
        git.commitFile(new File("testImages/a.jpg"));
        git.commitFile(new File("testTextFiles/a2.txt"));
        // do things here :D
    }
}
