import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class MyClock extends Thread 
{
	private JLabel txt;
	private boolean stop;
	SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm:ss a");
	
	public MyClock(JLabel txt) 
	{
		this.txt = txt;
		this.start();
	}
	public void run()
	{
		while(!stop)
		{
			txt.setText(formatDate.format(new Date()).toString());
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
