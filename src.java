import java.io.File;

public class src {
    public static void main(String[] args) throws Exception {
        IlleGITimate git = new IlleGITimate();

        git.commitFile(new File("testTextFiles/well.txt"));
        git.commitFile(new File("testTextFiles/well.txt"));
        git.commitFile(new File("testTextFiles/hey.txt"));
        git.commitFile(new File("testTextFiles/e.txt"));
        git.commitFile(new File("testTextFiles/helloworld.txt"));
    }
}
