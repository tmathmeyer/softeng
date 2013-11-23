package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.Interval;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;


/**
 * This is the entity manager for the Category
 * in the CategoryManager module.
 * @author Prateek, SarahS, Etienne
 *
 */

public class CategoryEntityManager implements EntityManager<Category> {
	/** The database */
	Data db;
	
	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in
	 * the ManagerLayer file.
	 * 
	 * @param db a reference to the persistent database
	 */
	public CategoryEntityManager(Data db) {
		this.db = db; 
	}
	
	/**
	 * Saves a Category when it is received from a client
	 * 
	 */
	@Override
	public Category makeEntity(Session s, String content) throws WPISuiteException {
		System.out.println(content+ " was just sent!");
		final Category newCategory = Category.fromJson(content);
		newCategory.setOwner(s.getUser());
		newCategory.setProject(s.getProject());
		if(!db.save(newCategory, s.getProject())) {
			throw new WPISuiteException();
		}
		return newCategory;
	}

	@Override
	public Category[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
		/**
		 * System.out.println(data+ " was just sent!");
		String[] args = data.split(",");
		
		Event[] retrievedEvents = null;
		
		switch (args[0]) {
			case "filter-events-by-range":
				return getEventsByRange(s, args[1], args[2]);
			default:
				System.out.println("Error: " + args[0] + " not a valid method");
		}

	
		return retrievedEvents;
		 */
		
		System.out.println(id+ " was just sent!");
		String[] args = id.split(",");
		
		Category[] retrievedCategories = null;
		
		switch (args[0]) {
			case "get-category-by-name":
				return 	getCategoryByName(s, args[1]);	
			case "get-category-by-id":
				return
			case "get-category-by-color":
				return
			default:
				System.out.println("Error: " + args[0] + " not a valid method");			
		}
	}
	/**For now, only return the first category it finds with a matching name.
	 * Currently have not decided how to approach categories with matching names.
	 * If a matching name is not there, returns a blank array.
	 * 
	 * @param ses
	 * @param name
	 * @return
	 * @throws WPISuiteException
	 */
	private Category[] getCategoryByName(Session ses, String name) throws WPISuiteException {
		List<Category> retrievedCategories = new ArrayList<>();
		
		Category[] all = getAll(ses);
		
		for(Category c: all) 
		{
			if(c.getName().contains(name)){
				retrievedCategories.add(c);
				return new Category[] {retrievedCategories.get(0)};
			}	
		}
		
		return new Category[] {};
				
	}
	
	@Override
	public Category[] getAll(Session s) throws WPISuiteException {
		System.out.println("GET ALL!");
		return db.retrieveAll(new Category(), s.getProject()).toArray(new Category[0]);
	}

	@Override
	public Category update(Session session, String content) throws WPISuiteException {
		
		Category updatedCategory = Category.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save Categories.
		 * We have to get the original defect from db4o, copy properties from updatedCategory,
		 * then save the original Category again.
		 */
		List<Model> oldCategories = db.retrieve(Category.class, "categoryID", updatedCategory.getCategoryID(), session.getProject());
		if(oldCategories.size() < 1 || oldCategories.get(0) == null) {
			throw new BadRequestException("Category with ID does not exist.");
		}
				
		Category existingCategory = (Category)oldCategories.get(0);		

		// Copy values to old event and fill in our changeset appropriately
		// TODO: existingCategory.copyFrom(updatedCategory);
		
		if(!db.save(existingCategory, session.getProject())) {
			throw new WPISuiteException();
		}
		
		return existingCategory;
	}

	@Override
	public void save(Session s, Category model) throws WPISuiteException {
		if (model.isProjectCategory())
			model.setProject(s.getProject());
		db.save(model);
		
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Event(), s.getProject());
		
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Category()).size();
	}

	@Override
	public String advancedGet(Session s, String[] args) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	@Override
	public String advancedPut(Session s, String[] args, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
}
