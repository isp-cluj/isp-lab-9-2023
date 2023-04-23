/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package isp.lab9.exercise1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mihai.hulea
 * @author radu.miron
 */
public class StockMarketJFrame extends JFrame {
    private StockMarketQueryService marketService;
    private Portfolio portfolio;

    /**
     * Creates new form StockMarketJFrame
     */
    public StockMarketJFrame() {
        try {
            marketService = new StockMarketQueryService();
            marketService.refreshMarketData();

            portfolio = new Portfolio(new BigDecimal(1000), new TreeMap<>());
        } catch (IOException ex) {
            Logger.getLogger(StockMarketJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();

        setVisible(true);
    }

    /**
     * Initializes the window
     */
    private void initComponents() {
        this.setSize(700, 400);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // configure windows the tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Market", createMarketPanel());
        tabs.addTab("Portfolio", createPortfolioPanel());
        tabs.addTab("Buy", createBuyPanel());
        tabs.addTab("Sell", createSellPanel());

        this.add(tabs);
    }

    /**
     * Creates the 'Market' panel
     *
     * @return the 'Market' panel
     */
    public JPanel createMarketPanel() {
        JPanel marketPanel = new JPanel();
        marketPanel.setLayout(new BoxLayout(marketPanel, BoxLayout.Y_AXIS));

        JTable jTableMarket = new JTable();
        jTableMarket.setModel(marketService);
        JScrollPane marketScrollablePane = new JScrollPane(jTableMarket);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(evt -> buttonRefreshActionPerformed(evt));

        marketPanel.add(marketScrollablePane);
        marketPanel.add(refreshButton);

        return marketPanel;
    }

    /**
     * Creates the 'Portfolio' panel
     *
     * @return the 'Portfolio' panel
     */
    public JPanel createPortfolioPanel() {
        JPanel portfolioPanel = new JPanel();
        // todo: implement portfolio panel; for each owned stock add symbol, quantity, price per unit, total price of the position
        //  it should look similar to the 'Market' panel
        return portfolioPanel;
    }

    /**
     * Creates the 'Sell' panel
     *
     * @return the 'Sell' panel
     */
    private JPanel createSellPanel() {
        JPanel sellPanel = new JPanel();
        //todo: implement - it should look similar to the 'Buy' panel
        return sellPanel;
    }

    /**
     * Creates the 'Buy' panel
     *
     * @return the 'Buy' panel
     */
    public JPanel createBuyPanel() {
        JPanel mainBuyPanel = new JPanel();
        mainBuyPanel.setLayout(new GridLayout(2, 2));

        JPanel buyPanel = new JPanel();
        buyPanel.setLayout(new GridLayout(10, 2));

        JLabel availableFundsLabel = new JLabel("Available funds:");
        JTextField availableFundsTextField = new JTextField(portfolio.getCash().toPlainString() + " $");
        availableFundsTextField.setEditable(false);

        JLabel symbolLabel = new JLabel("Symbol:");
        JComboBox<String> symbolComboBox = new JComboBox<>();
        symbolComboBox.setModel(new DefaultComboBoxModel(marketService.getSymbols()));

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityTextField = new JTextField();

        JLabel costLabel = new JLabel("Total cost:");
        JTextField costTextField = new JTextField();
        costTextField.setEditable(false);

        JButton buyButton = new JButton("Buy");
        // todo: add event listener to 'Buy' button

        JButton costButton = new JButton("Get cost");
        costButton.addActionListener(e ->
                calculateTotalCostActionPerformed(symbolComboBox, quantityTextField, costTextField));

        buyPanel.add(availableFundsLabel);
        buyPanel.add(availableFundsTextField);
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(symbolLabel);
        buyPanel.add(symbolComboBox);
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(quantityLabel);
        buyPanel.add(quantityTextField);
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(costLabel);
        buyPanel.add(costTextField);
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(new JPanel()); // empty cell in the grid
        buyPanel.add(costButton);
        buyPanel.add(buyButton);
        mainBuyPanel.add(buyPanel);
        mainBuyPanel.add(new JPanel()); // empty cell in the grid
        mainBuyPanel.add(new JPanel()); // empty cell in the grid
        mainBuyPanel.add(new JPanel()); // empty cell in the grid

        return mainBuyPanel;
    }

    /**
     * Refreshes the stock data
     */
    private void buttonRefreshActionPerformed(ActionEvent evt) {
        try {
            marketService.refreshMarketData();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(StockMarketJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Calculates the total transaction cost
     */
    private void calculateTotalCostActionPerformed(JComboBox<String> symbolComboBox,
                                                   JTextField quantityTextField,
                                                   JTextField totalCostTextField) {
        try {
            String symbol = (String) symbolComboBox.getSelectedItem();
            BigDecimal stockPrice = marketService.getStockPrice(symbol);

            try {
                int quantity = Integer.parseInt(quantityTextField.getText());
                DecimalFormat formatter = new DecimalFormat("#,##0.##");
                totalCostTextField.setText(
                        formatter.format(stockPrice.multiply(new BigDecimal(quantity))));
            } catch (NumberFormatException e) {
                totalCostTextField.setText("Invalid quantity value!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(StockMarketJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
