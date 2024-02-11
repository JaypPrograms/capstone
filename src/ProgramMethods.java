package capstone.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.InputMismatchException;


/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone ProgramMethods class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/

public class ProgramMethods extends DbConnection {

    public void pasChoices() {//method for the main choices.
        System.out.println("================================================");
        System.out.println("|1. Create a new Customer Account              |");
        System.out.println("|2. Get a policy quote and buy the policy.     |");
        System.out.println("|3. Cancel a specific policy                   |");
        System.out.println("|4. File an accident claim against a policy.   |");
        System.out.println("|5. Search for a Customer account              |");
        System.out.println("|6. Search for and display a specific policy.  |");
        System.out.println("|7. Search for and display a specific claim    |");
        System.out.println("|8. Exit the PAS System                        |");
        System.out.println("================================================");
    }

    public void polHolderchoices() {//method for choices in creating policy holder
        System.out.println("================================================");
        System.out.println("|1. Create a new Policy Holder                 |");
        System.out.println("|2. Use a existing Policy Holder               |");
        System.out.println("|3. Use the Account as Policy Holder.          |");
        System.out.println("|0. To back.                                   |");
        System.out.println("================================================");
    }

    public void cancelPolChoices() {//method for choices in creating a policy
        System.out.println("================================================");
        System.out.println("|1. Remove specific policy.                    |");
        System.out.println("|2. update expiration date to earlier date.    |");
        System.out.println("|0. To back.                                   |");
        System.out.println("================================================");
    }

    public String typeChoices() {//method for choices in selecting type of car and also return a string
        String result = "";
        int choice;
        while (result.length() < 2) {
            System.out.println("================================================");
            System.out.println("|Please select the type of the vehicle         |");
            System.out.println("|[1].4-door sedan                              |");
            System.out.println("|[2].2-door sports car                         |");
            System.out.println("|[3].SUV                                       |");
            System.out.println("|[4].truck                                     |");
            System.out.println("================================================");
            choice = intTrycatch("Please enter car Type:");//user input 

            switch (choice) {
                case 1:
                    result = "4-door sedan";
                    break;
                case 2:
                    result = "2-door sports car";
                    break;
                case 3:
                    result = "SUV";
                    break;
                case 4:
                    result = "truck";
                    break;
                default:
                    System.out.println("You enter an invalid number, Please enter again.");
                    break;
            }
        }
        return result;
    }

    public String typeFuelType() {//method for choices in fuel type that return string.
        String result = "";
        int choice;
        while (result.length() < 2) {
            System.out.println("================================================");
            System.out.println("|Please select the Fuel Type of the vehicle    |");
            System.out.println("|[1].Diesel                                    |");
            System.out.println("|[2].Electric                                  |");
            System.out.println("|[3].Petrol                                    |");
            System.out.println("================================================");
            choice = intTrycatch("Please enter car Fuel Type:");

            switch (choice) {
                case 1:
                    result = "Diesel";
                    break;
                case 2:
                    result = "Electric";
                    break;
                case 3:
                    result = "Petrol";
                    break;
                default:
                    System.out.println("You enter an invalid number, Please enter again.");
                    break;
            }
        }
        return result;
    }

    public int intTrycatch(String outprint) {//method for user int user input with validation in case user inputer string insted of int
        int number = 0;
        boolean loop = false;
        while (!loop) {
            try {
                System.out.print(outprint);//output the value inside the parameter.
                number = scan.nextInt();
                if (number >= 0)
                    loop = true;
                else
                    System.out.println("Invalid input!! you enter a negative number.");//if the user inputed negative number
                scan.nextLine();

            } catch (InputMismatchException e)// catch if the user input other data type than int
            {
                System.out.println("You enter an invalid input, please input a number.");
                scan.nextLine();
            }
        }
        return number;
    }

    public int negativeCheck( String outprint) {//method that check if the user inputed negative number.
        int number = 0;
        while (number <= 0) {
            number = intTrycatch(outprint);
            if (number <= 0)
                System.out.println("Invalid input!! You enter a number less than zero");
        }
        return number;
    }

    public double doubleTrycatch(String outprint) {//method for user input for double.
        double number = 0;
        boolean loop = false;
        while (!loop) {
            try {
                System.out.print(outprint);
                number = scan.nextDouble();
                if(number>0){
                loop = true;
                }
                else{ 
                    System.out.println("You enter an negative number, please input a positive.");
                }
                scan.nextLine();
            } catch (InputMismatchException e)// catch if the user input other data type than double
            {
                System.out.println("You enter an invalid input, please input a number.");
                scan.next();
            }
        }
        return number;
    }

    public String stringInput(String outprint) {//method for user input string that has validation.
        String result="";
        boolean isLoop=false;
        while(!isLoop){
            System.out.print(outprint);
            result = scan.nextLine();
            if (result.equals("")) {//if the user inputed blank
                System.out.println("The system doesn't consider blank input!!");
            }
            else if(result.contains("\\")){//if the user inputed  backslash
                System.out.println("The system doesn't consider backslash!!");
            }

            else if(result.length()>225){//if the user inputed a long string.
                System.out.println("Invalid input!! maximum letter is 225");
            }
            else{
                isLoop=true;
            }
        }
        
        return result;
    }

    public String dateCheck( String print) {//method that check if the user inputed a correct date.

        //local variable to be use in this method.
        boolean checkDate = false;
        String datereturn = "";
        while (!checkDate) {
            try {
                String effDate;

                effDate = stringInput(print);//user input for date.
                int year, month, day, index, start;

                index = effDate.indexOf('-');
                day = Integer.parseInt(effDate.substring(0, index));//getting the day date.

                start = index + 1;
                index = effDate.indexOf('-', start);
                month = Integer.parseInt(effDate.substring(start, index));//getting the month day

                index++;
                year = Integer.parseInt(effDate.substring(index));//getting the year date.

                if (year > Calendar.YEAR) {//validation if 
                    datereturn = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.of(year, month, day));
                    checkDate = true;
                }
            } catch (Exception e) {
                System.out.println("Incorrect Date. Please type in valid format. ");//in case user inputed a wrong format of date.
            }
        }
        return datereturn;
    }

    public double dlxReturn(int year) {//method that set a value in local variable and return it.
        double vecPriceFactor = 0;
        if (year <= 1) {
            vecPriceFactor = 0.01;
        } else if (year > 1 && year <= 3) {
            vecPriceFactor = 0.008;
        } else if (year > 3 && year <= 5) {
            vecPriceFactor = 0.007;
        } else if (year > 5 && year <= 10) {
            vecPriceFactor = 0.006;
        } else if (year > 10 && year <= 15) {
            vecPriceFactor = 0.004;
        } else if (year > 15 && year <= 20) {
            vecPriceFactor = 0.002;
        } else if (year > 20 && year <= 40) {
            vecPriceFactor = 0.001;
        }
        return vecPriceFactor;
    }

    public int numberRandom(int Start, int end, String tableName, String column) {//method that randomize a number.
        int number = 0;

        while (!checkAccNumber) {
            int accountFind = 0;
            number = Start + rand.nextInt(end);//random a number
            //statement that check if the random number is existing in the database or not.
            String statement = "select " + column + " from " + tableName + " where " + column + "= " + number + "";

            try {
                con = DriverManager.getConnection(url1, user, password);//making a connection 
                sqlStatement = con.createStatement();//use for sql query/
                ResultSet resultSet = sqlStatement.executeQuery(statement);//getting the result

                while (resultSet.next()) {//if result is not null
                    accountFind++;
                }
                if (accountFind == 0) {//if null
                    checkAccNumber = true;
                }
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
        checkAccNumber = false;
        return number;
    }

    public boolean decision( String display) {//method that check the decision of the user and return a boolean value.

        //local variable to be use in this method.
        boolean choice = true, loop = false;
        String input = "";

        while (!loop) {//while user doesn't input a valid input
            input = stringInput(display);
            if (input.equalsIgnoreCase("y")) {
                loop = true;
            } else if (input.equalsIgnoreCase("n")) {
                loop = true;
                choice = false;
            } else {
                System.out.println("Invalid input!");
            }
        }
        System.out.println("");
        return choice;
    }

    public void querySql(String Statement) {//method that use for sql statement.
        try {
            con = DriverManager.getConnection(url1, user, password);//connection to the database.

            Statement sqlStatement = con.createStatement();//use for sql query.
            sqlStatement.executeUpdate(Statement);//query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDb() {//method that check if the database is existing or not
        boolean dbExist = false;
        try {
            //checking if the database is existing or not.
            con = DriverManager.getConnection(url, user, password);
            String dbName = "capstone";

            if (con != null) {

                rs = con.getMetaData().getCatalogs();//configure connection

                while (rs.next()) {
                    String catalogs = rs.getString(1);

                    if (dbName.equals(catalogs)) {//if database exist.
                        dbExist = true;
                    }
                }
            } else {
                System.out.println("unable to create database connection");
            }

            if (!dbExist) {//if database doesn't exist it will create a new database in the system.
                Connection conn = DriverManager.getConnection(url, user, password);//connection
                Statement stmt = conn.createStatement();

                String sql = "CREATE DATABASE " + dbName;
                stmt.executeUpdate(sql);

                //statement to be use in the query.
                String statement="CREATE TABLE account("+
                    "AccountNum int,"+
                    "LastName varchar(255),"+
                    "FirstName varchar(255),"+
                    "Address varchar(255)"+
                    ")";
                    querySql(statement);
                
                statement="CREATE TABLE claims("+
                "ClaimNumber varchar(255),"+
                "AccidentDate date,"+
                "AccAddress varchar(255),"+
                "DescOfAccident varchar(255),"+
                "DescOfDamage varchar(255),"+
                "RepairCost decimal(16,2),"+
                "PolNumber int"+
                ")";
                querySql(statement);

                statement="CREATE TABLE polholder("+
                "LastName varchar(255),"+
                "FirstName varchar(255),"+
                "BirthDate date,"+
                "Address varchar(255),"+
                "LicenseNum varchar(255),"+
                "licenseDate date,"+
                "polHolderNum int"+
                ")";
                querySql(statement);

                statement="CREATE TABLE policy("+
                "PolNumber int,"+
                "AccountNum int,"+
                "AccountNumHolder int,"+
                "EffDate date,"+
                "ExpDate date,"+
                "TotalPremium decimal(16,2)"+
                ")";
                querySql(statement);

                statement="CREATE TABLE vehicle("+
                "Brand varchar(255),"+
                "Model varchar(255),"+
                "Type varchar(255),"+
                "FuelType varchar(255),"+
                "Color varchar(255),"+
                "PurchasePrice decimal(16,2),"+
                "Premium decimal(16,2),"+
                "Year int,"+
                "polNumber int,"+
                "vehicleId int,"+
                "PlateNumber varchar(255)"+
                ")";
                querySql(statement);
                System.out.println("Database created successfully...");
            }
        } catch (Exception ex) {//in case there is an exception
            ex.printStackTrace();
        } finally {
            if (rs != null) {//if rs is not null, it will close it.
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {//if con is not null, it will close it.
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public String ageDate( String print) {//method that check the age depends on the date the user inputed and return it.
        //local variable use in this method.
        String date = " ";
        boolean loop = false;
        int age;
        while (!loop) {
            date = dateCheck( print);//user input for date
            LocalDate licenseDate = LocalDate.parse(date);//creating localdate to get the year the user inputed.
            age = currentyear - licenseDate.getYear();

            if (age >= 18)
                loop = true;//if true

            else
                System.out.println("Invalid date! The age must be atleast 18 years old and above.");
        }
        return date;
    }

    public String effDate( String print) {//method that check the effective date of the user input and return it.

        //local variable to be use in this method.
        String date = " ";
        boolean loop = false;
        while (!loop) {
            date = dateCheck( print);//user input for date.
            LocalDate licenseDate = LocalDate.parse(date);//creating localdate to get the year the user inputed.
            if (licenseDate.compareTo(LocalDate.now()) >= 0){  
                loop = true;//if true

            }else
                System.out.println("Invalid date! Date must not be not from the past");
        }
        return date;
    }

    public String withinEffDate( String print, String effDate, String currentExpDate) {
        String date = " ";
        boolean loop = false;
        while (!loop) {
            LocalDate localCurrentExpDate = LocalDate.parse(currentExpDate);
            date = dateCheck( print);
            LocalDate localDate = LocalDate.parse(date);
            LocalDate localEffDate = LocalDate.parse(effDate);

            if ((localDate.compareTo(localEffDate) >= 0) && (localDate.compareTo(localCurrentExpDate) <= 0) )
                loop = true;

            else
                System.out.println("Invalid date! Date must be within effective date");
        }
        return date;
    }

    public String licenseEffDate( String print) {//method that check if the license date is valid.

        //local variable that will be use in the method.
        String date = " ";
        boolean loop = false;
        while (!loop) {
            date = dateCheck( print);
            LocalDate licenseDate = LocalDate.parse(date);

            if (licenseDate.compareTo(LocalDate.now()) <=0)
                loop = true;//if true.

            else
                System.out.println("Invalid date! Date must not from the future.");
        }
        return date;
    }

    //method that check account if existing or not.
    public boolean checkAccount( String table, String firstName, String lastName) {
        //local variable that will be use in the program.
        boolean result = false;
        ResultSet resultSet;
        int accountFind = 0;

        //statement in checking in the database if the user input is exist or not.
        String statement = "SELECT * FROM "+table+" WHERE FirstName ='" + firstName +
                "' AND LastName = '" + lastName + "'";

        resultSet = resultStatement(statement);
        accountFind = resultInt(resultSet);
        if (accountFind > 0)
            result = true;//if true it will return a true value.

        return result;
    }

    //method that return a result set
    public ResultSet resultStatement(String statement) {
        ResultSet resultSet = null;
        try {

            //running the sql query and return it
            con = DriverManager.getConnection(url1, user, password);
            sqlStatement = con.createStatement();
            resultSet = sqlStatement.executeQuery(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int resultInt(ResultSet resultSet) { //method that return a value if the result is not null
        int result = 0;
        try {
            while (resultSet.next())
                result++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void confirmation() {//method to temporally pause the program
        System.out.print("Please enter to continue.");
        System.out.println("");
        scan.nextLine();
    }

    public void clearScreen(){//method that clear the screen.
        for(int index=0; index<50; index++){
            System.out.println("\n");
        }
    }
}