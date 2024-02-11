package capstone.src;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone PolicyHolder class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/

public class PolicyHolder extends DbConnection {
    //instance variable of the class
    private ArrayList<Vehicle> vehicle = new ArrayList<Vehicle>();
    private RatingEngine ratingEngine=new RatingEngine();
    private String firstName, lastName, birthDate, address, licenseNum, licenseDate;
    private int polHolderNum;

    /*creating an object to use the method inside this class. I choose to create an object than using inheritance to easily 
    determine where to edit the method incase I have to modify something in that method*/
    ProgramMethods programTools = new ProgramMethods();
    public PolicyHolder() {
    }

    // constructor method that set the value of instance variable.
    public PolicyHolder(String firstName, String lastName, String birthDate, String address, String licenseNum,
            String licenseDate, int polHolderNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.licenseNum = licenseNum;
        this.licenseDate = licenseDate;
        this.polHolderNum = polHolderNum;
    }

    //Method that add policy holder.
    public void addPolholder(Policy buyPolicy) {
        boolean loop, isAccExisting;
        System.out.println("\nAdding Policy holder");
        loop = checkAccount();//method that return a boolean and the variable will be use in the if else in lower part.

        if (loop)//if policy holder existing in the database.
            System.out.println("Policy holder is existing, Please use the existing policy holder.");

        else {
            //check if the user inputed a name that existing in the account
            isAccExisting = programTools.checkAccount( "account", this.firstName, this.lastName);
            if (isAccExisting) {//if existing 
                System.out.println("Firstname and lastname match the account name. Please choose the account as policy holder in the choices.");
            } else {//if not existing.
                this.address = programTools.stringInput("Please enter your address: ");
                addDetailsHolder();//method that add the additional details of the policy holder.
                int issuedDate = getIssuedDate();//get the issued date for validation in adding details in vehicle
                addVehicle(buyPolicy,  issuedDate);//method for adding car.

                //output of all the user inputed.
                input(buyPolicy);

                //condition in order to determine if the user want to save the policy or not.
                System.out.println("Please take note your details if you want to save.");
                choose = programTools.decision( "Do you want to save this Policy?[Y]yes/[N]No: ");
                if (choose) {//if yes

                    //adding it to the database using sql query.
                    programTools.querySql(buyPolicy.addPolicy(getpolHolderNum()));
                    programTools.querySql(addPolHolder());

                    //for loop for vehicle if more than 1 is added to the policy.
                    for (int index = 0; index < vehicle.size(); index++) {
                        programTools.querySql(vehicle.get(index).statementVehicle(buyPolicy.getPolNumber()));
                    }
                    System.out.println("Policy Successfully saved. Please enter to continue.");
                    scan.nextLine();
                }
                vehicle.clear();//clearing the arraylist.
            }
        }
    }
    //method that add a existing policy.
    public void existingPolHolder(Policy buyPolicy) {
        //local variable to be use in the method.
        boolean checkAccNumber = true;
        while (checkAccNumber) {
            ResultSet resultSet;
            int result;

            //user input of policy holder to be search in the database.
            this.polHolderNum = programTools.intTrycatch("Please enter policy holder number, 0 to back: ");

            if (this.polHolderNum != 0) {
                //checking if account number is existing in the database.
                String statement = "SELECT * FROM polholder WHERE polHolderNum =" + this.polHolderNum;
                resultSet = programTools.resultStatement(statement);
                result = programTools.resultInt(resultSet);

                if (result <= 0)//if the program or result set is null.
                    System.out.println("Account doesn't exist:");

                else {
                    try {
                        resultSet = programTools.resultStatement(statement);//sql query
                        while (resultSet.next()) {

                            //set the value of instance variable.
                            this.lastName = resultSet.getString("LastName");
                            this.firstName = resultSet.getString("FirstName");
                            this.birthDate = resultSet.getString("BirthDate");
                            this.address = resultSet.getString("Address");
                            this.licenseNum = resultSet.getString("LicenseNum");
                            this.licenseDate = resultSet.getString("licenseDate");
                            this.polHolderNum = resultSet.getInt("polHolderNum");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();//in case there was an exception incounter
                    }
                    int issuedDate = getIssuedDate();//get the issued date for validation in adding details in vehicle
                    addVehicle(buyPolicy,  issuedDate);//adding car method.
                    input(buyPolicy);//output the user inputed

                    //determine the decision of the user if she/he wanted to add the policy or not
                    System.out.println("Please take note your details if you want to save.");
                    choose = programTools.decision( "Do you want to save this Policy?[Y]yes/[N]No: ");
                    if (choose) {//if yes.
                        
                        programTools.querySql(buyPolicy.addPolicy(getpolHolderNum()));//adding policy
                        for (int index = 0; index < vehicle.size(); index++) {//for loop in case user inputed more than 1 vehicle.
                            programTools.querySql(vehicle.get(index).statementVehicle(buyPolicy.getPolNumber()));
                        }
                        System.out.println("Policy Successfully saved. Please enter to continue.");
                        scan.nextLine();
                    }
                    vehicle.clear();//clearing the arraylist
                    checkAccNumber = false;//set it to false to go back to input policy number
                }
            }
            else
                checkAccNumber = false;//in case user inputed '0'
        }
    }

    //method that add existing account
    public void existingAccount(Policy buyPolicy, int accountNum) {
        //local variable to be use in the method.
        ResultSet resultSet;
        boolean isAccExist = false;
        try {
            String statement = "SELECT * FROM account WHERE AccountNum =" + accountNum;
            resultSet = programTools.resultStatement(statement);//sql query to get the attribute in the database and set it to instance variable.
            while (resultSet.next()) {
                //set the value of instance variable
                this.lastName = resultSet.getString("LastName");
                this.firstName = resultSet.getString("FirstName");
                this.address = resultSet.getString("Address");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //method that return boolean value for validation if the account existing in the database of policy holder.
        isAccExist = programTools.checkAccount( "polholder", this.firstName, this.lastName);
        if (!isAccExist) {
            System.out.println("\nAdding other details of Policy holder");
            addDetailsHolder();//method that add other details.
            int issuedDate = getIssuedDate();//get the issued date for validation in adding details in vehicle
            addVehicle(buyPolicy, issuedDate);//method that add vehicle
            input(buyPolicy);//output of all the user inputed.

            //determine the decision of the user if she/he wanted to add the policy or not
            System.out.println("Please take note your details if you want to save.");
            choose = programTools.decision( "Do you want to save this Policy?[Y]yes/[N]No: ");
            if (choose) {//if yes 

                //adding the policy and policy holder
                programTools.querySql(buyPolicy.addPolicy(getpolHolderNum()));
                programTools.querySql(addPolHolder());
                for (int index = 0; index < vehicle.size(); index++) {//incase user inputed more than 1 car.
                    programTools.querySql(vehicle.get(index).statementVehicle(buyPolicy.getPolNumber()));
                }
                System.out.println("Policy Successfully saved. Please enter to continue.");
            }
            vehicle.clear();//clearing the arraylist
        }

        else {
            System.out.println("Account already existing in the system. Please use the existing policy holder, thank You.");
        }
    }

    //getter method to get licenseDate
    public String getLicenseDate() {
        return this.licenseDate;
    }

    //getter method to get polHolderNum
    public int getpolHolderNum() {
        return this.polHolderNum;
    }

    public String addPolHolder() {//method that return a string that will be use as statement in sql query.
        return "INSERT INTO polholder (Lastname, Firstname, BirthDate, Address, LicenseNum, licenseDate, polHolderNum) VALUES ('"
                +
                this.lastName + "','" +
                this.firstName + "','" +
                this.birthDate + "' ,'" +
                this.address + "' ,'" +
                this.licenseNum + "','" +
                this.licenseDate + "'," +
                this.polHolderNum +
                ")";
    }

    public String polHolderOutput() {//method that return an output of instance variable.
        String output = String.format("%-28s %-1s %-28s %-1s %-1s %-28s %-1s %-28s %-1s %-28s %-1s",
                "\nPolicy holder number:", this.polHolderNum,
                "\nPolicy holder Fullname:", this.firstName, this.lastName,
                "\nPolicy holder birth date:", this.birthDate,
                "\nPolicy holder address:", this.address,
                "\nPolicy holder license Date:", this.licenseDate, "\n");
        return output;
    }

    public boolean checkAccount() {//Method that check if account exist and return a boolean

        //local variable that will be use in this method.
        boolean result = false;
        ResultSet resultSet;
        int accountFind = 0;

        //user input and will be use to determine if account is existing or not in the database
        this.firstName = programTools.stringInput("Please enter firstname: ");
        this.lastName = programTools.stringInput("Please enter lastname: ");
        String statement = "SELECT * FROM polholder WHERE FirstName ='" + this.firstName +
                "' AND LastName = '" + this.lastName + "'";//statement

                //checking if the user input is existing or not in the database.
        resultSet = programTools.resultStatement(statement);
        accountFind = programTools.resultInt(resultSet);

        if (accountFind > 0)
            result = true;//if existing the method will return true.

        return result;
    }

    public void addDetailsHolder() {//this method will add the remaining details of the policy holder.

        //local variable to be use in this method.
        int resultLicenseNum = 0, age = 0;
        ResultSet resultSet;
        this.birthDate = programTools.ageDate( "Please enter your birthdate(DD-MM-YYYY):");//user input with validation.
        this.polHolderNum = programTools.numberRandom(999, 9000, "polholder", "polHolderNum");//randomize a number and will be set to the variable.
        do {
            //user input for license date and validation if existing in the database or not.
            this.licenseNum = programTools.stringInput("Please enter your license number: ");
            String statement = "SELECT * FROM polholder WHERE LicenseNum ='" + this.licenseNum + "'";
            resultSet = programTools.resultStatement(statement);
            resultLicenseNum = programTools.resultInt(resultSet);
            if (resultLicenseNum > 0)
                System.out.println("Your license number is already existing in the system.");//if existing.
        } while (resultLicenseNum > 0);//do while if the license is existing.

        do {
            this.licenseDate = programTools.licenseEffDate("Please enter your license date issued(DD-MM-YYYY):");
            age = ((Integer.parseInt(this.licenseDate.substring(0, this.licenseDate.indexOf('-')))) -
                    (Integer.parseInt(this.birthDate.substring(0, this.birthDate.indexOf('-')))));//checking if the user inputed a correct date.
            if (age < 17)
                System.out.println(
                        "Invalid date!! Based on calculation your age doesn't match the required age to have a driver license.");
        } while (age < 17);//do while until user inputed a valid date.
    }

    public int getIssuedDate() {//method that check date and return a int value

        //checking the year old of the user inputed.
        LocalDate licenseDate = LocalDate.parse(this.licenseDate);
        int licenseEffDateyear = licenseDate.getYear();
        int issuedDate = currentyear - licenseEffDateyear;
        if (issuedDate == 0)
            issuedDate = 1;

        return issuedDate;
    }

    public void addVehicle(Policy buyPolicy,  int issuedDate) {//method in adding vehicle.
        //local variable to be use in this method.
        int numOfVehicle;
        double vecPriceFactor;
        boolean loop = false, isCheckPlateNum = true;
        programTools.clearScreen();//clear screen method.

        System.out.println("\nAdding vehicle.");
        numOfVehicle = programTools.negativeCheck( "Please enter number of vehicle: ");//user input with navigation
        System.out.println("");

        //local variable to be use in this method.
        String brand, model, type, fuelType, color, plateNumber;
        double purchasePrice, premium;
        int year = 0, vehicleId;

        for (int index = 0; index < numOfVehicle; index++) {//for loop depends on how many vehicle will be added.
            ResultSet resultSet;
            int accountFind = 0;
            
            do {
                //user input of license plate and validation if the license plate is existing or not
                plateNumber = programTools.stringInput("Please enter car " + (index + 1) + " license plate: ");
                String statement = " SELECT * FROM vehicle WHERE PlateNumber = '" + plateNumber + "'";
                resultSet = programTools.resultStatement(statement);
                accountFind = programTools.resultInt(resultSet);

                if (accountFind >= 1) {
                    System.out.println("Invalid input!!! you input an license plate that has already have policy.");
                } else {
                    isCheckPlateNum = false;
                }
                for (int index2 = 0; index2 < vehicle.size(); index2++) {//for loop to check if the user inputed the same plate number.
                    if (plateNumber.equals(vehicle.get(index2).getPlateNumber())) {
                        System.out.println("Invalid input!!! you input an license plate that you inputed earlier.");
                        isCheckPlateNum = true;
                    }
                }

            } while (isCheckPlateNum);//while the user inputed a existing plate number.

            //user input for additional details.
            brand = programTools.stringInput("Please enter car " + (index + 1) + " maker: ");
            model = programTools.stringInput("Please enter car " + (index + 1) + " model: ");
            while (!loop) {//while loop for validation of user input in purchase year and if it still can cover a policy
                year = programTools.intTrycatch("Please enter car " + (index + 1) + " purchase year: ");
                int minimumAgeYear = currentyear - 40;
                if (year <= currentyear && year > minimumAgeYear)
                    loop = true;
                else
                    System.out.println("Invalid year, please enter specific year");
            }
            loop = false;

            //user input for other details.
            type = programTools.typeChoices();
            fuelType = programTools.typeFuelType();
            color = programTools.stringInput("Please enter car " + (index + 1) + " color: ");
            purchasePrice = programTools.doubleTrycatch("Please enter car " + (index + 1) + " purchase price: ");
            vecPriceFactor = programTools.dlxReturn(currentyear - year);//determine the vecpricefactor for the computaion of vehicle premium
            premium = ratingEngine.carPremium(purchasePrice, vecPriceFactor, issuedDate);
            vehicleId = programTools.numberRandom(9999, 90000, "vehicle", "VehicleId");

            //adding vehicle 
            vehicle.add(new Vehicle(brand, model, type, fuelType, color, purchasePrice, premium, year,
                    buyPolicy.getPolNumber(), vehicleId, plateNumber));
            System.out.println("");
        }
        buyPolicy.setTotalPremium(ratingEngine.calPremium( vehicle));
    }   

    //output of the user inputed.
    public void input(Policy buyPolicy) {
        programTools.clearScreen();
        System.out.println("=======================Policy Details=======================");
        System.out.println(buyPolicy.policyOutput());
        System.out.print("============================================================\n\n");
        System.out.println("=====================Policy Holder Details==================");
        System.out.println(polHolderOutput());
        System.out.print("============================================================\n\n");
        System.out.print("=======================Vehicle Details======================\n");
        for (int index = 0; index < vehicle.size(); index++)
            vehicle.get(index).getOutput();
        System.out.print("============================================================\n");
    }
}
