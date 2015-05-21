package datas.samples;

import play.Logger;
import play.Play;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Retrieve and manage sample
 * Created by Mathieu on 21/05/2015.
 */
public class SampleManager {
    private static SampleManager instance;
    private final File rootDirectory;
    private Properties nameProperties;

    /**
     * Map that contains every sample to cache them
     */
    //TODO : use it
    private Map<String, Sample> sampleMap;

    /**
     * Singleton constructor
     */
    private SampleManager() {
        //Create the sample directory
        String directory = Play.application().configuration().getString("sample.root.directory");
        rootDirectory = new File(directory);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdir();
        }
        //Get the name
        this.nameProperties = new Properties();
        String propFile = Play.application().configuration().getString("sample.name.file");
        if (propFile != null) {
            try (FileInputStream fis = new FileInputStream(new File(propFile))) {
                this.nameProperties.load(fis);
            } catch (Exception e) {
                Logger.warn("Can't load the specified name properties file");
            }
        }
    }

    /**
     * @return a single instance of Sample Manager
     */
    public static SampleManager getInstance() {
        if (instance == null) instance = new SampleManager();
        return instance;
    }

    /**
     * Return the list of all available sample.
     *
     * @return the list that contains every available sample, return a empty list if a sample is not available.
     */
    public List<Sample> getAvailableSample() {
        List<Sample> sampleList = new ArrayList<>();
        //List all the files
        File[] files = this.rootDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    String filename = file.getName();
                    String name = this.nameProperties.containsKey(filename) ? this.nameProperties.getProperty(filename) : filename;
                    Sample sample = new Sample(name, filename,getContent(file));
                    sampleList.add(sample);
                } catch (Exception e) {
                    Logger.error("Can't read the given sample file %s", file.getPath(), e);
                }
            }
        }
        return sampleList;
    }

    /**
     * Get the full content of the file
     *
     * @param file the file to read
     * @return the full content of the file
     */
    private static String getContent(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                sb.append(scan.nextLine());
                if (scan.hasNextLine()) sb.append("\n");
            }
        }
        return sb.toString();
    }
}