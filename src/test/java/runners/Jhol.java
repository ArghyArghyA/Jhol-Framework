package runners;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.lang.reflect.*;

import managers.DataManager;
import managers.Reporter;
import scripts.*;
import settings.Configurations;

public class Jhol {
	static Reporter report = Reporter.getInstance();

	public static void main(String[] args) throws InvalidFormatException, IOException, InterruptedException {
		JFrame frame = new JFrame("Progress");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		Container content = frame.getContentPane();

		// progress bar
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		content.add(progressBar, BorderLayout.NORTH);

		// button
//		JButton pause = new JButton("Pause");
//		pause.setBounds(new Rectangle(50, 20));
//		pause.addActionListener(new ActionListener()
//		{
//			
//			public void actionPerformed(ActionEvent e)
//			{
//				if (Thread.currentThread().getState() == Thread.State.WAITING)
//				{
//					Thread.currentThread().resume();
//				} else
//				{
//					try
//					{
//						Thread.currentThread().wait();
//					} catch (InterruptedException e1)
//					{
//						e1.printStackTrace();
//					}
//				}
//				
//			}
//		});
//		content.add(pause, BorderLayout.SOUTH);

		frame.setSize(300, 45);
		frame.setLocationRelativeTo(null);
		
		ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(args));
		List<HashMap<String, String>> Data = DataManager.read(arguments);
		int numberOfTestcases = Data.size();

		int counter = 0;
		progressBar.setMaximum(numberOfTestcases);
		progressBar.setBorder(BorderFactory.createTitledBorder("Processed " + counter + "/" + numberOfTestcases));
		frame.setVisible(true);

		for (HashMap<String, String> dictionary : Data) {

			try {
				report.reportTest(dictionary);
				Class<?> cls = Class.forName(Configurations.ClassContainingTransactionScripts);
				Object object = cls.newInstance();
				Method method = cls.getMethod(dictionary.get("TestCaseDescription"),
						new HashMap<String, String>().getClass());
				method.invoke(object, dictionary);
				report.endTest();
			} catch (NoSuchMethodException e) {
				System.out.println("Transaction not implemented yet");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
			progressBar.setBorder(BorderFactory.createTitledBorder("Processed " + counter + "/" + numberOfTestcases));
			progressBar.setValue(counter);
			Thread.sleep(5000);
		}
		frame.dispose();
	}
}
