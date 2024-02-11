package capstone.src;

import java.util.ArrayList;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone Rating engine class that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/


public class RatingEngine {

    //method that calculate the total premium for each policy
    public double calPremium(ArrayList<Vehicle> vehicle){
        double totalPremium=0;
        for(int index=0; index<vehicle.size(); index++){
            totalPremium+=vehicle.get(index).getPremium();
        }
        return totalPremium;

    }

    public double carPremium(double purchasePrice, double vecPriceFactor, int issuedDate){
    double premium;
    premium = (purchasePrice * vecPriceFactor) + ((purchasePrice / 100) / issuedDate);//computation of premium

    return premium;
    }
}
