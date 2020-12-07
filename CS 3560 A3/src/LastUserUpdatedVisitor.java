import java.util.List;
/* ************* ~ CHANGE MADE HERE ~ ************* */
//Added new class for showing the last updated user in Admin 
public class LastUserUpdatedVisitor implements SysEntryVisitor{

	private class UserLongPair{
		private User user;
		private long value;

		public UserLongPair(User user, long value) {
			this.user = user;
			this.value = value;
		}

		public void setUser(User user) {
			this.user = user;
			this.value = user.getLastUpdateTime();
		}


		public User getUser() { 
			return this.user == null ? null : this.user; 
		}

		public long getValue() { 
			return this.value; 
		}
	}
	
	/**
	 * Allows for the last user to be visited and printed 
	 * out in the console
	 * If there are no user's, null is printed out for the 
	 * users 
	 * 
	 * @return 0 for default return (main objective is to 
	 * print the output for the last user)
	 * */
	@Override
	public int visit(SysEntry entry) {
		UserLongPair lastUpdatedPair = new UserLongPair(null, (long)0);
		findMostRecentUser(entry, lastUpdatedPair);
		User lastUpdatedUser = lastUpdatedPair.getUser();
		if(lastUpdatedUser != null) {
			System.out.println("Last User Updated: " + lastUpdatedUser.getID());
			System.out.println("");
			Admin.setVisitorOutput(lastUpdatedUser);
		}else {
			System.out.println("Last User Updated: NULL");
			System.out.println("");
			Admin.setVisitorOutput(null);
		}
		return 0;
	}

	/**
	 * Finds the last updated user and updates lastUpdatedPair which is associated with
	 * the user 
	 * 
	 * @param root from SysEntry gets the root of the tree
	 * @param lastUpdatedPair result of last user updated to keep track of and update
	 */
	private void findMostRecentUser(SysEntry root, UserLongPair lastUpdatedPair) {
		List<SysEntry> items = ((UserGroup)root).getEntries();
		for(int i = 0; i < items.size(); i++) {
			SysEntry item = items.get(i);
			if(item instanceof User) {
				if(lastUpdatedPair.getUser() == null) {
					lastUpdatedPair.setUser((User)item);
				}else {
					if(((User)item).getLastUpdateTime() > lastUpdatedPair.getValue()) { //compares the times 
						lastUpdatedPair.setUser((User)item);
					}
				}
			}
			else if(item instanceof UserGroup) {
				findMostRecentUser(item, lastUpdatedPair); //recurse using current item 
			}
		}
	}
}