package Controller;

import VendingMachineLogic.VendingMachine;
import usersMenu.Admin;
import usersMenu.User;

import java.util.Scanner;

public class mainTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        VendingMachine vendingMachine = new VendingMachine();
        Admin admin = new Admin(vendingMachine);
        User user = new User(vendingMachine);
        int choise;

        do{
            System.out.println("Benvenuto.");
            System.out.println("Ecco la lista delle bevande fresche disponibili");
            vendingMachine.getInventoryFromDatabase();

            System.out.println("1. Menu Utente.");
            System.out.println("2. Menu Admin");
            // non necessario in quanto il distributore dovrebbe essere sempre in funzione
            System.out.println("3. Chiudi programma.");
            choise=input.nextInt();

            switch(choise){
                case 1:
                    user.userMenu();
                    break;

                case 2:
                    input.nextLine();
                    System.out.println("Inserire la password:");
                    String password=input.nextLine();
                    if(admin.verifyPass(password)){
                        admin.adminMenu();
                    }else{
                        System.err.println("Password errata, accesso negato.");
                    }
                    break;
                case 3:
                    System.out.println("Uscita dal programma...");
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
        }while(choise != 3);
    }
}
