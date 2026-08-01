import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MainMenu extends JFrame implements GameView {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private CardLayout cardLayout;
    private JLabel lblInventoryTitle;
    private JLabel lblSpellbookTitle;  
    private JTextField txtNameInput;
    private JLabel lblDashboardStats;   
    private JTable tblInventory;
    private JTable tblSpellbook;
    private JTable tblMarket;
    private JTable tblRecipeSpellbook;   
    private JTextField txtMarketSlot;
    private JTextField txtMarketQty;
    private JTextField txtRecipeId;
    private JTextField txtCreativeBase;
    private JTextField txtCreativeFruits;    
    private JButton btnNewGame, btnLoadGame, btnExitMenu, btnStartJourney;
    private JButton btnBrewChoice, btnVisitMarket, btnClaimBonus, btnBlessCauldron, btnExitSave;
    private JButton btnRecipeMode, btnCreativeMode, btnBackFromBrewChoice;
    private JButton btnBuyMarket, btnBackFromMarket;
    private JButton btnExecuteRecipe, btnBackFromRecipe;
    private JButton btnExecuteCreative, btnBackFromCreative;
    
    
    private GameController controller;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainMenu frame = new MainMenu();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainMenu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        
        cardLayout = new CardLayout(0, 0);
        contentPane.setLayout(cardLayout);
        
        initMenuScreen();
        initNameInputScreen();
        initDashboardScreen();
        initBrewChoiceScreen();
        initMarketScreen();
        initRecipeModeScreen();
        initCreativeModeScreen();
        
        this.controller = new GameController(this);
    }

    private void initMenuScreen() {
        JPanel pnlMenu = new JPanel(null);
        JLabel lblTitle = new JLabel("Welcome to Potion Prodigy", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(200, 100, 400, 50);
        pnlMenu.add(lblTitle);
        
        btnNewGame = new JButton("New Game");
        btnNewGame.setBounds(300, 200, 200, 50);
        pnlMenu.add(btnNewGame);
        
        btnLoadGame = new JButton("Load Game");
        btnLoadGame.setBounds(300, 280, 200, 50);
        pnlMenu.add(btnLoadGame);
        
        btnExitMenu = new JButton("Exit Game");
        btnExitMenu.setBounds(300, 360, 200, 50);
        pnlMenu.add(btnExitMenu);
        
        contentPane.add(pnlMenu, "Menu");
    }

    private void initNameInputScreen() {
        JPanel pnlName = new JPanel(null);
        JLabel lblPrompt = new JLabel("What's your name wizard?", SwingConstants.CENTER);
        lblPrompt.setFont(new Font("Arial", Font.PLAIN, 20));
        lblPrompt.setBounds(200, 150, 400, 40);
        pnlName.add(lblPrompt);
        
        txtNameInput = new JTextField();
        txtNameInput.setBounds(250, 220, 300, 40);
        pnlName.add(txtNameInput);
        
        btnStartJourney = new JButton("OK");
        btnStartJourney.setBounds(350, 290, 100, 40);
        pnlName.add(btnStartJourney);
        
        contentPane.add(pnlName, "nameInput");
    }

    private void initDashboardScreen() {
        JPanel pnlDash = new JPanel(null);
        
        lblDashboardStats = new JLabel("Greetings Wizard | Crystals: 0 | Usable Cauldron: 3");
        lblDashboardStats.setFont(new Font("Arial", Font.BOLD, 16));
        lblDashboardStats.setBounds(20, 20, 700, 30);
        pnlDash.add(lblDashboardStats);
        
        lblInventoryTitle = new JLabel("Inventory");
        lblInventoryTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblInventoryTitle.setBounds(20, 50, 350, 20);
        pnlDash.add(lblInventoryTitle);
        
        lblSpellbookTitle = new JLabel("Spellbook");
        lblSpellbookTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblSpellbookTitle.setBounds(400, 50, 350, 20);
        pnlDash.add(lblSpellbookTitle);
        
        
        tblInventory = new JTable(new DefaultTableModel(new Object[]{"Ingredient", "Quantity"}, 0));
        JScrollPane scrollInv = new JScrollPane(tblInventory);
        scrollInv.setBounds(20, 70, 350, 350);
        pnlDash.add(scrollInv);
        
        tblSpellbook = new JTable(new DefaultTableModel(new Object[]{"Potion Name", "Recipe ID"}, 0));
        JScrollPane scrollSpell = new JScrollPane(tblSpellbook);
        scrollSpell.setBounds(400, 70, 350, 200);
        pnlDash.add(scrollSpell);
        
        btnBrewChoice = new JButton("Brew a Potion!");
        btnBrewChoice.setBounds(400, 290, 165, 40);
        pnlDash.add(btnBrewChoice);
        
        btnVisitMarket = new JButton("Visit Market");
        btnVisitMarket.setBounds(585, 290, 165, 40);
        pnlDash.add(btnVisitMarket);
        
        btnClaimBonus = new JButton("Claim Login Bonus");
        btnClaimBonus.setBounds(400, 340, 165, 40);
        pnlDash.add(btnClaimBonus);
        
        btnBlessCauldron = new JButton("Bless A Cauldron!");
        btnBlessCauldron.setBounds(585, 340, 165, 40);
        pnlDash.add(btnBlessCauldron);
        
        btnExitSave = new JButton("Exit and Save");
        btnExitSave.setBounds(400, 390, 165, 40);
        pnlDash.add(btnExitSave);
        
        contentPane.add(pnlDash, "Dashboard");
    }

    private void initBrewChoiceScreen() {
        JPanel pnlBrew = new JPanel(null);
        JLabel lblPrompt = new JLabel("Do you want to re-brew a concoction or be a daredevil?", SwingConstants.CENTER);
        lblPrompt.setFont(new Font("Arial", Font.PLAIN, 18));
        lblPrompt.setBounds(100, 150, 600, 40);
        pnlBrew.add(lblPrompt);
        
        btnRecipeMode = new JButton("Recipe Mode");
        btnRecipeMode.setBounds(200, 230, 180, 50);
        pnlBrew.add(btnRecipeMode);
        
        btnCreativeMode = new JButton("Creative Mode");
        btnCreativeMode.setBounds(420, 230, 180, 50);
        pnlBrew.add(btnCreativeMode);
        
        btnBackFromBrewChoice = new JButton("Go Back");
        btnBackFromBrewChoice.setBounds(310, 320, 180, 50);
        pnlBrew.add(btnBackFromBrewChoice);
        
        contentPane.add(pnlBrew, "BREW_CHOICE");
    }

    private void initMarketScreen() {
        JPanel pnlMarket = new JPanel(null);
        
        JLabel lblTitle = new JLabel("Greetings wizard, anything tickling your fancy?");
        lblTitle.setBounds(20, 20, 400, 30);
        pnlMarket.add(lblTitle);
        
        tblMarket = new JTable(new DefaultTableModel(new Object[]{"Slot", "Ingredient", "Quantity", "Price"}, 0));
        JScrollPane scrollMarket = new JScrollPane(tblMarket);
        scrollMarket.setBounds(20, 60, 450, 450);
        pnlMarket.add(scrollMarket);
        
        JLabel lblSlot = new JLabel("Which slot?");
        lblSlot.setBounds(550, 150, 100, 25);
        pnlMarket.add(lblSlot);
        
        txtMarketSlot = new JTextField();
        txtMarketSlot.setBounds(550, 180, 150, 30);
        pnlMarket.add(txtMarketSlot);
        
        JLabel lblQty = new JLabel("How many?");
        lblQty.setBounds(550, 230, 100, 25);
        pnlMarket.add(lblQty);
        
        txtMarketQty = new JTextField();
        txtMarketQty.setBounds(550, 260, 150, 30);
        pnlMarket.add(txtMarketQty);
        
        btnBuyMarket = new JButton("Buy");
        btnBuyMarket.setBounds(550, 310, 150, 40);
        pnlMarket.add(btnBuyMarket);
        
        btnBackFromMarket = new JButton("Go Back");
        btnBackFromMarket.setBounds(550, 360, 150, 40);
        pnlMarket.add(btnBackFromMarket);
        
        contentPane.add(pnlMarket, "Market");
    }

    private void initRecipeModeScreen() {
        JPanel pnlRecipe = new JPanel(null);
        
        tblRecipeSpellbook = new JTable(new DefaultTableModel(new Object[]{"Potion Name", "Recipe ID"}, 0));
        JScrollPane scrollRSpell = new JScrollPane(tblRecipeSpellbook);
        scrollRSpell.setBounds(100, 50, 600, 250);
        pnlRecipe.add(scrollRSpell);
        
        JLabel lblInput = new JLabel("Please input the recipe id you want to brew:", SwingConstants.CENTER);
        lblInput.setBounds(100, 320, 600, 30);
        pnlRecipe.add(lblInput);
        
        txtRecipeId = new JTextField();
        txtRecipeId.setBounds(300, 360, 200, 40);
        pnlRecipe.add(txtRecipeId);
        
        btnExecuteRecipe = new JButton("Brew");
        btnExecuteRecipe.setBounds(300, 420, 95, 40);
        pnlRecipe.add(btnExecuteRecipe);
        
        btnBackFromRecipe = new JButton("Go Back");
        btnBackFromRecipe.setBounds(405, 420, 95, 40);
        pnlRecipe.add(btnBackFromRecipe);
        
        contentPane.add(pnlRecipe, "recipeMode");
    }

    private void initCreativeModeScreen() {
        JPanel pnlCreative = new JPanel(null);
        
        JLabel lblBase = new JLabel("Enter Base Ingredient:");
        lblBase.setBounds(100, 100, 200, 30);
        pnlCreative.add(lblBase);
        
        txtCreativeBase = new JTextField();
        txtCreativeBase.setBounds(100, 140, 250, 40);
        pnlCreative.add(txtCreativeBase);
        
        JLabel lblFruits = new JLabel("Enter Fruits (comma separated):");
        lblFruits.setBounds(400, 100, 250, 30);
        pnlCreative.add(lblFruits);
        
        txtCreativeFruits = new JTextField();
        txtCreativeFruits.setBounds(400, 140, 250, 40);
        pnlCreative.add(txtCreativeFruits);
        
        JLabel lblConfirm = new JLabel("Are you sure with this combination?", SwingConstants.CENTER);
        lblConfirm.setBounds(200, 250, 400, 30);
        pnlCreative.add(lblConfirm);
        
        btnExecuteCreative = new JButton("Yes");
        btnExecuteCreative.setBounds(250, 300, 100, 50);
        pnlCreative.add(btnExecuteCreative);
        
        btnBackFromCreative = new JButton("No");
        btnBackFromCreative.setBounds(400, 300, 100, 50);
        pnlCreative.add(btnBackFromCreative);
        
        contentPane.add(pnlCreative, "creativeMode");
    }


    @Override
    public void updateDashboardStats(String name, int crystals, int usableCauldrons) {
        lblDashboardStats.setText("Greetings " + name + " | Crystals: " + crystals + " | Usable Cauldron: " + usableCauldrons);
    }

    @Override
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void showScreen(String screenName) {
        cardLayout.show(contentPane, screenName);
    }

    @Override
    public void updateInventoryTable(Object[][] data) {
        tblInventory.setModel(new DefaultTableModel(data, new Object[]{"Ingredient", "Quantity"}));
    }

    @Override
    public void updateSpellbookTable(Object[][] data) {
        tblSpellbook.setModel(new DefaultTableModel(data, new Object[]{"Potion Name", "Recipe ID"}));
        tblRecipeSpellbook.setModel(new DefaultTableModel(data, new Object[]{"Potion Name", "Recipe ID"}));
    }

    @Override
    public void updateMarketTable(Object[][] data) {
        tblMarket.setModel(new DefaultTableModel(data, new Object[]{"Slot", "Ingredient", "Quantity", "Price"}));
    }

    @Override 
    public String getNameInput() { 
    	return txtNameInput.getText(); 
    	}
    
    @Override 
    public String getMarketSlotInput() { 
    	return txtMarketSlot.getText(); 
    	}
    
    @Override 
    public String getMarketQtyInput() { 
    	return txtMarketQty.getText(); 
    	}
    
    @Override 
    public String getRecipeIdInput() { 
    	return txtRecipeId.getText(); 
    	}
    
    @Override 
    public String getCreativeBaseInput() { 
    	return txtCreativeBase.getText(); 
    	}
    
    @Override 
    public String getCreativeFruitInput() { 
    	return txtCreativeFruits.getText(); 
    	}

    @Override 
    public void addNewGameListener(ActionListener l) { 
    	btnNewGame.addActionListener(l); 
    	}
    
    @Override 
    public void addLoadGameListener(ActionListener l) { 
    	btnLoadGame.addActionListener(l); 
    	}
    
    @Override 
    public void addExitMenuListener(ActionListener l) { 
    	btnExitMenu.addActionListener(l); 
    	}
    
    @Override 
    public void addStartJourneyListener(ActionListener l) { 
    	btnStartJourney.addActionListener(l); 
    	}
    
    @Override 
    public void addBrewChoiceListener(ActionListener l) { 
    	btnBrewChoice.addActionListener(l); 
    	}
    
    @Override 
    public void addVisitMarketListener(ActionListener l) { 
    	btnVisitMarket.addActionListener(l); 
    	}
    
    @Override 
    public void addClaimBonusListener(ActionListener l) { 
    	btnClaimBonus.addActionListener(l); 
    	}
    
    @Override 
    public void addBlessCauldronListener(ActionListener l) { 
    	btnBlessCauldron.addActionListener(l); 
    	}
    
    @Override 
    public void addExitAndSaveListener(ActionListener l) { 
    	btnExitSave.addActionListener(l); 
    	}
    
    @Override 
    public void addNavRecipeModeListener(ActionListener l) { 
    	btnRecipeMode.addActionListener(l); 
    	}
    
    @Override 
    public void addNavCreativeModeListener(ActionListener l) { 
    	btnCreativeMode.addActionListener(l); 
    	}
    
    @Override 
    public void addNavBackToDashboardListener(ActionListener l) { 
        btnBackFromBrewChoice.addActionListener(l);
        btnBackFromMarket.addActionListener(l);
        btnBackFromRecipe.addActionListener(l);
        btnBackFromCreative.addActionListener(l);
    }
    
    @Override 
    public void addActionBuyMarketListener(ActionListener l) { 
    	btnBuyMarket.addActionListener(l); 
    	}
    
    @Override 
    public void addActionBrewRecipeListener(ActionListener l) { 
    	btnExecuteRecipe.addActionListener(l); 
    	}
    
    @Override 
    public void addActionBrewCreativeListener(ActionListener l) { 
    	btnExecuteCreative.addActionListener(l); 
    	}
    
    @Override
    public void updateTableTitles(String invTitle, String spellTitle) {
        lblInventoryTitle.setText(invTitle);
        lblSpellbookTitle.setText(spellTitle);
    }
}