package toastedbits.maze;

public interface MazeListener {
	void start();
	void buildStep(MazeGenerator maze);
	void finish();
}
