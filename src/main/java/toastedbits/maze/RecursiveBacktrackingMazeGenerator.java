package toastedbits.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RecursiveBacktrackingMazeGenerator implements MazeGenerator {
	private MazeSettings ms;
	private Cell [][] maze;
	private MazeListener listener;
	private Cell currentCell;

	// Monospace unicode character to fill the entire cursor with black
	private static final char FILL_CURSOR = '\u2588';
	private static final char BLANK_CURSOR = ' ';

	private List<Direction> randomDirections() {
		List<Direction> dirs = new ArrayList<>();
		dirs.add(Direction.NORTH);
		dirs.add(Direction.EAST);
		dirs.add(Direction.SOUTH);
		dirs.add(Direction.WEST);
		for(int i = 0; i < dirs.size(); ++i) {
			int pos = (int)(Math.random()*dirs.size());
			if(pos != i) {
				Direction temp = dirs.get(i);
				dirs.set(i, dirs.get(pos));
				dirs.set(pos, temp);
			}
		}
		return dirs;
	}

	private Cell getCell(int x, int y, Direction dir) {
		switch(dir) {
		case NORTH:
			if(y > 0) {
				return maze[y-1][x];
			}
			break;
		case EAST:
			if(x < ms.getWidth()-1) {
				return maze[y][x+1];
			}
			break;
		case SOUTH:
			if(y < ms.getHeight()-1) {
				return maze[y+1][x];
			}
			break;
		case WEST:
			if(x > 0) {
				return maze[y][x-1];
			}
			break;
		}

		return null;
	}

	private Cell[][] buildMazeCells(int width, int height) {
		Cell [][] cells = new Cell[width][height];
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				cells[y][x] = new Cell(x, y);
			}
		}

		return cells;
	}

	public RecursiveBacktrackingMazeGenerator(MazeSettings ms) {
		this.ms = ms;
	}

	@Override
	public void setListener(MazeListener listener) {
		this.listener = listener;
	}

	@Override
	public void build() {
		this.maze = buildMazeCells(ms.getWidth(), ms.getHeight());

		if(listener != null) {
			listener.start();
		}

		Stack<Cell> stack = new Stack<>();
		stack.push(maze[(int)(Math.random()*ms.getHeight())][(int)(Math.random()*ms.getWidth())]);

		while(!stack.isEmpty()) {
			Cell cell = currentCell = stack.peek();
			cell.setVisited(true);

			if(listener != null) {
				listener.buildStep(this);
			}

			int x = cell.getX();
			int y = cell.getY();
			boolean pop = true;
			for(Direction dir : randomDirections()) {
				Cell other = getCell(x, y, dir);
				if(other != null && !other.isVisited()) {
					cell.removeWall(dir);
					other.removeWall(dir.opposite());
					stack.push(other);
					pop = false;
					break;
				}
			}
			if(pop && !stack.isEmpty()) {
				stack.pop();
			}
		}

		if(listener != null) {
			listener.finish();
		}
	}

	@Override
	public MazeSettings getMazeSettings() {
		return this.ms;
	}

	@Override
	public Cell[][] getCells() {
		return this.maze;
	}

	@Override
	public Cell getCurrentCell() {
		return this.currentCell;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		//Each cell gets padding on the East and South edges to accomodate walls - this is what the *2 multiplier is for
		//To accomodate the north and west border of the entire maze we add 1 to everything
		//Each cell is considered as empty and we then knock down south or east walls as we iterate from top down, left to right.
		//mch = maze character
		char [][] mch = new char [ms.getHeight()*2+1][ms.getWidth()*2+1];
		for(int y = 0; y < mch.length; ++y) {
			for(int x = 0; x < mch[y].length; ++x) {
				mch[y][x] = FILL_CURSOR;
			}
		}

		for(int y = 0; y < ms.getHeight(); ++y) {
			for(int x = 0; x < ms.getWidth(); ++x) {
				mch[y*2+1][x*2+1] = BLANK_CURSOR;
				if(!maze[y][x].hasSouthWall()) {
					mch[y*2+2][x*2+1] = BLANK_CURSOR;
				}
				if(!maze[y][x].hasEastWall()) {
					mch[y*2+1][x*2+2] = BLANK_CURSOR;
				}
			}
		}

		for(int y = 0; y < mch.length; ++y) {
			for(int x = 0; x < mch[y].length; ++x) {
				sb.append(mch[y][x]);
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		MazeSettings ms = new MazeSettings();
		ms.setWidth(10);
		ms.setHeight(10);

		MazeVideoEncoder encoder = new MazeVideoEncoder("output");

		RecursiveBacktrackingMazeGenerator rb = new RecursiveBacktrackingMazeGenerator(ms);
		rb.setListener(encoder);
		rb.build();

		System.out.println(rb);
	}
}
