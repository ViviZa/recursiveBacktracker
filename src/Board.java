import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Board {
    private int width;
    private int height;
    public int size;
    public List<Cell> cells = new ArrayList<>();
    Stack<Integer> xStack = new Stack<Integer>();
    Stack<Integer> yStack = new Stack<Integer>();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        size = this.width * this.height;
    }

    public void initializeBoard() {
        for (int i = 0; i < size; i++) {
            cells.add(new Cell());
        }
    }

    public List<Cell> getMaze() {
        initializeBoard();
        generateMaze(0, 0);
        resetDuplicatedWalls();
        printCells(3, 3);

        return cells;
    }

    public Boolean generateMaze(int x, int y) {
        Cell currentCell = getCell(x, y);
        if (currentCell.visited == true) {
            xStack.pop();
            yStack.pop();
            int nextX = xStack.pop();
            int nextY = yStack.pop();
            getCell(nextX, nextY).visited = false;

            if (xStack.size() > 0) {
                //back at starting cell
                generateMaze(nextX, nextY);
            }
            return true;
        }
        currentCell.visited = true;
        xStack.push(x);
        yStack.push(y);
        List<Cell> neighbors = getCellNeighbors(x, y);
        Collections.shuffle(neighbors);
        int check = 0;
        while (check++ < neighbors.size()) {
            Cell randomCell = neighbors.get(check - 1);
            switch (randomCell.type) {
                case North:
                    if (!randomCell.visited) {
                        resetWall(x, y, "north");
                        resetWall(x, y + 1, "south");
                        y++;
                        check = neighbors.size();
                    }
                    break;
                case South:
                    if (!randomCell.visited) {
                        resetWall(x, y, "south");
                        resetWall(x, y - 1, "north");
                        y--;
                        check = neighbors.size();
                    }
                    break;
                case East:
                    if (!randomCell.visited) {
                        resetWall(x, y, "east");
                        resetWall(x + 1, y, "west");
                        x++;
                        check = neighbors.size();
                    }
                    break;
                case West:
                    if (!randomCell.visited) {
                        resetWall(x, y, "west");
                        resetWall(x - 1, y, "east");
                        x--;
                        check = neighbors.size();
                    }
                    break;

            }
        }
        generateMaze(x, y);
        return false;
    }

    public void printMaze() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                Cell currentCell = getCell(i, j);
                System.out.println("current Cell at x: " + i + " at y: " + j);
                System.out.println("Wall north: " + currentCell.cellWalls.north);
                System.out.println("Wall south: " + currentCell.cellWalls.south);
                System.out.println("Wall east: " + currentCell.cellWalls.east);
                System.out.println("Wall west: " + currentCell.cellWalls.west);

                if (!currentCell.cellWalls.north) {
                    System.out.print("0");
                } else {
                    System.out.print("1");
                }
                if (!currentCell.cellWalls.east) {
                    System.out.print("0");
                } else {
                    System.out.print("1");
                }
                if (!currentCell.cellWalls.west) {
                    System.out.print("0");
                } else {
                    System.out.print("1");
                }
                if (!currentCell.cellWalls.south) {
                    System.out.print("0");
                } else {
                    System.out.print("1");
                }
                System.out.println("");
                System.out.println("----");
            }
        }
    }

    private Cell getCell(int x, int y) {
        int i = y * width + x;
        cells.get(i).x = x;
        cells.get(i).y = y;

        if (i > cells.size() - 1 || x < 0 || y < 0) {
            return null;
        }
        return cells.get(i);
    }

    private boolean resetWall(int x, int y, String direction) {
        Cell cell = getCell(x, y);
        switch (direction) {
            case "north":
                cell.cellWalls.north = false;
                break;
            case "south":
                cell.cellWalls.south = false;
                break;
            case "east":
                cell.cellWalls.east = false;
                break;
            case "west":
                cell.cellWalls.west = false;
                break;
        }
        return true;
    }

    private List<Cell> getCellNeighbors(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();
        if (y > 0) {
            Cell cell = getCell(x, y - 1);
            cell.type = NeighborType.South;
            neighbors.add(cell);
        }
        if (y < height - 1) {
            Cell cell = getCell(x, y + 1);
            cell.type = NeighborType.North;
            neighbors.add(cell);
        }
        if (x > 0) {
            Cell cell = getCell(x - 1, y);
            cell.type = NeighborType.West;
            neighbors.add(cell);
        }
        if (x < width - 1) {
            Cell cell = getCell(x + 1, y);
            cell.type = NeighborType.East;
            neighbors.add(cell);
        }

        return neighbors;
    }

    public void resetDuplicatedWalls() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                Cell currentCell = getCell(i, j);
                List<Cell> cellNeighbors = getCellNeighbors(i, j);

                for (Cell cell : cellNeighbors) {
                    if (cell.type == NeighborType.North) {
                        if (cell.cellWalls.south == true && currentCell.cellWalls.north == true) {
                            currentCell.cellWalls.north = false;
                        }
                    }
                    if (cell.type == NeighborType.South) {
                        if (cell.cellWalls.north == true && currentCell.cellWalls.south == true) {
                            currentCell.cellWalls.south = false;
                        }
                    }
                    if (cell.type == NeighborType.East) {
                        if (cell.cellWalls.west == true && currentCell.cellWalls.east == true) {
                            currentCell.cellWalls.east = false;
                        }
                    }
                    if (cell.type == NeighborType.West) {
                        if (cell.cellWalls.east == true && currentCell.cellWalls.west == true) {
                            currentCell.cellWalls.west = false;
                        }
                    }
                }
            }
        }
    }

    private void printCells(int x, int y) {

        for (int j = y - 1; j >= 0; j--) {

            for (int k = 0; k < x; k++) {
                Cell cell = getCell(k, j);
                if (cell.cellWalls.east && cell.cellWalls.west && cell.cellWalls.north) {
                    System.out.print("|⎺⎺⎺|");
                } else if (cell.cellWalls.east && cell.cellWalls.west && cell.cellWalls.south) {
                    System.out.print("|___|");
                } else if (cell.cellWalls.west && cell.cellWalls.north && cell.cellWalls.south) {
                    System.out.print("|⼆⼆⼆");
                } else if (cell.cellWalls.east && cell.cellWalls.north && cell.cellWalls.south) {
                    System.out.print("⼆⼆⼆|");
                } else if (cell.cellWalls.east && cell.cellWalls.south) {
                    System.out.print("___|");
                } else if (cell.cellWalls.west && cell.cellWalls.south) {
                    System.out.print("|___");
                } else if (cell.cellWalls.east && cell.cellWalls.north) {
                    System.out.print("⎺⎺⎺|");
                } else if (cell.cellWalls.west && cell.cellWalls.north) {
                    System.out.print("|⎺⎺⎺");
                } else if (cell.cellWalls.west && cell.cellWalls.east) {
                    System.out.print("|   |");
                } else if (cell.cellWalls.south && cell.cellWalls.north) {
                    System.out.print("⼆⼆⼆");
                } else if (cell.cellWalls.south) {
                    System.out.print("___");
                } else if (cell.cellWalls.east) {
                    System.out.print("   |");
                } else if (cell.cellWalls.west) {
                    System.out.print("|  ");
                } else if (cell.cellWalls.north) {
                    System.out.print("⎺⎺⎺");
                }
            }
            System.out.println(" ");
        }

    }

}
