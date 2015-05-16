package compilation;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class JavaDynamicException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private DiagnosticCollector<JavaFileObject> collector;

    public JavaDynamicException(String message) {
        super(message);
    }

    public JavaDynamicException(String message, DiagnosticCollector<JavaFileObject> collector) {
        super(message);
        this.collector = collector;
    }

    public JavaDynamicException(Throwable e, DiagnosticCollector<JavaFileObject> collector) {
        super(e);
        this.collector = collector;
    }

    public String getCompilationError() {
        StringBuilder sb = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : collector.getDiagnostics()) {
            sb.append(diagnostic.getMessage(null));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getCompilationError();
    }
}
