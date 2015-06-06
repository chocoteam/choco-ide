import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("newfile.txt");
        if(file.createNewFile()) {
            System.out.println("File " + file + " has been created !");
        }
    }
}