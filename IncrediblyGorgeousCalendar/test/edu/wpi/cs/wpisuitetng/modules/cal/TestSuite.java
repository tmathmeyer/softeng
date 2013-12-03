package edu.wpi.cs.wpisuitetng.modules.cal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthCalendarTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthDayTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthItemTest;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.MonthsTest;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarNavigationModuleTest;
import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarHolderTest;


/**
 * Test suite for running all test classes.
 *
 */


@RunWith(Suite.class)
@SuiteClasses({CalendarNavigationModuleTest.class, MonthItemTest.class, MonthCalendarTest.class, YearCalendarHolderTest.class, MonthDayTest.class, MonthsTest.class})

public class TestSuite {

}
