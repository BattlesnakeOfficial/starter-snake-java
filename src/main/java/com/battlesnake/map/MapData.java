package com.battlesnake.map;

public class MapData {
  public int[][] map = new int[11][11];

  private void resetMap() {
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        map[i][j] = 0;
      }
    }
  }

  public void fillMap(JsonNode moveRequest) {
    resetMap();
    // TODO parse json here

  }

  public int getMapCoordinate(int x, int y) {
    return map[x][y];
  }
}


/*
  Example how to retrieve data from the request payload:

  String gameId = moveRequest.get("game").get("id").asText();
  int height = moveRequest.get("board").get("height").asInt();

*/