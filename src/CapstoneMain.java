package capstone.src;

/**
* Java Course 4 Capstone
*
* @author Jayperson Babaran
* @Description: Capstone mainclass that is Automobile Insurance Policy and Claims Administration system 
* (PAS) that  manage customer automobile insurance policies and as well as accident claims for an insurance company
* Created Date: 07/11/2022
* Modified Date: 09/09/2022
* @Modified By: Jayperson Babaran
*
*/
public class CapstoneMain {
    public static void main(String[] args) {
        
        
        Boolean looping = false;
        int choose;
        ProgramMethods programTools = new ProgramMethods();//create a object programTools that has method that needed in this program
        //Instantiation to create an object account, policy, claim, and policyholder.
        CreateAccount newAccount = new CreateAccount();
        Policy policy = new Policy();
        Claim claim = new Claim();
        PolicyHolder polHolder = new PolicyHolder();
        //run method that connect to database and check if database is existing or not.  
        programTools.connectDb();
        programTools.createDb();

        while (!looping) {//looping till the user choose to close the program
            programTools.pasChoices();//choices method
            choose = programTools.intTrycatch("Enter your choice: ");//user input to determine what the user want to do.
            switch (choose) {
                case 1:
                    newAccount.addAccount();//method that add account
                    break;
                case 2:
                    policy.buyPolicy(policy, polHolder); //method to create policy
                    break;
                case 3:
                    policy.cancelPol();//method for cancelation of policy.
                    break;
                case 4:
                    claim.fileClaim();//method to file a claim.
                    break;
                case 5:
                    newAccount.searchAccount();//method for search account.
                    break;
                case 6:
                    policy.searchPolicy();//method for search policy
                    break;
                case 7:
                    claim.searchClaims();//method for search claims
                    break;
                case 8:
                    System.out.println("Thank you, goodbye.");
                    programTools.confirmation();
                    looping = true;//if the user choose 8, the program will close
                    break;
                default:
                    System.out.println("Invalid input");
                    programTools.confirmation();
                    break;
            }
           programTools.clearScreen();//method that clear the screen.
        }
    }
}
