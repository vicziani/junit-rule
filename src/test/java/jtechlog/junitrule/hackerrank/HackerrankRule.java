package jtechlog.junitrule.hackerrank;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.*;

import static jtechlog.junitrule.hackerrank.ContainsSameLinesMatcher.containsSameLines;
import static org.junit.Assert.assertThat;

public class HackerrankRule implements TestRule {
    private static final String DEFAULT_POSTFIX = "00";
    // ExternalResource nem jó, mert lefogja az Exception-t

    private Class testClass;

    private boolean activated;

    private boolean muted = true;

    private String postfix = DEFAULT_POSTFIX;

    private InputStream tmpIn;

    private PrintStream tmpOut;

    private LoggerOutputStream loggerOutputStream;

    public void assertOutput() {
        // Assert
        assertThat(new ByteArrayInputStream(loggerOutputStream.getLog().toByteArray()),
                containsSameLines(testClass.getResourceAsStream("output/output" + postfix + ".txt")));
    }

    @Override
    public Statement apply(Statement base, Description description) {
        testClass = description.getTestClass();
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
                after();
            }
        };
    }

    public HackerrankRule withPostfix(String postfix) {
        this.postfix = postfix;
        return this;
    }

    public HackerrankRule enableSystemOut() {
        this.muted = false;
        return this;
    }

    public void activate() {
        activated = true;

        tmpIn = System.in;
        tmpOut = System.out;

        String filename = "input/input" + postfix + ".txt";
        InputStream is = testClass.getResourceAsStream(filename);
        if (is == null) {
            throw new IllegalArgumentException("File '" + filename + "' not found");
        }
        System.setIn(new BufferedInputStream(is));

        loggerOutputStream = new LoggerOutputStream(System.out, muted);
        System.setOut(new PrintStream(loggerOutputStream));
    }

    private void after() {
        // Revert
        if (activated) {
            System.setIn(tmpIn);
            System.setOut(tmpOut);
        }
    }

}
