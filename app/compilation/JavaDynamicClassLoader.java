package compilation;

import java.util.HashMap;
import java.util.Map;

public class JavaDynamicClassLoader extends ClassLoader {

    private Map<String, JavaDynamicBean> classes = new HashMap<>();

    public JavaDynamicClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);

    }

    public void addClass(JavaDynamicBean compiledObj) {
        classes.put(compiledObj.getName(), compiledObj);
    }

    @Override
    public Class<?> findClass(String qualifiedClassName)
            throws ClassNotFoundException {
        JavaDynamicBean bean = classes.get(qualifiedClassName);
        if (bean == null) {
            return super.findClass(qualifiedClassName);
        }
        byte[] bytes = bean.getBytes();
        return defineClass(qualifiedClassName, bytes, 0, bytes.length);
    }
}