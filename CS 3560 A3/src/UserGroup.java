import java.util.ArrayList;
import java.util.List;

/* ************* ~ CHANGE MADE HERE ~ ************* */
//added print statement to show the UserGroup ID's

public class UserGroup extends SysEntry {
	private UserGroup parent;
	private List<SysEntry> items;
	
	/**
	 * Overloading in order to satisfy and set each constraint within this class
	 * and other classes
	 */
	public UserGroup(String ID) {
		super(ID);
		parent = null;
		items = new ArrayList<>();
	}
	
	public UserGroup(String ID, UserGroup parent) {
		super(ID);
		items = new ArrayList<>();
		this.parent = parent;
	}
	
	/**
	 * Use for each loop to iterate recursively through all the list items to find 
	 * the userID and return it (or null if it doesn't exist)
	 * 
	 * @param user ID searching for 
	 * @return User object exist and return that object or null
	 */
	public User findUser(String userID) {
		for(SysEntry item : items) {
			if(item instanceof UserGroup) {
				if(((UserGroup) item).findUser(userID) != null) {
					return ((UserGroup) item).findUser(userID);
				}
			}
			if(item instanceof User && item.getID().equals(userID)) {
				return (User)item;
			}
		}
		return null;
	}
	
	/**
	 * Gets the items inside of list
	 * 
	 * @return items
	 */
	public List<SysEntry> getEntries() { 
		return this.items; 
	}
	
	/**
	 * Gets parent group in this class
	 * 
	 * @return parent group
	 */
	public UserGroup getParent() { 
		return this.parent; 
	}
	
	/**
	 * Use for each loop to iterate recursively through all the list items to find 
	 * the groupID and return it (or null if it doesn't exist)
	 * 
	 * @param group ID looking for  
	 * @return UserGroup object exist and return that object or null
	 */
	public UserGroup findGroup(String groupID) {
		if(this.getID().equals(groupID)) {
			return this;
		}
		for(SysEntry item : items) {
			if(item instanceof UserGroup && item.getID().equals(groupID)) {
				return (UserGroup)item;
			} else if(item instanceof UserGroup) {
				if(((UserGroup) item).findGroup(groupID) != null) {
					return ((UserGroup) item).findGroup(groupID);
				}
			}
		}
		return null;
	}
	
	/**
	 * Adds user to the tree list   
	 * 
	 * @param newUserID is added to list of SysEntry
	 */
	public void addUser(String newUserID) {
		SysEntry newUser = new User(newUserID);
		items.add(newUser);
	}
	
	/**
	 * Adds user object to user tree list
	 * 
	 * @param user object added to list 
	 */
	public void addUser(User user) { 
		items.add(user); 
	}
	
	/**
	 * Adds a user group to tree list 
	 * 
	 * @param group ID creates a new group 
	 */
	public void addUserGroup(String groupID) {
		SysEntry addedGroup = new UserGroup(groupID);
		items.add(addedGroup);
	}
	
	/**
	 * Adds a user group object to tree list 
	 * 
	 * @param add group to tree list 
	 */
	public void addUserGroup(UserGroup addedGroup) { 
		items.add(addedGroup); 
	}
	
	/* ************* ~ CHANGE MADE HERE ~ ************* */
	//prints all the entries with the creation times for the groups 
	/**
	 * Prints depth-first search (DFS) of all entries UserGroup with the creation 
	 * time for the user and the groups (User has output in User window GUI but the 
	 * group creation time in is the terminal)
	 */
	public void printAllEntries(int tabCount) {
		for(int i = 0; i < items.size(); i++){
			SysEntry element = items.get(i);
			for(int j = 0; j < tabCount; j++) {
				System.out.print("\t");
			}
			System.out.println(element.getID() + " : " + element.getClass().getName() + " Creation Time : " + element.getCreationTime());
			if(element instanceof UserGroup) {
				((UserGroup) element).printAllEntries(tabCount + 1);
			}
		}
	}
}
