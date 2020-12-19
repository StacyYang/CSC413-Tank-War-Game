package edu.csc413.tankgame;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Stores the information associated with a wall that can be drawn in the game. */
public class WallImageInfo {
    private static final String WALL_IMAGE_FILE_PREFIX = "wall-";
    private static final String WALL_IMAGE_FILE_SUFFIX = ".png";
    private static final String WALLS_SETUP_FILE = "walls.txt";
    private static final double WALL_WIDTH = 32.0;
    private static final double WALL_HEIGHT = 32.0;

    private final String imageFile;
    private final double x;
    private final double y;

    private WallImageInfo(String imageFile, double x, double y) {
        this.imageFile = imageFile;
        this.x = x;
        this.y = y;
    }

    /** Returns the image file name for this wall. */
    public String getImageFile() {
        return imageFile;
    }

    /** Returns the x coordinate of the wall. */
    public double getX() {
        return x;
    }

    /** Returns the y coordinate of the wall. */
    public double getY() {
        return y;
    }

    /**
     * Returns a list of WallImageInfos created by reading the layout described in the
     * WALLS_SETUP_FILE.
     */
    public static List<WallImageInfo> readWalls() {
        ArrayList<ArrayList<Integer>> walls;
        URL fileUrl = WallImageInfo.class.getClassLoader().getResource(WALLS_SETUP_FILE);
        if (fileUrl == null) {
            throw new RuntimeException("Unable to find the resource: " + WALLS_SETUP_FILE);
        }

        try {
            walls =
                    Files.lines(Path.of(fileUrl.toURI()))
                            .map(WallImageInfo::lineToList)
                            .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException | URISyntaxException exception) {
            throw new RuntimeException(exception);
        }

        ArrayList<WallImageInfo> wallImageInfos = new ArrayList<>();
        for (int row = 0; row < walls.size(); row++) {
            for (int col = 0; col < walls.get(row).size(); col++) {
                if (walls.get(row).get(col) != 0) {
                    double x = col * WALL_WIDTH;
                    double y = row * WALL_HEIGHT;
                    wallImageInfos.add(
                            new WallImageInfo(
                                    String.format("%s%d%s", WALL_IMAGE_FILE_PREFIX, walls.get(row).get(col), WALL_IMAGE_FILE_SUFFIX),
                                    x,
                                    y));
                }
            }
        }
        return wallImageInfos;
    }

    private static ArrayList<Integer> lineToList(String line) {
        return Arrays.stream(line.split("\\s")).map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
    }
}