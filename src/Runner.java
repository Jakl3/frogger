import javax.swing.JFrame;

public class Runner extends JFrame
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	public Runner()
	{
		super("Frogger");

		setSize(WIDTH,HEIGHT);

		getContentPane().add( new FroggerGame(5) );

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		requestFocus();
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		Runner run = new Runner();

	}
}
