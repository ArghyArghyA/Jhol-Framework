package runners;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import dataProviders.Configurations;

import java.lang.reflect.*;

import managers.DataManager;
import managers.Reporter;
import scripts.*;

public class Jhol {
	static Reporter report = Reporter.getInstance();

	/*
	 * demo on transaction implementation below DO NOT Uncomment/Do NOT Change
	 ***********************************************************************************************************************/
//	public static void NB(HashMap<String, String> dictionary) {
//		boolean x = true;
//		report.reportTest(dictionary);
//		Commons c = new Commons(report);
//		NBNH n = new NBNH(report);
//		try {
//			x = c.login() && c.SelectEnvironment() && c.goToPLM() && n.goToECMS() && n.continueQuote()
//					&& n.fillHouseholdInformation() && c.open360() && c.complete360() && n.fillDwelling()
//					&& n.fillProtectionClass() && n.Rate();
//			if (x) {
//				x = n.addLoss() && n.fillBasicCoverage() && n.fillOptionalCoverage() && n.fillDetailedCoverage()
//						&& n.addCEA() && n.proceedToBuy() && n.fillPaperlessOption()
//						&& n.fillAdditionalHouseholdInformation() && n.fillAdditionalPolicyInformation()
//						&& n.addMortgagee() && n.Verify() && n.setUpAccount() && n.processPayment();
//				x = x && n.submit() ? c.completeAgentDecision() : n.saveAndExit();
//			}
//		} catch (Exception e) {
//		}
//		report.endTest();
//	}
	/*
	 * demo on transaction implementation above DO NOT Uncomment/Do NOT Change
	 ***********************************************************************************************************************/

	public static void Google(HashMap<String, String> dictionary) {
		report.reportTest(dictionary);
		Search s = new Search(report);
		if (s.search()) s.clickFirstResult();
		report.endTest();
	}
	
	public static void windows(HashMap<String, String> dictionary)
	{
		report.reportTest(dictionary);
		Windows w = new Windows(report);
		w.notepad();
		report.endTest();
	}

	public static void main(String[] args) throws InvalidFormatException, IOException {
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
//				// TODO Auto-generated method stub
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
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				}
//				
//			}
//		});
//		content.add(pause, BorderLayout.SOUTH);

		frame.setSize(300, 45);
		frame.setLocationRelativeTo(null);

		List<HashMap<String, String>> Data = DataManager.read();
		int numberOfTestcases = Data.size();

		int counter = 0;
		progressBar.setMaximum(numberOfTestcases);
		progressBar.setBorder(BorderFactory.createTitledBorder("Processed " + counter + "/" + numberOfTestcases));
		frame.setVisible(true);

		for (HashMap<String, String> dictionary : Data) {

			try {
				Class<?> cls = Class.forName(Configurations.ClassContainingTransactionScripts);
				Object object = cls.newInstance();
				Method method = cls.getMethod(dictionary.get("TestCaseDescription"),
						new HashMap<String, String>().getClass());
				method.invoke(object, dictionary);
			} catch (NoSuchMethodException e) {
				System.out.println("Transaction not implemented yet");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
			progressBar.setBorder(BorderFactory.createTitledBorder("Processed " + counter + "/" + numberOfTestcases));
			progressBar.setValue(counter);
		}
	}

}
