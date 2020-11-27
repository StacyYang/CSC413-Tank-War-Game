package edu.csc413.tankgame.model;

import com.sun.tools.javac.Main;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;

import java.util.ArrayList;
import java.util.List;

/**
 * GameState represents the state of the game "world." The GameState object tracks all of the moving entities like tanks
 * and shells, and provides the controller of the program (i.e. the GameDriver) access to whatever information it needs
 * to run the game. Essentially, GameState is the "data context" needed for the rest of the program.
 */
public class GameState {
    public static final double TANK_X_LOWER_BOUND = 30.0;
    public static final double TANK_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width - 100.0;
    public static final double TANK_Y_LOWER_BOUND = 30.0;
    public static final double TANK_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height - 120.0;

    public static final double SHELL_X_LOWER_BOUND = -10.0;
    public static final double SHELL_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width;
    public static final double SHELL_Y_LOWER_BOUND = -10.0;
    public static final double SHELL_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height;

    public static final String PLAYER_TANK_ID = "player-tank";
    public static final String AI_TANK_ID = "ai-tank";

    private boolean isUpPressed;
    private boolean isDownPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isShootPressed;
    private boolean isEscapePressed;

    // TODO: Feel free to add more tank IDs if you want to support multiple AI tanks! Just make sure they're unique.

    // TODO: Implement.
    // There's a lot of information the GameState will need to store to provide contextual information. Add whatever
    // instance variables, constructors, and methods are needed.

    private final List<Entity> entities = new ArrayList<>();

    public void addEntity(Entity tank){
        entities.add(tank);
    }

    public List<Entity> getEntities(){
        return entities;
    }



    // these XXPressed and XXReleased methods are associated with KeyListeners
    public void upPressed(){
        isUpPressed = true;
    }
    public void upReleased(){
        isUpPressed = false;
    }

    public void downPressed(){ isDownPressed = true; }
    public void downReleased(){ isDownPressed = false;}

    public void leftPressed(){
        isLeftPressed = true;
    }
    public void leftReleased(){ isLeftPressed = false; }

    public void rightPressed(){ isRightPressed = true; }
    public void rightReleased(){
        isRightPressed = false;
    }

    public void shootPressed() {isShootPressed = true;}
    public void shootReleased() {isShootPressed = false;}

    public void escapePressed() {isEscapePressed = true;}
    public void escapeReleased() {isEscapePressed = false;}

    //getter methods, so we can make boolean variables private
    public boolean getIsUpPressed(){
        return isUpPressed;
    }

    public boolean getIsDownPressed(){
        return isDownPressed;
    }

    public boolean getIsLeftPressed(){
        return isLeftPressed;
    }

    public boolean getIsRightPressed(){
        return isRightPressed;
    }

    public boolean getIsShootPressed() { return isShootPressed;}

    public boolean getIsEscapePressed() { return isEscapePressed;}


    public void changeToEndMenu(MainView mainView){
        mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
    }
}
