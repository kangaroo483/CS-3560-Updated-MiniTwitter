/**
 * This class uses the idea of Singleton by creating an variable and checking in a 
 * static method called getInstance and returning if it is an instance of the class  
 */

/* ************* ~ CHANGE MADE HERE ~ ************* */
//change variables and method: 
//1. changed private static int visitorOutput; to object type 
//2. public static int getVisitorOutput() { 
//		return visitorOutput; 
//	 }
//changed to return Object type 
//3. changed public static void setVisitorOutput(int output) 
// to pass in (object output)
//changed the block comments of 1. - 3. changes 

public class Admin {
	//make a static AdminController Object for Singleton pattern
	private static Admin instance = null;
	private static UserGroup rootUserGroup;
	private static Object visitorOutput;
	
	//private constructor used for Singleton pattern 
	private Admin() { 
		rootUserGroup = new UserGroup("root"); 
	}

	/**
	 * Returns if instance equals null to AdminController object return instance 
	 * or just return the instance
	 * 
	 * @return instance 
	 */
	public static Admin getInstance() { 
		if (instance == null) {
		    	instance = new Admin();
		}
		return instance;
	}
	
	/**
	 * Returns the root of the tree 
	 * 
	 * @return the root SysEntry [UserGroup type]
	 */
	public static SysEntry getRootEntry() {
		return rootUserGroup; 
	}
	
	/**
	 * Gets the user ID 
	 * 
	 * @param ID of the user 
	 * @return user or null (no group)
	 */
	public static User getUserID(String userID) { 
		return rootUserGroup.findUser(userID); 
	}
	
	/**
	 * Gets UserGroup ID
	 * 
	 * @param ID of the group
	 * @return group or null (no group)
	 */
	public static UserGroup getGroupID(String groupID) { 
		return rootUserGroup.findGroup(groupID); 
	}
	
	/**
	 * Set's the root group [for Admin]
	 * @param root from UserGroup
	 */
	public static void setRootGroup(UserGroup root) { 
		rootUserGroup = root; 
	}
		
	/**
	 * Sets object in visitorOutput to input from parameter 
	 * 
	 * @param object being passed in 
	 */
	public static void setVisitorOutput(Object output) { 
		visitorOutput = output;
	}
	
	/**
	 * Returns the Object value in visitorOutput
	 * 
	 * @return visitorOutput
	 */
	public static Object getVisitorOutput() { 
		return visitorOutput; 
	}
	
	/**
	 * Prints all entries in the working tree
	 */
	public static void printAllEntries() {
		System.out.println(rootUserGroup.getID() + " " + rootUserGroup.getClass().getName());
		rootUserGroup.printAllEntries(1);
	}
}
