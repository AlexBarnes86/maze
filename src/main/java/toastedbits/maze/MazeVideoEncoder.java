package toastedbits.maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.awt.SequenceEncoder;

public class MazeVideoEncoder implements MazeListener {
	private static final int SCALE = 10;
	private SequenceEncoder enc;
	private int seq = 0;
	private boolean images = false;
	private boolean video = true;

	public MazeVideoEncoder(String outputName) throws IOException {
		if(video) {
			this.enc = new SequenceEncoder(new File(outputName + ".mp4"));
		}
	}

	public void setOutputVideo(boolean video) {
		this.video = video;
	}

	public void setOutputImages(boolean images) {
		this.images = images;
	}

	public void start() {
		seq = 0;
	}

	private void drawBox(Graphics g, int x, int y) {
		g.fillRect(x*SCALE, y*SCALE, SCALE, SCALE);
	}

	//Each cell gets padding on the East and South edges to accomodate walls - this is what the *2 multiplier is for
	//To accomodate the north and west border of the entire maze we add 1 to everything
	//Each cell is considered as empty and we then knock down south or east walls as necessary
	public void buildStep(MazeGenerator maze) {
		MazeSettings ms = maze.getMazeSettings();
		Cell[][] cells = maze.getCells();

		int imageWidth = (ms.getWidth()*2+1)*SCALE;
		int imageHeight = (ms.getHeight()*2+1)*SCALE;
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());

		g.setColor(Color.WHITE);
		for(int y = 0; y < ms.getHeight(); ++y) {
			for(int x = 0; x < ms.getWidth(); ++x) {
				int boxX = x*2+1;
				int boxY = y*2+1;

				drawBox(g, boxX, boxY);
				if(!cells[y][x].hasSouthWall()) {
					drawBox(g, boxX, boxY+1);
				}
				if(!cells[y][x].hasEastWall()) {
					drawBox(g, boxX+1, boxY);
				}
			}
		}

		g.setColor(Color.GREEN);
		Cell cell = maze.getCurrentCell();
		drawBox(g, cell.getX()*2+1, cell.getY()*2+1);

		try {
			image.flush();
			if(this.images) {
				ImageIO.write(image, "png", new File("output" + seq + ".png"));
			}
			if(this.video) {
				enc.encodeImage(image);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		seq++;
	}

	public void finish() {
		try {
			if(this.video) {
				enc.finish();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
