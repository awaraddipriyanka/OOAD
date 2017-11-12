package Controller.database;

import java.sql.*;
//PersistanceStorageBroker

public class DB_management
{	
	private Connection connection = null;

	public void openConn() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver"); 
		connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/ooad","root","password");
	}
	
	protected ResultSet execute(String psSQL) throws SQLException
	{
		Statement statement = connection.createStatement();
		return statement.executeQuery(psSQL);
	}

	protected void insert(String sql) throws SQLException
	{
        Statement statement = connection.createStatement();
		statement.execute(sql);
	}
	
	protected void updateOrDelete(String sql) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate(sql);
	}
}
