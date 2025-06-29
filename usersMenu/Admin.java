package usersMenu;

import VendingMachineLogic.VendingMachine;
import model.Product;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
    Scanner input = new Scanner(System.in);


    private final String passFinal = "12345";

    private VendingMachine vendingMachine;

    public Admin(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public boolean verifyPass(String password) {
        return passFinal.equals(password);
    }

    public void adminMenu() {
        int choise;

        do
        {
            System.out.println("Benvenuto nel menu Admin.");
            System.out.println("1) Visualizza Inventario.");
            System.out.println("2) Aggiungi una prodotto.");
            System.out.println("3) Rimuovi un prodotto.");
            System.out.println("4) Modifica il prezzo di un prodotto.");
            System.out.println("5) Rifornisci quantita di un prodotto.");
            System.out.println("6) Visualizza incasso.");
            System.out.println("7) Azzera credito cliente.");
            System.out.println("8) Esci dal menu Admin.");

            choise = input.nextInt();
            switch (choise) {

                case 1:
                    vendingMachine.getInventoryFromDatabase();
                    break;

                case 2:
                    System.out.println("Inserisci il nome del prodotto:");
                    String productName = input.next();
                    input.nextLine();

                    System.out.println("Inserisci codice:");
                    String productCode = input.next();
                    input.nextLine();

                    Double productPrice = 0.0;
                    boolean controlPrice = false;

                    while (!controlPrice) {
                        System.out.println("inserisci il prezzo della bevanda:");
                        try {
                            productPrice = input.nextDouble();
                            input.nextLine();
                            if (productPrice >= 0) {
                                controlPrice = true;
                            } else {
                                System.out.println("inserisci un numkjero positivo.");
                            }
                        } catch (
                                InputMismatchException e) {
                            System.err.println("Errore: Inserisci un numero.");
                            input.nextLine();
                        }
                    }
                    int productQuantity = 0;
                    boolean controlQuantity = false;
                    while (!controlQuantity) {
                        System.out.println("Inserisci la quantita:");
                        try {
                            productQuantity = input.nextInt();
                            input.nextLine();
                            if (productQuantity > 0) {
                                controlQuantity = true;
                            } else {
                                System.out.println("Inserisci un numero positivo.");
                            }
                        } catch (
                                InputMismatchException e) {
                            System.err.println("Errore: Inserisci un numero.");
                        }

                    }
                    vendingMachine.addProductToDatabase(new Product(productName, productCode, productPrice, productQuantity));
                    break;
                case 3:
                    System.out.println("inserisci il codice del prodotto che vuoi rimuovere:");
                    String productToRemove = input.nextLine();
                    vendingMachine.removeProductFromDatabase(productToRemove);
                    break;
                case 4:
                    System.out.println("mInserisci il codice del prodotto:");
                    String productPriceModifierCode = input.nextLine();
                    input.nextLine();
                    double productNewPrice = 0.0;
                    boolean controlNewPrice = false;
                    while (!controlNewPrice) {
                        System.out.println("Inserisci il nuovo prezzo:");
                        try {
                            productNewPrice = input.nextDouble();
                            input.nextLine();
                            if (productNewPrice > 0) {
                                controlNewPrice = true;
                            } else {
                                System.out.println("inserisci un numero positivo.");
                            }

                        } catch (
                                InputMismatchException e) {
                            System.err.println("Errore. Inserisci un numero.");
                            input.nextLine();
                        }
                    }
                    vendingMachine.priceModifierByCode(productPriceModifierCode, productNewPrice);
                    break;
                case 5:
                    //refill
                    System.out.println("Inserisci il codice della bevanda da rinfornire:");
                    String productCodeToRefill = input.next();
                    input.nextLine();
                    int quantityToAdd = 0;
                    boolean controlQuantityAdd = false;
                    while (!controlQuantityAdd) {
                        System.out.println("Inserisci la nuova quantita:");
                        try {
                            quantityToAdd = input.nextInt();
                            input.nextLine();
                            controlQuantityAdd = true;
                        } catch (
                                InputMismatchException e) {
                            System.out.println("Inserisci un numero.");
                        }
                    }
                    vendingMachine.refillProductByCodeToDatabase(productCodeToRefill, quantityToAdd);
                    break;
                case 6:
                    vendingMachine.getRevenue();
                    break;
                case 7:
                    vendingMachine.resetCredit();
                    break;
                case 8:
                    System.out.println("Uscita dal menu Admin...");
                    return;
                default:
                    System.out.println("Inserisci un numero valido.");
            }
        } while (choise != 8);
    }
}
