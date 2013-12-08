package edu.wpi.cs.wpisuitetng.modules.cal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthCalendarTest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventEntityManagerTest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentEntityManagerTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthDayTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthItemTest;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.MonthsTest;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.GoToPanelTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.EventUIValidationTest;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarNavigationModuleTest;
import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarTest;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;


/**
 * Test suite for running all test classes.
 *
 */


@RunWith(Suite.class)
@SuiteClasses({CalendarNavigationModuleTest.class, EventEntityManagerTest.class, GoToPanelTest.class, EventUIValidationTest.class, CommitmentEntityManagerTest.class, MonthItemTest.class, MonthCalendarTest.class, YearCalendarTest.class, MonthDayTest.class, MonthsTest.class})
public class TestSuite
{
	@BeforeClass public static void setUpClass() {
		MockNetwork mn = new MockNetwork();
		mn.addUser("testing11");
		mn.addUser("testing12");
		mn.addUser("testing21");
		mn.addUser("testing22");
		mn.addProject("project1");
		mn.addProject("project2");
		mn.addSession("testing11", "project1", "default");
		// TODO: add more sessions
		mn.loginSession("default");
		Network.initNetwork(mn);
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration("http://wpisuitetng"));
		MainPanel.getInstance().finishInit();
    }
    @AfterClass public static void tearDownClass() {
        // Common cleanup for all tests
    }
}
