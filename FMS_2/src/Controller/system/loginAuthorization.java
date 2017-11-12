package Controller.system;

import Controller.database.DbOperationHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class loginAuthorization {
	private String ssUserID = "";

	public loginAuthorization() {
	}
	
	public boolean loginValidation (String psUserID, String psPassword) throws SQLException
	{
		String sql = "select * from user where user_id = " +
					 "\"" +psUserID + "\"" + "and password = "+
					 "\"" +psPassword + "\"" + ";";
		
		ResultSet rsResult = DbOperationHelper.execute(sql);

		try
		{
			while (rsResult.next())
			{
				ssUserID = rsResult.getString(4);	// Column 1 - model.User ID
			}
			
			if (ssUserID.trim ().equals(""))
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
		
		return !ssUserID.equals("");
	}
	
	public String getUserId()
	{
		return ssUserID;
	}
}
