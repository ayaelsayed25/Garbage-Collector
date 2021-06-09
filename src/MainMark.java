import java.io.IOException;
import java.util.Scanner;

public class MainMark {
    public static void main(String[] args) {
        mainGC runner = new mainGC();
        String path1=null, path2=null, path3=null, path4=null;
        try {
            path1 = args[0];//put the heap path here
            path2 = args[1];// put ur pointers file path
            path3 = args[2];
            path4 = args[3];
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ArrayIndexOutOfBoundsException caught");
        }
        finally {
            runner.readHeap(path1);
            runner.readPointers(path2);
            runner.readRoot(path3);
            try {
                runner.markAndCompact(path4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}