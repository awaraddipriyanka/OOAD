import java.sql.ResultSet;
import java.sql.SQLException;

public class loginAuthorization {
	
	DB_management 	dbObj = new DB_management();
	ResultSet 		rsResult	= null;
	static String 	ssUserID	= "";
	
	public loginAuthorization() {
		// TODO Auto-generated constructor stub
		try {
			dbObj.openConn();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean loginValidation (String psUserID, String psPassword) throws SQLException
	{
		String sql = "select * from user where user_id = " +
					 "\"" +psUserID + "\"" + "and password = "+
					 "\"" +psPassword + "\"" + ";";
		
		rsResult	= dbObj.Execute(sql);
		
		try
		{
			while (rsResult.next())
			{
				ssUserID = rsResult.getString(4);	// Column 1 - User ID
			}
			
			if (ssUserID.trim () == "")
			{
				ssUserID	= "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			ssUserID =  "";
		}
		
		ssUserID = ssUserID.trim ();
		
		if (ssUserID != "")
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getUserId()
	{
		return ssUserID;
	}
}
