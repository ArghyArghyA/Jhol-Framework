package scripts.transaction;

import java.util.HashMap;

import managers.DriverUtil;
import managers.Reporter;
import scripts.Search;
import scripts.Windows;

/**
 * This class contains all the end to end transactions
 * @author Alpha Romeo
 *
 */
public class Transactions {
	
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
//			x = c.login() && c.SelectEnvironment() && n.fillClientDetails() && n.fillHouseholdInformation()
//						&& n.Rate();
//			if (x) {
//				x = n.addLoss() && n.fillCoverage() && n.fillAdditionalInformation()
//						&& n.addMortgagee() && n.setUpAccount() && n.processPayment();
//				x = x && (n.submit() ? c.completeAgentDecision() : n.saveAndExit());
//			}
//		} catch (Exception e) {
//		}
//		report.endTest();
//	}
	/*
	 * demo on transaction implementation above DO NOT Uncomment/Do NOT Change
	 ***********************************************************************************************************************/

	public static boolean Google(HashMap<String, String> dictionary) {
		Search s = new Search(report);
		return s.search() && s.clickFirstResult();// && w.saveSearchResult();
	}
	
	public static boolean windows(HashMap<String, String> dictionary)
	{
		Windows w = new Windows(report);
		return w.notepad();
	}

}
