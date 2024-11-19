package medico_java_project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import dbconnection.DBconnection;

class Medicine {
    int id;
    String name;
    String category;
    int quantity;
    double price;
    int reorderPoint;
    
    public Medicine(int id, String name, String category, int quantity, double price, int reorderPoint) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.reorderPoint = reorderPoint;
    }
    
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getReorderPoint() {
		return reorderPoint;
	}


	public void setReorderPoint(int reorderPoint) {
		this.reorderPoint = reorderPoint;
	}
}

public class Medico {
	
	static Connection connection=DBconnection.mydbconnect();
    static Scanner s=new Scanner(System.in);
	
	private static void addNewMedicine() throws Exception {
		System.out.print("Enter Medicine ID:");
        int id = s.nextInt();
		System.out.print("Enter Medicine Name:");
        String name = s.next();
        System.out.print("Enter Category:");
        String category = s.next();
        System.out.print("Enter Quantity:");
        int quantity = s.nextInt();
        System.out.print("Enter Price per Unit:");
        float price = s.nextFloat();
        System.out.print("Enter Reorder Point:");
        int reorderPoint = s.nextInt();

        
            String query = "INSERT INTO Medicines (medicine_id,medicine_name, category, quantity, price_per_unit, reorder_point) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psadd = connection.prepareStatement(query);
            psadd.setInt(1, id);
            psadd.setString(2, name);
            psadd.setString(3, category);
            psadd.setInt(4, quantity);
            psadd.setFloat(5, price);
            psadd.setInt(6, reorderPoint);
            psadd.executeUpdate();
            System.out.println("Medicine added successfully!");
      
    }
	
	private static void deleteMedicine() throws Exception {
        System.out.print("Enter Medicine ID to delete:");
        int id = s.nextInt();
 
            String query1 = "DELETE FROM Sales WHERE medicine_id = ?";
            PreparedStatement psds = connection.prepareStatement(query1);
            psds.setInt(1, id);
            psds.executeUpdate();
            
            String query2 = "DELETE FROM Purchases WHERE medicine_id = ?";
            PreparedStatement psdp = connection.prepareStatement(query2);
            psdp.setInt(1, id);
            psdp.executeUpdate();

            String query = "DELETE FROM Medicines WHERE medicine_id = ?";
            PreparedStatement psd = connection.prepareStatement(query);
            psd.setInt(1, id);
            psd.executeUpdate();
            System.out.println("Medicine deleted successfully!");
        
    }
	
	private static void updateMedicineDetails() throws Exception {
        System.out.print("Enter Medicine ID to update:");
        int id = s.nextInt();
        System.out.print("Enter New Quantity:");
        int quantity = s.nextInt();
        System.out.print("Enter New Price per Unit:");
        double price = s.nextDouble();

        
            String query = "UPDATE Medicines SET quantity = ?, price_per_unit = ? WHERE medicine_id = ?";
            PreparedStatement psu = connection.prepareStatement(query);
            psu.setInt(1, quantity);
            psu.setDouble(2, price);
            psu.setInt(3, id);
            psu.executeUpdate();
            System.out.println("Medicine updated successfully!");
    }
	
	private static void viewAllMedicines() throws Exception{
	        String query = "SELECT * FROM Medicines";
	        Statement stu = connection.createStatement();
	        ResultSet rs = stu.executeQuery(query);

	        List<Medicine> medicines = new ArrayList<Medicine>();
	        while (rs.next()) {
	        	int id=rs.getInt("medicine_id");
	        	String name=rs.getString("medicine_name");
	        	String category=rs.getString("category");
	        	int quantity=rs.getInt("quantity");
	        	float price=rs.getFloat("price_per_unit");
	        	int reorder=rs.getInt("reorder_point");
	        	
	            Medicine medicine = new Medicine(id,name,category,quantity,price,reorder);
	            medicines.add(medicine);
	        }

	        System.out.println("ID\tName\t\tCategory\tQuantity\tPrice\tReorder point");
	        for (Medicine obj : medicines) {
	        	System.out.print("\n"+obj.getId()+"\t"+obj.getName()+"\t"+obj.getCategory()+"\t\t"+obj.getQuantity()+"\t\t"+obj.getPrice()+"\t"+obj.getReorderPoint());
	        }
	}
	
	private static void recordSale() throws Exception {
        System.out.print("Enter Medicine ID:");
        int medicineId = s.nextInt();
        System.out.print("Enter Sales ID:");
        int salesId = s.nextInt();
        System.out.print("Enter Quantity Sold:");
        int quantitySold = s.nextInt();
        s.nextLine();
        System.out.print("Enter The Date Of Sale(Eg:2002-04-20):");
        String dos=s.nextLine();

      
            String updateQuery = "UPDATE Medicines SET quantity = quantity - ? WHERE medicine_id = ?";
            PreparedStatement psum = connection.prepareStatement(updateQuery);
            psum.setInt(1, quantitySold);
            psum.setInt(2, medicineId);
            psum.executeUpdate();

            String insertQuery = "INSERT INTO Sales (sale_id,medicine_id, quantity_sold, sale_date) VALUES (?, ?, ?, ?)";
            PreparedStatement psis = connection.prepareStatement(insertQuery);
            psis.setInt(1, salesId);
            psis.setInt(2, medicineId);
            psis.setInt(3, quantitySold);
            
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date ddos=sdf.parse(dos);
            java.sql.Date  sdos=new java.sql.Date(ddos.getTime());
            psis.setDate(4, sdos);

            psis.executeUpdate();

            System.out.println("Sale recorded successfully!");
            checkReorder(medicineId);

        }
	
	private static void recordPurchase() throws Exception {
        System.out.print("Enter Purchase ID:");
        int purchaseId = s.nextInt();
        System.out.print("Enter Medicine ID:");
        int medicineId = s.nextInt();
        System.out.print("Enter Quantity Purchased:");
        int quantityPurchased = s.nextInt();
        s.nextLine();
        System.out.print("Enter The Date Of Purchase(Eg:2002-04-20):");
        String dop=s.nextLine();
        System.out.print("Enter Supplier Name:");
        String supplierName = s.next();

       
            String updateQuery = "UPDATE Medicines SET quantity = quantity + ? WHERE medicine_id = ?";
            PreparedStatement pstmt1 = connection.prepareStatement(updateQuery);
            pstmt1.setInt(1, quantityPurchased);
            pstmt1.setInt(2, medicineId);
            pstmt1.executeUpdate();

            String insertQuery = "INSERT INTO Purchases (purchase_id,medicine_id, quantity_purchased, purchase_date, supplier_name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psip= connection.prepareStatement(insertQuery);
            psip.setInt(1, purchaseId);
            psip.setInt(2, medicineId);
            psip.setInt(3, quantityPurchased);
            
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date ddop=sdf.parse(dop);
            java.sql.Date  sdop=new java.sql.Date(ddop.getTime());
            
            psip.setDate(4, sdop);

            psip.setString(5, supplierName);
            psip.executeUpdate();

            System.out.println("Purchase recorded successfully added!");
    }
	
	private static void checkReorder(int medicineId) throws Exception {
            String query = "SELECT quantity, reorder_point FROM Medicines WHERE medicine_id = ?";
            PreparedStatement psr = connection.prepareStatement(query);
            psr.setInt(1, medicineId);
            ResultSet rs = psr.executeQuery();

            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                int reorderPoint = rs.getInt("reorder_point");

                if (quantity <= reorderPoint) {
                    System.out.println("Warning: Medicine ID " + medicineId + " has reached its reorder point!");
                }
            }
    }
	
	private static void viewSalesAndPurchasesReport() throws Exception{
        System.out.print("Enter start date (YYYY-MM-DD):");
        String startDate = s.next();
        System.out.print("Enter end date (YYYY-MM-DD):");
        String endDate = s.next();

            String query = "SELECT s.sale_id, s.sale_date, m.medicine_name, s.quantity_sold, p.purchase_id, p.purchase_date, p.quantity_purchased, p.supplier_name " +
                           "FROM Sales s " +
                           "LEFT JOIN Medicines m ON s.medicine_id = m.medicine_id " +
                           "LEFT JOIN Purchases p ON s.medicine_id = p.medicine_id " +
                           "WHERE s.sale_date BETWEEN ? AND ? OR p.purchase_date BETWEEN ? AND ?";

            PreparedStatement pssp = connection.prepareStatement(query);
            
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sd=sdf.parse(startDate);
            java.sql.Date  sdd=new java.sql.Date(sd.getTime());
            
            java.util.Date ed=sdf.parse(endDate);
            java.sql.Date  edd=new java.sql.Date(ed.getTime());
            
            
            pssp.setDate(1,sdd);
            pssp.setDate(2,edd);
            pssp.setDate(3,sdd);
            pssp.setDate(4,edd);
            
            ResultSet rs = pssp.executeQuery();

            System.out.println("\nSales and Purchases Report");
            while (rs.next()) {
                int saleId = rs.getInt("sale_id");
                Date saleDate = rs.getDate("sale_date");
                String medicineName = rs.getString("medicine_name");
                int quantitySold = rs.getInt("quantity_sold");

                int purchaseId = rs.getInt("purchase_id");
                Date purchaseDate = rs.getDate("purchase_date");
                int quantityPurchased = rs.getInt("quantity_purchased");
                String supplierName = rs.getString("supplier_name");

                System.out.println("Sale ID: " + saleId + ", Sale Date: " + saleDate + ", Medicine: " + medicineName + ", Quantity Sold: " + quantitySold);
                if (purchaseId > 0) {
                    System.out.println("Purchase ID: " + purchaseId + ", Purchase Date: " + purchaseDate + ", Quantity Purchased: " + quantityPurchased + ", Supplier: " + supplierName);
                }
            }    
	   }
	
	private static void viewMedicines() throws Exception{
		System.out.print("Enter Medicine ID:");
        int medicineId = s.nextInt();
        String query = "SELECT * FROM Medicines WHERE medicine_id =?";
        PreparedStatement psv=connection.prepareStatement(query);
        psv.setInt(1, medicineId);
        ResultSet rs=psv.executeQuery();
        
        List<Medicine> medicines = new ArrayList<Medicine>();
        if (rs.next()) {
        	int id=rs.getInt("medicine_id");
        	String name=rs.getString("medicine_name");
        	String category=rs.getString("category");
        	int quantity=rs.getInt("quantity");
        	float price=rs.getFloat("price_per_unit");
        	int reorder=rs.getInt("reorder_point");
        	
            Medicine medicine = new Medicine(id,name,category,quantity,price,reorder);
            medicines.add(medicine);
        }

        System.out.println("ID\tName\t\tCategory\tQuantity\tPrice\tReorder_point");
        for (Medicine obj : medicines) {
        	System.out.print(obj.getId()+"\t"+obj.getName()+"\t"+obj.getCategory()+"\t\t"+obj.getQuantity()+"\t\t"+obj.getPrice()+"\t"+obj.getReorderPoint());
        }
}
	
	


	public static void main(String[] args) throws Exception {
		//Connection con=DBconnection.mydbconnect();
     
        while (true) {
            System.out.println("\n Medico Application Menu");
            System.out.println("\n1.Add New Medicine");
            System.out.println("\n2.Update Medicine Details");
            System.out.println("\n3.Delete Medicine");
            System.out.println("\n4.View All Medicines");
            System.out.println("\n5.Record a Sale");
            System.out.println("\n6.Record a Purchase");
            System.out.println("\n7.View Sales and Purchases Report");
            System.out.println("\n8.View Perticular Medicine Info");
            System.out.println("\n9.Exit");
            System.out.print("\nChoose an option: ");
            int choice = s.nextInt();

            switch (choice) {
                case 1:
                    addNewMedicine();
                    break;
                case 2:
                    updateMedicineDetails();
                    break;
                case 3:
                    deleteMedicine();
                    break;
                case 4:
                    viewAllMedicines();
                    break;
                case 5:
                    recordSale();
                    break;
                case 6:
                    recordPurchase();
                    break;
                case 7:
                    viewSalesAndPurchasesReport();
                    break;
                case 8:
                	viewMedicines();
                	break;
                case 9:
                	connection.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
	}
}
