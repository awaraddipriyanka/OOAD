import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Item {
	int item_id;
	String item_name;
	String user_name;
	int price;
	String category;
	int quantity;
	String description;
	String post_date;
	boolean forSale;
	boolean forRent;
	int availability;
	Date rentStartDate;
	Date rentEndDate;
	
	public void addItem(Item item, DB_management oDBM)
	{
		String sql = " Insert into item(item_name, user_name,price, category,"+
					 " quantity, description, post_date,availability, forsale,forrent, rentstartdate, rentenddate)"+
					 " values( "+
					 " '"+item.item_name+"',"+
					 " '"+item.user_name+"',"+
					 " "+item.price+","+
					 " '"+item.category+"',"+
					 " "+item.quantity+","+
					 " '"+item.description+"',"+
					 " '"+item.post_date+"',"+
					 " "+item.availability+","+
					 " "+item.forSale+","+
					 " "+item.forRent+","+
					 " "+item.rentStartDate+","+
					 " "+item.rentEndDate+""+
					 ")";	
		try {
			oDBM.insert(sql);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getItems(DB_management dbObj) throws SQLException
	{
		String sql = " select * from item where quantity >0 ";
		return dbObj.Execute(sql);
	}
	
	public void updateQuantity(int itemId, int quatityTaken,DB_management dbObj) throws SQLException
	{
		String sql = " update item set availability = quantity - "+quatityTaken+
					 " where item_id = "+itemId+";";
		dbObj.updateOrDelete(sql);
	}
	
	public void updateQuantityOfItem(Item item, boolean increment, DB_management oDBM)
	{
		String sql = "";	
		try {
			oDBM.Execute(sql);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
