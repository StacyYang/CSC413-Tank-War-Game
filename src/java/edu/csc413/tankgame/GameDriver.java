package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;
import edu.csc413.tankgame.view.StartMenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameDriver is the primary controller class for the tank game. The game is launched from GameDriver.main, and
 * GameDriver is responsible for running the game loop while coordinating the views and the data models.
 */
public class GameDriver {
    // TODO: Implement.
    // Add the instance variables, constructors, and other methods needed for this class. GameDriver is the centerpiece
    // for the tank game, and should store and manage the other components (i.e. the views and the models). It also is
    // responsible for running the game loop.
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameState gameState;
    private final GameKeyListener gameKeyListener;
    private final GameActionListener gameActionListener;


    public GameDriver() {
        gameState = new GameState();
        gameKeyListener = new GameKeyListener(gameState);
        gameActionListener = new GameActionListener();
        mainView = new MainView(gameKeyListener, gameActionListener);
        runGameView = mainView.getRunGameView();
    }//Constructor

    public void start() {
        // This sets the MainView's screen to the start menu screen.
        mainView.setScreen(MainView.Screen.START_MENU_SCREEN);
    }

     private void runGame() {
        Tank playerTank =
                new PlayerTank(
                        GameState.PLAYER_TANK_ID,
                        RunGameView.PLAYER_TANK_INITIAL_X,
                        RunGameView.PLAYER_TANK_INITIAL_Y,
                        RunGameView.PLAYER_TANK_INITIAL_ANGLE);

        Tank aiTank =
                //new CushionAiTank(
                new DumbAiTank(
                        GameState.AI_TANK_ID,
                        RunGameView.AI_TANK_INITIAL_X,
                        RunGameView.AI_TANK_INITIAL_Y,
                        RunGameView.AI_TANK_INITIAL_ANGLE);

        gameState.addEntity(playerTank);
        gameState.addEntity(aiTank);

        runGameView.addDrawableEntity(
                GameState.PLAYER_TANK_ID,
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle()
        );

        runGameView.addDrawableEntity(
                GameState.AI_TANK_ID,
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank.getX(),
                aiTank.getY(),
                aiTank.getAngle()
        );

        Runnable gameRunner = () -> {
            while (update()) {
                runGameView.repaint();
                try {
                    Thread.sleep(8L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
        };
        new Thread(gameRunner).start();
    }

    // TODO: Implement.
    // update should handle one frame of gameplay. All tanks and shells move one step, and all drawn entities
    // should be updated accordingly. It should return true as long as the game continues.
    private boolean update() {
        //Ask all tanks, shells, etc. to move
        for(Entity entity: gameState.getEntities() ){
            entity.move(gameState);
        }

        //Ask all tanks, shells, etc. to check bounds
        for(Entity entity: gameState.getEntities() ){
            entity.checkBorder();
        }

        //Ask gameState -- any new shells to draw?
        //if so, call addDrawableEntity
        //TODO: need to update this.....Consult Professor
        Entity playerTank = null;
        for(Entity entity: gameState.getEntities()) {
            if (entity instanceof PlayerTank) {
                playerTank = entity;
            }
        }
        if (gameState.getIsShootPressed() && playerTank != null) {
            Shell newShell = new Shell(playerTank.getX(), playerTank.getY(), playerTank.getAngle());
            gameState.addEntity(newShell);
            runGameView.addDrawableEntity(newShell.getId(), RunGameView.SHELL_IMAGE_FILE, newShell.getX(), newShell.getY(), newShell.getAngle());
        }
//        for(Entity entity: gameState.getEntities()) {
//            if(gameState.getIsShootPressed()){
//                entity.shoot(gameState, runGameView);
//            }
//        }

        //Ask gameState -- any shells to remove?
        //if so, call removeDrawableEntity

        //check collisions(part II)


        for(Entity entity: gameState.getEntities()){
            runGameView.setDrawableEntityLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
        }

        return true;
    }


    public class GameActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            String actionCommand = event.getActionCommand();

            if(actionCommand.equals(StartMenuView.START_BUTTON_ACTION_COMMAND)) {
                mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
                runGame();
            }else if(actionCommand.equals(StartMenuView.EXIT_BUTTON_ACTION_COMMAND)){
                mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
                mainView.closeGame();
            }
        }
    }


    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
