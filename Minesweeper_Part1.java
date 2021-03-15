import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//represent the cell class
class Cell {
  String fill;
  ArrayList<Cell> neighbors;
  boolean mine;
  int nearby;

  int cellSize = 15;
  int flagSize = 8;
  WorldImage grid = new RectangleImage(cellSize, cellSize, OutlineMode.OUTLINE, Color.BLACK);

  // initial constructor
  Cell() {
    this.fill = "hidden";
    this.neighbors = new ArrayList<Cell>();
    this.mine = false;
    this.nearby = 0;
  }

  // convinient constructor 1
  Cell(String fill, boolean mine) {
    this.fill = fill;
    this.neighbors = new ArrayList<Cell>();
    this.mine = mine;
    this.nearby = 0;
  }

  // convinient constructor 2 - Random
  Cell(int seed, String fill, boolean mine) {
    this.fill = fill;
    this.neighbors = new ArrayList<Cell>();
    this.mine = mine;
    this.nearby = 0;
  }

  // convinient constructor 3 - Neighbors
  Cell(ArrayList<Cell> neighbors) {
    this.fill = "hidden";
    this.neighbors = neighbors;
    this.mine = false;
    this.nearby = 0;
  }

  // draw the cell image
  public WorldImage drawCell() {
    // check if a cell is clicked
    if (this.fill.equals("hidden") || this.fill.equals("flagged")) {
      return this.unclicked();
    }
    else {
      return this.clicked();
    }
  }

  // draw an unclicked cell
  public WorldImage unclicked() {

    WorldImage cover = new RectangleImage(cellSize, cellSize, OutlineMode.SOLID, Color.PINK);
    WorldImage hidden = new OverlayImage(grid, cover);
    WorldImage flag = new EquilateralTriangleImage(flagSize, OutlineMode.SOLID, Color.YELLOW);
    WorldImage flagged = new OverlayImage(flag, hidden);

    // check if this cell is marked as flagged
    if (this.fill.equals("flagged")) {
      return flagged;
    }
    else {
      return hidden;
    }
  }

  // draw a clicked cell
  public WorldImage clicked() {

    // a cell with a mine ("here is a mine")
    WorldImage mine = new CircleImage(7, OutlineMode.SOLID, Color.BLACK);
    WorldImage mined = new OverlayImage(grid, mine);
    // a cell with a mine and a cross ("no mine here")
    WorldImage cross = new OverlayImage(new LineImage(new Posn(0, cellSize), Color.BLACK),
        new LineImage(new Posn(cellSize, 0), Color.BLACK));
    WorldImage crossed = new OverlayImage(cross, mined);
    // a cell with a mine and a red background ("you clicked a mine!")
    WorldImage redBG = new RectangleImage(cellSize, cellSize, OutlineMode.SOLID, Color.RED);
    WorldImage bomb = new OverlayImage(mined, redBG);
    // a cell with a number ("# of mines nearby")
    this.nearby = this.countMines();
    WorldImage text = new TextImage(String.valueOf(this.nearby), Color.GREEN);
    WorldImage numbered = new OverlayImage(grid, text);

    // check if this cell has a mine
    if (this.mine) {
      // check the its state
      if (this.fill.equals("crossed")) {
        return crossed;
      }
      else if (this.fill.equals("bomb")) {
        return bomb;
      }
      else {
        return mined;
      }
    }
    else {
      // check if it has any neighbor with mine
      if (this.nearby == 0) {
        return grid;
      }
      else {
        return numbered;
      }
    }
  }

  // count the number of mines neighboring a particular cell
  public int countMines() {
    int num = 0;

    for (Cell c : this.neighbors) {
      if (c.mine) {
        num += 1;
      }
    }
    return num;
  }

  // random assign this cell as a mine
  public Cell random() {
    int n = new Random().nextInt(10);
    if (n % 5 == 0) {
      return new Cell("hidden", true);
    }
    else {
      return this;
    }
  }

  // add the given cell to the neighbor of this cell
  public void addNeighbor(Cell neighbor) {
    this.neighbors.add(neighbor);
  }
}

//represent the Game class
class Game extends World {

  int xgridNum;
  int ygridNum;
  int mineNum;
  ArrayList<Cell> cells;

  // VARIABLES
  int cellSize = 15;
  int width = this.xgridNum * cellSize;
  int height = this.ygridNum * cellSize;
  int num = this.xgridNum * this.ygridNum;

  // the initial constructor for the Game
  Game(int xgridNum, int ygridNum, int mineNum) {
    this.xgridNum = xgridNum;
    this.ygridNum = ygridNum;
    this.mineNum = mineNum;
    this.cells = this.generateArr();
    this.neighboring();
  }

  // constructor for mouseEvent
  Game(int xgridNum, int ygridNum, int mineNum, ArrayList<Cell> cells) {
    this.xgridNum = xgridNum;
    this.ygridNum = ygridNum;
    this.mineNum = mineNum;
    this.cells = cells;
  }

  // create the ArrayList of all cells for the board
  public ArrayList<Cell> generateArr() {
    int num = this.xgridNum * this.ygridNum;
    ArrayList<Cell> arr = new ArrayList<Cell>();
    int numMineSofar = 0;

    // 1. For-loop generate the Arraylist of Cells
    for (int i = 0; i <= num; i++) {
      Cell c = new Cell("hidden", false).random();
      // a). update the number of mine so far
      if (c.mine) {
        numMineSofar += 1;
      }
      // b).add the cell to the cells
      if (numMineSofar <= this.mineNum) {
        arr.add(c);
      }
      else {
        arr.add(new Cell());
      }
    }

    // 2. Fix the array list of cells if the number of mine
    // is less than the given number
    int n = this.mineNum - numMineSofar;
    if (n == 0) {
      return arr;
    }
    else {
      return this.fix(arr, n);
    }
  }

  // fix the arraylist with mine to satisfy the condition
  // with enough random mine in game
  public ArrayList<Cell> fix(ArrayList<Cell> arr, int n) {
    for (int j = 0; j < n; j++) {
      int x = new Random().nextInt(10);
      Cell xget = arr.get(x);
      if (xget.mine) {
        j = j - 1;
      }
      else {
        arr.set(x, new Cell("hidden", true));
      }
    }
    return arr;
  }

  // create neighbor for each cell in the game
  public void neighboring() {

    for (int i = 0; i < this.cells.size(); i = i + 1) {

      Cell cell = this.cells.get(i);
      ArrayList<Integer> candidates = new ArrayList<Integer>(Arrays.asList((i - this.xgridNum - 1),
          (i - this.xgridNum), (i - this.xgridNum + 1), (i + 1), (i + this.xgridNum + 1),
          (i + this.xgridNum), (i + this.xgridNum - 1), (i - 1)));

      for (int candidate : candidates) {
        if (!(this.invalidNeighbor(i, candidate))) {
          Cell neighbor = this.cells.get(candidate);
          cell.addNeighbor(neighbor);
        }
      }
      this.cells.set(i, cell);
    }
  }

  // determine whether the given cell is the neighbors of current cell
  public boolean invalidNeighbor(int current, int neighbor) {
    int boardSize = this.xgridNum * this.ygridNum;
    return (((current + 1) % this.xgridNum == 0) && (neighbor % this.xgridNum == 0))
        || ((current % this.xgridNum == 0) && ((neighbor + 1) % this.xgridNum == 0))
        || (neighbor < 0) || (neighbor > boardSize);
  }

  // draw cells on the board
  public WorldScene makeScene() {

    WorldScene board = new WorldScene(width, height);
    WorldImage bg = new RectangleImage(width, height, OutlineMode.SOLID, Color.GRAY);
    board.placeImageXY(bg, width / 2, height / 2);

    int index = 0;

    for (int i = 0; i < this.ygridNum; i++) {
      for (int j = 0; j < this.xgridNum; j++) {
        int y = i * cellSize + cellSize / 2;
        int x = j * cellSize + cellSize / 2;

        Cell cell = this.cells.get(index);
        WorldImage cellImage = cell.drawCell();
        board.placeImageXY(cellImage, x, y);

        index += 1;
      }
    }
    return board;
  }

  @Override
  // reveal cells, including flood-fill effect (left-mouse-click)
  // cell fill has five states : flagged ; hidden ; revealed ; bomb ; crossed;
  public void onMouseClicked(Posn pos, String buttonName) {
    if (buttonName.equals("LeftButton")) {
      this.fixCell(pos, "revealed");
    }
    if (buttonName.equals("RightButton")) {
      this.fixCell(pos, "flagged");
    }
  }

  // fix the cell by changing its state
  public void fixCell(Posn pos, String str) {
    // int index = 0;
    boolean found = false;

    // find out the posn for each cell in the arraylist
    for (int i = 0; i < this.ygridNum; i++) {
      for (int j = 0; j < this.xgridNum; j++) {
        int y = i * cellSize;
        int x = j * cellSize;
        int index = i * xgridNum + j;
        // update the number of cell's surrounding mines
        Cell c = this.cells.get(index);
        c.nearby = c.countMines();
        boolean xRange = x <= pos.x && pos.x <= x + cellSize;
        boolean yRange = y <= pos.y && pos.y <= y + cellSize;

        if (xRange && yRange) {
          // update the boolean
          found = true;
          // update this clicked cell by changing its states
          if (c.fill.equals("flagged") && str.equals("flagged")) {
            c.fill = "hidden";
          }
          else if (c.fill.equals("hidden")) {
            c.fill = str;
            if (c.mine) {
              c.fill = "bomb";
              this.endgame("lose");
            }
            if (!c.mine && c.nearby == 0) {
              this.floodfill(c.neighbors);
            }
          }
          found = true;
          break;
        }
        // this.worldEnds();
        if (this.allClear()) {
          this.endgame("win");
        }

      }
      if (found) {
        break;
      }
    }
  }

  // to check and reveal all possible blank cell
  public void floodfill(ArrayList<Cell> neighbors) {
    for (Cell cell : neighbors) {
      // update the number of cell's surrounding mines
      cell.nearby = cell.countMines();
      if (!(cell.fill.equals("revealed"))) {
        if (!cell.mine && cell.nearby == 0) {
          cell.fill = "revealed";
          this.floodfill(cell.neighbors);
        }
        else if (cell.fill.equals("flagged")) {
          cell.fill = "flagged";
        }
        else {
          cell.fill = "revealed";
        }
      }
    }
  }

  // check if all the safe cells are revealed
  boolean allClear() {
    boolean isClear = true;
    for (Cell c : this.cells) {
      boolean isRevealedText = !c.mine && c.fill.equals("revealed");
      boolean isHiddenMine = c.mine && !c.fill.equals("revealed");
      if (isRevealedText || isHiddenMine) {
        continue;
      }
      else {
        isClear = false;
        break;
      }
    }
    if (!isClear) {
      return false;
    }
    return isClear;
  }

  // end the game with an appropriate message
  void endgame(String s) {
    if (s.equals("win")) {
      this.endOfWorld("CONGRATULATIONS!");
      this.endOfWorld("CONGRATULATIONS!");
      System.out.println("CONGRATULATIONS!");
    }
    else {
      // reveal all mines (except the flagged ones)
      for (Cell c : this.cells) {
        // check if a mine is not flagged
        if (c.mine && c.fill.equals("hidden")) {
          // if true, reveal it
          c.fill = "revealed";
        }
        // check if this is flagged but this is not a mine
        else if (!c.mine && c.fill.equals("flagged")) {
          // if true, reveal it with a cross
          c.fill = "crossed";
        }
      }
      this.endOfWorld("BETTER LUCK NEXT TIME :(");
      this.endOfWorld("BETTER LUCK NEXT TIME :(");
      System.out.println("BETTER LUCK NEXT TIME :(");
    }
  }

  // make the final sceen
  public void makeEndScene(String str) {
    WorldScene current = this.makeScene();
    TextImage msg = new TextImage(str, Color.BLACK);

    current.placeImageXY(msg, width / 2, height / 2);
  }
}
