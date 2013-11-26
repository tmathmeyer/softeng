package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.awt.Color;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;


/**
 *  Basic categories class that contains all information required to 
 *  designate an event category in the calendar
 *  
 *  @author sarahsawatzki Etienne Prateek
 */
public class Category extends AbstractModel
{
	private UUID categoryID = UUID.randomUUID();
	private String name;
	private Color color;
	private User owner;
	private boolean isProjectCategory;
	
	/**
	 * Sets the name of the category
	 * @param name the name to set to the category	
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Gets the name of the category
	 * @return name the name of the category
	 */
	public String getName()
	{
		return this.name;
	}
	
	/** 
	 * Sets the color of the category
	 * @param color the color to set the category
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	/**
	 * Gets the color of the category
	 * @return color the color of the category
	 */
	public Color getColor()
	{
		return this.color;
	}
	
	/**
	 * Gets the UUID of the given category
	 * @return categoryID the UUID of the given category
	 */
	
	public UUID getUUID()
	{
		return categoryID;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toJSON() {
		return new Gson().toJson(this, Category.class);
	}

	@Override
	public Boolean identify(Object o) {
		if(o instanceof Category)
		{
			return ((Category) o).toJSON().equals(this.toJSON());
			
		}
		return false;
	}

	/**
	 * Sets the owner of the category
	 * @param user tracks the user ID of the person signed into the module
	 */
	
	public void setOwner(User user) {
		this.owner = user;
	}
	
	public User getOwner() {
		return this.owner;
	}

	/**
	 * @param content string of information for the category
	 * @return nothing
	 */
	
	public static Category fromJson(String content) {
		return null;
	}

	/**
	 * Checks to see if the given category is a ProjectCategory
	 * @return boolean if the category is a ProjectCategory
	 */
	
	public boolean isProjectCategory() {
		return isProjectCategory;
	}

	/**
	 * sets the category ID for each category
	 * @return categoryID the UUID for the category
	 */
	
	public Object getCategoryID() {
		return this.categoryID;
	}
}
