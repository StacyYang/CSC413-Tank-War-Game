package edu.csc413.tankgame;
import edu.csc413.tankgame.model.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener{

    private final GameState gameState;

    public GameKeyListener(GameState gameState){
        this.gameState = gameState;
    }

    @Override
    public void keyTyped(KeyEvent event){
        //Useless
    }

    @Override
    public void keyPressed(KeyEvent event){ //this will update gamestate
        int keyCode = event.getKeyCode();

        if(keyCode == KeyEvent.VK_W){
            gameState.upPressed();
        }

        if(keyCode == KeyEvent.VK_S){
            gameState.downPressed();
        }

        if(keyCode == KeyEvent.VK_A){
            gameState.leftPressed();
        }

        if(keyCode == KeyEvent.VK_D){
            gameState.rightPressed();
        }

        if(keyCode == KeyEvent.VK_SPACE){
            gameState.shootPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent event){
        int keyCode = event.getKeyCode();
        if(keyCode == KeyEvent.VK_W){
            gameState.upReleased();
        }
        if(keyCode == KeyEvent.VK_S){
            gameState.downReleased();
        }

        if(keyCode == KeyEvent.VK_A) {
            gameState.leftReleased();
        }

        if(keyCode == KeyEvent.VK_D){
            gameState.rightReleased();
        }

        if(keyCode == KeyEvent.VK_SPACE){
            gameState.shootReleased();
        }
    }
}
