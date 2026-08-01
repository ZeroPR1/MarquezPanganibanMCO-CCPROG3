import java.awt.event.ActionListener;

public interface GameView {
    void updateDashboardStats(String name, int crystals, int usableCauldrons);
    void displayMessage(String message);
    void showScreen(String screenName);

    void updateInventoryTable(Object[][] inventoryData);
    void updateSpellbookTable(Object[][] spellbookData);
    void updateMarketTable(Object[][] marketData);
    void updateTableTitles(String invTitle, String spellTitle);

    String getNameInput();
    String getMarketSlotInput();
    String getMarketQtyInput();
    String getRecipeIdInput();
    String getCreativeBaseInput(); 
    String getCreativeFruitInput(); 

    void addNewGameListener(ActionListener l);
    void addLoadGameListener(ActionListener l);
    void addExitMenuListener(ActionListener l);
    void addStartJourneyListener(ActionListener l);
    
    void addBrewChoiceListener(ActionListener l);
    void addVisitMarketListener(ActionListener l);
    void addClaimBonusListener(ActionListener l);
    void addBlessCauldronListener(ActionListener l);
    void addExitAndSaveListener(ActionListener l);
    
    void addNavRecipeModeListener(ActionListener l);
    void addNavCreativeModeListener(ActionListener l);
    void addNavBackToDashboardListener(ActionListener l);
    
    void addActionBuyMarketListener(ActionListener l);
    void addActionBrewRecipeListener(ActionListener l);
    void addActionBrewCreativeListener(ActionListener l);
}