package compilation;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class JavaDynamicBean extends SimpleJavaFileObject {
    private String source;
    private ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

    public JavaDynamicBean(String baseName, String source) {
        super(JavaDynamicUtils.INSTANCE.createURI(JavaDynamicUtils.INSTANCE.getClassNameWithExt(baseName)),
                Kind.SOURCE);
        this.source = source;
    }

    public JavaDynamicBean(String name) {
        super(JavaDynamicUtils.INSTANCE.createURI(name), Kind.CLASS);
    }

    @Override
    public String getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }

    @Override
    public OutputStream openOutputStream() {
        return byteCode;
    }

    public byte[] getBytes() {
        return byteCode.toByteArray();
    }
}