
public class Cell {

    public boolean visited;
    public Wall cellWalls;
    public NeighborType type;

    public Cell() {
        cellWalls = new Wall();
    }

}