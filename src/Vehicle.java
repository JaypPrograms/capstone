package capstone.src;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone Vehicle class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/
public class Vehicle{

    //instance variable
    private String brand, model, type, fuelType, color, plateNumber;
    private double purchasePrice, premium;
    private int year, polNumber, vehicleId;

    /*creating an object to use the method inside this class. I choose to create an object than using inheritance to easily 
    determine where to edit the method incase I have to modify something in that method*/
    ProgramMethods programTools = new ProgramMethods();

    public double getPremium(){
        return this.premium;
    }
    //constructor method to set the value of instance variable.
    public Vehicle(String brand, String model, String type, String fuelType, String color, double purchasePrice,
            double premium, int year, int polNumber, int vehicleId, String plateNumber) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.fuelType = fuelType;
        this.color = color;
        this.purchasePrice = purchasePrice;
        this.premium = premium;
        this.year = year;
        this.polNumber = polNumber;
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
    }

    //getter method to get plate number
    public String getPlateNumber() {
        return this.plateNumber;
    }
    
    //method that return string which is going to be statement in sql query.
    public String statementVehicle(int polNumber) {
        return "INSERT INTO vehicle (Brand, Model, Type, FuelType, Color, PurchasePrice, Premium, Year, polNumber, vehicleId,PlateNumber) VALUES ('"
                +
                this.brand + "','" +
                this.model + "','" +
                this.type + "' ,'" +
                this.fuelType + "' ,'" +
                this.color + "', " +
                this.purchasePrice + "," +
                this.premium + "," +
                this.year + "," +
                this.polNumber + "," +
                this.vehicleId + ",'" +
                this.plateNumber +
                "')";
    }

    //Method the output the value of every instance variable 
    public void getOutput() {
        System.out.println("\nVehicle ID:            " + this.vehicleId);
        System.out.println("Vehicle Plate:         " + this.plateNumber);
        System.out.println("Vehicle maker:         " + this.brand);
        System.out.println("Vehicle model:         " + this.model);
        System.out.println("Vehicle year:          " + this.year);
        System.out.println("Vehicle type:          " + this.type);
        System.out.println("Vehicle fuelType:      " + this.fuelType);
        System.out.println("Vehicle color:         " + this.color);
        System.out.println("Vehicle purchasePrice: " + this.purchasePrice);
        System.out.println("Vehicle premium:       " + this.premium);
    }
}
