package compilation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yann on 28/05/15.
 */
public class StringListStream extends PrintStream {
    private static final String NOT_SUPPORTED = "Operation non supportée !";
    List<String> list;

    protected StringListStream(ByteArrayOutputStream outputStream) {
        super(outputStream);
        list = new ArrayList<>();
    }

    @Override
    public void println(String line) {
        this.list.add(line);
    }

    @Override
    public void println(Object x) {
        super.println(x);
    }

    @Override
    public void print(boolean b) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(char c) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(int i) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(long l) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(float f) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(double d) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(char[] s) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(String s) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void print(Object obj) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println() {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println(boolean x) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println(char x) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println(int x) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println(long x) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println(float x) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println(double x) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void println(char[] x) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    public List<String> getList() {;
        return list;
    }
}
