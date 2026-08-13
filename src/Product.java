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
	
	public Product(String name, LocalDate expiryDate){
		this.name = name;
		this.expiryDate = expiryDate;
	}
	
	public String getName() {
		return this.name;
	}
	
	public LocalDate getExpiryDate() {
		return this.expiryDate;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public void setExpirydate(LocalDate value) {
		this.expiryDate = value;
	}
	
}
//end of class