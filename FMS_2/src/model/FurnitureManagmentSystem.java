package model;

import Controller.database.DB_management;
import view.FMS_Layout;

public class FurnitureManagmentSystem {
	public static void main(String[] args) {
		try
		{
			DB_management oDBM = new DB_management ();
			oDBM.openConn();
			FMS_Layout layout = new FMS_Layout(oDBM);
			layout.set_main_layout("Furniture Managment System");
		}
		catch(Exception e)
		{
			System.out.println("Error with DB connection");
			e.printStackTrace();
		}
	}
}
