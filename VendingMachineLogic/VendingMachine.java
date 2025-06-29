package VendingMachineLogic;
import DatabaseConnection.DatabaseConnection;
import model.Product;

import java.sql.*;

public class VendingMachine {
    private Double credit;

    public VendingMachine() {
        credit = 0.0;
    }


    public Double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }



    public Boolean findByCodeIntoDatabase(String code) {
        Boolean check = false;
        String QuerySQL = "SELECT code FROM products WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(QuerySQL)) {
                stmt.setString(1, code);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    check = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    public void addProductToDatabase(Product b) {
        String code = b.getCode();
        boolean check = findByCodeIntoDatabase(code);
        if(check) {
            System.out.println("codice gia presente. Riprovare.");
        } else {
            String queryAddBeverage = "INSERT INTO products (name,code,price,quantity) VALUES (?,?,?,?) ";
            try (Connection conn = DatabaseConnection.getConnection()) {
                try (PreparedStatement stmtAdd = conn.prepareStatement(queryAddBeverage)) {
                    stmtAdd.setString(1, b.getName());
                    stmtAdd.setString(2, b.getCode());
                    stmtAdd.setDouble(3, b.getPrice());
                    stmtAdd.setInt(4, b.getQuantity());
                    int rows = stmtAdd.executeUpdate();
                    System.out.println(rows + " prodotto aggiunto.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


        public void removeProductFromDatabase(String code){
            boolean check = findByCodeIntoDatabase(code);
            if (!check) {
                System.out.println("nessun codice trovato.");
            } else {
                String removeQuery = "DELETE FROM products WHERE code = ?";
                try (Connection conn = DatabaseConnection.getConnection()) {
                    try (PreparedStatement stmt = conn.prepareStatement(removeQuery)) {
                        stmt.setString(1, code);
                        int row = stmt.executeUpdate();
                        System.out.println(row + " prodotto rimossso.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public void priceModifierByCode(String code,double newPrice){
            boolean check = findByCodeIntoDatabase(code);
            if(!check){
                System.out.println("Codice non trovato");
            } else{
                String modifyPriceSQL="UPDATE products SET price = ? WHERE code = ?";
                try(Connection conn= DatabaseConnection.getConnection()){
                    try(PreparedStatement stmt= conn.prepareStatement(modifyPriceSQL)){
                        stmt.setDouble(1,newPrice);
                        stmt.setString(2,code);
                        int row = stmt.executeUpdate();
                        System.out.println(row+" prodotti modificati");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        private Product getProductFromDatabase(String code){
            boolean check = findByCodeIntoDatabase(code);
            Product p = null;
            if(!check){
                System.out.println("Codice non trovato. Riprovare");
            } else {
                String getProductSQL="SELECT * FROM products WHERE code = ?";
                try(Connection conn = DatabaseConnection.getConnection()){
                    try(PreparedStatement stmt=conn.prepareStatement(getProductSQL)){
                        stmt.setString(1,code);
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next()){
                            String name = rs.getString("name");
                            String codeP = rs.getString("code");
                            Double price = rs.getDouble("price");
                            int quantity = rs.getInt("quantity");
                            p = new Product(name,codeP,price,quantity);
                            return p;
                        }

                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            return p;
        }

        public void refillProductByCodeToDatabase(String code, int quantityToAdd){
            boolean check = findByCodeIntoDatabase(code);
            Product refill = getProductFromDatabase(code);
            if(!check){
                System.out.println("codice non trovato. Riprovare.");
            } else{
                int quantityBefore = refill.getQuantity();
                System.out.println("Ci sono "+quantityBefore+ " prodotti.");
                String modifyQuantitySQL = "UPDATE products SET quantity = quantity + ? WHERE code = ?";
                try(Connection conn=DatabaseConnection.getConnection()){
                    try(PreparedStatement stmt = conn.prepareStatement(modifyQuantitySQL)){
                        stmt.setDouble(1,quantityToAdd);
                        stmt.setString(2,code);
                        stmt.executeUpdate();
                        System.out.println("refill del prodotto completato. Nuova quantita:"+(quantityBefore+quantityToAdd));
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }



        public void addTransaction(double price){
            String addTransactionQuery="INSERT INTO transactions (datetime, revenue) VALUES (now(),?)";
            try(Connection conn=DatabaseConnection.getConnection()){
                try(PreparedStatement stmt= conn.prepareStatement(addTransactionQuery)){
                    stmt.setDouble(1,price);
                    stmt.executeUpdate();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }



        public void buyBeverage(String code){
            boolean check = findByCodeIntoDatabase(code);
            if(!check){
                System.out.println("Codice non disponibile.");
            } else {
                Product r=getProductFromDatabase(code);
                if (r.getQuantity() <= 0) {
                    System.out.println("Prodotto esaurito.");
                    return;
                }
                if(credit>=r.getPrice()) {
                    String buyQuerySQL = "UPDATE products SET quantity = quantity-1 WHERE code=?";
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        try (PreparedStatement stmt = conn.prepareStatement(buyQuerySQL)) {
                            stmt.setString(1,code);
                            stmt.executeUpdate();
                            //metodo per aggiungere la trasazione al DB
                            addTransaction(r.getPrice());
                            setCredit(getCredit()-r.getPrice());
                            System.out.println("transazione avvenuta con successo.");
                            System.out.println(r.getName()+" erogata.");
                            }
                        } catch (
                            SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("credito non sufficiente.");
                }
            }
        }

        public void getInventoryFromDatabase(){
            String inventoryQuerySQL="SELECT name,code,price,quantity FROM products";
            try(Connection conn = DatabaseConnection.getConnection()){
                try(PreparedStatement stmt=conn.prepareStatement(inventoryQuerySQL)){
                    ResultSet rs=stmt.executeQuery();
                    while(rs.next()){
                        String name = rs.getString("name");
                        String code = rs.getString("code");
                        Double price = rs.getDouble("price");
                        int quantity = rs.getInt("quantity");
                        System.out.println("-----------------------");
                        System.out.println(code+" "+name+" "+price+"€ "+quantity);
                        System.out.println("-----------------------");
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }


        public void addCredit(double inputUtente){
            setCredit(getCredit()+inputUtente);
        }

        public void resetCredit() {
        //controlla se il credito e' superiore a zero
            if (getCredit() <= 0) {
            System.err.println("Nessun credito da erogare.");
                } else {
                     System.out.println("Erogazione di " + getCredit() + "€  in corso...");
            // Azzera il credito del cliente
            setCredit(0);
        }
    }

        public void getRevenue(){
            String queryRevenue="SELECT SUM(revenue) AS total_revenue FROM transactions";
            try(Connection conn= DatabaseConnection.getConnection()){
                try(Statement stmt = conn.createStatement()){
                    ResultSet rs = stmt.executeQuery(queryRevenue);
                    if (rs.next()){
                        double revenue = rs.getDouble("total_revenue");
                        System.out.println("Guadagno totale: "+ revenue+" €");
                    } else{
                        System.out.println("Nessun guadagno generato.");
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

}

