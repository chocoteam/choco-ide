package datas.keywords;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Jean-Baptiste on 01/06/2015.
 */
public class KeywordsManager {

    private static String startPackageName = "org/chocosolver";


    /**
     * Get the list of all classes from Choco Jar
     *
     * @return the list of all classes
     */
    public static String getChocoClassesName() {

        String classes = "";
        URL url = KeywordsManager.class.getClassLoader().getResource(
                startPackageName);
        System.out.println(url.getPath());
        String path = url.getPath().replaceAll("file:/", "")
                .replaceAll("!/"+startPackageName, "");
        System.out.println(path);
        try {
            JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(path));
            JarEntry crunchifyJar;

            while (true) {
                crunchifyJar = crunchifyJarFile.getNextJarEntry();
                if (crunchifyJar == null) {
                    break;
                }
                if ((crunchifyJar.getName().endsWith(".class"))) {
                    String className = crunchifyJar.getName().replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    if (myClass.contains(".") && !myClass.contains("$")) {
                        String[] parts = myClass.split("\\.");
                        myClass = parts[parts.length - 1];
                        classes = classes + myClass + "|";
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Oops.. Encounter an issue while parsing jar" + e.toString());
        }
        return classes;
    }

}
