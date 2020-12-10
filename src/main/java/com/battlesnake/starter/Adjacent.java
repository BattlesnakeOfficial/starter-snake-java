package com.battlesnake.starter;

import com.battlesnake.starter.Point;

/**
 * This class holds the
 */

public class Adjacent {
  private class Tile {

    private Point point;
    private int value;
    private boolean deadTile;
    // private float preference;

    // constructor
    public Tile(Point point, int value, boolean deadTile) {
      this.point = point;
      this.value = value;
      this.deadTile = deadTile;
    }

    // Getters
    public Point getPoint() {
      return point;
    }

    public int getValue() {
      return value;
    }

    public boolean isDeadTile() {
      return deadTile;
    }

    // Setters
    public void setCoordinate(Point point) {
      this.point = point;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public void setDeadTile(boolean deadTile) {
      this.deadTile = deadTile;
    }
  }

  private Tile[] tile = new Tile[4];

  public Adjacent(Point head) {

    if (tile.value == enum.death) {
      tile.deadTile = true;
    }

    // Set all adjacent tiles pont value and enum value
    // TODO: Set enum value
    // TODO: corrent the assignment of the position value
    tile[0] = new Adjacent( // up
        new Point(head.point.x + 1, head.point.p), GameBoardObjects.EMPTY, false);
    tile[1] = new Adjacent( // right
        new Point(head.point.x, head.point.p + 1), GameBoardObjects.EMPTY, false);
    tile[2] = new Adjacent( // down
        new Point(head.point.x - 1, head.point.p), GameBoardObjects.EMPTY, false);
    tile[3] = new Adjacent( // left
        new Point(head.point.x, head.point.p - 1), GameBoardObjects.EMPTY, false);
  }

}

private void immediateReduction(Adjacent[] adjacent) {
  for (Adjacent a : adjacent) {

  }
}
