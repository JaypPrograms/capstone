package capstone.src;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone claim class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/

public class Claim extends DbConnection {

    //instance variables.
    private String claimNumber, accidentDate, accAddress, descOfAccident, descOfDamage;
    private double repairCost;
    private int polNumber;

    /*creating an object to use the method inside this class. I choose to create an object than using inheritance to easily 
    determine where to edit the method incase I have to modify something in that method*/
    ProgramMethods programTools = new ProgramMethods();

    //method the file claim
    public void fileClaim() {
        boolean checkAccNumber = true;
        while (checkAccNumber) {//loop until the user choose to press 0 or go back to main menu after filing claims
            programTools.clearScreen();
            //local variable that will be use in the program
            ResultSet resultSet, resultSet1;
            int accountFindPol = 0;

            this.polNumber = programTools.intTrycatch("\nPlease enter policy number, 0 to back: ");//user input

            String statement = " SELECT * FROM policy WHERE PolNumber = " + this.polNumber;//Statement that will use in query in database
            resultSet = programTools.resultStatement(statement);//method that is sql query and return resultset.
            accountFindPol = programTools.resultInt(resultSet);//method that return value to check if policy is existing in the database or not

            //nested if else condition 
            if (this.polNumber != 0) {
                if (accountFindPol > 0) {
                    //method that random a number to set as claim number for the claim
                    this.claimNumber = "C"+ String.valueOf(programTools.numberRandom(9999, 90000, "claims", "ClaimNumber"));
                    String startDate = "", expDate = "";
                    
                    //try catch to get the result from the database
                    try {
                        resultSet1 = programTools.resultStatement(statement);//sql query to get the result
                        while (resultSet1.next()) {
                            //getting the result.
                            startDate = resultSet1.getString("EffDate");
                            expDate = resultSet1.getString("ExpDate");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();//in case for exception in the program
                    }
                    LocalDate localStartDate = LocalDate.parse(startDate);//create object to be use invalidation on date
                    if (localStartDate.compareTo(LocalDate.now()) <= 0) {//validation condition

                        //user input for other details
                        this.accidentDate = withinEffDate("Please enter accident date(DD-MM-YYYY): ",startDate, expDate);
                        this.accAddress = programTools.stringInput("Address of Accident: ");
                        this.descOfAccident = programTools.stringInput("Description of accident: ");
                        this.descOfDamage = programTools.stringInput("Description of damage to vehicle: ");
                        this.repairCost = programTools.doubleTrycatch("Estimated cost of repairs: ");

                        //method use in the program for clear screen and output for the user input.
                        programTools.clearScreen();
                        getOutput();

                        boolean isChoose;
                        System.out.println("Please take note your details if you want to save.");
                        isChoose = programTools.decision("Do you really want to Save this Claim?[Y]yes/[N]No: ");//user input for the user if he/she want to add that claim or not.
                        if (isChoose) {
                            String statement1 = addClaims();//stting up statement1 as query to use in adding the data in the database
                            programTools.querySql(statement1);//method for running query in sql
                            System.out.println("Claims successfully Claimed. Please enter to continue.");
                            scan.nextLine();
                        }
                        checkAccNumber = programTools.decision("Do you want to claim another policy?[Y]yes/[N]no: ");//method that return boolean value.
                        
                    }else{
                        System.out.println("You cannot file claims because your effective date didn't start yet.");//notif for validation of progaram
                        programTools.confirmation();//method for to pause the program
                    }

                } else {
                    System.out.println("Policy number doesn't exist, Please enter a correct Policy number");//Notification
                }
            }

            else {
                checkAccNumber = false;
            }
        }
    }

    //Method for search claims
    public void searchClaims() {
        //Local variable that use in this method
        ResultSet resultSet, resultSet1;
        int accountFind = 0;
        boolean checkAccNumber = true;
        programTools.clearScreen();
        while (checkAccNumber) {//for loop until user choose to close the method
            this.claimNumber = programTools.stringInput("Please enter claim number, 0 to back to main menu: ");
            String statement = " SELECT * FROM claims WHERE ClaimNumber = '" + this.claimNumber + "'";//statement for sql query.

            //sql query and getting the result.
            resultSet = programTools.resultStatement(statement);
            accountFind = programTools.resultInt(resultSet);

            //nested if else if the user input 0 or choose to the choose go back to main menu
            if (!this.claimNumber.equals("0")) {
                if (accountFind > 0) {
                    try {
                        resultSet = programTools.resultStatement(statement);//getting the result of query in sql
                        while (resultSet.next()) {
                            //setting the value depends the result of sql query
                            this.accidentDate = resultSet.getString("AccidentDate");
                            this.accAddress = resultSet.getString("AccAddress");
                            this.descOfAccident = resultSet.getString("DescOfAccident");
                            this.descOfDamage = resultSet.getString("DescOfDamage");
                            this.repairCost = resultSet.getDouble("RepairCost");
                            this.polNumber = resultSet.getInt("PolNumber");
                        }
                        //method to clear and showing user input
                        programTools.clearScreen();
                        getOutput();

                        statement = " SELECT * FROM policy WHERE PolNumber = " + this.polNumber + "";
                        resultSet1 = programTools.resultStatement(statement);

                        //method to show the policy of claims
                        System.out.println("\n=======================Policy Details=======================");
                        while (resultSet1.next()) {
                            //create a object policy that use constructor method to set the instance variable of the object
                            Policy policies = new Policy(resultSet1.getInt("PolNumber"),
                                    resultSet1.getInt("AccountNum"),
                                    resultSet1.getString("EffDate"),
                                    resultSet1.getString("ExpDate"),
                                    resultSet1.getDouble("TotalPremium"));
                            System.out.print(policies.policyOutput() + "\n");
                        }

                        System.out.println("============================================================\n");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    programTools.confirmation();//method for confirmation
                    programTools.clearScreen();
                } else {
                    System.out.println("Claim Doesn't exist.");
                }
            } else {
                checkAccNumber = false;//if the user input "0", the method will be terminated
            }
        }
    }
        //method that create a sql statement
    public String addClaims() {
        return "INSERT INTO claims (ClaimNumber, AccidentDate, AccAddress, DescOfAccident, DescOfDamage,RepairCost,PolNumber) VALUES ('"
                +
                this.claimNumber + "','" +
                this.accidentDate + "','" +
                this.accAddress + "' ,'" +
                this.descOfAccident + "' ,'" +
                this.descOfDamage + "' ," +
                this.repairCost + "," +
                this.polNumber + ")";
    }
    //method that create a output of the input user.
    public void getOutput() {
        System.out.println("\n=======================Claims Details=======================");
        System.out.println("Claim number:                  " + this.claimNumber);
        System.out.println("Accident date:                 " + this.accidentDate);
        System.out.println("Address of accident:           " + this.accAddress);
        System.out.println("Description of accident:       " + this.descOfAccident);
        System.out.println("Vehicle damage:                " + this.descOfDamage);
        System.out.println("Estimated cost of Repairs:     " + this.repairCost);
        System.out.println("============================================================");
    }
    //method that check if the date if the claim is valid or not then return it.
    public String withinEffDate( String print, String effDate, String currentExpDate) {
        String date = " ";
        boolean loop = false;
        while (!loop) {//while loop until the user input a correct date.

            //create an objects to be use in validation in the program.
            LocalDate localCurrentExpDate = LocalDate.parse(currentExpDate);
            date = programTools.dateCheck(print);//user input for date
            LocalDate localDate = LocalDate.parse(date);
            LocalDate localEffDate = LocalDate.parse(effDate);

            //if else statement to check of the date is valid or not.
            if ((localDate.compareTo(localEffDate) >= 0) && (localDate.compareTo(localCurrentExpDate) <= 0) && (localDate.compareTo(LocalDate.now()) <= 0) )
                loop = true;

            else//in case the date is not valid.
                System.out.println("Invalid date! Date must be within effective date");
        }
        return date;// thie will return the user input date
    }
}
