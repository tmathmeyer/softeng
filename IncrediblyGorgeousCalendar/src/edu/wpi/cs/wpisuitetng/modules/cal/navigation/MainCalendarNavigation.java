package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;

public class MainCalendarNavigation extends JPanel {

	private JButton nextButton = new JButton(">");
	private JButton previousButton = new JButton("<");
	private JButton todayButton = new JButton("Today");
	private JPanel navigationButtonPanel = new JPanel();

	public MainCalendarNavigation(JComponent parent, final AbstractCalendar mAbstractCalendar) {
		
		navigationButtonPanel.setLayout(new BorderLayout());
		navigationButtonPanel.add(nextButton, BorderLayout.EAST);
		navigationButtonPanel.add(todayButton, BorderLayout.CENTER);
		navigationButtonPanel.add(previousButton, BorderLayout.WEST);
		
		nextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mAbstractCalendar.next();
			}
		});
		previousButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mAbstractCalendar.previous();
				
			}
		});
		todayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mAbstractCalendar.display(DateTime.now());
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(navigationButtonPanel, BorderLayout.WEST);
	}
}