/*
 * This class is used to create the hierarchy for the Visitor pattern  
 */

public abstract class SysEntry implements SysEntryVisitable{
	private String ID;
	/* ************* ~ CHANGE MADE HERE ~ ************* */
	//new var used to get creation time of users and groups 
	private long creationTime;
	/**
	 * Creates SysEntry object with a passed in ID (user or group)
	 * 
	 * @param ID is set to ID in this class as a private string 
	 */
	public SysEntry(String ID) { 
		this.ID = ID; 
		/* ************* ~ CHANGE MADE HERE ~ ************* */
		//used to initialize the time in milliseconds
		this.creationTime = System.currentTimeMillis();
	}
	
	/**
	 * Send the visitor's visit method the current object    
	 * 
	 * @param visitor of SysEntryVisitor type 
	 */
	public void accept(SysEntryVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * Returns a string copy of the ID
	 * 
	 * @return ID as a string 
	 */
	public String getID() {
		//Don't return a direct reference to ID
		return String.copyValueOf(ID.toCharArray());
	}
	
	/**
	 * Returns the string to display the inputed user ID 
	 */
	public String toString() {
		return ID; 
	}
	
	/* ************* ~ CHANGE MADE HERE ~ ************* */
	//allow for getting creation time for the user and the groups
	/**
	 * Gets the creation time of the entry (Users/Groups)
	 * 
	 * @return creation time in milliseconds 
	 */
	public long getCreationTime() { 
		return this.creationTime; 
	}
}
