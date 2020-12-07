import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.SwingConstants;

public class AdminPanelGUI {
	private DefaultMutableTreeNode chosenNode;
	private final DefaultMutableTreeNode emptyNode = new DefaultMutableTreeNode(new User("empty"));
	
	private JFrame mainAppWindow;
	private JTree treeDisplay;
	private JTextField userInputText;
	private JTextField groupInputText;
	private JButton addUserButton;
	private JButton addGroupButton;
	private JButton userViewButton;

	//open the window for admin panel 
	public AdminPanelGUI() {
		initialize();
		mainAppWindow.setVisible(true);
	}
	
	//Initialize to show GUI elements of AdminPanelGUI
	private void initialize() {
		mainAppWindow = new JFrame();
		mainAppWindow.getContentPane().setBackground(new Color(176, 224, 230));
		mainAppWindow.setResizable(false);
		mainAppWindow.setTitle("MiniTwitter Admin");
		mainAppWindow.setBounds(100, 100, 579, 396);
		mainAppWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainAppWindow.getContentPane().setLayout(null);
		
		JScrollPane treeViewPanel = new JScrollPane();
		treeViewPanel.setBounds(10, 11, 156, 346);
		mainAppWindow.getContentPane().add(treeViewPanel);
		
		JLabel userIDText = new JLabel("User ID :");
		userIDText.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		userIDText.setBounds(235, 103, 55, 27);
		mainAppWindow.getContentPane().add(userIDText);
		
		JLabel groupIDText = new JLabel("Group ID :");
		groupIDText.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		groupIDText.setBounds(225, 140, 65, 27);
		mainAppWindow.getContentPane().add(groupIDText);	
		
		JLabel welcomeText = new JLabel("Welcome to Mini Twitter! (✿◠ ‿ ◠)");
		welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeText.setVerticalAlignment(SwingConstants.BOTTOM);
		welcomeText.setFont(new Font("Hiragino Maru Gothic ProN", Font.PLAIN, 19));
		welcomeText.setBounds(214, 11, 306, 58);
		mainAppWindow.getContentPane().add(welcomeText);
				
		treeDisplay = new JTree();
		treeDisplay.setBackground(new Color(224, 255, 255));
		treeDisplay.setFont(new Font("Hiragino Sans GB", Font.BOLD, 12));
		treeDisplay.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				//Lets you use the buttons when user or group is chosen in tree 
				addUserButton.setEnabled(true);
				addGroupButton.setEnabled(true);
				
				//Tree node clicked on 
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        treeDisplay.getLastSelectedPathComponent();
				//Set the clicked on node 
				chosenNode = node;
				try {
					//Get the node: user or group object 
					String nodeName = node.getUserObject().toString() == null ? "" : node.getUserObject().toString();
					//The users are leafs based on composite pattern 
					if(node.isLeaf()){
						if(!nodeName.equals("empty")) {
							userViewButton.setEnabled(true);
						} else {
							userViewButton.setEnabled(false);
						}
						//Get the parent of the node object 
						DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
						//Get the parent ID 
						String nodeParentName = (String)parent.getUserObject().toString();
						userInputText.setText(nodeName);
						groupInputText.setText(nodeParentName);
					} else {
						//UserGroup 
						userViewButton.setEnabled(false);
						userInputText.setText(null);
						groupInputText.setText(nodeName);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		treeDisplay.setExpandsSelectedPaths(true);
		treeDisplay.setShowsRootHandles(true);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(Admin.getRootEntry());
		treeDisplay.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode(root) {
				{
					add(new DefaultMutableTreeNode(emptyNode));
				}
			}
		));
		
		treeViewPanel.setViewportView(treeDisplay);
		
		userInputText = new JTextField();
		userInputText.setBackground(new Color(224, 255, 255));
		userInputText.setFont(new Font("Hiragino Sans GB", Font.PLAIN, 12));
		userInputText.setEditable(false);
		userInputText.setBounds(302, 98, 101, 29);
		mainAppWindow.getContentPane().add(userInputText);
		userInputText.setColumns(10);
		
		addUserButton = new JButton("Add User");
		addUserButton.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		//Initially set to disabled until a tree element is selected
		addUserButton.setEnabled(false);
		addUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Add User
					AddUserGUI userDialog = new AddUserGUI();
					userDialog.setSelectedGroup(groupInputText.getText());
					userDialog.setModalityType(ModalityType.APPLICATION_MODAL);
					userDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					userDialog.setVisible(true);
					
					//Get inputed ID, show model of tree
					//where to input a user, remove extra node if a folder 
					//insert a node, get parent of userGroup
					String inputID = userDialog.getID();
					if(inputID != null && Admin.getUserID(inputID) == null) {
						DefaultTreeModel model = (DefaultTreeModel) treeDisplay.getModel();
						DefaultMutableTreeNode insertionNode = chosenNode.isLeaf() ? (DefaultMutableTreeNode)chosenNode.getParent() : chosenNode;
						if(insertionNode.getChildCount() == 1) {
							DefaultMutableTreeNode child = (DefaultMutableTreeNode) insertionNode.getChildAt(0);
							if(child.getUserObject().toString().equals("empty")) {
								model.removeNodeFromParent(child);
							}
						}
						model.insertNodeInto(new DefaultMutableTreeNode(inputID), insertionNode, insertionNode.getChildCount());
						UserGroup parentGroup = Admin.getGroupID(insertionNode.getUserObject().toString());
						User newUser = new User(inputID, parentGroup);
						parentGroup.addUser(newUser);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				Admin.printAllEntries(); 
			}
		});
		addUserButton.setBounds(404, 98, 99, 29);
		mainAppWindow.getContentPane().add(addUserButton);
		
		groupInputText = new JTextField();
		groupInputText.setBackground(new Color(224, 255, 255));
		groupInputText.setFont(new Font("Hiragino Sans GB", Font.PLAIN, 12));
		groupInputText.setEditable(false);
		groupInputText.setColumns(10);
		groupInputText.setBounds(302, 138, 101, 29);
		mainAppWindow.getContentPane().add(groupInputText);
		
		addGroupButton = new JButton("Add Group");
		addGroupButton.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		addGroupButton.setEnabled(false);
		addGroupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					/**
					 *  Get inputed ID, show model of tree
					 *  where to input a user
					 *  remove extra node when the folder 
					 *  insert a node, get parent of userGroup
					 */
					AddGroupGUI groupText = new AddGroupGUI();
					groupText.setChosenGroup(groupInputText.getText());
					groupText.setModalityType(ModalityType.APPLICATION_MODAL);
					groupText.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					groupText.setVisible(true);
					String inputID = groupText.getID();
					if(inputID != null && Admin.getGroupID(inputID) == null) {
						DefaultTreeModel model = (DefaultTreeModel) treeDisplay.getModel();
						DefaultMutableTreeNode insertionNode = chosenNode.isLeaf() ? (DefaultMutableTreeNode)chosenNode.getParent() : chosenNode;
						if(insertionNode.getChildCount() == 1) {
							DefaultMutableTreeNode child = (DefaultMutableTreeNode) insertionNode.getChildAt(0);
							if(child.getUserObject().toString().equals("empty")) {
								model.removeNodeFromParent(child);
							}
						}
						/*
						 * insert a node
						 * make new inserted node to recent one added
						 * created an empty node inside the folder 
						 */
						model.insertNodeInto(new DefaultMutableTreeNode(inputID), insertionNode, insertionNode.getChildCount());
						DefaultMutableTreeNode insertionNodeNext = insertionNode.getLastLeaf();
						//Insert empty node as a placeholder so it appears as a folder in the tree view
						DefaultMutableTreeNode emptyCopy = new DefaultMutableTreeNode(new User("empty"));
						model.insertNodeInto(emptyCopy, insertionNodeNext, insertionNodeNext.getChildCount());
						
						/*
						 * Find the parent group 
						 * Make and add a new group based on the inputID and parent group
						 */
						UserGroup parentGroup = Admin.getGroupID(insertionNode.getUserObject().toString());
						UserGroup newGroup = new UserGroup(inputID, parentGroup);
						parentGroup.addUserGroup(newGroup);
					}
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				Admin.printAllEntries(); 

			}
		});
		addGroupButton.setBounds(404, 138, 99, 29);
		mainAppWindow.getContentPane().add(addGroupButton);

		//user profile 
		userViewButton = new JButton("Open User Profile");
		userViewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userSelectedID = chosenNode.getUserObject().toString();
				User userSet = Admin.getUserID(userSelectedID);
				
				/* ************* ~ CHANGE MADE HERE ~ ************* */
				//pass in userSet into the newProfile window 
				UserProfileGUI newProfileWindow = new UserProfileGUI(userSet);
				newProfileWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				/* ************* ~ CHANGE MADE HERE ~ ************* */
				//this is omitted : newProfileWindow.setUserProfile(userSet);
				newProfileWindow.setVisible(true);
			}
		});
		userViewButton.setEnabled(false);
		userViewButton.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		userViewButton.setBounds(249, 179, 248, 29);
		mainAppWindow.getContentPane().add(userViewButton);
		
		//user total button and actions 
		JButton userTotalButton = new JButton("Show User Total");
		userTotalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin.getRootEntry().accept(new UserTotal());
				/* ************* ~ CHANGE MADE HERE ~ ************* */
				//casting as type int since Object type
				int totalUsers = (int)Admin.getVisitorOutput();
				String userTotalString = String.format("There are %d users.", totalUsers);
				VisitorButtonsGUI visitorMessage = new VisitorButtonsGUI(userTotalString);
				visitorMessage.setTitle("Total Users");
				visitorMessage.setVisible(true);
			}
		});
		userTotalButton.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		userTotalButton.setBounds(180, 244, 188, 29);
		mainAppWindow.getContentPane().add(userTotalButton);
		
		//group total button and actions 
		JButton groupTotalButton = new JButton("Show Group Total");
		groupTotalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin.getRootEntry().accept(new GroupTotal());
				/* ************* ~ CHANGE MADE HERE ~ ************* */
				//casting as type int since Object type
				int groupTotal = (int)Admin.getVisitorOutput();
				String groupTotalString = String.format("There are %d groups", groupTotal);
				VisitorButtonsGUI visitorMessage = new VisitorButtonsGUI(groupTotalString);
				visitorMessage.setTitle("Total Groups");
				visitorMessage.setVisible(true);
			}
		});
		groupTotalButton.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		groupTotalButton.setBounds(378, 244, 188, 29);
		mainAppWindow.getContentPane().add(groupTotalButton);
		
		//message total button and actions 
		//find the message total through total message method and display 
		JButton totalMessagesButton = new JButton("Show Total Messages");
		totalMessagesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin.getRootEntry().accept(new MessageTotal());
				/* ************* ~ CHANGE MADE HERE ~ ************* */
				//casting as type int since Object type
				int messageTotal = (int)Admin.getVisitorOutput();
				String messageTotalString = String.format("There are %d messages.", messageTotal);
				VisitorButtonsGUI visitorMessage = new VisitorButtonsGUI(messageTotalString);
				visitorMessage.setTitle("Total Messages");
				visitorMessage.setVisible(true);
			}
		});
		totalMessagesButton.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		totalMessagesButton.setBounds(178, 275, 188, 29);
		mainAppWindow.getContentPane().add(totalMessagesButton);
		
		//positive message total button and actions 
		//find the + message total through total + message method and display GUI 
		JButton positivePercentButton = new JButton("Show Positive %");
		positivePercentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin.getRootEntry().accept(new MessageTotal());
				/* ************* ~ CHANGE MADE HERE ~ ************* */
				//casting as type int since Object type
				int posMessageTotal = (int)Admin.getVisitorOutput();
				Admin.getRootEntry().accept(new PositiveMessageTotal());
				/* ************* ~ CHANGE MADE HERE ~ ************* */
				//casting as type int since Object type
				int totalPositiveMessages = (int)Admin.getVisitorOutput();
				
				double positiveMessagePercent = 0.0;
				if(posMessageTotal != 0) {
					positiveMessagePercent = ((totalPositiveMessages + 0.0) / (posMessageTotal + 0.0)) * 100;
				} else {
					positiveMessagePercent = 0;
				}
				String posMessageString = String.format("%.2f percent are positive messages.", positiveMessagePercent);
				VisitorButtonsGUI visitorMessage = new VisitorButtonsGUI(posMessageString);
				visitorMessage.setTitle("Positive Message Percent");
				visitorMessage.setVisible(true);
			}
		});
		positivePercentButton.setFont(new Font("Hiragino Sans", Font.BOLD, 12));
		positivePercentButton.setBounds(378, 275, 188, 29);
		mainAppWindow.getContentPane().add(positivePercentButton);

		JSeparator lineSeparator = new JSeparator();
		lineSeparator.setForeground(new Color(224, 255, 255));
		lineSeparator.setBounds(176, 220, 397, 12);
		mainAppWindow.getContentPane().add(lineSeparator);
		
		/* ************* ~ CHANGE MADE HERE ~ ************* */
		//358 ~ end of the method  includes new buttons for valid ID's and last updated user
		//check valid ID Button 
		JButton checkValidIDButton = new JButton("Valid ID's Check");
		checkValidIDButton.setFont(new Font("Hiragino Sans GB", Font.BOLD, 12));
		checkValidIDButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//check if valid ID via method 
				Admin.getRootEntry().accept(new ValidIDVisitor());
				int visitorOutput = (int)Admin.getVisitorOutput();
				//checks if valid or not valid in new window when clicking on it
				String validIDString = visitorOutput == 1 ? "User ID Check: VALID" : "User ID Check: INVALID";
				//Information passed for the visitor buttons
				VisitorButtonsGUI visitorButtonGUI = new VisitorButtonsGUI(validIDString);
				visitorButtonGUI.setTitle("ID Valid");
				visitorButtonGUI.setVisible(true);
			}
		});
		checkValidIDButton.setBounds(376, 311, 188, 23);
		mainAppWindow.getContentPane().add(checkValidIDButton);
		
		//update last user added button
		JButton lastUpdatedUserButton = new JButton("Show Last Updated User");
		lastUpdatedUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Have the root entry visitor entry visitor accept the show last updated user visitor method
				Admin.getRootEntry().accept(new LastUserUpdatedVisitor());
				//check in Admin.visitorOutput for the last user ID else null
				String userOutputID = "";
				if(Admin.getVisitorOutput() != null) {
					userOutputID = ((User)Admin.getVisitorOutput()).getID();
				}else {
					userOutputID = "NULL";
				}
				//String for the output in visitor button output in the new window
				String lastUpdatedUserString = String.format("The most recently updated user is %s", userOutputID);
				VisitorButtonsGUI visitorButtonGUI = new VisitorButtonsGUI(lastUpdatedUserString);
				visitorButtonGUI.setTitle("Last Updated User");
				visitorButtonGUI.setVisible(true);
			}
		});
		lastUpdatedUserButton.setToolTipText("Shows the last updated user in the system.");
		lastUpdatedUserButton.setFont(new Font("Hiragino Sans GB", Font.BOLD, 12));
		lastUpdatedUserButton.setBounds(178, 312, 188, 23);
		mainAppWindow.getContentPane().add(lastUpdatedUserButton);
		
		
	}
}
