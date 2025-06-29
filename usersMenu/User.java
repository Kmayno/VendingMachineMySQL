package usersMenu;

import VendingMachineLogic.VendingMachine;

import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    VendingMachine vendingMachine;

    public User(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public void userMenu() {
        Scanner input = new Scanner(System.in);
        int choise;
        do
        {
            System.out.println("Benvenuto.");
            System.out.println("Credito attuale: " + vendingMachine.getCredit() + " €");
            System.out.println("1. Inserisci credito");
            System.out.println("2. Visualizza l'inventario");
            System.out.println("3. Acquista bevanda");
            System.out.println("4. Eroga resto");
            System.out.println("5. Esci");
            System.out.print("Scegli un'opzione: ");

            choise = input.nextInt();

            switch (choise) {
                case 1:
                    System.out.println("Inserisci importo da aggiungere:");
                    double cash = 0.0;
                    boolean controlCash = false;
                    while (!controlCash) {
                        try {
                            cash = input.nextDouble();
                            if (cash > 0) {
                                vendingMachine.addCredit(cash);
                                controlCash = true;
                            } else {
                                System.out.println("Inserisci importo positivo.");
                            }

                        } catch (
                                InputMismatchException e) {
                            System.out.println("inserisci importo valido.");
                            input.nextLine();
                        }
                    }
                    break;

                case 2:
                    vendingMachine.getInventoryFromDatabase();
                    break;
                case 3:
                    input.nextLine();
                    System.out.println("inserisci il codice della bevanda:");
                    String code = input.nextLine();
                    vendingMachine.buyBeverage(code);
                    break;
                case 4:
                    vendingMachine.resetCredit();
                    break;
                case 5:
                    System.out.println("Uscita dal menu Cliente.");
                    return;
                default:
                    System.out.println("inserisci un numero valido.");
            }
        } while (choise != 5);
    }
}






