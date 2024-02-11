package capstone.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Date;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone DbConnection class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/

public class DbConnection {
    
    //object and instance variable that will be use in the program, this method will be inherited by other classes.
    protected Scanner scan = new Scanner(System.in);
    protected Random rand = new Random();
    protected Date dt = new Date();
    protected Connection con = null;
    protected ResultSet rs = null;
    protected Statement sqlStatement;
    protected Calendar calendar = Calendar.getInstance();;
    protected LocalDate localDate = LocalDate.now();

    protected String url = "jdbc:mysql://localhost:3306/";
    protected String url1 = "jdbc:mysql://localhost:3306/capstone";
    protected String user = "root";
    protected String password = "";
    protected int currentyear = localDate.getYear();
    protected boolean checkAccNumber = false, choose;

    //method to connect to database.
    public void connectDb() {
        try {
            con = DriverManager.getConnection(url, user, password);//connection to SQL database
        }
         catch (Exception ex) {
            ex.printStackTrace();//incase the program has exception, this will catch it.
        } 
        
            finally {
            if (rs != null) {// for closing the resultset.
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            }
            if (con != null) {//for closing the connection
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            }
        }
    }
}
