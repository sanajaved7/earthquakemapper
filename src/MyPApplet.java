import processing.core.*;

public class MyPApplet extends PApplet
{
	private String URL = "https://upload.wikimedia.org/wikipedia/commons/5/58/Sunset_2007-1.jpg";
	
	private PImage backgroundImg;
	
	public void setup()
	{
		size(200, 200);
		backgroundImg = loadImage(URL, "jpg");
	}
	
	public void draw()
	{
		image(backgroundImg, 0, 0);
	}
}