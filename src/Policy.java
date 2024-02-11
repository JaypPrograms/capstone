package capstone.src;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone Policy class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/

public class Policy extends DbConnection {

    //instance variable of the class
    private int polNumber, accountNum;
    private String effDate, expDate;
    private double totalPremium;

    /*creating an object to use the method inside this class. I choose to create an object than using inheritance to easily 
    determine where to edit the method incase I have to modify something in that method*/
    ProgramMethods programTools = new ProgramMethods();
    public Policy() {
    }

    //constructor method to set the value of instance variable.
    public Policy(int polNumber, int accountNum, String effDate, String expDate, double totalPremium) {
        this.polNumber = polNumber;
        this.accountNum = accountNum;
        this.effDate = effDate;
        this.expDate = expDate;
        this.totalPremium=totalPremium;
    }

    public void setTotalPremium(double totalPremium){
        this.totalPremium=totalPremium;
    }

    //method to buy a policy
    public void buyPolicy(Policy BuyPolicy, PolicyHolder polHolder) {
        boolean checkAccNumber = true, isPolicyChoice = true;//local variable that will be use in the program.
        programTools.clearScreen();//clear screen method.
        while (checkAccNumber) {
            //local variable to be use in the program.
            ResultSet resultSet;
            int polOption, accountFind = 0;
            this.accountNum = programTools.intTrycatch("\nPlease Enter Account Number, 0 to back: ");//user input.
            
            //creating a statement and checking it in the database if existing or not.
            String statement = " SELECT * FROM account WHERE AccountNum = " + this.accountNum + "";
            resultSet = programTools.resultStatement(statement);
            accountFind = programTools.resultInt(resultSet);//method that return a value if account exist

            //Nested if else, if the user input "0" the method will be terminated and also will continue if the account exist.
            if (this.accountNum != 0) {
                if (accountFind > 0) {
                    this.polNumber = programTools.numberRandom(99999, 900000, "policy", "PolNumber");//method that randomize number that will be set as policy number.
                    System.out.println("\nPolicy Creation.");
                    this.effDate = programTools.effDate( "Please enter effective date(DD-MM-YYYY): ");//user input

                    //creating a object to add 6 month to the date the user inputed and set to the instance variable.
                    LocalDate tempexpDate = LocalDate.parse(effDate).plusMonths(6);
                    this.expDate = DateTimeFormatter.ISO_LOCAL_DATE.format(tempexpDate);
                    
                    do {
                        programTools.clearScreen();//clear screen method.
                        System.out.println("\nPolicy holder creation.");
                        programTools.polHolderchoices();//policy holder action choices.
                        polOption = programTools.intTrycatch("Please enter your choice: ");

                        switch (polOption) {//switch case that will run depends on the user choices.
                            case 1:
                                polHolder.addPolholder(BuyPolicy);// add policy holder
                                break;
                            case 2:
                                polHolder.existingPolHolder(BuyPolicy);//use existing policy holder
                                break;
                            case 3:
                                polHolder.existingAccount(BuyPolicy,this.accountNum);//use the account as policy holder.
                                break;
                            case 0:
                                isPolicyChoice = false;//in case the user what to back.
                                break;
                            default:
                                System.out.println("You enter an invalid number, Please choice number in choices.");
                                break;
                        }
                        //if statement that will let the user choose if he/she want to back to main menu or add another policy.
                        if (polOption >= 1 && polOption <= 3) {
                            System.out.println("\nDo you want to add another policy?");
                            checkAccNumber = programTools.decision("Please enter [Y]to create another policy, [N]to go back to main menu: ");
                            isPolicyChoice = false;
                        }
                    } while (isPolicyChoice);
                } else {
                    System.out.println("Account number doesn't exist, Please enter a correct account number");
                }
            } else {
                checkAccNumber = false;//if the user input '0'
            }
        }
    }

    //method that search policy.
    public void searchPolicy() {
        //local variable to be use in the method.
        ResultSet resultSet, resultSet1, resultSet2;
        int accountFind = 0;
        boolean checkAccNumber = true;

        programTools.clearScreen();//clear screen method.
        while (checkAccNumber) {//while that will continue to loop until the user choose to back or go back to main menu.
        this.polNumber = programTools.intTrycatch("Please Enter Policy Number, 0 to back to main menu: ");//user input and will be use to check if policy exist or not.
        
        //adding statement to be use in sql statement to check if policy is existing or not.
        String statement = " SELECT * FROM policy WHERE PolNumber = " + this.polNumber + "";
        resultSet = programTools.resultStatement(statement);
        accountFind = programTools.resultInt(resultSet);//return a value if policy exist or not.

        //nested if else depends on what the user inputed and if the policy is existing or not.
        if (this.polNumber != 0) {
            if (accountFind > 0) {//if policy existing or not.
                try {
                    int accNumHolder = 0;
                    resultSet = programTools.resultStatement(statement);
                    while (resultSet.next()) {
                        //set the value of instance variable and also local variable of the method to be use in this method.
                        accNumHolder = resultSet.getInt("AccountNumHolder");
                        this.accountNum = resultSet.getInt("AccountNum");
                        this.effDate = resultSet.getString("EffDate");
                        this.expDate = resultSet.getString("ExpDate");
                        this.totalPremium = resultSet.getDouble("TotalPremium");
                    }
                    programTools.clearScreen();//clear screen method.
                    System.out.println("\n\n**********************Policy details**********************");
                    
                    System.out.println(policyOutput());//output of policy.

                    //creating statement for sql query and run the method to check.
                    statement = "SELECT * FROM polholder WHERE polHolderNum =" + accNumHolder;
                    resultSet1 = programTools.resultStatement(statement);
                    System.out.print("\n======================Policy holder=======================");
                    while (resultSet1.next()) {
                        //creating an object that will set a value of its instance variable using constructor.
                        PolicyHolder policyHolder = new PolicyHolder(resultSet1.getString("FirstName"),
                                resultSet1.getString("Lastname"),
                                resultSet1.getString("BirthDate"),
                                resultSet1.getString("Address"),
                                resultSet1.getString("LicenseNum"),
                                resultSet1.getString("licenseDate"),
                                resultSet1.getInt("polHolderNum"));
                        System.out.print(policyHolder.polHolderOutput() + "\n");//output of result.

                        //creating statement for sql query and run the method to check the data connected to policy.
                        statement = " SELECT * FROM vehicle WHERE polNumber = " + this.polNumber + "";
                        resultSet2 = programTools.resultStatement(statement);
                        System.out.println("\n**********************Vehicle details**********************");
                        while (resultSet2.next()) {

                            //creating an object that will set a value of its instance variable using constructor.
                            Vehicle vehicle = new Vehicle(resultSet2.getString("Brand"),
                                    resultSet2.getString("Model"),
                                    resultSet2.getString("Type"),
                                    resultSet2.getString("FuelType"),
                                    resultSet2.getString("Color"),
                                    resultSet2.getDouble("PurchasePrice"),
                                    resultSet2.getDouble("Premium"),
                                    resultSet2.getInt("year"),
                                    resultSet2.getInt("polNumber"),
                                    resultSet2.getInt("vehicleId"),
                                    resultSet2.getString("PlateNumber"));

                                    vehicle.getOutput();//output
                        }
                    }
                    System.out.println("==========================================================\n");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                programTools.confirmation();//method that will temporaly pause the program.
                programTools.clearScreen();
            } else {
                System.out.println("Account is Doesn't exist.");//if the account doesn't exist
            }
        }
        else{
            checkAccNumber=false;//if the user inputed '0'
        }
    }
    }

    //method that return an statement for sql query.
    public String addPolicy(int AccountNumHolder) {
        return "INSERT INTO policy (PolNumber, AccountNum, AccountNumHolder, EffDate, ExpDate,TotalPremium) VALUES (" +
                this.polNumber + "," +
                this.accountNum + "," +
                AccountNumHolder + ",'" +
                this.effDate + "' ,'" +
                this.expDate + "' ," +
                totalPremium+")";


    }
    //method that will show output.
    public String policyOutput() {
        String output = String.format("%-23s %-15s %-24s %-15s %-24s %-15s %-24s %-15s", "Policy Number:", this.polNumber,
                "\nPolicy Effective Date:", this.effDate,
                "\nPolicy Expiration Date:", this.expDate,
                "\nPolicy Total Premium: ", this.totalPremium);
        return output;
    }

    //getter method that return polnumber
    public int getPolNumber() {
        return this.polNumber;
    }

    //method that cancel a policy.
    public void cancelPol() {
        boolean checkAccNumber = false, isPolicyChoice = false;
        while (!checkAccNumber) {
            //local variable that will be use in this method.
            ResultSet resultSet;
            int polOption, accountFind = 0;
            programTools.clearScreen();//clear screen method.
            this.polNumber = programTools.intTrycatch("\nPlease enter policy number, 0 to back: ");
            String statement = " SELECT * FROM policy WHERE PolNumber = " + this.polNumber;//statement for sql query/.

            //run the sql query to check if account exist or not
            resultSet = programTools.resultStatement(statement);
            accountFind = programTools.resultInt(resultSet);
            
            //nested if else that will determine the user inputed.
            if (this.polNumber != 0) {
                if (accountFind > 0) {
                    try {
                        resultSet = programTools.resultStatement(statement);//running again sql query and return the value searched.
                        while (resultSet.next()) {
                            //set a value to instance variable.
                            this.accountNum = resultSet.getInt("AccountNum");
                            this.effDate = resultSet.getString("EffDate");
                            this.expDate = resultSet.getString("ExpDate");
                            this.totalPremium = resultSet.getDouble("TotalPremium");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    do {
                        //methods for clear screen and choices.
                        programTools.clearScreen();
                        programTools.cancelPolChoices();
                        polOption = programTools.intTrycatch("Please enter your choice: ");

                        switch (polOption) {
                            case 1:
                                removePol();//method that permanently remove a policy.
                                break;
                            case 2:
                                updateEffDate();//method that shorten the expiration date of a policy.
                                break;
                            case 0:
                                isPolicyChoice = true;//in case the user choose to back.
                                break;
                            default:
                                System.out.println("You enter an invalid number, Please choice number in choices.");
                                break;
                        }
                        if (polOption >= 1 && polOption <= 2) {

                            //ask the user if he/she wants to go back to main menu or search another policy.
                            System.out.println("\nGo back to main menu?");
                            checkAccNumber = programTools.decision("Please enter [Y]/to go back to main menu, [N]/to cancel another policy: ");
                            isPolicyChoice = true;
                        }

                    } while (!isPolicyChoice);//do while loop until the condition become true.

                } else {
                    System.out.println("Policy number doesn't exist, Please enter a correct account number");
                }
            } else {
                checkAccNumber = true;//if the user input '0'
            }
        }
    }

    //method that permanently remove policy.
    public void removePol() {
        boolean isChoose;
        programTools.clearScreen();
        //output of the searched policy.
        System.out.println("\n======================Policy details=======================");
        System.out.println(policyOutput());
        System.out.println("==========================================================");
        isChoose = programTools.decision( "Do you really want to cancel this Policy?[Y]yes/[N]No: ");
        if (isChoose) {
            //created arraylist to store statement to be use in sql query
            ArrayList<String> statement = new ArrayList<String>();
            statement.add("DELETE FROM policy WHERE PolNumber= " + this.polNumber);
            statement.add("DELETE FROM vehicle WHERE polNumber= " + this.polNumber);
            //for loop to run all statement in sql query.
            for (int index = 0; index < statement.size(); index++)
                programTools.querySql(statement.get(index));
            statement.clear();//clear method.

            System.out.println("Policy successfully cancelled.");

        }

        else System.out.println("Policy didn't cancel.");
    }
        //update method for policy
    public void updateEffDate() {
        boolean isChoose;
        //user input to enter new date of expiration with validation.
        this.expDate = programTools.withinEffDate( "Please enter new expiration date(DD-MM-YYYY): ",
                this.effDate, this.expDate);
        String statement = "UPDATE policy SET ExpDate= '" + this.expDate + "' WHERE PolNumber = " + this.polNumber;//Statement for sql statement to update new expiration date.
        programTools.clearScreen();//clear screen method.
        System.out.print("\n======================Policy details=======================\n");
        System.out.println(policyOutput());//output of policy
        System.out.println("==========================================================");
        isChoose = programTools.decision("Do you really want to update expiration date of this Policy?[Y]yes/[N]No: ");
        if (isChoose) {
            //updating policy expiration date.
            programTools.querySql(statement);
            System.out.println("Policy expiration date updated.");
        }

        else System.out.println("Policy expiration didn't update.");
    }
}
