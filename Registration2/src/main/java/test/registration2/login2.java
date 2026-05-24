
package test.registration2;

import java.util.Scanner;


   public class login2 {
   Scanner input = new Scanner(System.in);
    
   // variables used to store user details
    private String capturedName;
    private String username;
    private String Password;
    
   public String Names(){
   System.out.print("Enter your First Name : ");
   String Name = input.nextLine();
   System.out.print("Enter your Last Name  : ");
   String Surname = input.nextLine();
   capturedName = Name + " " + Surname; 
   return Name + " " + Surname;
   
   }
   
   public String Username(){
    //prompt user 
    System.out.print("Enter your Username   : ");
     username = input.nextLine();
    return username;
    
    }
   
   public String Password(){
       
    System.out.print("Enter Password        : ");
    Password = input.nextLine();   
    
   return Password;
   }
   public String CellNumber (){
       
    System.out.print("Enter your number     : ");
    String number  = input.nextLine();  
    
    return number ;
   }
       public void loginUser() {
          
        System.out.println("\n=== LOGIN ===");
      
        int counter =1;
        boolean loggedIn = false;
        while (counter<=3 &&!loggedIn ){
            
        System.out.print("\nEnter username : " );
        String Username = input.nextLine();
        System.out.print("Enter password : ");
        String Pass = input.nextLine();
                
            
    if (Username.equals(username) && Pass.equals(Password)){
    System.out.println("Welcome " + capturedName + " it is great to see you again.\n" );
    loggedIn =true;
    Message me = new Message();
    System.out.println(me.Message());
  
    }
    else{
    System.out.print("Username or password incorrect. Try again.");
    }
     
      counter = counter +1 ;
    }
       if (!loggedIn) {
    System.out.print("\n\nToo many attempts, Try again later");
    System.exit(0);
}
       }
  // method to return Message after logging in 
       public String returnLoginStatus(boolean loginStatus){

    if(loginStatus){
        return "Welcome " + capturedName + " it is great to see you again.";
    }
    else{
        return "Username or password incorrect, please try again.";
    }

}
   //validation

   
 public void validate (String username,String Password,String CellNumber){
 
 boolean ValidUsername = checkUserName(username);
 boolean ValidPassword = checkPasswordComplexity(Password);
 boolean ValidPhoneNumber = checkCellPhoneNumber(CellNumber);
 
    

    
    while (!checkUserName(username)) {
        System.out.println("Username is not correctly formatted.");
        System.out.println("It must contain an underscore and be no more than 5 characters.");
        
        System.out.print("enter username again: ");
        username = input.nextLine();
    }
    System.out.println("Username successfully captured");
    this.username = username;

    
    while (!checkPasswordComplexity(Password)) {
        System.out.println("Password is not correctly formatted.");
        System.out.println("It must have at least 8 characters, a capital letter, a number, and a special character.");
        
        System.out.print("enter password again: ");
        Password = input.nextLine();
    }
    System.out.println("Password successfully captured");
    this.Password = Password;

    
    while (!checkCellPhoneNumber(CellNumber)) {
        System.out.println("Cell phone number incorrectly formatted or missing international code.");
        
        System.out.print("enter cell number again: ");
        CellNumber = input.nextLine();
    }
    System.out.println("Cell phone number successfully added");

    
    System.out.print("Registration Successful\n");
    loginUser();
 }
   
   
public static boolean checkUserName(String username){
    return username.contains("_") && username.length()<=5;
}

public static boolean checkPasswordComplexity(String password){
    return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$");
}

public static boolean checkCellPhoneNumber(String cellNumber){
    
    return cellNumber.matches("^\\+27[0-9]{9}$");
}

   }