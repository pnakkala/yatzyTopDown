package aho.uozu.test.tests;

import aho.uozu.DiceRoll;
import aho.uozu.ScoreCategory;
import aho.uozu.ScoreCategoryWithScore;
import aho.uozu.test.ConstantDiceRoller;
import aho.uozu.test.TextInputMock;
import aho.uozu.test.YatzyConsoleAppRunner;
import org.junit.Test;

public class YatzyConsoleAppEndToEndTest
{
    @Test
    public void gameShouldContinueUntilAllCategoriesAreChosen()
    {
        final var constantRoll = new DiceRoll(new int[] {1, 1, 1, 1, 1});
        var diceRoller = new ConstantDiceRoller(constantRoll);
        final var chanceScore = 5;
        final var yatzyScore = 50;
        var playerInput = new TextInputMock();

        var game = new YatzyConsoleAppRunner(playerInput, diceRoller);
        game.isNotOver();

        // turn 1
        playerInput.enqueueLine(ScoreCategory.CHANCE.toString());
        game.doNextTurn();
        game.displayedRoll(constantRoll);
        game.displayedAvailableCategoriesInAnyOrder(new ScoreCategoryWithScore[] {
                new ScoreCategoryWithScore(ScoreCategory.CHANCE, chanceScore),
                new ScoreCategoryWithScore(ScoreCategory.YATZY, yatzyScore)
        });
        game.promptedUserForCategoryOrReRoll();
        game.displayedScore(chanceScore);

        // turn 2
        playerInput.enqueueLine(ScoreCategory.YATZY.toString());
        game.doNextTurn();
        game.displayedRoll(constantRoll);
        game.displayedAvailableCategoriesInAnyOrder(new ScoreCategoryWithScore[] {
                new ScoreCategoryWithScore(ScoreCategory.YATZY, yatzyScore)
        });
        game.promptedUserForCategoryOrReRoll();
        game.displayedScore(yatzyScore);

        game.isOver();
    }

    @Test
    public void sameCategoriesAreDisplayedWhenPlayerReRolls() {
        final var constantRoll = new DiceRoll(new int[] {1, 1, 1, 1, 1});
        var diceRoller = new ConstantDiceRoller(constantRoll);
        final var chanceScore = 5;
        final var yatzyScore = 50;
        var playerInput = new TextInputMock();

        var game = new YatzyConsoleAppRunner(playerInput, diceRoller);

        playerInput.enqueueLine(
                "reroll",
                ScoreCategory.CHANCE.toString()
        );
        game.doNextTurn();
        game.displayedRoll(constantRoll);
        game.displayedAvailableCategoriesInAnyOrder(new ScoreCategoryWithScore[] {
                new ScoreCategoryWithScore(ScoreCategory.CHANCE, chanceScore),
                new ScoreCategoryWithScore(ScoreCategory.YATZY, yatzyScore)
        });
        game.promptedUserForCategoryOrReRoll();
        // player re-rolls here
        game.displayedRoll(constantRoll);
        game.displayedAvailableCategoriesInAnyOrder(new ScoreCategoryWithScore[] {
                new ScoreCategoryWithScore(ScoreCategory.CHANCE, chanceScore),
                new ScoreCategoryWithScore(ScoreCategory.YATZY, yatzyScore)
        });
        game.promptedUserForCategoryOrReRoll();
        // playerInput chooses category here
        game.displayedScore(chanceScore);
    }

    @Test
    public void playerCanOnlyReRoll3TimesInOneTurn() {
        final var constantRoll = new DiceRoll(new int[] {1, 1, 1, 1, 1});
        var diceRoller = new ConstantDiceRoller(constantRoll);
        final var chanceScore = 5;
        final var yatzyScore = 50;
        var playerInput = new TextInputMock();

        var game = new YatzyConsoleAppRunner(playerInput, diceRoller);

        playerInput.enqueueLine(
                "reroll",
                "reroll",
                "reroll",
                ScoreCategory.CHANCE.toString()
        );

        game.doNextTurn();
        // 3 re-rolls
        for (int i = 0; i < 3; i++) {
            game.displayedRoll(constantRoll);
            game.displayedAvailableCategoriesInAnyOrder(new ScoreCategoryWithScore[] {
                    new ScoreCategoryWithScore(ScoreCategory.CHANCE, chanceScore),
                    new ScoreCategoryWithScore(ScoreCategory.YATZY, yatzyScore)
            });
            game.promptedUserForCategoryOrReRoll();
            // player re-rolls here
        }

        game.displayedRoll(constantRoll);
        game.displayedAvailableCategoriesInAnyOrder(new ScoreCategoryWithScore[] {
                new ScoreCategoryWithScore(ScoreCategory.CHANCE, chanceScore),
                new ScoreCategoryWithScore(ScoreCategory.YATZY, yatzyScore)
        });
        // this time, choosing a category is the only option
        game.promptedUserForCategory();
        game.displayedScore(chanceScore);
    }
}
