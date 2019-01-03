
public class Cell {

    public boolean visited;
    public Wall cellWalls;
    public NeighborType type;
    public int x;
    public int y;

    public Cell() {
        cellWalls = new Wall();
    }

}