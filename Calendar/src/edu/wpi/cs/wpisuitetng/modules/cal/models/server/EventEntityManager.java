/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;
/**
 * This is the entity manager for the Event in the
 * EventManager module.
 */
public class EventEntityManager extends CachedEntityManager<Event> {
	
	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in
	 * the ManagerLayer file.
	 * 
	 * @param db a reference to the persistent database
	 */
	public EventEntityManager(Data db) {
		super(db);
		pollPusher = PollPusher.getInstance(Event.class);
	}

	/**
	 * Saves an Event when it is received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public Event makeEntity(Session s, String content) throws WPISuiteException {
		final Event newEvent = Event.fromJson(content);
		newEvent.setOwner(s.getUser());
		newEvent.setProject(s.getProject());
		if(!db.save(newEvent, s.getProject())) {
			throw new WPISuiteException();
		}
		PollPusher.getInstance(Event.class).updated(updated(newEvent));
		return newEvent;
	}
	
	/**
	 * Retrieves events from the database in the range of String data
	 * @param s the session
	 * @param data the data (String from and String to) of the events to retrieve
	 * @return the event matching the given id * @throws NotFoundException * @throws NotFoundException * @throws NotFoundException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(Session, String) */
	@Override
	public Event[] getEntity(Session s, String data) throws NotFoundException
	{
		return getEventByUUID(s, data);
	}

	/**
	 * gets the event with the current UUID
	 * @param ses the session
	 * @param uuid the event's UUID
	 * @return an array containing just this event
	 * @throws NotFoundException
	 */
	private Event[] getEventByUUID(Session ses, String uuid) throws NotFoundException
	{
		UUID from = UUID.fromString(uuid);
		try 
		{
			return db.retrieve(Event.class, "uuid", from, ses.getProject()).toArray(new Event[0]);
		}
		catch (WPISuiteException e)
		{
			System.out.println("Tryiing to find " + uuid + " fAILED!");
			throw new NotFoundException(e.toString());
		}
		
	}
	
	/**
	 * Retrieves all events from the database
	 * @param s the session
	 * @return array of all stored events * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session)
	 */
	@Override
	public Event[] getAll(Session s) {
		Event [] allEvents = db.retrieveAll(new Event(), s.getProject()).toArray(new Event[0]);
		ArrayList<Event> eventArray = new ArrayList<>();
		for (Event e: allEvents)
		{
			if (s.getUser().equals(e.getOwner()) || e.isProjectwide())
					eventArray.add(e);
		}
		return eventArray.toArray(new Event[0]);
	}

	/**
	 * Saves a data model to the database
	 * @param s the session
	 * @param model the model to be saved
	 */
	@Override
	public void save(Session s, Event model) {
		if (model.isProjectwide())
			model.setProject(s.getProject());
		db.save(model);
		PollPusher.getInstance(Event.class).updated(updated(model));
	}
	

	/**
	 * Deletes a event from the database
	 * @param s the session
	 * @param id the id of the event to delete	
	 * @return true if the deletion was successful * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(Session, String) */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		Event toDelete = db.delete(getEntity(s, id)[0]);
		if (toDelete != null)
			PollPusher.getInstance(Event.class).updated(deleted(UUID.fromString(id)));
		return toDelete != null;
	}
	
	/**
	 * Deletes all events from the database
	 * @param s the session
	 * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Event(), s.getProject());
	}
	
	/**
	 * Returns the number of events in the database
	 * @return number of events stored * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count() */
	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Event()).size();
	}

	/**
	 * Method update.
	 * @param session Session
	 * @param content String
	 * @return Event * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String) * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String)
	 */
	@Override
	public Event update(Session session, String content) throws WPISuiteException {
		
		Event updatedEvent = Event.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save Events.
		 * We have to get the original defect from db4o, copy properties from updatedEvent,
		 * then save the original Event again.
		 */
		List<Model> oldEvents = db.retrieve(Event.class, "uuid", updatedEvent.getUuid(), session.getProject());
		if(oldEvents.size() < 1 || oldEvents.get(0) == null) {
			throw new BadRequestException("Event with ID does not exist.");
		}
				
		Event existingEvent = (Event)oldEvents.get(0);		
		
		db.delete(existingEvent);
		
		updatedEvent.setOwner(existingEvent.getOwner());
		updatedEvent.setProject(existingEvent.getProject());
		if(!db.save(updatedEvent, session.getProject())) {
			throw new WPISuiteException();
		}

		PollPusher.getInstance(Event.class).updated(updated(updatedEvent));
		
		return existingEvent;
		
	}

	/**
	 * Method advancedGet.
	 * @param arg0 Session
	 * @param arg1 String[]
	
	
	
	 * @return String * @throws NotImplementedException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(Session, String[]) * @throws NotImplementedException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(Session, String[])
	 */
	@Override
	public String advancedGet(Session s, String[] args) throws NotFoundException
	{
		// shift cal/events off
		args = Arrays.copyOfRange(args, 2, args.length);
		switch (args[0])
		{
			case "filter-event-by-uuid":
				return json((Object[])getEventByUUID(s, args[1]));
			case "poll":
				return getFromPoll(s);
			default:
				System.out.println(args[0]);
				throw new NotFoundException("Error: " + args[0] + " not a valid method");
		}
	}
	
	@Override
	protected String updated(Event e)
	{
		return new Gson().toJson(new Event.SerializedAction(e, e.getUuid(), false));
	}

	@Override
	protected String deleted(UUID id)
	{
		return new Gson().toJson(new Event.SerializedAction(null, id, true));
	}
}
