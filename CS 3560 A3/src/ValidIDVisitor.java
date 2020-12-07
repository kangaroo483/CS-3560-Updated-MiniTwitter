import java.util.ArrayList;
import java.util.List;

/* ************* ~ CHANGE MADE HERE ~ ************* */
//New class for finding valid users and print it to the console

public class ValidIDVisitor implements SysEntryVisitor {

	@Override
	/**
	 * Checks all ID's, prints the results from checkValidID,  
	 * set's the visitor output for GUI, and returns that as
	 * 1/0 for T/F
	 * 
	 * @return 0 or 1 (t/f) if ID's are valid or not 
	 * */
	public int visit(SysEntry entry) {
		List<String> iDsChecked = new ArrayList<>();
		boolean isValidResult = validID(entry, iDsChecked);
		System.out.println("Valid IDs: " + isValidResult);
		Admin.setVisitorOutput(isValidResult ? 1 : 0);
		return isValidResult ? 1 : 0;
	}

	/**
	 * Checks if the entries of the children of root and its sub-children are valid.
	 * The criteria for being valid is the following:
	 * 
	 * Iterates and loops through all the elements in the system and check if 
	 * there are no duplicate ID's and no spaces in a ID for users and groups.
	 * The returned result is T/F based on the 2 rules.
	 * 
	 * @param root from the list of ID's
	 * @param keeps track of already seen ID's
	 * @return true or false based on if ID's have the 2 rules 
	 */
	public boolean validID(SysEntry root, List<String> checkedIDs) {
		List<SysEntry> elements = ((UserGroup)root).getEntries();
		for(int i = 0; i < elements.size(); i++) {
			SysEntry entry = elements.get(i);
			//contains present ID or space 
			if(checkedIDs.contains(entry.getID()) || entry.getID().contains(" ")) {
				return false;
			}
			checkedIDs.add(elements.get(i).getID());
			//checks if UserGroup then recursive and return false when not validID = false 
			if(entry instanceof UserGroup) {
				if(!validID(entry, checkedIDs)) {
					return false;
				}
			}
		}
		return true;
	}
}