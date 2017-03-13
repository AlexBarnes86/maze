package toastedbits.maze;

public enum Direction {
	NORTH(1), EAST(2), SOUTH(4), WEST(8);

	private int bit;

	private Direction(int bit) {
		this.bit = bit;
	}

	public static Direction random() {
		switch((int)(Math.random()*4)) {
		case 0:	return NORTH;
		case 1: return EAST;
		case 2: return SOUTH;
		case 3: return WEST;
		default: return null;
		}
	}

	public Direction opposite() {
		switch(this) {
		case NORTH: return SOUTH;
		case EAST: return WEST;
		case SOUTH: return NORTH;
		case WEST: return EAST;
		default: return null;
		}
	}

	public int bit() {
		return this.bit;
	}
}
