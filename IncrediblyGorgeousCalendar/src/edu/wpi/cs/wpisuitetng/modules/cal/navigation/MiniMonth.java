package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthDay;

public class MiniMonth extends JPanel
{
	/**
	 * space for holding all the days
	 */

	public MiniMonth(DateTime time, final MiniCalendarHostIface mc) {
		this.setLayout(new GridLayout(7, 7));
		MutableDateTime prevMonth = new MutableDateTime(time);
		prevMonth.setDayOfMonth(1);
		prevMonth.addMonths(-1);
		String[] dayLabel = {"S", "M", "T", "W", "R", "F", "S"};

		MouseListener monthChanger = new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent me) {}
			@Override
			public void mouseEntered(MouseEvent me) {}
			@Override
			public void mouseExited(MouseEvent me) {}
			@Override
			public void mousePressed(MouseEvent me) {}
			@Override
			public void mouseReleased(MouseEvent me) {
				DayLabel d = (DayLabel)(me.getSource());
				if (!(d instanceof DescriptiveDayLabel))
				{
					mc.display(d.getMonth());
				}
			}
		};

		MutableDateTime referenceDay = new MutableDateTime(time);
		// reset to the first of the month at midnight, then find Sunday
		referenceDay.setDayOfMonth(1);
		referenceDay.setMillisOfDay(0);
		int first = referenceDay.getDayOfWeek();
		referenceDay.addDays(-first);
		boolean flipFlop = false;
		
		// add day labels
		for (int i = 0; i < 7; i++)
		{
			DayLabel day = new DescriptiveDayLabel(dayLabel[i], time);
			day.borderize((i % 7) == 0, i >= 5*7, (i % 7) == 6);
			add(day);
			day.addMouseListener(monthChanger);
		}

		// generate days, 6*7 covers all possible months, so we just loop
		// through and add each day
		for (int i = 0; i < (6 * 7); i++)
		{
			DayLabel day;
			if (MainPanel.getInstance().getView() == ViewSize.Month)
			{
				if (referenceDay.getDayOfMonth() == 1)
					flipFlop
					    ^= true; // flops the flip flop
			}
			else if (MainPanel.getInstance().getView() == ViewSize.Day)
				flipFlop = referenceDay.getDayOfYear() == time.getDayOfYear() && referenceDay.getYear() == time.getYear();
			
			if (flipFlop)
				day = new ActiveDayLabel(referenceDay.toDateTime());
			else
				day = new InactiveDayLabel(referenceDay.toDateTime());

			day.borderize((i % 7) == 0, i >= 5*7, (i % 7) == 6);
			add(day);
			day.addMouseListener(monthChanger);
			referenceDay.addDays(1); // go to next day
		}
	}
	
	private class DayLabel extends JLabel {
		private DateTime day;
		
		public DayLabel(DateTime time) {
			this.setForeground(Color.BLACK);
			this.setText(Integer.toString(time.getDayOfMonth()));
			this.setHorizontalAlignment(SwingConstants.CENTER);
			
			DateTime now = DateTime.now();
			if (now.getDayOfYear() == time.getDayOfYear() && now.getYear() == time.getYear())
					this.setFont(getFont().deriveFont(Font.BOLD));
			this.day = time;
		}
		
		public DateTime getMonth()
		{
			return day;
		}
		
		public void borderize(boolean left, boolean bottom, boolean right)
		{
			setBorder(javax.swing.BorderFactory.createMatteBorder(0, left?1:0, bottom?1:0, right?1:0, Colors.BORDER));
		}
	}

	private class ActiveDayLabel extends DayLabel {
		public ActiveDayLabel(DateTime time) {
			super(time);
			setForeground(Colors.TABLE_TEXT);
			setBackground(Colors.TABLE_BACKGROUND);
			setOpaque(true);
		}
	}

	private class InactiveDayLabel extends DayLabel {
		public InactiveDayLabel(DateTime time) {
			super(time);
			setBackground(Colors.TABLE_GRAY_HEADER);
			setForeground(Colors.TABLE_GRAY_TEXT);
			this.setOpaque(true);
		}
	}
	
	private class DescriptiveDayLabel extends DayLabel {
		public DescriptiveDayLabel(String text, DateTime time) {
			super(time);
			setText(text);
			this.setFont(getFont().deriveFont(Font.ITALIC));
			setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.BORDER));
		}
		@Override
		public void borderize(boolean left, boolean bottom, boolean right) {
			// don't screw with borders. we don't need them here
		}
	}

}
