package toastedbits.maze;

public interface MazeGenerator {
	void setListener(MazeListener listener);
	void build();
	MazeSettings getMazeSettings();
	Cell[][] getCells();
	Cell getCurrentCell();
}
