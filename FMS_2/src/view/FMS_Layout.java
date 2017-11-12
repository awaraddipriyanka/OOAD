package view;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Controller.database.DB_management;
import Controller.database.DbOperationHelper;
import Controller.system.ManageItem;
import Controller.system.loginAuthorization;
import net.proteanit.sql.DbUtils;
import model.Constant;
import model.Item;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FMS_Layout extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	protected	JTabbedPane	tabbedPane;
	protected	int	iScreenWidth;
	protected	int	iScreenHeight;
	protected	Dimension screenSize;
	protected	JTable tableForSearchBuy;
	protected	JTable tableForSearchRent;
	protected 	JScrollPane	scrollPaneForSearchBuy;
	protected 	JScrollPane	scrollPaneForSearchRent;
	protected	JTextField	tfSearchBuy;
	protected	JTextField	tfSearchRent;
	protected 	JButton	bSearchBuy;
	protected 	JButton	bSearchRent;
	protected 	JButton	bAddToCartBuy;
	protected 	JButton	bAddToCartRent;
	protected	JButton	bCart;
	protected	JButton	bCheckout;
	protected	JButton	bremoveFromCart;
	protected	JTable	tableForCart;
	protected 	JScrollPane	scrollPaneForCart;
	// Login related artifacts - START
	
	private loginAuthorization loginAuthObj;
		
	private		boolean	bFlag;
	JFrame	frame;
	// Login related artifacts - END
	private ManageItem manageItemObj  ;
	protected Map<Integer,Integer> itemIdQuantityForBuy;
	protected Map<Integer,Integer> itemIdQuantityForRent;
	protected Map<Integer,Integer> itemIdQuantityUpdateAfterCheckout;
	protected List<Integer> listItemIdToRemove;
	protected List<Integer> listItemIdToCheckout;
	public DB_management dbObj = null;
	
	public FMS_Layout (DB_management oDBM)
	{	
		screenSize	= Toolkit.getDefaultToolkit().getScreenSize();	
		iScreenWidth	= screenSize.width;
		iScreenHeight	= screenSize.height;
		itemIdQuantityForBuy = new  HashMap<Integer,Integer>();
		itemIdQuantityForRent = new  HashMap<Integer,Integer>();
		itemIdQuantityUpdateAfterCheckout = new HashMap<Integer,Integer>();
		listItemIdToRemove = new ArrayList();
		listItemIdToCheckout = new ArrayList();
		dbObj = oDBM;
		
	}
	
	public void set_main_layout (String pTitle) throws SQLException
	{
		frame = new JFrame (pTitle);
		manageItemObj = new ManageItem();
		
		frame.getContentPane().setLayout (new GridLayout(1, 1));
		frame.setSize (iScreenWidth, iScreenHeight);
		frame.setDefaultCloseOperation (EXIT_ON_CLOSE);
		
		tabbedPane = (new JTabbedPane (JTabbedPane.TOP));
		
		// Login related artifacts - START
		loginAuthObj	= new loginAuthorization();
		tabbedPane.add ("Login", makeLoginPanel ("Login"));
		// Login related artifacts - END
				
		tabbedPane.add ("Sell", makeSellPanel ("Sell"));
		tabbedPane.add ("Buy", makeBuyPanel ("Buy"));
		tabbedPane.add ("Rent", makeRentPanel ("Rent"));
		tabbedPane.add ("model.Cart", makeCartPanel ("model.Cart"));
				
		// Login related artifacts - START
		// Disable the Tabs by default. Enabled after successful login
				
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 4, false);	// Sell Tab
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 3, false);	// Buy Tab
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 2, false);	// Rent Tab
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 1, false);	// model.Cart Tab
			
		// Add action listener for search button
		bSearchBuy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				populateBuyGrid(manageItemObj.displaySearch(tfSearchBuy.getText(), Constant.BUY));
			}
		});
		
		bSearchRent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				populateRentGrid(manageItemObj.displaySearch(tfSearchRent.getText(), Constant.RENT));
			}
		});
		
		bAddToCartBuy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getSelectedBuyItems();
				try {
					manageItemObj.addToCart(itemIdQuantityForBuy, Constant.BUY);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				populateCartGrid();
			}
		});
		
		bAddToCartRent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getSelectedRentalItems();
				try {
					manageItemObj.addToCart(itemIdQuantityForRent, Constant.RENT);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				populateCartGrid();
			}
		});
		
		bremoveFromCart.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeSelectedItemsFromCart();
				try {
					manageItemObj.removeItemsFromCart(listItemIdToRemove);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				populateCartGrid();
			}
		});
		
		populateBuyGrid(manageItemObj.displaySearch(tfSearchBuy.getText(), Constant.BUY));
		populateRentGrid(manageItemObj.displaySearch(tfSearchBuy.getText(), Constant.RENT));
		populateitemIdQuantityMap(DbOperationHelper.getCartItems());
		populateitemIdQuantityMap(DbOperationHelper.getCartItems());
		updateQuantityForCartItems();
		populateCartGrid();
		frame.getContentPane().add (tabbedPane);
		frame.setVisible (true);
	}
	
	private void updateQuantityForCartItems()
	{
		manageItemObj.updateCartQuantity(itemIdQuantityForBuy, itemIdQuantityForRent);
	}
	
	private  JPanel makeLoginPanel (String pID)
	{	
		JPanel	panel	= new JPanel ();
		panel.setLayout(null);
		JLabel title = new JLabel("Welcome To Furniture Management System");
		title.setFont(new Font("Chalkboard", 1, 28));
		title.setBounds(400, 30,800,100);
		
		JLabel	lLoginUserName = new JLabel("Enter UserName: ");
		lLoginUserName.setBounds(400, 150, 300, 30);
		JLabel	lUserPassword = new JLabel("Enter Password: ");
		lUserPassword.setBounds(400, 200, 300, 30);
		
		JTextField	tfLoginUserName = new JTextField(iScreenWidth/40);
		tfLoginUserName.setBounds(600, 150, 300, 30);
		JPasswordField	tfLoginPassword= new JPasswordField(iScreenWidth/40);
		tfLoginPassword.setBounds(600, 200, 300, 30);
		
		JButton	bLogin = new JButton("Login");	
		bLogin.setBounds(600, 250, 150, 30);
		
		tfLoginUserName.setVisible (true);
		tfLoginPassword.setVisible (true);
		
		panel.add(tfLoginUserName);
		panel.add(tfLoginPassword);
		panel.add(lLoginUserName);
		panel.add(lUserPassword);
		panel.add(bLogin);
		panel.add(title);
		bLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					bFlag = loginAuthObj.loginValidation (tfLoginUserName.getText(),
															String.valueOf (tfLoginPassword.getPassword()));
					
					System.out.println("model.User Validated = " + bFlag);
					
					if (bFlag == true)
					{
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 4, true);	// Sell Tab
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 3, true);	// Buy Tab
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 2, true);	// Rent Tab
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 1, true);	// model.Cart Tab
						tabbedPane.setEnabledAt (0, false);	// Disable login Tab
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "Credentials Invalid. Cannot Login to the system");
					}
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		return panel;
	}
	// Login related artifacts - END
	
	
	private void populateitemIdQuantityMap(ResultSet rs) throws SQLException
	{
		while(rs.next())
		{
			if((boolean)rs.getObject("forSale"))
			{
				itemIdQuantityForBuy.put(Integer.parseInt(rs.getObject("item_id").toString()), 
						   Integer.parseInt(rs.getObject("quantity").toString()));
			}
			else
			{
				itemIdQuantityForRent.put(Integer.parseInt(rs.getObject("item_id").toString()), 
						   Integer.parseInt(rs.getObject("quantity").toString()));
			}
		}
	}
	
	private void removeSelectedItemsFromCart()
	{
		listItemIdToRemove.clear();
		int [] iaIndeces;
		try
		{
			iaIndeces	= tableForCart.getSelectedRows();
			for (int iCount : iaIndeces)
			{
				try
				{
					int key = Integer.valueOf (tableForCart.getValueAt (iCount, Constant.ITEM_ID_INDEX).toString ());
					listItemIdToRemove.add(key);
					if (itemIdQuantityForBuy.containsKey(key))
					{
						itemIdQuantityForBuy.remove(key);
					}
					if (itemIdQuantityForRent.containsKey(key))
					{
						itemIdQuantityForRent.remove(key);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void  getSelectedBuyItems()
	{
		Integer	iReqQty	= 0;
		int [] iaIndeces;
		try
		{
			iaIndeces	= tableForSearchBuy.getSelectedRows();
			for (int iCount : iaIndeces)
			{
				try
				{
					iReqQty	= Integer.valueOf (tableForSearchBuy.getValueAt (iCount, Constant.REQ_QUANTITY_INDEX).toString ());
					if (iReqQty > 0)
					{
						int key = Integer.valueOf (tableForSearchBuy.getValueAt (iCount, Constant.ITEM_ID_INDEX).toString ());
						itemIdQuantityForBuy.put(key, iReqQty);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	private void  getSelectedRentalItems()
	{
		Integer	iReqQty	= 0;
		int [] iaIndeces;
		try
		{
			iaIndeces	= tableForSearchRent.getSelectedRows();
			for (int iCount : iaIndeces)
			{
				try
				{
					iReqQty	= Integer.valueOf (tableForSearchRent.getValueAt (iCount, Constant.REQ_QUANTITY_INDEX).toString ());
					if (iReqQty > 0)
					{
						int key = Integer.valueOf (tableForSearchRent.getValueAt (iCount, Constant.ITEM_ID_INDEX).toString ());
						itemIdQuantityForRent.put(key, iReqQty);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	private void populateCartGrid()
	{
		DefaultTableModel	tableModel;
		tableModel = new DefaultTableModel();
		
		// Add required columns here
		// Quantity Required
		tableModel.addColumn ("ItemID");
		tableModel.addColumn ("Quantity");
		
		// Add the model to the table
		tableForCart.setModel (tableModel);
		
		for(Map.Entry<Integer, Integer> entry : itemIdQuantityForBuy.entrySet())
		{
			// Quantity Required column is set to zero
			Object[] rowData = new Object[2];
			rowData[0]=entry.getKey();
			rowData[1]=entry.getValue();
			
			tableModel.addRow(rowData);
		}
		for(Map.Entry<Integer, Integer> entry : itemIdQuantityForRent.entrySet())
		{
			// Quantity Required column is set to zero
			Object[] rowData = new Object[2];
			rowData[0]=entry.getKey();
			rowData[1]=entry.getValue();
			
			tableModel.addRow(rowData);
		}
		tableForCart.setVisible(true);
	}
	
	private void populateBuyGrid(ResultSet rs)
	{
		DefaultTableModel	tableModel;
		int iRowCount;
		tableModel = (DefaultTableModel) DbUtils.resultSetToTableModel (rs);
		
		// Add required columns here
		// Quantity Required
		tableModel.addColumn ("Quantity Required");
		
		// Add the model to the table
		tableForSearchBuy.setModel (tableModel);
		
		// Set the default value for the columns as required
		iRowCount	= tableForSearchBuy.getRowCount();
		
		System.out.println ("Num of rows returned - " + iRowCount);
		
		for (int iCount = 0; iCount < iRowCount; iCount++)
		{
			// Quantity Required column is set to zero
			tableModel.setValueAt (0, iCount, 5);
		}
		
		bAddToCartBuy.setVisible (true);
		tableForSearchBuy.setVisible (true);
		scrollPaneForSearchBuy.setVisible (true);
	}
	
	private void populateRentGrid(ResultSet rs)
	{
		DefaultTableModel	tableModel;
		int iRowCount;
		tableModel = (DefaultTableModel) DbUtils.resultSetToTableModel (rs);
		
		// Add required columns here
		// Quantity Required
		tableModel.addColumn ("Quantity Required");
		
		// Add the model to the table
		tableForSearchRent.setModel (tableModel);
		
		// Set the default value for the columns as required
		iRowCount	= tableForSearchRent.getRowCount();
		
		System.out.println ("Num of rows returned - " + iRowCount);
		
		for (int iCount = 0; iCount < iRowCount; iCount++)
		{
			// Quantity Required column is set to zero
			tableModel.setValueAt (0, iCount, 5);
		}
		
		bAddToCartRent.setVisible (true);
		tableForSearchRent.setVisible (true);
		scrollPaneForSearchRent.setVisible (true);
	} 
	
	private  JPanel makeBuyPanel (String pID)
	{	
		JPanel	panelMain;
		JPanel	panelSearch;
		JPanel	panelTable;
		JPanel	panelBuyOperations;
		
		panelMain	= new JPanel ();
		panelMain.setLayout (new GridLayout(6,1));
		
		panelSearch	= new JPanel ();
		panelSearch.setLayout (new FlowLayout());
		tfSearchBuy =(new JTextField("Search", iScreenWidth/40));
		tfSearchBuy.setVisible (true);
		bSearchBuy		= new JButton("Search");
		bSearchBuy.setVisible (true);
		panelSearch.add (tfSearchBuy);
		panelSearch.add (bSearchBuy);
		
		// Table view Panel
		panelTable = new JPanel ();
		panelTable.setLayout (new GridLayout ());
		
		tableForSearchBuy = (new JTable ()
		{
			// Define the editable columns of the table here
			private static final long serialVersionUID = 1L;
					
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return column==5 ? true : false;
		    }
		});
		
		scrollPaneForSearchBuy = (new JScrollPane (tableForSearchBuy));
		panelTable.add (scrollPaneForSearchBuy);
		tableForSearchBuy.setVisible (false);
		scrollPaneForSearchBuy.setVisible (false);
		
		// Add To model.Cart view Panel - Part of Buy Operations
		panelBuyOperations	= new JPanel ();
		panelBuyOperations.setLayout (new FlowLayout ());
		bAddToCartBuy = (new JButton("Add To model.Cart"));
		bAddToCartBuy.setVisible(false);
		panelBuyOperations.add (bAddToCartBuy);
		
		panelMain.add (panelSearch);
		panelMain.add (panelTable);
		panelMain.add (panelBuyOperations);

		return panelMain;
	}
	
	private  JPanel makeRentPanel (String pID)
	{	
		JPanel	panelMainRent;
		JPanel	panelSearchRent;
		JPanel	panelTableRent;
		JPanel	panelRentOperationsRent;
		
		panelMainRent	= new JPanel ();
		panelMainRent.setLayout (new GridLayout(6,1));
		
		// Add the elements here
		// Search Panel
		panelSearchRent	= new JPanel ();
		panelSearchRent.setLayout (new FlowLayout());
		tfSearchRent = new JTextField("Search", iScreenWidth/40);
		tfSearchRent.setVisible (true);
		bSearchRent		= new JButton("Search");
		bSearchRent.setVisible (true);
		panelSearchRent.add (tfSearchRent);
		panelSearchRent.add (bSearchRent);
		
		// Table view Panel
		panelTableRent		= new JPanel ();
		panelTableRent.setLayout (new GridLayout ());
		
		tableForSearchRent = (new JTable ()
		{
			// Define the editable columns of the table here
			private static final long serialVersionUID = 1L;
					
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return column==5 ? true : false;
		    }
		});
		
		scrollPaneForSearchRent = (new JScrollPane (tableForSearchRent));
		panelTableRent.add (scrollPaneForSearchRent);
		tableForSearchRent.setVisible (false);
		scrollPaneForSearchRent.setVisible (false);
		
		// Add To model.Cart view Panel - Part of Rent Operations
		panelRentOperationsRent	= new JPanel ();
		panelRentOperationsRent.setLayout (new FlowLayout ());
		bAddToCartRent = (new JButton("Add To model.Cart"));
		bAddToCartRent.setVisible(false);
		panelRentOperationsRent.add (bAddToCartRent);
		
		panelMainRent.add (panelSearchRent);
		panelMainRent.add (panelTableRent);
		panelMainRent.add (panelRentOperationsRent);

		return panelMainRent;
	}
	
	// Function to set the layout of the window - Sell
	private  JPanel makeSellPanel (String pID)
	{
		JPanel	panel	= new JPanel ();
		panel.setLayout(null);
		JLabel title = new JLabel("Enter Details of model.Item to be Sold/Rented");
		title.setFont(new Font("Chalkboard", 1, 28));
		title.setBounds(400, -30,800,100);
		
		JLabel itemName  = new JLabel("Enter model.Item Name:");
		itemName.setBounds(10, 50, 300, 30);
		JLabel itemDesc = new JLabel("Enter Desciption:");
		itemDesc.setBounds(10, 100, 300, 30);
		JLabel price = new JLabel("Enter the Price:");
		price.setBounds(10, 150, 300, 30);
		JLabel category = new JLabel("Select Category");
		category.setBounds(10, 200, 300, 30);
		JLabel quantity = new JLabel("Enter quantity");
		quantity.setBounds(10, 250, 300, 30);
		
		JTextField itemNameText = new JTextField();
		itemNameText.setBounds(200, 50, 300, 30);
		JTextField itemDescText = new JTextField();
		itemDescText.setBounds(200, 100, 300, 30);
		JTextField priceText = new JTextField();
		priceText.setBounds(200, 150, 300, 30);
		JComboBox<String> cb = new JComboBox<String>(Constant.TYPES);
		cb.setBounds(200, 200, 300, 30);
		JTextField quantityText = new JTextField();
		quantityText.setBounds(200, 250, 300, 30);
		
		JRadioButton sale = new JRadioButton("Sale");
		sale.setBounds(100, 300, 100, 30);
		JRadioButton rent = new JRadioButton("Rent");
		rent.setBounds(200, 300, 100, 30);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(sale);
		buttonGroup.add(rent);
				
		JButton addItem = new JButton("ADD");
		addItem.setEnabled(true);
		addItem.setBounds(200, 450, 100, 30);
		
		JLabel toDateForRentText  = new JLabel("Enter Rent start date:");
		toDateForRentText.setBounds(10, 350, 300, 30);
		JLabel fromDateForRentText = new JLabel("Enter Rent End date:");
		fromDateForRentText.setBounds(10, 400, 300, 30);
		JTextField fromDateForRent = new JTextField();
		fromDateForRent.setBounds(200, 350, 100, 30);
		fromDateForRent.setVisible(false);
		JTextField toDateForRent = new JTextField();
		toDateForRent.setBounds(200, 400, 100, 30);
		toDateForRent.setVisible(false);
		toDateForRentText.setVisible(false);
		fromDateForRentText.setVisible(false);
		
		rent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toDateForRent.setVisible(true);
				fromDateForRent.setVisible(true);
				toDateForRentText.setVisible(true);
				fromDateForRentText.setVisible(true);
			}
		});
		
		sale.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toDateForRent.setVisible(false);
				fromDateForRent.setVisible(false);
				toDateForRentText.setVisible(false);
				fromDateForRentText.setVisible(false);
			}
		});

		panel.add(title);
		panel.add(itemName);
		panel.add(itemDesc);
		panel.add(price);
		panel.add(category);
		panel.add(quantity);
		panel.add(itemNameText);
		panel.add(itemDescText);
		panel.add(priceText);
		panel.add(cb);
		panel.add(quantityText);
		panel.add(addItem);
		panel.add(rent);
		panel.add(sale);
		
		panel.add(fromDateForRent);
		panel.add(toDateForRent);
		panel.add(toDateForRentText);
		panel.add(fromDateForRentText);
		
		addItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				Item itemobj = new Item();
				itemobj.item_name = itemNameText.getText();
				itemobj.user_name = "1";
				itemobj.price = Integer.parseInt(priceText.getText());
				itemobj.category = cb.getSelectedItem().toString();
				itemobj.quantity = Integer.parseInt(quantityText.getText());
				itemobj.description = itemDescText.getText();
				itemobj.post_date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
				itemobj.availability = Integer.parseInt(quantityText.getText());
				itemobj.forSale = sale.isSelected();
				itemobj.forRent = rent.isSelected();
				//itemobj.rentStartDate = new SimpleDateFormat("yyyy-MM-dd").format();
				DbOperationHelper.addItem(itemobj);
			}
		});
		
		return panel;
	}
	
	// Function to set the layout of the window - model.Cart
	private  JPanel makeCartPanel (String pID)
	{
		JPanel		panelMain;
		JPanel		panelTableCart;
		JPanel		panelCartOperations;
		
		panelMain	= new JPanel ();
		panelMain.setLayout (new GridLayout (6,1));
		
		// Add the elements here
		// Table view Panel
		panelTableCart = new JPanel ();
		panelTableCart.setLayout (new GridLayout());
		
		tableForCart =  (new JTable ()
		{
			// Define the editable columns of the table here
			private static final long serialVersionUID = 1L;
				
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
		    }		
		});
		
		scrollPaneForCart = (new JScrollPane (tableForCart));
		panelTableCart.add (scrollPaneForCart);
		tableForCart.setVisible (true);
		scrollPaneForCart.setVisible (true);
		
		panelCartOperations	= new JPanel ();
		panelCartOperations.setLayout (new FlowLayout());
		bCheckout =  (new JButton ("model.Checkout"));
		bCheckout.setVisible (true);
		panelCartOperations.add (bCheckout);
		bremoveFromCart = new JButton("Remove Items from model.Cart");
		bremoveFromCart.setVisible(true);
		panelCartOperations.add(bremoveFromCart);
		
		panelMain.add (panelTableCart);
		panelMain.add (panelCartOperations);
		
		bCheckout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					createItemIdlistForCheckout();
					manageItemObj.updateItemsAfterCheckout(itemIdQuantityUpdateAfterCheckout);
					manageItemObj.removeItemsFromCart(listItemIdToCheckout);
					manageItemObj.addItemsToCheckoutTable(itemIdQuantityUpdateAfterCheckout);
					populateCartGrid();
					populateBuyGrid(manageItemObj.displaySearch("", Constant.BUY));
					populateRentGrid(manageItemObj.displaySearch("", Constant.RENT));
				} 
				catch (SQLException e2) 
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}				
			}
		});
		
		return panelMain;
	}
	
	
	private void createItemIdlistForCheckout()
	{
		itemIdQuantityUpdateAfterCheckout.clear();
		listItemIdToCheckout.clear();
		int [] iaIndeces;
		try
		{
			iaIndeces	= tableForCart.getSelectedRows();
			for (int iCount : iaIndeces)
			{
				try
				{
					int itemId = Integer.valueOf (tableForCart.getValueAt (iCount, Constant.ITEM_ID_INDEX).toString ());
					itemIdQuantityUpdateAfterCheckout.put(itemId,Integer.valueOf (tableForCart.getValueAt (iCount, 1).toString ()));
					listItemIdToCheckout.add(itemId);
					
					if(itemIdQuantityForBuy.containsKey(itemId))
					{
						itemIdQuantityForBuy.remove(itemId);
					}
					if(itemIdQuantityForRent.containsKey(itemId))
					{
						itemIdQuantityForRent.remove(itemId);
					}
					
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

