import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/** 
 * Program Name: MainApp.java
 * Purpose: TODO
 * @author CrppyPnts
 * Date: Aug 10, 2026
 */
public class MainApp extends JFrame
{
	private JPanel pnlProductsSub;
	private LocalDateTime deviceDateTime;
	private JTextField tfMainLabel;
	private JButton btnEnter;
	private JTextField tfInputField;
	
	private ArrayList<Product> userProducts = new ArrayList<>();
	private ArrayList<JPanel> pnlProductListVisuals = new ArrayList<>();
	private ArrayList<JLabel> lblProductDetails = new ArrayList<>();
	private JButton[] jbRemoveButton; // do this later
	
	private String productName;
	private LocalDate expiryDate;
	
	
	private boolean isInputProductPhase = true;
	
	private void newButtonListener(ActionEvent e) {
		if (tfInputField.getText().trim().isEmpty() || tfInputField.getText().equals("Enter a Product...") || 
				tfInputField.getText().equals("Enter an Expiry Date...")) {
			if (isInputProductPhase) {
				JOptionPane.showMessageDialog(null, "Please Enter a Product Name!");
			} else {
				JOptionPane.showMessageDialog(null, "Please Enter an Expiry Date!");
			}
		} else {
			if (isInputProductPhase) {
				inputProductPhase();
			} else {
				if (inputExpiryDatePhase()) {
					addProductToList();
				}
			}
		}
	}
	
	private void inputProductPhase() {
	  this.productName = tfInputField.getText();
	  isInputProductPhase = false;
		tfInputField.setText("Enter an Expiry Date...");
	}
	
	private boolean inputExpiryDatePhase() {
		try {
			this.expiryDate = LocalDate.parse(tfInputField.getText());
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(null, "Invalid Date!\n(yyyy-MM-dd)");
			return false;
		}
		userProducts.add(new Product(productName,expiryDate));
		isInputProductPhase = true;
		tfInputField.setText("Enter a Product...");
		return true;
	}
	
	private void addProductToList() {
		int index = 0;
		
		for (int i = 0; i < userProducts.size(); i++) {
			if (productName.equals(userProducts.get(i).getName())) {
				index = i;
				break;
			}
		}
		
			pnlProductListVisuals.add(new JPanel());
			pnlProductListVisuals.get(index).setLayout(new BoxLayout(pnlProductListVisuals.get(index), BoxLayout.Y_AXIS));
			
			lblProductDetails.add(new JLabel(userProducts.get(index).getName() + "     -     " 
			+ calculateDaysLeft(userProducts.get(index).getExpiryDate())));
			lblProductDetails.get(index).setFont(new Font("Arial", Font.BOLD, 12));
			
			pnlProductListVisuals.get(index).add(lblProductDetails.get(index));
			
			pnlProductsSub.add(pnlProductListVisuals.get(index));
		
		pnlProductsSub.revalidate();
		pnlProductsSub.repaint();
	}
	
	private String calculateDaysLeft(LocalDate productExpiryDate) {
		LocalDate currentDate = LocalDate.now();
		long daysLeft = ChronoUnit.DAYS.between(currentDate, productExpiryDate);
		return (daysLeft > 1) ?  "Expires in " + daysLeft + " days..." : "Expires in " + daysLeft + " day!!!";
	}
	
	
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
			JPanel pnlProducts = new JPanel(new BorderLayout());
			pnlProducts.setBorder(BorderFactory.createTitledBorder("Products"));
			
				pnlProductsSub = new JPanel();
				pnlProductsSub.setLayout(new BoxLayout(pnlProductsSub,BoxLayout.Y_AXIS));
				
				JScrollPane scrollpane = new JScrollPane(pnlProductsSub);
				scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				
			pnlProducts.add(scrollpane, BorderLayout.CENTER);
				
			// 2nd Column - User Inputs
			JPanel pnlInputs = new JPanel(new BorderLayout());
			pnlInputs.setBorder(BorderFactory.createRaisedBevelBorder());
			
				JPanel pnlInputsSubPanel = new JPanel();
				
					btnEnter = new JButton("Add");
						btnEnter.setPreferredSize(new Dimension(100,30));
						btnEnter.setFont(new Font("Arial", Font.BOLD, 12));
						btnEnter.setBackground(Color.MAGENTA);
						btnEnter.setForeground(Color.WHITE);
						btnEnter.addActionListener((ae) -> newButtonListener(ae));
						
					tfInputField = new JTextField("Enter a Product...");
						tfInputField.setFont(new Font("Arial", Font.ITALIC, 12));
						tfInputField.setPreferredSize(new Dimension(150,30));
						tfInputField.addFocusListener(new FocusAdapter() {
							@Override
							public void focusGained(FocusEvent e)
							{
								if (tfInputField.getText().equals("Enter a Product...") || tfInputField.getText().equals("Enter an Expiry Date..."))
								{
									tfInputField.setText("");
								}
							}

							@Override
							public void focusLost(FocusEvent e)
							{
								if (tfInputField.getText().trim().isEmpty())
								{
									if (isInputProductPhase) {
										tfInputField.setText("Enter a Product...");
									} else {
										tfInputField.setText("Enter an Expiry Date...");
									}
								}
							}
						});
				pnlInputsSubPanel.add(btnEnter);
				pnlInputsSubPanel.add(tfInputField);
			pnlInputs.add(pnlInputsSubPanel, BorderLayout.SOUTH);
			
			// 3rd Column - Recommended Recipes
			JPanel pnlRecipes = new JPanel();
			pnlRecipes.setBorder(BorderFactory.createTitledBorder("Recipes"));
				
		subLayout.add(pnlProducts);
		subLayout.add(pnlInputs);
		subLayout.add(pnlRecipes);
		
		this.add(subLayout, BorderLayout.CENTER);
	
		// To Make Timer Run
			Timer timer = new Timer(1000, e -> {
				deviceDateTime = LocalDateTime.now();
				tfMainLabel.setText(deviceDateTime.format(formatter));
				});
			
		timer.start();
		
			
			
		
		this.setSize(900,800);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainApp();
	}
}
//end of class