import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
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
	
	private ArrayList<Product> userProducts = new ArrayList<>();
	private JButton btnEnter;
	private JTextField tfInputField;
	
	private boolean isInputProductPhase = true;
	
	private void newButtonListener(ActionEvent e) {
		String input;
		if (tfInputField.getText().isEmpty() || tfInputField.getText().isBlank()) {
			System.out.println("test");
			if (isInputProductPhase) {
				JOptionPane.showMessageDialog(null, "Please Enter a Product Name!");
			} else {
				JOptionPane.showMessageDialog(null, "Please Enter an Expiry Date!");
			}
		} else {
			input = tfInputField.getText();
		}
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
			JPanel pnlProducts = new JPanel();
			pnlProducts.setBorder(BorderFactory.createTitledBorder("Products"));
				
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
								if (tfInputField.getText().equals("Enter a Product..."))
								{
									tfInputField.setText("");
								}
							}

							@Override
							public void focusLost(FocusEvent e)
							{
								if (tfInputField.getText().trim().isEmpty())
								{
									tfInputField.setText("Enter a Product...");
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