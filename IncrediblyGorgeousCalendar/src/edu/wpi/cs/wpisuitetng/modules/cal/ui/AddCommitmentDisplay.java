/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import javax.swing.Box.Filler;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class AddCommitmentDisplay extends JPanel
{
	private JTextField nameTextField;
	private JTextField participantsTextField;
	private int tabid;
	private UUID existingID;//the old ID of the commitment, might be unused if it is a new commitment
	private boolean editingCommitment;
	private DatePicker commitTime1;
	private JLabel dateErrorLabel;
	private JLabel nameErrorLabel;
	public AddCommitmentDisplay()
	{
		editingCommitment=false;
		init(new Commitment());
	}
	public AddCommitmentDisplay(Commitment oldCommitment)
	{
		editingCommitment=true;
		existingID=oldCommitment.getCommitmentID();
		init(oldCommitment);
	}
	private void init(Commitment oldCommitment)
	{

		//TODO: Clean this whole thing up!!
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel NameLabelPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) NameLabelPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		add(NameLabelPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setVerticalAlignment(SwingConstants.BOTTOM);
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		NameLabelPanel.add(lblName);
		JPanel NamePane = new JPanel();
		FlowLayout fl_NamePane = (FlowLayout) NamePane.getLayout();
		fl_NamePane.setAlignment(FlowLayout.LEFT);
		
		add(NamePane);
		

		nameErrorLabel = new JLabel();

		nameErrorLabel.setForeground(Color.RED);

		NameLabelPanel.add(nameErrorLabel);
		nameTextField = new JTextField();
		NamePane.add(nameTextField);
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		nameTextField.setColumns(25);
		if (editingCommitment)
			nameTextField.setText(oldCommitment.getName());
		
		
		JPanel DateLabelPane = new JPanel();
		DateLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblDateTime = new JLabel("Date:");
		lblDateTime.setVerticalAlignment(SwingConstants.BOTTOM);
		lblDateTime.setHorizontalAlignment(SwingConstants.LEFT);
		DateLabelPane.add(lblDateTime);
		
		JPanel CommitDatePickerPanel = new JPanel();
		FlowLayout flowLayout_51 = (FlowLayout) CommitDatePickerPanel.getLayout();
		flowLayout_51.setAlignment(FlowLayout.LEFT);
		add(CommitDatePickerPanel);

		//final CommitmentDatePicker commitTime1 = new CommitmentDatePicker(true, null);
		commitTime1 = new DatePicker(false, null);
		if (editingCommitment)
			commitTime1.display(oldCommitment.getDate());
		
		CommitDatePickerPanel.add(lblDateTime);
		CommitDatePickerPanel.add(commitTime1);
		
		JPanel ParticipantsLabelPane = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) ParticipantsLabelPane.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(ParticipantsLabelPane);
		
		JLabel lblParticipants = new JLabel("Participants:");
		lblParticipants.setVerticalAlignment(SwingConstants.BOTTOM);
		ParticipantsLabelPane.add(lblParticipants);
		
		JPanel ParticipantsPanel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) ParticipantsPanel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		add(ParticipantsPanel);
		
		participantsTextField = new JTextField();
		ParticipantsPanel.add(participantsTextField);
		participantsTextField.setColumns(30);
		if (editingCommitment)
			participantsTextField.setText(oldCommitment.getParticipants());
		
		
		JPanel DescriptionLabelPane = new JPanel();
		FlowLayout fl_DescriptionLabelPane = (FlowLayout) DescriptionLabelPane.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		add(DescriptionLabelPane);
		
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setVerticalAlignment(SwingConstants.BOTTOM);
		DescriptionLabelPane.add(lblDescription);
		lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblDescription.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel DescriptionPanel = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) DescriptionPanel.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		DescriptionPanel.setMinimumSize(new Dimension(100, 100));
		add(DescriptionPanel);
        Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767), new java.awt.Dimension(0, 32767));
        
        add(filler1);
		
        final JTextArea descriptionTextArea = new JTextArea(5,35);
		descriptionTextArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		if (editingCommitment)
			descriptionTextArea.setText(oldCommitment.getDescription());
		
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
		descriptionScrollPane.setBorder(null);
		DescriptionPanel.add(descriptionScrollPane);
		
		JPanel SubmitPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) SubmitPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(SubmitPanel);
		
		final JLabel errorText = new JLabel();
		errorText.setForeground(Color.RED);
		errorText.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 13));
		
		final JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					commitTime1.getDate();
					errorText.setVisible(true);
					
					if (nameTextField.getText() == null || nameTextField.getText().trim().length() == 0)
					{
						errorText.setText("* Please enter a commitment title");
					}
					else
					{
						errorText.setVisible(false);
						Commitment e = new Commitment();
						e.setName(nameTextField.getText().trim());
						e.setDescription(descriptionTextArea.getText());
						e.setDate(commitTime1.getDate());
						if (editingCommitment)
							e.setCommitmentID(existingID);
						
						if (editingCommitment)
							MainPanel.getInstance().updateCommitment(e);
						else
							MainPanel.getInstance().addCommitment(e);
						saveButton.setEnabled(false);
						saveButton.setText("Saved!");
						MainPanel.getInstance().closeTab(tabid);
						MainPanel.getInstance().refreshView();
					}
				}
				catch (IllegalArgumentException exception)
				{
					errorText.setText("* Invalid Date");
					errorText.setVisible(true);
				}
			}
		});
		saveButton.setHorizontalAlignment(SwingConstants.LEFT);
		SubmitPanel.add(saveButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				MainPanel.getInstance().closeTab(tabid);
			}
		});
		SubmitPanel.add(btnCancel);
		SubmitPanel.add(errorText);

		dateErrorLabel = new JLabel();
		dateErrorLabel.setForeground(Color.RED);
		CommitDatePickerPanel.add(dateErrorLabel);
		
		nameTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(nameTextField.getText(), nameErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(nameTextField.getText(), nameErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//Not triggered by plaintext fields
			}
		});
		commitTime1.addChangeListener(new DatePickerListener() {
			
			@Override
			public void datePickerUpdate(DateTime mDateTime) {
				dateErrorLabel.setVisible(!validateDate(commitTime1.getDate(), dateErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
		});
		validateDate(commitTime1.getDate(), dateErrorLabel);
		validateText(nameTextField.getText(), nameErrorLabel);
		
	}
	public boolean isSaveable()
	{
		return validateText(nameTextField.getText(), nameErrorLabel) && 
				validateDate(commitTime1.getDate(), dateErrorLabel);
	}
	
	/**
	 * 
	 * @param dueDate first DatePicker to validate and compare
	 * @param mEndTime second DatePicker to validate and compare
	 * @param mErrorLabel text field to be updated with any error message
	 * @return true if all validation checks pass, else returns false
	 */
	private boolean validateDate(DateTime dueDate, JLabel mErrorLabel)
	{
		if(dueDate == null)//Make sure that a date has been selected
		{
			mErrorLabel.setText("* Commitment must have a due date");
			return false;
		}
		
		//no errors found
		return true;
		
		
	}
	/**
	 * 
	 * @param mText text to be validated
	 * @param mErrorLabel JLabel to display resulting error message
	 * @return true if all pass, else return true
	 */
	private boolean validateText(String mText, JLabel mErrorLabel)
	{
		if(mText==null || mText.trim().length()==0)
		{
			mErrorLabel.setText("* Required Field");
			return false;
		
		}
		return true;
	}
	
	public void setTabId(int id)
	{
		tabid = id;
	}
}
