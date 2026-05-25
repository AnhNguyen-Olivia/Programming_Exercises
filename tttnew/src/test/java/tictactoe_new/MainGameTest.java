package tictactoe_new;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Startup validation tests (Type A: exit before game loop)
 * Use ByteArrayOutputStream for simple output capture.
 *
 * NOTE: Tests only verify startup validation (invalid args).
 * Valid args ("1" or "2") will start the game and wait for input → these cannot be tested here.
 * See InteractiveGameTest.java for full game scenarios with injected input.
 */
public class MainGameTest {

    private String runMainAndCapture(String[] args) throws IOException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(outputBuffer, true, StandardCharsets.UTF_8));
            MainGame.main(args);
            return outputBuffer.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(originalOut);
        }
    }

    // P0 - Critical: Invalid Arguments (program exits immediately)

    @Test
    void testStartupWithoutArgument() throws IOException {
        String output = runMainAndCapture(new String[]{});
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    }

    @Test
    void testStartupWithInvalidArgument_Zero() throws IOException {
        String output = runMainAndCapture(new String[]{"0"});
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    }

    @Test
    void testStartupWithInvalidArgument_Three() throws IOException {
        String output = runMainAndCapture(new String[]{"3"});
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    }

    @Test
    void testStartupWithInvalidArgument_Negative() throws IOException {
        String output = runMainAndCapture(new String[]{"-1"});
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    }

    @Test
    void testStartupWithNonNumericArgument() throws IOException {
        String output = runMainAndCapture(new String[]{"abc"});
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    }

    @Test
    void testStartupWithQuotedArgument() throws IOException {
        String output = runMainAndCapture(new String[]{"'1'"});
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    }

    // NOTE: Valid arguments ("1" or "2") start the game and wait for input.
    // These cannot be tested here. Use InteractiveGameTest.java with injected input stream
    // to test full game scenarios.
    //
    // Example of what CANNOT be tested here:
    // @Test
    // void testStartupWithValidArgument_Player1() throws IOException {
    //     // This would start the game and wait for input from System.in
    //     // See InteractiveGameTest.java instead
    // }
}
