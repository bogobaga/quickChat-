/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package test.registration2;

/**
 *
 * @author username
 */

public class Registration2 {
    public static void main(String[] args) {
        
        login2 Login = new login2();
         Message me = new Message();
        
        
       System.out.println("=== REGISTRATION ===");
        
       String Name     = Login.Names();
       String Username = Login.Username();
       String Password = Login.Password();
       String Number   = Login.CellNumber();
       
        
       Login.validate(Username, Password, Number);
       
       me.runMenu();
     
    }
}
