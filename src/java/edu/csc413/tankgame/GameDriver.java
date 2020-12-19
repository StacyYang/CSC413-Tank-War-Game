package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;
import edu.csc413.tankgame.view.StartMenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GameDriver is the primary controller class for the tank game. The game is launched from GameDriver.main, and
 * GameDriver is responsible for running the game loop while coordinating the views and the data models.
 */
public class GameDriver {
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameState gameState;
    private final GameKeyListener gameKeyListener;
    private final GameActionListener gameActionListener;
    private int aiTankNum = 0;
    private int playerTankNum = 0;
    private Tank playerTank;
    private Tank aiTank1;
    private Tank aiTank2;

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
         playerTank = new PlayerTank(
                        GameState.PLAYER_TANK_ID,
                        RunGameView.PLAYER_TANK_INITIAL_X,
                        RunGameView.PLAYER_TANK_INITIAL_Y,
                        RunGameView.PLAYER_TANK_INITIAL_ANGLE);
         playerTankNum++;

         aiTank1 = new CushionAiTank(
                        GameState.AI_TANK_ID_1,
                        RunGameView.AI_TANK_INITIAL_X,
                        RunGameView.AI_TANK_INITIAL_Y,
                        RunGameView.AI_TANK_INITIAL_ANGLE);
         aiTankNum++;

         aiTank2 = new TurretAiTank(
                         GameState.AI_TANK_ID_2,
                         RunGameView.AI_TANK_2_INITIAL_X,
                         RunGameView.AI_TANK_2_INITIAL_Y,
                         RunGameView.AI_TANK_2_INITIAL_ANGLE);
         aiTankNum++;

         PowerUp powerUp = new PowerUp(
                        GameState.POWER_UP_ID,
                        RunGameView.POWER_UP_INITIAL_X,
                        RunGameView.POWER_UP_INITIAL_Y,
                        RunGameView.POWER_UP_INITIAL_ANGLE
                        );

         gameState.addEntity(playerTank);
         gameState.addEntity(aiTank1);
         gameState.addEntity(aiTank2);
         gameState.addEntity(powerUp);

         runGameView.addDrawableEntity(
                GameState.PLAYER_TANK_ID,
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle()
         );

         runGameView.addDrawableEntity(
                GameState.AI_TANK_ID_1,
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank1.getX(),
                aiTank1.getY(),
                aiTank1.getAngle()
         );

         runGameView.addDrawableEntity(
                 GameState.AI_TANK_ID_2,
                 RunGameView.AI_TANK_IMAGE_FILE,
                 aiTank2.getX(),
                 aiTank2.getY(),
                 aiTank2.getAngle()
         );

         runGameView.addDrawableEntity(
                 GameState.POWER_UP_ID,
                 RunGameView.POWER_UP_IMAGE_FILE,
                 powerUp.getX(),
                 powerUp.getY(),
                 powerUp.getAngle()
         );

         //Add walls to gameState first, then draw walls on the screen
         List<WallImageInfo> wallImageInfoList = WallImageInfo.readWalls();
         for(WallImageInfo wallInfo: wallImageInfoList){
             Wall newWall = new Wall(wallInfo.getX(), wallInfo.getY(),0);
             gameState.addEntity(newWall);
             runGameView.addDrawableEntity(newWall.getId(), wallInfo.getImageFile(), newWall.getX(), newWall.getY(), 0);
         }

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

    // update should handle one frame of gameplay. All tanks and shells move one step, and all drawn entities
    // should be updated accordingly. It should return true as long as the game continues.
    private boolean update() {
        //Ask all tanks, shells, etc. to move
        for(Entity entity: gameState.getEntities()){
            entity.move(gameState);
        }


        //Ask all tanks, shells, etc. to check bounds
        for(Entity entity: gameState.getEntities() ){
            entity.checkBounds();
        }


        //Ask gameState -- any new shells to draw?
        //if so, call addDrawableEntity
        List<Entity> newShellEntities = gameState.getNewShells(); //Save new shells in a separate list (TO AVOID concurrentModificationException)
        for(Entity newShellEntity: newShellEntities){             //draw new shells on screen first
            runGameView.addDrawableEntity(newShellEntity.getId(), RunGameView.SHELL_IMAGE_FILE, newShellEntity.getX(), newShellEntity.getY(), newShellEntity.getAngle());
        }

        for (Entity entity: newShellEntities) {                   //then add these shells to entity list
            gameState.addEntity(entity);
        }
        newShellEntities.removeAll(newShellEntities);             //NEED TO EMPTY newShellEntities list before next update.


        //Ask gameState -- any shells to remove?
        //if so, call removeDrawableEntity
        List<Entity> removeShells = new ArrayList<>();
        for(Entity entity: gameState.getEntities()){
            if (entity.isAtBorder()) {
                removeShells.add(entity);
            }
        }

        for(Entity entity: removeShells){
            gameState.rmEntity(entity);
            runGameView.removeDrawableEntity(entity.getId());
        }


        //check collisions
        //////This is the old-version, brute-force iteration.//////
//        for(Entity entity1: gameState.getEntities()){
//            for(Entity entity2: gameState.getEntities()) {
//                if(entity1.getId() == entity2.getId()) continue;
//                if (entitiesOverlap(entity1, entity2)) {
//                    allRMEntities.addAll(handleCollision(entity1, entity2));
//                }
//            }
//        }
        List<Entity> allRMEntities = new ArrayList<>();
        List<Entity> allEntities = gameState.getEntities();
        int size = allEntities.size();
        //////No duplicate pairs -- iteration version//////
        for(int i = 0; i < size - 1 ; i++){
            for(int j = i + 1; j < size; j++){
                if(entitiesOverlap(allEntities.get(i), allEntities.get(j))){
                    allRMEntities.addAll(handleCollision(allEntities.get(i), allEntities.get(j)));
                }
            }
        }

        for(Entity entity: allRMEntities){
            gameState.rmEntity(entity);
            runGameView.removeDrawableEntity(entity.getId());
        }
        //playerTank dies will end the game
        //And last AiTank dies will end the game as well
        if(playerTankNum == 0 || aiTankNum == 0) {
            if(playerTankNum == 0) {
                gameState.playSound("LostSound.wav");
            }else{
                gameState.playSound("WinSound.wav");
            }
            resetGameVars();
            return false;
        }
        allRMEntities.removeAll(allRMEntities);


        //update entity location and angle
        for(Entity entity: gameState.getEntities()){
            runGameView.setDrawableEntityLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
        }


        //change to END_MENU_SCREEN is Esc is pressed.
        if(gameState.getIsEscapePressed()){
            resetGameVars();
            return false;
        }

        runGameView.showHP(playerTank, aiTank1, aiTank2);
        return true;
    }

    //private helper methods to check collide
    private boolean entitiesOverlap(Entity entity1, Entity entity2) {
        return entity1.getX() < entity2.getXBound()
                && entity1.getXBound() > entity2.getX()
                && entity1.getY() < entity2.getYBound()
                && entity1.getYBound() > entity2.getY();
    }

    private List<Entity> handleCollision(Entity entity1, Entity entity2){
        List<Entity> allRMEntities = new ArrayList<>();
        if (entity1 instanceof Tank && entity2 instanceof Tank) {
            tankVStank(entity1, entity2);
        } else if (entity1 instanceof Shell && entity2 instanceof Shell) {
            allRMEntities.addAll(shellVSshell(entity1, entity2));
        } else if (entity1 instanceof Tank && entity2 instanceof Shell) {
            allRMEntities.addAll(tankVSshell(entity1, entity2));
        } else if (entity1 instanceof Shell && entity2 instanceof Tank) {
            allRMEntities.addAll(tankVSshell(entity2, entity1));
        } else if (entity1 instanceof Tank && entity2 instanceof Wall){
            tankVSwall(entity1, entity2);
        } else if (entity1 instanceof Wall && entity2 instanceof Tank){
            tankVSwall(entity2, entity1);
        } else if (entity1 instanceof Shell && entity2 instanceof Wall){
            allRMEntities.addAll(shellVSwall(entity1, entity2));
        } else if (entity1 instanceof Wall && entity2 instanceof Shell) {
            allRMEntities.addAll(shellVSwall(entity2, entity1));
        } else if (entity1 instanceof Tank && entity2 instanceof PowerUp) {
            allRMEntities.addAll(tanklVSbloodBag(entity1, entity2));
        }else if (entity1 instanceof PowerUp && entity2 instanceof Tank) {
            allRMEntities.addAll(tanklVSbloodBag(entity2, entity1));
        }
        return allRMEntities;
    }

    private void tankVStank(Entity entity1, Entity entity2){
        double disA = entity1.getXBound() - entity2.getX();
        double disB = entity2.getXBound() - entity1.getX();
        double disC = entity1.getYBound() - entity2.getY();
        double disD = entity2.getYBound() - entity1.getY();
        double[] dis = {disA,disB, disC, disD};
        Arrays.sort(dis);
        double minDistance = dis[0];

        if(minDistance == disA){
            entity1.setX (entity1.getX() - minDistance/2);
            entity2.setX (entity2.getX() + minDistance/2);
        }else if(dis[0] == disB){
            entity1.setX (entity1.getX() + minDistance/2);
            entity2.setX (entity2.getX() - minDistance/2);
        }else if(dis[0] == disC){
            entity1.setY (entity1.getY() - minDistance/2);
            entity2.setY (entity2.getY() + minDistance/2);
        }else {
            entity1.setY (entity1.getY() + minDistance/2);
            entity2.setY (entity2.getY() - minDistance/2);
        }
    }

    private List<Entity> shellVSshell(Entity entity1, Entity entity2){
        List<Entity> rmEntities = new ArrayList<>();
        rmEntities.add(entity1);
        rmEntities.add(entity2);
        runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity1.getX(), entity1.getY());
        runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity2.getX(), entity2.getY());
        return rmEntities;
    }

    private List<Entity> tankVSshell(Entity entity1, Entity entity2){ //entity1 is tank, entity2 is shell
        List<Entity> rmEntities = new ArrayList<>();

        //shell.shooterID == tank.id, do nothing
        if(((Shell)entity2).getShooterID().equals(entity1.getId())){}

        //shell.shooterID != tank.id && tank's healthpoint != 1
        //tank take away 1 hp, remove shell
        if(!((Shell)entity2).getShooterID().equals(entity1.getId()) && ((Tank)entity1).getHealthPoint() != 1){
            //tank takes the damage
            ((Tank)entity1).decreaseHealthPoint();
            //remove shell
            rmEntities.add(entity2);
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity2.getX(), entity2.getY());
        }

        //shell.shooterID != tank.id && tank's healthPoint == 1
        //remove tank, remove shell;
        //if tank.ID == playerTank || tank is the last AiTank, game over, transit to the end menu screen
        if(!((Shell)entity2).getShooterID().equals(entity1.getId()) && ((Tank)entity1).getHealthPoint() == 1){
            //remove tank & shell
            rmEntities.add(entity1);
            rmEntities.add(entity2);
            ((Tank)entity1).decreaseHealthPoint();
            runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION, RunGameView.BIG_EXPLOSION_FRAME_DELAY, entity1.getX(), entity1.getY());
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity2.getX(), entity2.getY());

            //Tank num --, take out all the end game action to update
            if(entity1.getId().equals(GameState.PLAYER_TANK_ID)){
                playerTankNum--;
                gameState.playSound("TankExplosionSound.wav");
            }else if(!entity1.getId().equals(GameState.PLAYER_TANK_ID)) {
                aiTankNum--;
                gameState.playSound("TankExplosionSound.wav");
            }
        }
        return rmEntities;
    }

    private void tankVSwall(Entity entity1, Entity entity2){ //entity1 is tank
        double disA = entity1.getXBound() - entity2.getX();
        double disB = entity2.getXBound() - entity1.getX();
        double disC = entity1.getYBound() - entity2.getY();
        double disD = entity2.getYBound() - entity1.getY();
        double[] dis = {disA,disB, disC, disD};
        Arrays.sort(dis);
        double minDistance = dis[0];

        if(minDistance == disA){
            entity1.setX (entity1.getX() - minDistance);
        }else if(dis[0] == disB){
            entity1.setX (entity1.getX() + minDistance);
        }else if(dis[0] == disC){
            entity1.setY (entity1.getY() - minDistance);
        }else {
            entity1.setY (entity1.getY() + minDistance);
        }
    }

    private List<Entity> shellVSwall(Entity entity1, Entity entity2){ //entity1 is shell, entity2 is wall
        List<Entity> rmEntities = new ArrayList<>();
        //remove shell
        rmEntities.add(entity1);
        runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity1.getX(), entity1.getY());
        //if healthPoint != 1, wall will loose a healthPoint  else wall will be remove too.
        if(((Wall) entity2).getHealthPoint() != 1){
            ((Wall) entity2).decreaseHealthPoint();
        }else if(((Wall) entity2).getHealthPoint() == 1){
            rmEntities.add(entity2);
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity2.getX(), entity2.getY());
            //add explosion sound
            gameState.playSound("WallExplosionSound.wav");
        }
        return rmEntities;
    }

    private List<Entity> tanklVSbloodBag(Entity entity1, Entity entity2){//entity1 is Tank, entity2 is bloodBag
        List<Entity> rmEntities = new ArrayList<>();
        //remove bloodBag
        rmEntities.add(entity2);
        ((Tank)entity1).powerUp();
        //add sound
        gameState.playSound("PickUpPowerupSound.wav");
        return rmEntities;
    }


    public class GameActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            String actionCommand = event.getActionCommand();

            if(actionCommand.equals(StartMenuView.START_BUTTON_ACTION_COMMAND)) {
                mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
                runGame();
            }else if(actionCommand.equals(StartMenuView.EXIT_BUTTON_ACTION_COMMAND)){
                mainView.closeGame();
            }
        }
    }

    private void resetGameVars(){
        mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
        gameState.reset();
        runGameView.reset();
        playerTankNum = 0;
        aiTankNum = 0;
    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
