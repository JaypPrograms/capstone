package capstone.src;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone CreateAccount class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/

public class CreateAccount extends DbConnection{
    //instance variable
    private int accountNumber;
    private String lastname, firstname, address;

    /*creating an object to use the method inside this class. I choose to create an object than using inheritance to easily 
    determine where to edit the method incase I have to modify something in that method*/
    ProgramMethods programTools = new ProgramMethods();

    //method of adding account
    
    public void addAccount( ) {
        boolean isExisting;//local variable that will be use in the this method
        programTools.clearScreen();//method to clear screen
        
        //user input that will be check if the account is existing or not.
        this.firstname = programTools.stringInput("\nPlease Enter your Firstname: ");
        this.lastname = programTools.stringInput("Please Enter your Lastname: ");
        isExisting = programTools.checkAccount( "account", this.firstname, this.lastname);//calling a method that check if the account is existing in the database or not.
        if (isExisting) {
            System.out.println("Account is already existing, Please use the existing account to create policy.");//if existing
        } else {//if not existing
            this.address = programTools.stringInput("Please Enter your address: ");//user inpuut for additional details
            this.accountNumber = programTools.numberRandom(999, 9000, "account", "AccountNum");//calling a method randomizer to create a ramdom number for the account number.
            System.out.println(returnResult());//output for user input
            System.out.println("please take note your details if you want to save.");
            choose = programTools.decision( "Do you want to save this account?[Y]yes/[N]No: ");//user input that return boolean that will be set to the local variable.
            if (choose) {
                programTools.querySql(accountStatement());//running a method that will save the data to the database.
                System.out.println("Data successfully added!!");
            }else System.out.println("Data didn't save!!");//in case the user choose not to save the data.
        }
        programTools.confirmation();
    }

    //method that return Statement for sql query
    public String accountStatement() {
        return "INSERT INTO account (AccountNum, LastName, FirstName, Address) VALUES ("
                + this.accountNumber + ",'" +
                this.lastname + "' ,'" +
                this.firstname + "' ,'" +
                this.address +
                "')";
    }
    
    //method that return the user inputed.
    public String returnResult() {
        return "\n===================Account details====================\n" +
                "Account number: " + this.accountNumber +
                "\nFull name:    " + this.firstname + " " + this.lastname +
                "\nAddress:      " + this.address +
                "\n======================================================";
    }
    
    //method for search account in the program.
    public void searchAccount() {
        //local variable to be use in the program.
        ResultSet resultSet, resultSet1, resultSet2;
        boolean isExisting;

        programTools.clearScreen();//method for clear screen

        //user input for the name and to be check in the database if existing or not
        this.firstname = programTools.stringInput("\nPlease Enter your Firstname: ");
        this.lastname = programTools.stringInput("Please Enter your Lastname: ");
        isExisting = programTools.checkAccount( "account", this.firstname, this.lastname);//method that check if the account existing and return boolean value.
        if (!isExisting) {
            System.out.println("Account is Doesn't existing.");//in case the account doesn't exist.
        } else {
            int accNumHolder;
            String statement = "SELECT * FROM account WHERE FirstName ='" + this.firstname +
                    "' AND LastName = '" + this.lastname + "'"; //statement to be use in Sql query.
            try {
                resultSet = programTools.resultStatement(statement);//method that will return the resultset of the sql query.
                while (resultSet.next()) {
                    //set the value of the remaining instance variable.
                    this.accountNumber = resultSet.getInt("AccountNum");
                    this.address = resultSet.getString("Address");
                }
                //methods for showing the value the user searched and clearing the screen.
                programTools.clearScreen();
                System.out.println(returnResult());

                //creating statement that will search for policy connected to the account.
                statement = "SELECT * FROM policy WHERE AccountNum =" + this.accountNumber;
                resultSet1 = programTools.resultStatement(statement);//method that will check the database for result and then return to be set in the local variable.
               
                //user input output.
                System.out.println("\nPolicy owned by that account");
                System.out.println("======================================================");
                while (resultSet1.next()) {
                    accNumHolder = resultSet1.getInt("AccountNumHolder");

                    //creating a object with constructor method that will set the value of instance variable of that object.
                    Policy policies = new Policy(resultSet1.getInt("PolNumber"),
                            resultSet1.getInt("AccountNum"),
                            resultSet1.getString("EffDate"),
                            resultSet1.getString("ExpDate"),
                            resultSet1.getDouble("TotalPremium"));
                    //output of policy that connected to the searched account.
                    System.out.print(policies.policyOutput() + "\n\n");

                    statement = "SELECT * FROM polholder WHERE polHolderNum =" + accNumHolder;
                    resultSet2 = programTools.resultStatement(statement);
                    System.out.print("********************Policy holder*********************");
                    while (resultSet2.next()) {
                        //creating a object with constructor method that will set the value of instance variable of that object.
                        PolicyHolder policyHolder = new PolicyHolder(resultSet2.getString("FirstName"),
                                resultSet2.getString("Lastname"),
                                resultSet2.getString("BirthDate"),
                                resultSet2.getString("Address"),
                                resultSet2.getString("LicenseNum"),
                                resultSet2.getString("licenseDate"),
                                resultSet2.getInt("polHolderNum"));
                        
                        //output of policy holder connected to the policy.
                        System.out.print(policyHolder.polHolderOutput() + "\n");
                    }
                    System.out.println("======================================================\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        programTools.confirmation();//method that temporally pause the program.
    }
}
