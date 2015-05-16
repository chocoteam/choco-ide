package compilation;

/**
 * Created by yann on 16/05/15.
 */
public class StringCompilerAndRunner {
    private static final String CLASS_NAME = "ChocoProgram";

    public void compileAndRun(String code) throws IllegalAccessException, InstantiationException {
        JavaDynamicCompiler<Runnable> compiler = new JavaDynamicCompiler<>();
        Class<Runnable> clazz = compiler.compile(null, CLASS_NAME, code);
        System.out.println(clazz.getName());
        Runnable r = clazz.newInstance();
        r.run();
    }
}
