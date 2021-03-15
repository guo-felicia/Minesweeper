import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javalib.impworld.WorldScene;
import javalib.worldimages.CircleImage;
import javalib.worldimages.EquilateralTriangleImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

//examples and tests for Games
class ExamplesGame {
  ExamplesGame() {
  }

  int cellSize = 15;
  int flagSize = 8;

  WorldImage cover = new RectangleImage(cellSize, cellSize, OutlineMode.SOLID, Color.PINK);
  WorldImage grid = new RectangleImage(cellSize, cellSize, OutlineMode.OUTLINE, Color.BLACK);
  WorldImage mine = new CircleImage(7, OutlineMode.SOLID, Color.RED);
  WorldImage flag = new EquilateralTriangleImage(flagSize, OutlineMode.SOLID, Color.YELLOW);
  int width = 15;
  int height = 15;

  ArrayList<Cell> somecell = new ArrayList<Cell>();
  Cell cell_unclicked_0;
  Cell cell_unclicked_1;
  Cell cell_clicked_0;
  Cell cell_clicked_1;
  Cell cell_clicked_0_disconnected;
  Cell cell_flagged_1;
  Cell cell_flagged_0;

  WorldScene board;
  WorldImage bg;
  WorldScene board_hidden;
  WorldScene board_revealed_0;
  WorldScene board_revealed_1;
  WorldScene board_flagged;

  Game game_hidden;
  Game game_revealed_0;
  Game game_revealed_1;
  Game game_flagged;
  Game testGame;
  Game testGame2;
  Game initialGame = new Game(30, 16, 99);

  void initCellData() {

    this.cell_unclicked_0 = new Cell("hidden", false);
    this.cell_unclicked_1 = new Cell("hidden", true);
    this.cell_clicked_0 = new Cell("revealed", false);
    this.cell_clicked_1 = new Cell("revealed", true);
    this.cell_clicked_0_disconnected = new Cell("revealed", false);
    this.cell_flagged_1 = new Cell("flagged", true);
    this.cell_flagged_0 = new Cell("flagged", false);
  }

  void initSceneData() {

    board = new WorldScene(width, height);
    bg = new RectangleImage(width, height, OutlineMode.SOLID, Color.GRAY);

    game_hidden = new Game(1, 1, 0);
    game_revealed_0 = new Game(1, 1, 0);
    game_revealed_1 = new Game(1, 1, 1);
    game_flagged = new Game(1, 1, 1);
    testGame = new Game(3, 2, 2);
    testGame2 = new Game(1, 4, 1);

    board.placeImageXY(bg, width / 2, height / 2);
    board_hidden.placeImageXY(bg, width / 2, height / 2);
    board_revealed_0.placeImageXY(bg, width / 2, height / 2);
    board_revealed_1.placeImageXY(bg, width / 2, height / 2);
    board_flagged.placeImageXY(bg, width / 2, height / 2);

    game_hidden.cells.get(0).fill = "hidden";
    game_revealed_0.cells.get(0).fill = "revealed";
    game_revealed_1.cells.get(0).fill = "revealed";
    game_flagged.cells.get(0).fill = "flagged";

    board_hidden.placeImageXY(cell_unclicked_0.unclicked(), width / 2, height / 2);
    board_revealed_0.placeImageXY(cell_clicked_0.clicked(), width / 2, height / 2);
    board_revealed_1.placeImageXY(cell_clicked_1.clicked(), width / 2, height / 2);
  }

  // some variables for test method
  Cell cell0 = new Cell("hidden", false);
  Cell cell1 = new Cell("hidden", true);
  Cell cellR0 = new Cell(1, "hidden", false);
  Cell cellR1 = new Cell(3, "hidden", false);
  Cell cellR2 = new Cell(8, "hidden", false);
  ArrayList<Cell> arrays1 = new ArrayList<Cell>(
      Arrays.asList(cell1, cell1, cell0, cell0, cell0, cell1));
  ArrayList<Cell> arrays2 = new ArrayList<Cell>(
      Arrays.asList(cell0, cell1, cell0, cell1, cell1, cell0));

  // test the method drawCell
  boolean testDrawCell(Tester t) {
    initCellData();
    this.cell_clicked_0.addNeighbor(cell_clicked_1);
    return t.checkExpect(this.cell_unclicked_0.unclicked(), new OverlayImage(grid, cover))
        && t.checkExpect(this.cell_unclicked_1.unclicked(), new OverlayImage(grid, cover))
        && t.checkExpect(this.cell_clicked_0_disconnected.clicked(), grid)
        && t.checkExpect(this.cell_clicked_0.clicked(),
            new OverlayImage(grid, new TextImage(String.valueOf(1), Color.GREEN)))
        && t.checkExpect(this.cell_clicked_1.clicked(), new OverlayImage(grid, mine));
  }

  // test the method unclicked
  boolean testUnclicked(Tester t) {
    initCellData();
    return t.checkExpect(this.cell_unclicked_0.unclicked(), new OverlayImage(grid, cover))
        && t.checkExpect(this.cell_unclicked_1.unclicked(), new OverlayImage(grid, cover));
  }

  // test the method clicked
  boolean testClicked(Tester t) {
    initCellData();
    this.cell_clicked_0.addNeighbor(cell_clicked_1);
    return t.checkExpect(this.cell_clicked_0_disconnected.clicked(), grid)
        && t.checkExpect(this.cell_clicked_0.clicked(),
            new OverlayImage(grid, new TextImage(String.valueOf(1), Color.GREEN)))
        && t.checkExpect(this.cell_clicked_1.clicked(), new OverlayImage(grid, mine));
  }

  // test the method makeScene
  void testMakeScene(Tester t) {
    initSceneData();
    // one cell unclicked
    t.checkExpect(this.game_hidden.makeScene(), board_hidden);
    // one cell clicked
    t.checkExpect(this.game_revealed_0.makeScene(), board_revealed_0);
    t.checkExpect(this.game_revealed_1.makeScene(), board_revealed_1);
    // one cell flagged
    t.checkExpect(this.game_flagged.makeScene(), board_flagged);
  }

  // test of countMine method
  boolean testCountMine(Tester t) {
    // 1. modify the arrayList
    somecell.add(cell0);
    somecell.add(cell1);
    somecell.add(cell0);
    somecell.add(cell0);
    somecell.add(cell1);
    somecell.add(cell1);
    ArrayList<Cell> neighbor = this.cell1.neighbors;
    neighbor.add(new Cell(1, "revealed", true));
    // displaying elements
    System.out.println(somecell);

    // 2.define local variable
    Cell cellTest = new Cell(somecell);

    // 3. check the Expection of the method
    return t.checkExpect(new Cell().countMines(), 0)
        && t.checkExpect(cell1.neighbors,
            new ArrayList<Cell>(Arrays.asList(new Cell(1, "revealed", true))))
        && t.checkExpect(cell1.countMines(), 1) && t.checkExpect(cellTest.countMines(), 3);
  }

  // test the random method
  boolean testRandom(Tester t) {
    return t.checkExpect(cellR0.random(), new Cell(1, "hidden", true))
        && t.checkExpect(cellR1.random(), new Cell(3, "hidden", false))
        && t.checkExpect(cellR2.random(), new Cell(8, "hidden", false));
  }

  // test the addNeighbor method
  boolean testaddNeighbor(Tester t) {
    // 1. define the local vairable
    ArrayList<Cell> neighbors1 = new ArrayList<Cell>();
    ArrayList<Cell> neighbors2 = new ArrayList<Cell>();

    // 2. modify the arrayList
    cell0.addNeighbor(cell0);
    cell_unclicked_0.addNeighbor(cell1);
    cell_unclicked_0.addNeighbor(cell0);
    neighbors1.add(cell0);
    neighbors2.add(cell1);
    neighbors2.add(cell0);

    // 3. check the Expection of the method
    return t.checkExpect(new Cell().neighbors, new ArrayList<Cell>())
        && t.checkExpect(cell0.neighbors, neighbors1)
        && t.checkExpect(cell_unclicked_0.neighbors, neighbors2);
  }

  // test for generateArrTest method
  boolean testGenerateArrTest(Tester t) {
    // 1. define the local vairable
    ArrayList<Cell> arr1 = new ArrayList<Cell>();
    ArrayList<Cell> arr2 = new ArrayList<Cell>();
    // 2. modify the arraylist
    arr1.add(cell0);
    arr1.add(cell0);
    arr1.add(cell0);
    arr1.add(cell1);
    arr1.add(cell0);
    arr1.add(cell1);
    arr2.add(cell1);
    arr2.add(cell0);
    arr2.add(cell0);
    arr2.add(cell1);

    // 3. check the Expection of the method
    return t.checkExpect(this.testGame.cells,
        new ArrayList<Cell>(Arrays.asList(cell0, cell0, cell0, cell0, cell1, cell1)))
        && t.checkExpect(this.testGame.generateArr(), this.arrays1)
        && t.checkExpect(this.testGame2.generateArr(), this.arrays2);
  }

  // test the fix method
  boolean testFix(Tester t) {
    // define local variavle
    ArrayList<Cell> cells0 = new ArrayList<Cell>(Arrays.asList(cell0));
    ArrayList<Cell> cells1 = new ArrayList<Cell>(
        Arrays.asList(cell0, cell0, cell0, cell1, cell0, cell1));
    ArrayList<Cell> cells2 = new ArrayList<Cell>(Arrays.asList(cell1, cell0, cell0, cell1));
    ArrayList<Cell> cells2Fix = new ArrayList<Cell>(Arrays.asList(cell1, cell0, cell1, cell1));
    Cell c1 = new Cell(1, "hidden", false);
    Cell c2 = new Cell(0, "hidden", true);

    return t.checkExpect(new Game(1, 1, 1).fix(cells0, 0), cells0)
        && t.checkExpect(testGame.fix(cells1, 1), new ArrayList<Cell>(Arrays.asList(c1, c2)))
        && t.checkExpect(testGame2.fix(cells2, 1), cells2Fix);
  }

  // test for neighboring
  boolean testNeighboring(Tester t) {
    // 1. define local variable
    Cell c1 = new Cell(1, "hidden", false);
    Cell c2 = new Cell(0, "hidden", true);
    ArrayList<Cell> cells1 = new ArrayList<Cell>(Arrays.asList(cell0, cell1));
    ArrayList<Cell> cells2 = new ArrayList<Cell>(Arrays.asList(cell0, c1, cell0, c2, cell0, cell1));
    Game game1 = new Game(2, 1, 0, cells1);
    Game game2 = new Game(2, 3, 2, cells2);

    // 2. modify the data
    game1.neighboring();
    game2.neighboring();

    // 3. check the expected output
    return t.checkExpect(cell0.neighbors, new ArrayList<Cell>(Arrays.asList(cell1)))
        // test the top right
        && t.checkExpect(game2.cells.get(0), new ArrayList<Cell>(Arrays.asList(c1, cell0)))
        // test the top left
        && t.checkExpect(game2.cells.get(1), new ArrayList<Cell>(Arrays.asList(cell0, cell0)))
        // test the botom left
        && t.checkExpect(game2.cells.get(5), new ArrayList<Cell>(Arrays.asList(cell0, cell1)))
        // test the botome right
        && t.checkExpect(game2.cells.get(6), new ArrayList<Cell>(Arrays.asList(c2, cell0)));
  }

  // test for isValidNeighbor methos
  boolean testInValidNeighbor(Tester t) {
    initSceneData();

    return t.checkExpect(this.initialGame.invalidNeighbor(0, 0), false)
        & t.checkExpect(this.initialGame.invalidNeighbor(1, 0), false)
        & t.checkExpect(this.testGame.invalidNeighbor(1, 4), false)
        & t.checkExpect(this.testGame.invalidNeighbor(5, 2), false)
        & t.checkExpect(this.testGame2.invalidNeighbor(2, 4), true)
        & t.checkExpect(this.testGame2.invalidNeighbor(1, 4), true);
  }

  // test the onMouseClicked method in bigbang
  boolean testOnMouseClicked(Tester t) {
    initSceneData();
    // modify the game by clicked the mouuse
    // click on the first cell by "LeftButton" and suppose this cell is not mine
    this.testGame.onMouseClicked(new Posn(10, 8), "LeftButton");
    Cell c0 = this.testGame.cells.get(0);
    // click on the last cell by "RightButton" ane suppose this cell is not mine
    this.testGame.onMouseClicked(new Posn(10, 8), "RightButton");
    Cell c1 = this.testGame.cells.get(5);
    // click on the first cell by "LeftButton" and suppose this cell is mine
    this.testGame2.onMouseClicked(new Posn(8, 10), "LeftButton");
    Cell c3 = this.testGame2.cells.get(0);
    // click on the third cell by "RightButton" and suppose this cell is mine
    this.testGame2.onMouseClicked(new Posn(46, 13), "RightButton");
    Cell c4 = this.testGame2.cells.get(2);
    // click on the first cell by "RightButton" and suppose this cell is a flag
    this.game_revealed_0.onMouseClicked(new Posn(10, 8), "RightButton");
    Cell c5 = this.testGame2.cells.get(0);
    // click on the first cell by "LeftButton" and suppose this cell is a flag
    this.game_revealed_0.onMouseClicked(new Posn(10, 8), "LeftButton");
    Cell c6 = this.testGame2.cells.get(0);

    return t.checkExpect(c0.fill, "revealed") && t.checkExpect(c1.fill, "flagged")
        && t.checkExpect(c3.fill, "bomb") && t.checkExpect(c4.fill, "flagged")
        && t.checkExpect(c5.fill, "flagged") && t.checkExpect(c6.fill, "hidden");
  }

  // test the fixCell method
  boolean testFixCell(Tester t) {
    initSceneData();

    // modify the game by clicked the mouuse
    // click on the first cell by "LeftButton" and suppose this cell is not mine
    this.testGame.fixCell(new Posn(10, 8), "revealed");
    Cell c0 = this.testGame.cells.get(0);
    // click on the last cell by "RightButton" ane suppose this cell is not mine
    this.testGame.fixCell(new Posn(10, 8), "flagged");
    Cell c1 = this.testGame.cells.get(5);
    // click on the first cell by "LeftButton" and suppose this cell is mine
    this.testGame2.fixCell(new Posn(8, 10), "revealed");
    Cell c3 = this.testGame2.cells.get(0);
    // click on the third cell by "RightButton" and suppose this cell is mine
    this.testGame2.fixCell(new Posn(46, 13), "flagged");
    Cell c4 = this.testGame2.cells.get(2);
    // click on the first cell by "RightButton" and suppose this cell is a flag
    this.game_revealed_0.fixCell(new Posn(10, 8), "flagged");
    Cell c5 = this.testGame2.cells.get(0);
    // click on the first cell by "LeftButton" and suppose this cell is a flag
    this.game_revealed_0.fixCell(new Posn(10, 8), "revealed");
    Cell c6 = this.testGame2.cells.get(0);
    // check the expected ressult
    return t.checkExpect(c0.fill, "revealed") && t.checkExpect(c1.fill, "flagged")
        && t.checkExpect(c3.fill, "bomb") && t.checkExpect(c4.fill, "flagged")
        && t.checkExpect(c5.fill, "flagged") && t.checkExpect(c6.fill, "hidden");
  }

  // test he floodfill method
  boolean testFloodfill(Tester t) {
    initSceneData();

    Cell c1 = new Cell(1, "hidden", false);
    Cell c2 = new Cell(0, "hidden", true);

    // the neighbor has one mine
    ArrayList<Cell> arr1 = new ArrayList<Cell>(Arrays.asList(c1, c1, c2, c1, c1));
    // he neighbor has two mines next to each other
    ArrayList<Cell> arr2 = new ArrayList<Cell>(Arrays.asList(c1, c1, c2, c2));
    // the neioghbor has no mine
    ArrayList<Cell> arr3 = new ArrayList<Cell>(Arrays.asList(c1, c1));

    // modify the arraylist
    this.initialGame.floodfill(arr2);
    this.testGame2.floodfill(arr1);
    this.game_hidden.floodfill(arr3);

    return t.checkExpect(this.initialGame.cells.get(0).fill, "revealed")
        && t.checkExpect(this.initialGame.cells.get(1).fill, "revealed")
        && t.checkExpect(this.initialGame.cells.get(2).fill, "hidden")
        && t.checkExpect(this.initialGame.cells.get(3).fill, "revealed")
        && t.checkExpect(this.initialGame.cells.get(4).fill, "revealed")
        && t.checkExpect(this.testGame2.cells.get(0).fill, "revealed")
        && t.checkExpect(this.testGame2.cells.get(1).fill, "revealed")
        && t.checkExpect(this.testGame2.cells.get(2).fill, "hidden")
        && t.checkExpect(this.initialGame.cells.get(0).fill, "revealed")
        && t.checkExpect(this.game_hidden.cells.get(0).fill, "revealed")
        && t.checkExpect(this.game_hidden.cells.get(1).fill, "revealed");
  }

  Game winning;
  Game flaggedWinning;
  Game losing;
  Game flaggedLosing;
  Cell g = new Cell("bomb", true);
  Cell f = new Cell("hidden", false);
  Cell e = new Cell("flagged", false);
  Cell d = new Cell("flagged", true);
  Cell c = new Cell("revealed", true);
  Cell b = new Cell("hidden", true);
  Cell a = new Cell("revealed", false);
  ArrayList<Cell> loc1Before = new ArrayList<Cell>(Arrays.asList(b, a, a, a));
  ArrayList<Cell> loc2Before = new ArrayList<Cell>(Arrays.asList(c, a, f, g));
  ArrayList<Cell> loc3Before = new ArrayList<Cell>(Arrays.asList(d, a, a, a));
  ArrayList<Cell> loc4Before = new ArrayList<Cell>(Arrays.asList(d, e, a, a));

  // examples of four ending cases
  void initEnding() {
    winning = new Game(2, 2, 1, loc1Before);
    losing = new Game(2, 2, 2, loc2Before);
    flaggedWinning = new Game(2, 2, 1, loc3Before);
    flaggedLosing = new Game(2, 2, 1, loc4Before);
    winning.neighboring();
    flaggedWinning.neighboring();
    losing.neighboring();
  }

  // test the method allClear()
  void testAllClear(Tester t) {
    initEnding();
    t.checkExpect(winning.allClear(), true);
    t.checkExpect(flaggedWinning.allClear(), true);
    t.checkExpect(losing.allClear(), false);
  }

  // test the method endgame(String str)
  void testEndgame(Tester t) {
    initEnding();
    losing.endgame("lose");
    t.checkExpect(losing.cells.get(2).fill, "hidden");
    t.checkExpect(losing.cells.get(1).fill, "revealed");
    t.checkExpect(losing.cells.get(0).fill, "revealed");
    t.checkExpect(losing.cells.get(3).fill, "bomb");
    flaggedLosing.endgame("lose");
    t.checkExpect(flaggedLosing.cells.get(1).fill, "crossed");
    winning.endgame("win");
    t.checkExpect(winning.cells, loc1Before);
    flaggedWinning.endgame("win");
    t.checkExpect(flaggedWinning.cells, loc3Before);
  }

  // test the bigbang function of the Game
  void testBigBang(Tester t) {

    Game wyWorld = this.initialGame;
    int worldWidth = 550;
    int worldHeight = 350;
    double tickRate = 1;
    wyWorld.bigBang(worldWidth, worldHeight, tickRate);
  }
}