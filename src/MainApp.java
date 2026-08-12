import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** 
 * Program Name: MainApp.java
 * Purpose: TODO
 * @author CrppyPnts
 * Date: Aug 10, 2026
 */
public class MainApp extends JFrame
{
	private LocalDateTime deviceDateTime;
	private LocalTime deviceTime;
	private JTextField tfMainLabel;
	
	private JButton btnEnter;
	private JTextField tfInputField;
	
	MainApp(){
		super("ZeroSpoil");
		this.setLayout(new BorderLayout());
		
			// North - Time's and Dates
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd     HH:mm:ss");
			deviceDateTime = LocalDateTime.now();
			
			tfMainLabel = new JTextField(deviceDateTime.format(formatter));
			tfMainLabel.setFont(new Font("Arial", Font.BOLD, 15));
			tfMainLabel.setEditable(false);
			tfMainLabel.setBackground(Color.MAGENTA);
			tfMainLabel.setForeground(Color.CYAN);
			tfMainLabel.setHorizontalAlignment(JTextField.CENTER);
		
		this.add(tfMainLabel, BorderLayout.NORTH);
		
		// Sub-layout - for the Product's Display and Inputs
		JPanel subLayout = new JPanel(new GridLayout(0,3,10,10));
		
		  // 1st Column - User's Products 
			JPanel pnlProducts = new JPanel();
				
			// 2nd Column - User Inputs
			JPanel pnlInputs = new JPanel();
				btnEnter = new JButton("Enter");
				tfInputField = new JTextField("Enter a Product...");
				pnlInputs.add(btnEnter);
				pnlInputs.add(tfInputField);
				
		subLayout.add(pnlProducts);
		subLayout.add(pnlInputs);
		
		this.add(subLayout, BorderLayout.CENTER);
	
		// To Make Timer Run
			Timer timer = new Timer(1000, e -> {
				deviceDateTime = LocalDateTime.now();
				tfMainLabel.setText(deviceDateTime.format(formatter));
				});
			
		timer.start();
		
			
			
		
		this.setSize(1200,800);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainApp();
	}
}
//end of class