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
    public Tile(Point point) {
      this.point = new Point(point);
      // this.value = getTileValue(point);
      // this.deadTile = isDeadTile(point);
    }

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

    // Set all adjacent tiles pont value and enum value
    tile[0] = new Tile(new Point(head.getX() + 1, head.getY()));	// up
    tile[1] = new Tile(new Point(head.getX(), head.getY() + 1));	// right
    tile[2] = new Tile(new Point(head.getX() - 1, head.getY()));	// down
    tile[3] = new Tile(new Point(head.getX(), head.getY() - 1));	// left
  }
}
