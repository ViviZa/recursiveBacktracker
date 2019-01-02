public class Executer {

    public static void main(String[] args) {
        Board board = new Board(2, 2);
        board.initializeBoard();
        board.generateMaze(0, 0);
        board.printCellWalls();
        board.printMaze();
        //board.printCellWalls();
    }

}
