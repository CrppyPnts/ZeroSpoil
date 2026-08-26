import java.time.LocalDate;

/** 
 * Program Name: MainApp.java
 * Purpose: TODO
 * @author CrppyPnts
 * Date: Aug 10, 2026
 */
public class Product
{
	private String name;
	private LocalDate expiryDate;
	private long productID;
	private static long nextProductID = 0;
	
	public Product(String name, LocalDate expiryDate){
		this.name = name;
		this.expiryDate = expiryDate;
		this.productID = nextProductID++;
	}
	
	public Product(String name, LocalDate expiryDate, long productID) {
		this.name = name;
		this.expiryDate = expiryDate;
		this.productID = productID;
	}
	
	//getters
	public String getName() {
		return this.name;
	}
	
	public LocalDate getExpiryDate() {
		return this.expiryDate;
	}
	
	public long getProductID() {
		return this.productID;
	}
	
	//setters
	public void setName(String value) {
		this.name = value;
	}
	
	public void setExpirydate(LocalDate value) {
		this.expiryDate = value;
	}
	
}
//end of class