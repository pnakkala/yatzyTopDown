package aho.uozu;

import aho.uozu.score_calculators.ScoreCalculatorFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class YatzyGame {
    private final DiceRoller _diceRoller;
    private final ScoreCalculatorFactory _scoreCalculatorFactory;
    private final YatzyPlayerInterface _playerInterface;

    private DiceRoll _currentRoll;

    YatzyGame(DiceRoller diceRoller, ScoreCalculatorFactory scoreCalculatorFactory, YatzyPlayerInterface playerInterface) {
        _diceRoller = diceRoller;
        _scoreCalculatorFactory = scoreCalculatorFactory;
        _playerInterface = playerInterface;
    }

    void start() {
        rollDice();
        _playerInterface.showPlayerRolled(getCurrentRoll());
        _playerInterface.showAvailableCategories(getAvailableCategoriesWithScores());
        var category = _playerInterface.promptForCategoryInput();
        _playerInterface.showPlayerScore(scoreCurrentRoll(category));
    }

    void rollDice() {
        _currentRoll = _diceRoller.nextRoll();
    }

    DiceRoll getCurrentRoll() {
        return _currentRoll;
    }

    ScoreCategory[] getAvailableCategories() {
        return ScoreCategory.all();
    }

    List<ScoreCategoryWithScore> getAvailableCategoriesWithScores() {
        return Arrays.stream(ScoreCategory.all())
                .map(category -> new ScoreCategoryWithScore(category, 0))
                .collect(Collectors.toList());
    }

    int scoreCurrentRoll(ScoreCategory category) {
        var calculator = _scoreCalculatorFactory.calculatorFor(category);
        return calculator.calculateScore(_currentRoll);
    }

    public boolean isFinished() {
        return true;
    }
}
