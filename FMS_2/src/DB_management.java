import java.sql.*;
//PersistanceStorageBroker

class DB_management
{	
	Connection	connection	= null;
	Statement	statement	= null;
	PreparedStatement preparedStatement = null;
	ResultSet	rsResult	= null;
	
		
	protected void openConn() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver"); 
		connection	= DriverManager.getConnection ("jdbc:mysql://localhost:3306/ooad","root","password");
	}
	
	protected ResultSet Execute(String psSQL) throws SQLException
	{
		statement	= connection.createStatement();
		rsResult	= statement.executeQuery(psSQL);
		return	rsResult;
	}
	protected void insert(String sql) throws SQLException
	{
		statement	= connection.createStatement();
		statement.execute(sql);
	}
	
	protected void updateOrDelete(String sql) throws SQLException
	{
		preparedStatement	= connection.prepareStatement(sql);
		preparedStatement.executeUpdate(sql);
	}
		
}
