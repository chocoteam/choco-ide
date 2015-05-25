package compilation;

import net.openhft.compiler.CachedCompiler;

/**
 * Created by yann on 16/05/15.
 */
public class StringCompilerAndRunner {
    private final ClassLoader cl;

    public StringCompilerAndRunner(ClassLoader cl) {
        this.cl = cl;
    }

    public void compileAndRun(String code) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        CachedCompiler cc = new CachedCompiler(null, null);
        cc.loadFromJava("compilation.ChocoProject", code, cl);
        Class clazz = cc.loadFromJava("compilation.ChocoProjectImpl", code, cl);
        ChocoProject instance = (ChocoProject) clazz.newInstance();
        instance.init();
        instance.run();
    }
}
