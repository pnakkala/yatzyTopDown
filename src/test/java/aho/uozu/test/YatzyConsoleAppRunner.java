package aho.uozu.test;

import aho.uozu.*;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;

public class YatzyConsoleAppRunner {
    private YatzyConsoleApp _game;
    private TextOutputMock _consoleOutput;
    private TextInput _consoleInput;

    public YatzyConsoleAppRunner(TextInput input, DiceRoller diceRoller) {
        _consoleInput = input;
        _consoleOutput = new TextOutputMock();
        _game = new YatzyConsoleApp(_consoleInput, _consoleOutput, diceRoller);
    }

    public YatzyConsoleAppRunner(TextInput input, DiceRoller diceRoller, Collection<ScoreCategory> availableCategories) {
        // todo: consolidate with above constructor
        _consoleInput = input;
        _consoleOutput = new TextOutputMock();
        _game = new YatzyConsoleApp(_consoleInput, _consoleOutput, diceRoller, availableCategories);
    }

    public void doNextTurn() {
        _game.doNextTurn();
    }

    public void displayedRoll(DiceRoll roll) {
        assertThat(_consoleOutput.readNextLine(), is(equalTo("you rolled: " + roll.toString())));
    }

    public void promptedUserForCategory() {
        expectBlankLine();
        assertThat(_consoleOutput.readNextLine(), is(equalTo("Enter a category")));
        expectBlankLine();
    }

    public void promptedUserForCategoryOrReRoll() {
        expectBlankLine();
        assertThat(_consoleOutput.readNextLine(), is(equalTo("Enter a category, or 'reroll'")));
        expectBlankLine();
    }

    private void expectBlankLine() {
        assertThat(_consoleOutput.readNextLine(), is(equalTo("")));
    }

    public void displayedScore() {
        assertThat(_consoleOutput.readNextLine(), startsWith("your score: "));
    }

    public void displayedScore(int score) {
        assertThat(_consoleOutput.readNextLine(), is(equalTo("your score: " + score)));
    }

    public void isOver() {
        assertTrue(_game.isFinished());
    }

    public void isNotOver() {
        assertFalse(_game.isFinished());
    }

    public void displayedAvailableCategories(int numCategories) {
        displayedAvailableCategoriesHeader();
        for (int i = 0; i < numCategories; i++) {
            assertThat(_consoleOutput.readNextLine(), matchesPattern("    .+: \\d+ points"));
        }
    }

    public void displayedAvailableCategoriesInAnyOrder(ScoreCategoryWithScore[] catScores) {
        displayedAvailableCategoriesHeader();
        var outputLines = new ArrayList<String>();
        for (int i = 0; i < catScores.length; i++) {
            outputLines.add(_consoleOutput.readNextLine());
        }
        for (var catScore : catScores) {
            var expectedDisplay = String.format("    %s: %d points", catScore.category, catScore.score);
            var matchFound = outputLines.stream().anyMatch(expectedDisplay::equals);
            assertThat("Found expected category and score in output: " + expectedDisplay, matchFound, is(true));
        }
    }

    public void displayedIncorrectInputMessage() {
        assertThat(_consoleOutput.readNextLine(), is(equalTo("bad input")));
    }

    public void displayedUnavailableCategoryMessage() {
        assertThat(_consoleOutput.readNextLine(), is(equalTo("That category is not available!")));
    }

    public void displayedCannotReRollMessage() {
        assertThat(_consoleOutput.readNextLine(), is(equalTo("You have no re-rolls remaining!")));
    }

    private void displayedAvailableCategoriesHeader() {
        expectBlankLine();
        assertThat(_consoleOutput.readNextLine(), is(equalTo("Available categories:")));
    }
}
