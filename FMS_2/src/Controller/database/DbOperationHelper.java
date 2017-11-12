package Controller.database;

import model.Cart;
import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbOperationHelper {
    private static DB_management dbObj = new DB_management();

    public static void insert(Cart cart) throws SQLException
    {
        String sql = " Insert into cart(user_id, item_id, quantity, forSale) values( "+
                " "+cart.getUserId()+","+
                " "+cart.getItemId()+","+
                " "+cart.getQuantity()+","+
                " "+cart.isForSale()+");";
        dbObj.insert(sql);
    }

    public static void update(Cart cart) throws SQLException
    {
        String sql = " update cart set quantity = "+cart.getQuantity()+
                " where item_id = "+cart.getItemId()+
                " and user_id = "+cart.getUserId()+";";
        dbObj.updateOrDelete(sql);
    }

    public static void deleteCartObject(int itemId, int userId) throws SQLException
    {
        String sql = " deleteCartObject from cart where item_id = "+ itemId+" and user_id = "+ userId +";";
        dbObj.updateOrDelete(sql);
    }

    public static ResultSet getCartItems() throws SQLException
    {
        String sql = " select * from cart where user_id = "+
                ""+1+";";
        return dbObj.execute(sql);
    }

    public static void addItem(Item item)
    {
        String sql = " Insert into item(item_name, user_name,price, category,"+
                " quantity, description, post_date,availability, forsale,forrent, rentstartdate, rentenddate)"+
                " values( "+
                " '"+item.getItem_name()+"',"+
                " '"+item.getUser_name()+"',"+
                " "+item.getPrice()+","+
                " '"+item.getCategory()+"',"+
                " "+item.getQuantity()+","+
                " '"+item.getDescription()+"',"+
                " '"+item.getPost_date()+"',"+
                " "+item.getAvailability()+","+
                " "+item.isForSale()+","+
                " "+item.isForRent()+","+
                " "+item.getRentStartDate()+","+
                " "+item.getRentEndDate()+""+
                ")";
        try {
            dbObj.insert(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getItems() throws SQLException
    {
        String sql = " select * from item where quantity >0 ";
        return dbObj.execute(sql);
    }

    public static void updateItemQuantity(int itemId, int quatityTaken) throws SQLException
    {
        String sql = " update item set availability = quantity - "+quatityTaken+
                " where item_id = "+itemId+";";
        dbObj.updateOrDelete(sql);
    }


    public void updateQuantityOfItem(Item item, boolean increment, DB_management oDBM)
    {
        String sql = "";
        try {
            oDBM.execute(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet execute(String sql) throws SQLException {
        return dbObj.execute(sql);
    }
}
