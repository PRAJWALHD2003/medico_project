package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
	public static Connection mydbconnect()
	{
		Connection con=null;
		
		try
		{
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","harsha1234");
		}
		catch(Exception e)
		{
			System.out.print("Connection Failed");
		}
		if(con!=null)
		{
			System.out.print("Database connection success");
		}
		else
		{
			System.out.print("Database connection failed");

		}
		return con;
	}
}
