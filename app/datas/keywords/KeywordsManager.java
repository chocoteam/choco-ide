package datas.keywords;

import play.Play;

import java.io.FileInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Jean-Baptiste on 01/06/2015.
 */
public class KeywordsManager {

    /**
     * Get the list of all classes from Choco Jar
     *
     * @return the list of all classes
     */
    public static String getChocoClassesName() {

        String classes = "";
        String path = Play.application().configuration().getString("datas.keywords.chocoPath");

        try(JarInputStream jarInputStream = new JarInputStream(new FileInputStream(path))){
            JarEntry jarEntry;
            while ((jarEntry = jarInputStream.getNextJarEntry())!=null) {
                if ((jarEntry.getName().endsWith(".class"))) {
                    String className = jarEntry.getName().replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    if (myClass.contains(".") && !myClass.contains("$")) {
                        String[] parts = myClass.split("\\.");
                        myClass = parts[parts.length - 1];
                        classes = classes + myClass + "|";
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Encounter an issue while parsing jar " + e.toString());
        }
        return classes;
    }

}
