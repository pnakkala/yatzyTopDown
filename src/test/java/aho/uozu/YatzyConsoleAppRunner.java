package aho.uozu;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class YatzyConsoleAppRunner {
    private YatzyConsoleApp game;
    private TextOutputMock consoleOutput;
    private TextInput consoleInput;

    public YatzyConsoleAppRunner(TextInput input) {
        consoleInput = input;
        consoleOutput = new TextOutputMock();
        game = new YatzyConsoleApp(consoleInput, consoleOutput);
    }

    public void start() {
        game.start();
    }

    public void displayedRoll(Roll roll) {
        assertThat(consoleOutput.readNextLine(), is(equalTo("you rolled: " + roll.toString())));
    }

    public void promptedUserForCategory() {
        assertThat(consoleOutput.readNextLine(), is(equalTo("enter a category")));
    }

    public void displayedScore(int i) {
        assertThat(consoleOutput.readNextLine(), is(equalTo("your score: " + i)));
    }

    public void gameIsOver() {
        assertTrue(game.isFinished());
    }

    public void setNextDiceRoll(Roll roll) {
        game.setNextRoll(roll);
    }
}
