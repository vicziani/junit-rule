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

    private String postfix;

    private InputStream tmpIn;

    private PrintStream tmpOut;

    private LoggerOutputStream loggerOutputStream;

    public HackerrankRule() {
        postfix = DEFAULT_POSTFIX;
    }

    public HackerrankRule(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before(description.getTestClass());
                base.evaluate();
                after(description.getTestClass());
            }
        };
    }

    private void before(Class<?> testClass) throws Throwable {

        tmpIn = System.in;
        tmpOut = System.out;

        String filename = "input/input" + postfix + ".txt";
        InputStream is = testClass.getResourceAsStream(filename);
        if (is == null) {
            throw new IllegalArgumentException("File '" + filename + "' not found");
        }
        System.setIn(new BufferedInputStream(is));

        loggerOutputStream = new LoggerOutputStream(System.out);
        System.setOut(new PrintStream(loggerOutputStream));
    }

    private void after(Class<?> testClass) {
        // Revert
        System.setIn(tmpIn);
        System.setOut(tmpOut);

        // Assert
        assertThat(new ByteArrayInputStream(loggerOutputStream.getLog().toByteArray()),
                containsSameLines(testClass.getResourceAsStream("output/output" + postfix + ".txt")));



    }
}
