import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageItem
{
	DB_management oDBM = new DB_management ();
	protected Map<Integer,Integer> cartTable_ItemIdToQuantity;
	protected Map<Integer,Integer> itemTable_ItemIdToQuantity;
	
	ManageItem()
	{
		try {
			oDBM.openConn();
			cartTable_ItemIdToQuantity = new HashMap<Integer,Integer>();
			itemTable_ItemIdToQuantity = new HashMap<Integer,Integer>();
			populateInitialCartData();
			populateInitialItemData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateInitialItemData() throws SQLException
	{
		Item itemObj = new Item();
		ResultSet rs = itemObj.getItems(oDBM);
		while(rs.next())
		{
			itemTable_ItemIdToQuantity.put(Integer.parseInt(rs.getObject("item_id").toString()), 
					   Integer.parseInt(rs.getObject("quantity").toString()));
		}
	}
	
	public void populateInitialCartData() throws SQLException
	{
		Cart cartObj = new Cart();
		ResultSet rs = cartObj.getCartItems(oDBM);
		while(rs.next())
		{
			cartTable_ItemIdToQuantity.put(Integer.parseInt(rs.getObject("item_id").toString()), 
					   Integer.parseInt(rs.getObject("quantity").toString()));
		}
	}
	
	public void updateCartQuantity(Map<Integer,Integer> buyMap, Map<Integer,Integer> rentMap)
	{
		for(Map.Entry<Integer, Integer> entry : buyMap.entrySet())
		{
			if(itemTable_ItemIdToQuantity.containsKey(entry.getKey()))
			{
				if(entry.getValue()> itemTable_ItemIdToQuantity.get(entry.getKey()))
				{
					buyMap.put(entry.getKey(),itemTable_ItemIdToQuantity.get(entry.getKey()));
				}
			}
		}
		for(Map.Entry<Integer, Integer> entry : rentMap.entrySet())
		{
			if(itemTable_ItemIdToQuantity.containsKey(entry.getKey()))
			{
				if(entry.getValue()> itemTable_ItemIdToQuantity.get(entry.getKey()))
				{
					buyMap.put(entry.getKey(),itemTable_ItemIdToQuantity.get(entry.getKey()));
				}
			}
		}
	}
	
	public ResultSet displaySearch(String Key, int isBuy)
	{
		ResultSet rs=null;
		String sql ="";
		if (Key.equals ("Search") || Key.equals (""))
		{
			sql	= "select item_id,item_name, price,availability,item_id from item ";
			if(isBuy == Constant.BUY)
			{
				sql = sql+" where forSale = true;";
			}
			else
			{
				sql = sql+" where forRent = true;";
			}
		}
		
		else
		{
			sql	= "select item_id,item_name, price,availability,item_id from item where item_name like '%"+Key+"%' ";
			if(isBuy == Constant.BUY)
			{
				sql = sql+" and forSale = true;";
			}
			else
			{
				sql = sql+" and forRent = true;";
			}
		}
		try {
			rs = oDBM.Execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public void addToCart(Map<Integer,Integer> itemIdQuantity, int forSale) throws SQLException
	{
		int userId=1;
		for(Map.Entry<Integer, Integer> entry : itemIdQuantity.entrySet())
		{
			Cart cartobj = new Cart();
			cartobj.itemId = entry.getKey();
			cartobj.userId = userId;
			cartobj.quantity = entry.getValue();
			cartobj.forSale = (forSale == Constant.BUY? true : false);
			if(cartTable_ItemIdToQuantity.containsKey(entry.getKey()))
			{
				if((Integer)(cartTable_ItemIdToQuantity.get(entry.getKey())) != cartobj.quantity)
				{
					cartobj.update(cartobj, oDBM);
				}
			}
			else
			{
				cartobj.insert(cartobj, oDBM);
			}
		}
	}
	
	public void removeItemsFromCart(List<Integer> listItemId) throws SQLException
	{
		int userId = 1;
		for(int itemId : listItemId )
		{
			Cart cartobj = new Cart();
			cartobj.itemId = itemId;
			cartobj.userId = userId;
			cartobj.delete(cartobj, oDBM);
		}	
	}
	
	public void updateItemsAfterCheckout(Map<Integer,Integer> checkoutMap) throws SQLException
	{
		for(Map.Entry<Integer, Integer> entry : checkoutMap.entrySet())
		{
			Item item = new Item();
			item.updateQuantity(entry.getKey(),entry.getValue(), oDBM);
		}	
	}
	
	public void addItem(Item item)
	{
		item.addItem(item, oDBM);
	}
	
	public void addItemsToCheckoutTable(Map<Integer,Integer> checkoutMap)
	{
		
	}
	
	public ResultSet getCartItems() throws SQLException
	{
		Cart cartObj = new Cart();
		ResultSet rs = cartObj.getCartItems(oDBM);
		return rs;
	}
}
