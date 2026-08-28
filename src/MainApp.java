import javax.swing.*;
import java.io.File;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
	
	private final String[] OPTIONS = {"Name","Date","Cancel"};
	
	private JPanel pnlProductsSub;
	private LocalDateTime deviceDateTime;
	private JTextField tfMainLabel;
	
	
	private JButton btnEnter;
	private JButton btnSearch;
	private JButton btnFilter;
	private JTextField tfFilterInputField;
	private JTextField tfInputField;
	
	private ArrayList<Product> userProducts = new ArrayList<>();
	
	private String productName;
	private LocalDate expiryDate;
	
	private boolean isInputProductPhase = true;
	
	// File I/O Methods
	private void createSaveFile() {
		
		try {
			File file = new File("products.csv");
			FileWriter fileWriter = new FileWriter(file);
			
			fileWriter.write("ProductID,Name,Expiry\n");
			
			for (Product product : userProducts) {
				fileWriter.write(product.getProductID() +  ","
						+ product.getName() + ","
						+ product.getExpiryDate()
						+ "\n"
						);
			}
			
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSaveFile() { 
		try {
			
			userProducts.clear();
			pnlProductsSub.removeAll();
			Scanner scanner = new Scanner(new File("products.csv"));
			
			scanner.nextLine();
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
			
				String[] itemData = line.split(",");
				
				long productID = Long.parseLong(itemData[0]);
				String name = itemData[1];
				LocalDate expiryDate = LocalDate.parse(itemData[2]);

				 Product product = new Product(
			        name,
			        expiryDate,
			        productID
			    );

			    userProducts.add(product);

			    addProductToList(product);
			}
			
			scanner.close();
			
			pnlProductsSub.revalidate();
      pnlProductsSub.repaint();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No Save Files Found... Create a New One!");
		}
		
	}
	
	private void saveData() {
		try {
			FileWriter fileWriter = new FileWriter("products.csv");
			
			fileWriter.write("ProductID,Name,Expiry\n");
			
			for (Product product : userProducts) {
				fileWriter.write(product.getProductID() +  ","
						+ product.getName() + ","
						+ product.getExpiryDate()
						+ "\n"
						);
			}
		
			
			fileWriter.close();
		}catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No Save Files Found... Create a New One!");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	// Listener Methods
	private void newAddButtonListener(ActionEvent e) {
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
				inputExpiryDatePhase();
			}
		}
	}
	
	// App Logic Methods
	private boolean inputProductPhase() {
	  this.productName = tfInputField.getText();
	  if (productName.length() > 10) {
	  	JOptionPane.showMessageDialog(null, "Sorry Name Cannot be >=10 :(");
	  	return false;
	  }
	  	
	  isInputProductPhase = false;
		tfInputField.setText("Enter an Expiry Date...");
		return true;
	}
	
	private boolean inputExpiryDatePhase() {
		try {
			this.expiryDate = LocalDate.parse(tfInputField.getText());
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(null, "Invalid Date!\n(yyyy-MM-dd)");
			return false;
		}
		
		Product product = new Product(productName,expiryDate);
		
		userProducts.add(product);
		
		isInputProductPhase = true;
		tfInputField.setText("Enter a Product...");
		
		addProductToList(product);
		
		return true;
	}
	
	private void addProductToList(Product product) {

    JPanel pnlMainProductPanel = new JPanel();
    pnlMainProductPanel.setLayout(new BoxLayout(pnlMainProductPanel, BoxLayout.Y_AXIS));
    
    JPanel pnlOrganizer = new JPanel();
    pnlOrganizer.setLayout(new BoxLayout(pnlOrganizer, BoxLayout.X_AXIS));
    
    JLabel lblProductDetails = new JLabel(
        product.getName()
        + "    -    "
        + calculateDaysLeft(
            product.getExpiryDate()
        )
    );
    
    // Remove Button - List
    JButton btnRemoveButton = new JButton("X");
    btnRemoveButton.setActionCommand(String.valueOf(product.getProductID()));
    btnRemoveButton.addActionListener((ae) -> {
    	
      long productID = Long.parseLong(ae.getActionCommand());

      for (int i = 0; i < userProducts.size(); i++) {

          if (userProducts.get(i).getProductID() == productID) {
              userProducts.remove(i);
              pnlProductsSub.remove(pnlMainProductPanel);
              
              saveData();
              pnlProductsSub.revalidate();
              pnlProductsSub.repaint();
              break;
          }
      }
    		
    });
    
    // Edit Button - List
    JButton btnEditButton = new JButton("Edit");
    btnEditButton.setActionCommand(String.valueOf(product.getProductID()));
    btnEditButton.addActionListener((ae) -> {
    		
    	long productID = Long.parseLong(ae.getActionCommand());
    	String input;
    	LocalDate newExpiry = null;
    	
    	int choice = JOptionPane.showOptionDialog(
    			 null,
    			 "Select a Detail to Edit...", 
    			 "", 
    			 JOptionPane.YES_NO_CANCEL_OPTION, 
    			 JOptionPane.QUESTION_MESSAGE,
    			 null, 
    			 OPTIONS, 
    			 OPTIONS[2]
    	);
    	 
    	if ( choice == JOptionPane.YES_OPTION) {
    		input = JOptionPane.showInputDialog("Enter a New Product Name:");
    		
      	for (int i = 0; i < userProducts.size(); i++) {
    			if (userProducts.get(i).getProductID() == productID) {
    				userProducts.get(i).setName(input);

    				lblProductDetails.setText(
    				    userProducts.get(i).getName()
    				    + " - "
    				    + calculateDaysLeft(
    				        userProducts.get(i).getExpiryDate()
    				    )
    				);

    				saveData();
    				pnlProductsSub.revalidate();
    				pnlProductsSub.repaint();
           break;
    			}
      	}
    	} else if ( choice == JOptionPane.NO_OPTION) {
    		while (true) {
    	    input = JOptionPane.showInputDialog(
    	        "Enter a New Product Expiry Date:"
    	    );

    	    if (input == null) {
    	        return;
    	    }

    	    try {
    	        newExpiry = LocalDate.parse(input);
    	        break;
    	    } catch (DateTimeParseException e) {
    	        JOptionPane.showMessageDialog(
    	            null,
    	            "Invalid Date!\n(yyyy-MM-dd)"
    	        );
    	    }
    	}
    		 
    			for (int i = 0; i < userProducts.size(); i++) {
      			if (userProducts.get(i).getProductID() == productID) {
      				userProducts.get(i).setExpirydate(newExpiry);

      				lblProductDetails.setText(
      				    userProducts.get(i).getName()
      				    + "    -    "
      				    + calculateDaysLeft(
      				        userProducts.get(i).getExpiryDate()
      				    )
      				);

      				saveData();
      				pnlProductsSub.revalidate();
      				pnlProductsSub.repaint();
             break;
      			}
        	}
    	} else {
    		 JOptionPane.showMessageDialog(null, "Edit Cancelled..");
    	}
    });
    
    lblProductDetails.setFont(
        new Font("Arial", Font.BOLD, 12)
    );

    
    pnlOrganizer.add(lblProductDetails);
    pnlOrganizer.add(Box.createHorizontalGlue());
    pnlOrganizer.add(btnRemoveButton);
    pnlOrganizer.add(btnEditButton);

    pnlMainProductPanel.add(pnlOrganizer);
    
    pnlProductsSub.add(pnlMainProductPanel);

    pnlProductsSub.revalidate();
    pnlProductsSub.repaint();
}
	
	private String calculateDaysLeft(LocalDate productExpiryDate) {
		LocalDate currentDate = LocalDate.now();
		long daysLeft = ChronoUnit.DAYS.between(currentDate, productExpiryDate);
		if (daysLeft > 1) {
			return "Expires in " + daysLeft + " days...";
		} else if (daysLeft == 1) {
			return "Expires in " + daysLeft + " day!!";
		} else if (daysLeft == 0){
			return "Expires today!!!";
		} else if (daysLeft < 0){
			return "Expired :(";
		} else {
			return "Error";
		}
	}
	
	// Main Object
	MainApp(){
		super("ZeroSpoil");
		this.setLayout(new BorderLayout());
		
			// Main Menu
		
			JMenuBar menuBar = new JMenuBar();
				
				// MenuBar Items
				JMenu moSettings = new JMenu("⚙");
				
					Path filePath = Paths.get("products.csv");
					JMenuItem miSave = new JMenuItem("Save File");
					miSave.addActionListener((ae)-> {
						if (userProducts.size() < 1) {
							JOptionPane.showMessageDialog(null, "There's nothing to Save Currently! Enter an Item. ");
						} else {
							if (Files.exists(filePath)) {
								saveData();
							} else {
								createSaveFile();
							}
						}
					});
					
					JMenuItem miLoad = new JMenuItem("Load File");
					miLoad.addActionListener((ae)-> {
						loadSaveFile();
					});
					
				moSettings.add(miSave);
				moSettings.add(miLoad);
				
			menuBar.add(moSettings);
			
		this.setJMenuBar(menuBar);
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
			pnlProducts.setLayout(new BorderLayout());
			pnlProducts.setBorder(BorderFactory.createTitledBorder("Products"));
			
				JPanel pnlSearchSection = new JPanel();
				pnlSearchSection.setLayout(new BoxLayout(pnlSearchSection, BoxLayout.X_AXIS));
					tfFilterInputField = new JTextField("Search a Product...");
					btnSearch = new JButton("Search");
					btnFilter = new JButton("=");
				pnlSearchSection.add(tfFilterInputField);
				pnlSearchSection.add(btnSearch);
				pnlSearchSection.add(btnFilter);
			
				pnlProductsSub = new JPanel();
				pnlProductsSub.setLayout(new BoxLayout(pnlProductsSub,BoxLayout.Y_AXIS));
				JScrollPane scrollpane = new JScrollPane(pnlProductsSub);
				scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				
			pnlProducts.add(pnlSearchSection, BorderLayout.NORTH);
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
						btnEnter.addActionListener((ae) -> newAddButtonListener(ae));
						
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