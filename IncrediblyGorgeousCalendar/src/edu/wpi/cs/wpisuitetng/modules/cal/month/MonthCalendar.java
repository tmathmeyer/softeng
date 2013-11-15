package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.joda.time.*;

import com.lowagie.text.Font;

import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

/**
 *
 * @author patrick
 */
public class MonthCalendar extends JPanel
{
	private JPanel inside = new JPanel(), top = new JPanel(), mainCalendarView = new JPanel(), navigationPanel = new JPanel(), navigationButtonPanel = new JPanel();
	private JLabel monthLabel = new JLabel();
	private DateTime time;
	private JButton nextButton = new JButton(">"), previousButton = new JButton("<"), todayButton = new JButton("Today");
	private MainPanel mainPanel;

	public MonthCalendar(DateTime on, MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.setLayout(new BorderLayout());
		
		
		navigationPanel.setLayout(new BorderLayout());
		
		this.add(navigationPanel, BorderLayout.NORTH);
		
		
		monthLabel.setHorizontalAlignment(JLabel.CENTER);
		
		monthLabel.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));
		
		navigationButtonPanel.setLayout(new BorderLayout());
		navigationButtonPanel.add(nextButton, BorderLayout.EAST);
		navigationButtonPanel.add(todayButton, BorderLayout.CENTER);
		navigationButtonPanel.add(previousButton, BorderLayout.WEST);
		
		//unnecessary if arrows are used because both are same size
		//nextButton.setPreferredSize(previousButton.getPreferredSize());
		
		navigationPanel.add(monthLabel, BorderLayout.CENTER);
		navigationPanel.add(navigationButtonPanel, BorderLayout.WEST);
		
		nextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});
		previousButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				previous();
				
			}
		});
		todayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				display(DateTime.now());
			}
		});
		time = on;

		// layout code
		mainCalendarView.setBackground(UIManager.getDefaults().getColor("Table.background"));
		mainCalendarView.setLayout(new BorderLayout());
		top.setLayout(new GridLayout(1, 7));
		
		mainCalendarView.add(top, BorderLayout.NORTH);
		mainCalendarView.add(inside, BorderLayout.CENTER);
		
		this.add(mainCalendarView, BorderLayout.CENTER);
		// end layout code

		MutableDateTime fom = new MutableDateTime(on);
		fom.setDayOfMonth(1);
		fom.setMillisOfDay(0);
		int first = (fom.getDayOfWeek() % 7);
		fom.addDays(-first);

		// generate days on top
		for (int i = 0; i < 7; i++)
		{
			JLabel jl = new JLabel(fom.dayOfWeek().getAsText());
			jl.setHorizontalAlignment(SwingConstants.CENTER);
			fom.addDays(1);
			top.add(jl);
		}
		generateDays(fom);
	}
	
	/**
	 * 
	 * @param events
	 */
	public void displayEvents(List<Event> events)
	{
		
	}

	boolean isToday(ReadableDateTime fom)
	{
		DateTime now = DateTime.now();
		return fom.getYear() == now.getYear() && fom.getDayOfYear() == now.getDayOfYear();
	}
	
	public void display(DateTime newtime)
	{
		if (time.getMonthOfYear() == newtime.getMonthOfYear() && time.getYear() == newtime.getYear())
			return; // nothing changed. don't update
		time = newtime;
		generateDays(new MutableDateTime(time));
	}

	public void next()
	{
		MutableDateTime fom = new MutableDateTime(time);
		fom.addMonths(1);
		time = fom.toDateTime();
		generateDays(fom);
	}

	public void previous()
	{
		MutableDateTime fom = new MutableDateTime(time);
		fom.addMonths(-1);
		time = fom.toDateTime();
		generateDays(fom);
	}

	/**
	 * Fill calendar with month in referenceDay
	 * @param referenceDay what month should we display
	 */
	protected void generateDays(MutableDateTime referenceDay)
	{
		// reset to the first of the month at midnight, then find Sunday
		referenceDay.setDayOfMonth(1);
		referenceDay.setMillisOfDay(0);
		int first = (referenceDay.getDayOfWeek() % 7);
		int daysInView = first + referenceDay.dayOfMonth().getMaximumValue();
		int weeks = (int)Math.ceil(daysInView / 7.0);
		inside.setLayout(new java.awt.GridLayout(weeks, 7));
		referenceDay.addDays(-first);

		// remove all old days
		inside.removeAll();

		// generate days, weeks*7 covers all possible months, so we just loop through and add each day
		for (int i = 0; i < (weeks*7); i++)
		{
			MonthDay md = new MonthDay(referenceDay.toDateTime(), getMarker(referenceDay));
			inside.add(md);
			md.reBorder(i < 7, (i % 7 ) == 0, i >= 5 * 7);
			referenceDay.addDays(1); // go to next day
		}
		
		monthLabel.setText(this.getTime().toString(Months.monthLblFormat));

		// notify mini-calendar to change
		mainPanel.miniMove(time);
		
		// repaint when changed
		inside.revalidate();
	}

	/**
	 * Gets the DayStyle of given date
	 * @param date
	 * @return
	 */
	protected DayStyle getMarker(ReadableDateTime date)
	{
		if (date.getMonthOfYear() == time.getMonthOfYear())
		{
			return (isToday(date) ? DayStyle.Today: DayStyle.Normal);
		}
		else
			return DayStyle.OutOfMonth;
	}

	public DateTime getTime()
	{
		return time;
	}
}