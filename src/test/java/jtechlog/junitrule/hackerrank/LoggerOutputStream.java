package jtechlog.junitrule.hackerrank;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LoggerOutputStream extends OutputStream {

    private ByteArrayOutputStream log = new ByteArrayOutputStream();

    private OutputStream target;

    public LoggerOutputStream(OutputStream target) {
        this.target = target;
    }

    @Override
    public void write(int b) throws IOException {
        log.write(b);
        target.write(b);
    }

    @Override
    public void flush() throws IOException {
        log.flush();
        target.flush();
    }

    @Override
    public void close() throws IOException {
        log.close();
        target.close();
    }

    public ByteArrayOutputStream getLog() {
        return log;
    }
}
