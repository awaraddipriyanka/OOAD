import java.sql.ResultSet;
import java.sql.SQLException;

public class Cart {
	int itemId;
	int userId;
	int quantity;
	boolean forSale;
	DB_management dbObj;
	
	public void insert(Cart c,DB_management dbObj) throws SQLException
	{
		String sql = " Insert into cart(user_id, item_id, quantity, forSale) values( "+
					 " "+c.userId+","+
					 " "+c.itemId+","+
					 " "+c.quantity+","+
					 " "+c.forSale+");";
		dbObj.insert(sql);
	}
	
	public void update(Cart c,DB_management dbObj) throws SQLException
	{
		String sql = " update cart set quantity = "+c.quantity+
					 " where item_id = "+c.itemId+
					 " and user_id = "+c.userId+";";
		dbObj.updateOrDelete(sql);
	}
	
	public void delete(Cart c,DB_management dbObj) throws SQLException
	{
		String sql = " delete from cart where item_id = "+c.itemId+" and user_id = "+c.userId+";";
		dbObj.updateOrDelete(sql);
	}
	
	public ResultSet getCartItems(DB_management dbObj) throws SQLException
	{
		String sql = " select * from cart where user_id = "+
					""+1+";";
		return dbObj.Execute(sql);
	}
}
