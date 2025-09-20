import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

public class IlleGITimateTester {
    public static void main(String[] args) throws IOException {
        IlleGITimate test = new IlleGITimate();
        testRepositoryCreation(test);
        testCommittingNewFiles(test);
        testClearingRepository(test);

        // test.deleteRepository();
    }

    public static void testRepositoryCreation(IlleGITimate test) {

        // (1) Tests if all the folders are in the right locations and created
        if (test.isRepositoryHealthy()) {
            System.out.println("Passed || (1) testRepositoryCreation");
        } else {
            System.out.println("Failed || (1) testRepositoryCreation");
        }

    }

    public static void testCommittingNewFiles(IlleGITimate test) throws IOException {
        File a = new File("testTextFiles/a.txt");
        File b = new File("testTextFiles/b.txt");
        File c = new File("testTextFiles/c.txt");
        File d = new File("testTextFiles/d.txt");
        File[] files = { a, b, c, d };
        String[] contents = new String[files.length];
        String[] hashes = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            contents[i] = Files.readString(files[i].toPath());
            hashes[i] = DigestUtils.sha1Hex(Files.readString(files[i].toPath()));
        }

        for (File file : files) {
            test.commitFile(file);
        }

        // (1) Checks if the right number of files were put in objects directory
        if (test.getObjects().listFiles().length == files.length) {
            System.out.println("Passed || (1) testCommittingNewFiles");
        } else {
            System.out.println("Failed || (1) testCommittingNewFiles");
        }

        // (2) Checks to see if the created files have the right hashed names
        for (int i = 0; i < hashes.length; i++) {
            if (!Arrays.asList(test.getObjects().list()).contains(hashes[i])) {
                System.out.println("Failed || (2) testCommittingNewFiles");
                break;
            }
            if (i == hashes.length - 1) {
                System.out.println("Passed || (2) testCommittingNewFiles");
            }
        }

        // (3) Checks to see if the created files have the right contents
        String[] objectsDirContents = new String[test.getObjects().listFiles().length];
        for (int i = 0; i < objectsDirContents.length; i++) {
            objectsDirContents[i] = Files.readString(test.getObjects().listFiles()[i].toPath());
        }

        for (int i = 0; i < contents.length; i++) {
            if (!Arrays.asList(objectsDirContents).contains(contents[i])) {
                System.out.println("Failed || (3) testCommittingNewFiles");
                break;
            }
            if (i == contents.length - 1) {
                System.out.println("Passed || (3) testCommittingNewFiles");
            }
        }

        // (4) Checks to see if the index has the right number of entries
        if (test.getIndex().getNumberOfEntries() == files.length) {
            System.out.println("Passed || (4) testCommittingNewFiles");
        }
        else {
            System.out.println("Failed || (4) testCommittingNewFiles");
        }

        // (5) Checks to see if the index has all the path names in it
        for (int i = 0; i < files.length; i++) {
            if (!test.getIndex().containsPath(files[i].getPath())) {
                System.out.println("Failed || (5) testCommittingNewFiles");
                break;
            }
            if (i == files.length - 1) {
                System.out.println("Passed || (5) testCommittingNewFiles");
            }
        }

        // (6) Checks to see if the index has all the hashes in it
        for (int i = 0; i < files.length; i++) {
            if (!test.getIndex().containsHash(files[i].getPath(), hashes[i])) {
                System.out.println("Failed || (6) testCommittingNewFiles");
                break;
            }
            if (i == files.length - 1) {
                System.out.println("Passed || (6) testCommittingNewFiles");
            }
        }
    }

    public static void testClearingRepository(IlleGITimate test) throws IOException {
        File a = new File("testTextFiles/a.txt");
        test.commitFile(a);
    }
}
