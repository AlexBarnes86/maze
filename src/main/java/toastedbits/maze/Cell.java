package toastedbits.maze;

public class Cell {
	private int walls = Direction.NORTH.bit() | Direction.EAST.bit() |Direction.SOUTH.bit() | Direction.WEST.bit();
	private boolean visited;
	private int x;
	private int y;

	public Cell() {}

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Cell randomCell(int x, int y) {
		return new Cell((int)(Math.random()*x), (int)(Math.random()*y));
	}

	public void addWall(Direction dir) {
		this.walls |= dir.bit();
	}

	public void removeWall(Direction dir) {
		this.walls &= ~dir.bit();
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean hasNorthWall() {
		return (this.walls & Direction.NORTH.bit()) != 0;
	}
	public boolean hasEastWall() {
		return (this.walls & Direction.EAST.bit()) != 0;
	}
	public boolean hasSouthWall() {
		return (this.walls & Direction.SOUTH.bit()) != 0;
	}
	public boolean hasWestWall() {
		return (this.walls & Direction.WEST.bit()) != 0;
	}
}
