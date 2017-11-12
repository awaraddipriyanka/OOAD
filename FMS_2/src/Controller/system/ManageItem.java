package Controller.system;

import Controller.database.DB_management;
import Controller.database.DbOperationHelper;
import model.Cart;
import model.Constant;
import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageItem
{
	protected Map<Integer,Integer> cartTable_ItemIdToQuantity;
	protected Map<Integer,Integer> itemTable_ItemIdToQuantity;
	
	public ManageItem()
	{
		try {
			cartTable_ItemIdToQuantity = new HashMap<Integer,Integer>();
			itemTable_ItemIdToQuantity = new HashMap<Integer,Integer>();
			populateInitialCartData();
			populateInitialItemData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateInitialItemData() throws SQLException
	{
		ResultSet rs = DbOperationHelper.getItems();
		while(rs.next())
		{
			itemTable_ItemIdToQuantity.put(Integer.parseInt(rs.getObject("item_id").toString()), 
					   Integer.parseInt(rs.getObject("quantity").toString()));
		}
	}
	
	public void populateInitialCartData() throws SQLException
	{
		ResultSet rs = DbOperationHelper.getCartItems();
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
			rs = DbOperationHelper.execute(sql);
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
			Cart cart = new Cart(entry.getKey(), userId, entry.getValue(), forSale == Constant.BUY);
			if(cartTable_ItemIdToQuantity.containsKey(entry.getKey())
					&& (cartTable_ItemIdToQuantity.get(entry.getKey())) != cart.getQuantity()) {
					DbOperationHelper.update(cart);
			} else {
				DbOperationHelper.insert(cart);
			}
		}
	}
	
	public void removeItemsFromCart(List<Integer> listItemId) throws SQLException
	{
		int userId = 1;
		for(int itemId : listItemId ) {
			DbOperationHelper.deleteCartObject(itemId, userId);
		}
	}
	
	public void updateItemsAfterCheckout(Map<Integer,Integer> checkoutMap) throws SQLException
	{
		for(Map.Entry<Integer, Integer> entry : checkoutMap.entrySet()) {
			DbOperationHelper.updateItemQuantity(entry.getKey(),entry.getValue());
		}
	}
	
	public void addItemsToCheckoutTable(Map<Integer,Integer> checkoutMap)
	{
		
	}
}
