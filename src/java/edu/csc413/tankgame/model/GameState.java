package edu.csc413.tankgame.model;

import edu.csc413.tankgame.view.RunGameView;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
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
    public static final String AI_TANK_ID_1 = "ai-tank_1";
    public static final String AI_TANK_ID_2 = "ai-tank_2";
    public static final String POWER_UP_ID = "blood";

    private boolean isUpPressed;
    private boolean isDownPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isShootPressed;
    private boolean isEscapePressed;


    //Feel free to add more tank IDs if you want to support multiple AI tanks! Just make sure they're unique.

    private final List<Entity> entities = new ArrayList<>();
    private List<Entity> newShells = new ArrayList<>();

    public void addEntity(Entity tank){
        entities.add(tank);
    }
    public void addShell(Entity shell) {newShells.add(shell);}

    public void rmEntity(Entity entity) {
        entities.remove(entity);
    }

    public void reset() {
        entities.removeAll(entities);
    }

    public List<Entity> getEntities(){
        return entities;
    }
    public Entity getEntity(String id) {
        for(Entity entity: entities){
            if(entity.getId().equals(id)){
                return entity;
            }
        }
        return null;
    }
    public List<Entity> getNewShells() {return newShells;}



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

    public boolean getIsShootPressed() {
        if (isShootPressed) {
            isShootPressed = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean getIsEscapePressed() {
        if (isEscapePressed) {
            isEscapePressed = false;
            return true;
        } else {
            return false;
        }
    }

    public void playSound(String soundFileName){
        try{
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.setFramePosition(0);
            clip.start();
        }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
            e.printStackTrace();
        }
    }

}
