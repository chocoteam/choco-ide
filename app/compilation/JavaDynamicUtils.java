package compilation;


import org.apache.commons.lang3.StringUtils;

import javax.tools.JavaFileObject.Kind;
import java.net.URI;
import java.net.URISyntaxException;

public enum JavaDynamicUtils {

    INSTANCE;

    public URI createURI(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getQualifiedClassName(String packageName,
                                        String className) {
        if (StringUtils.isEmpty(packageName)) {
            return className;
        } else {
            return packageName + "." + className;
        }
    }

    public String getClassNameWithExt(String className) {
        return className + Kind.SOURCE.extension;
    }

}