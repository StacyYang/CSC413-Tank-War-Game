package edu.csc413.tankgame.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * MainView is the primary view that contains and controls individual screens (represented by the separate StartMenuView
 * and RunGameView classes). MainView can be interacted with to set which of those screens is currently showing, and it
 * is also registered to listen for keyboard events.
 */
public class MainView {
    /** The different screens that can be shown. */
    public enum Screen {
        START_MENU_SCREEN("start"),
        RUN_GAME_SCREEN("game"),
        END_MENU_SCREEN("end");

        private final String screenName;

        Screen(String screenName) {
            this.screenName = screenName;
        }

        public String getScreenName() {
            return screenName;
        }
    }

    private final JFrame mainJFrame;
    private final JPanel mainPanel;
    private final CardLayout mainPanelLayout;
    private final RunGameView runGameView;

    // TODO: Finish implementing this.
    // MainView is responsible for assigning listeners to various UI components (like buttons and keyboard input).
    // However, we want to return control to GameDriver when those events happen. How can we have listeners that directs
    // us back to the code in GameDriver?
    public MainView() {
        mainJFrame = new JFrame();
        mainJFrame.setVisible(false);
        mainJFrame.setResizable(false);
        mainJFrame.setTitle("Tank Wars");
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainJFrame.addKeyListener(null);

        mainPanel = new JPanel();
        mainPanelLayout = new CardLayout();
        mainPanel.setLayout(mainPanelLayout);

        StartMenuView startMenuView = new StartMenuView("Start Game");
        mainPanel.add(startMenuView, Screen.START_MENU_SCREEN.getScreenName());

        StartMenuView endMenuView = new StartMenuView("Restart Game");
        mainPanel.add(endMenuView, Screen.END_MENU_SCREEN.getScreenName());

        runGameView = new RunGameView();
        mainPanel.add(runGameView, Screen.RUN_GAME_SCREEN.getScreenName());

        mainJFrame.add(mainPanel);
    }

    /**
     * Returns the contained RunGameView. This method provides the GameDriver with direct access, which is needed for
     * updating game-related graphics while the program is running.
     */
    public RunGameView getRunGameView() {
        return runGameView;
    }

    /** Changes the screen that is currently showing. */
    public void setScreen(Screen screen) {
        mainJFrame.setVisible(false);

        Dimension screenSize = switch (screen) {
            case START_MENU_SCREEN, END_MENU_SCREEN -> StartMenuView.SCREEN_DIMENSIONS;
            case RUN_GAME_SCREEN -> RunGameView.SCREEN_DIMENSIONS;
        };
        mainJFrame.setSize(screenSize);
        mainPanelLayout.show(mainPanel, screen.getScreenName());

        mainJFrame.setVisible(true);
    }

    /** Ends the program. */
    public void closeGame() {
        mainJFrame.dispatchEvent(new WindowEvent(mainJFrame, WindowEvent.WINDOW_CLOSING));
    }
}
