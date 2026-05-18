package tictactoe_new;

/**
 * ⚠️ DEPRECATED: This test file demonstrates an INCORRECT pattern.
 *
 * ISSUE: PipedInputStream/PipedOutputStream used backwards.
 *
 * The test sets System.setOut() to write to outputStream, then tries to:
 * 1. Call MainGame.main() which reads from System.in (never set up!)
 * 2. Read from inputStream (which is connected to outputStream for writing)
 *
 * Result: Game hangs trying to read input that never arrives.
 *
 * SOLUTION: Use the patterns in TESTING_GUIDE.md instead:
 * - Type A (startup): Use ByteArrayOutputStream (MainGameTest.java)
 * - Type B (interactive): Refactor HumanPlayer, use StringReader (InteractiveGameTest.java)
 *
 * See: TESTING_GUIDE.md and REFACTORING_CHECKLIST.md for correct approaches.
 */

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated(forRemoval = true, since = "Use MainGameTest and InteractiveGameTest instead")
public class MainTest2 {
    private final PrintStream originalOut = System.out;
    private PipedOutputStream outputStream;
    private BufferedReader scanner;

   @BeforeEach
   void setUp() {

        outputStream = new PipedOutputStream();
        try {
            PipedInputStream inputStream = new PipedInputStream(outputStream); // Connect in constructor
            scanner = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            Logger.getLogger(MainTest2.class.getName()).log(Level.SEVERE, null, ex);
        }

       System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8)); }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
        scanner.close();
        outputStream.close();
    }

  // main (adapted to this project)

    @Deprecated(forRemoval = true)
    public static void mainAdapted(String[] args) {
        if (args.length > 0) {
            try {
                String option = args[0].trim();
                Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN");
                Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");

                int turn = Integer.parseInt(option);
                if (turn == 1 || turn == 2) {
                    Board boardGame = new Board2D();
                    GameLogic game = new GameLogic(boardGame, human, computer, turn == 1);
                    game.play();
                } else {
                    System.out.println("Please, input a valid option [1-2]");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please, input a valid option [1-2]");
            }
        } else {
            System.out.println("Please, input a valid option [1-2]");
        }
    }

  // game::play (adapted to this project)

    @Deprecated(forRemoval = true)
    void playAdapted(GameLogic game) {
        game.play();
    }

    @Test
    @Deprecated(forRemoval = true)
    @Disabled("Streams configured incorrectly — game hangs waiting for input")
    void emptyOptionTest() throws IOException {
        MainGame.main(new String[]{});

        String expectedOutput = "Please, input a valid option [1-2]";
        String actualOutput = scanner.readLine();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    @Deprecated(forRemoval = true)
    @Disabled("Streams configured incorrectly — game hangs waiting for input")
    void InvalidArgument() throws IOException {
        MainGame.main(new String[]{"3"});

        String expectedOutput = "Please, input a valid option [1-2]";
        String actualOutput = scanner.readLine();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    @Deprecated(forRemoval = true)
    @Disabled("Streams configured incorrectly — game hangs waiting for input")
    void InvalidArgument_2() throws IOException {
        MainGame.main(new String[]{"3" , "extra"});

        String expectedOutput = "Please, input a valid option [1-2]";
        String actualOutput = scanner.readLine();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    @Deprecated(forRemoval = true)
    @Disabled("Streams configured incorrectly — game hangs waiting for input")
    void QuoteArgument() throws IOException {
        MainGame.main(new String[]{"'1'"});

        String expectedOutput = "Please, input a valid option [1-2]";
        String actualOutput = scanner.readLine();
        assertEquals(expectedOutput, actualOutput);
    }
}