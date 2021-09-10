import javax.swing.JFrame;

public class Runner extends JFrame
{
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;

	public Runner()
	{
		super("Frogger");
		setSize(WIDTH,HEIGHT);
		getContentPane().add( new Display(WIDTH, HEIGHT) );
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
