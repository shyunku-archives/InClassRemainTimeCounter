package main;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Global {
	public static final int DEFAULT_TILE_SIZE = 18;
	
	public static final Dimension FRAME_DIMEN = new Dimension(440, 140);
	public static final Rectangle SETTING_AREA = new Rectangle(0, 0, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
	public static final Rectangle EVERYTIME_AREA = new Rectangle(DEFAULT_TILE_SIZE, 0, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
	public static final Rectangle EXIT_AREA = new Rectangle(FRAME_DIMEN.width - DEFAULT_TILE_SIZE, 0, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
	public static final Rectangle PIN_AREA = new Rectangle(FRAME_DIMEN.width - DEFAULT_TILE_SIZE*2, 0, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
	
	public static final Rectangle DRAGGABLE_AREA = new Rectangle(DEFAULT_TILE_SIZE*2-2, 0, EXIT_AREA.x - DEFAULT_TILE_SIZE*2, DEFAULT_TILE_SIZE);
	public static final Rectangle ALPHA_CHOOSER_AREA = new Rectangle(DEFAULT_TILE_SIZE + 30, 2, 120, 15);
	
	public static final Rectangle FRAME_BOUNDARY = new Rectangle(1920 - FRAME_DIMEN.width, 1080-FRAME_DIMEN.height-30,FRAME_DIMEN.width,FRAME_DIMEN.height);
	
	public static Font CUSTOM_FONT = null;
	public static BufferedImage SettingIcon = null;
	public static BufferedImage UnpinnedIcon = null;
	public static BufferedImage PinnedIcon = null;
	public static BufferedImage ExitIcon = null;
	
	public static BufferedImage IncreaseIcon = null;
	public static BufferedImage DecreaseIcon = null;
	
	public static BufferedImage EverytimeIcon = null;
	public static BufferedImage HighResEverytimeIcon = null;
}
