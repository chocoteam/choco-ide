package datas.samples;

/**
 * Represent a code sample for the IDE.
 * Created by Mathieu on 21/05/2015.
 */
public class Sample {
    private String name;
    private String filename;
    private String content;

    public Sample(String name, String filename, String content) {
        this.name = name;
        this.filename = filename;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

}
