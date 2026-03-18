/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe.Buyer;

import homequest.jframe.*;
import homequest.jframe.Agent.*;
import homequest.jframe.Owner.*;
import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author crnc
 */
public class PurchaseProperty extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(PurchaseProperty.class.getName());

    /**
     * Creates new form Main
     */
    public PurchaseProperty() {
        initComponents();
        loadUserData();
        loadAvailableProperties();
        setupEventHandlers();
    }

    private void loadUserData() {
        homequest.model.Buyer buyer = homequest.HomeQuest.getBuyer();
        UserName1.setText(buyer.getName());
        UserType1.setText("Buyer");
    }

    private void setupEventHandlers() {
        Return.addActionListener(e -> returnToWorkspace());
        Logout.addActionListener(e -> returnToMain());
    }

    private void returnToWorkspace() {
        homequest.jframe.Buyer.Workspace workspace =
            new homequest.jframe.Buyer.Workspace();
        workspace.setVisible(true);
        this.dispose();
    }

    private void returnToMain() {
        homequest.jframe.Main main = new homequest.jframe.Main();
        main.setVisible(true);
        this.dispose();
    }

    private void loadAvailableProperties() {
        homequest.model.Buyer buyer = homequest.HomeQuest.getBuyer();
        java.util.List<homequest.model.Property> availableProps =
            buyer.getAvailableProperties(
                homequest.HomeQuest.getAllProperties()
            );

        JPanel container = new JPanel();
        container.setLayout(
            new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS)
        );
        container.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );
        container.setOpaque(false);

        if (availableProps.isEmpty()) {
            JLabel emptyLabel = new JLabel(
                "No available properties to purchase."
            );
            emptyLabel.setFont(new java.awt.Font("Segoe UI", 0, 18));
            emptyLabel.setForeground(java.awt.Color.GRAY);
            JPanel emptyPanel = new JPanel();
            emptyPanel.add(emptyLabel);
            emptyPanel.setPreferredSize(new java.awt.Dimension(400, 100));
            emptyPanel.setMaximumSize(
                new java.awt.Dimension(Integer.MAX_VALUE, 100)
            );
            emptyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            container.add(emptyPanel);
        } else {
            for (int i = 0; i < availableProps.size(); i++) {
                homequest.model.Property property = availableProps.get(i);
                JPanel panel = createPropertyPanel(property, i + 1);
                container.add(panel);
                if (i < availableProps.size() - 1) {
                    container.add(javax.swing.Box.createVerticalStrut(8));
                }
            }
        }

        ScrollWrapper.setViewportView(container);
        ScrollWrapper.revalidate();
        ScrollWrapper.repaint();
    }

    private JPanel createPropertyPanel(
        homequest.model.Property property,
        int index
    ) {
        JPanel panel = new JPanel();

        panel.setBackground(new java.awt.Color(220, 255, 220));
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setLayout(new java.awt.BorderLayout(10, 10));
        panel.setPreferredSize(new java.awt.Dimension(400, 130));
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 130));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(
            "<html><b>#" +
                index +
                ": " +
                property.getName() +
                "</b><br>" +
                "Block/Lot: " +
                property.getBlockLot() +
                "<br>" +
                "TCP: ₱" +
                String.format("%,.2f", property.getTCP()) +
                "<br>" +
                "Status: <b>" +
                property.getStatus() +
                "</b></html>"
        );
        label.setFont(new java.awt.Font("Segoe UI", 0, 14));
        label.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        javax.swing.JButton buyButton = new javax.swing.JButton("Buy Property");
        buyButton.setFont(new java.awt.Font("Segoe UI", 0, 12));
        buyButton.addActionListener(e -> selectPaymentMethod(property));

        JPanel buttonPanel = new JPanel(
            new java.awt.FlowLayout(java.awt.FlowLayout.LEFT)
        );
        buttonPanel.setOpaque(false);
        buttonPanel.add(buyButton);

        panel.add(label, java.awt.BorderLayout.CENTER);
        panel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        return panel;
    }

    private void selectPaymentMethod(homequest.model.Property property) {
        String[] paymentOptions = {
            "Spot Cash",
            "Check",
            "Bank Financing",
            "Pag-IBIG Financing",
        };

        String paymentMethod = (String) javax.swing.JOptionPane.showInputDialog(
            this,
            "Select payment method for " + property.getName() + ":",
            "Payment Method",
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            paymentOptions,
            paymentOptions[0]
        );

        if (paymentMethod == null) return;

        int paymentChoice =
            java.util.Arrays.asList(paymentOptions).indexOf(paymentMethod) + 1;
        String bankName = null;
        int loanTerm = 0;

        if (paymentChoice == 3) {
            bankName = javax.swing.JOptionPane.showInputDialog(
                this,
                "Enter bank name:"
            );
            if (bankName == null || bankName.trim().isEmpty()) return;

            String termStr = javax.swing.JOptionPane.showInputDialog(
                this,
                "Enter loan term (years):"
            );
            if (termStr == null) return;
            try {
                loanTerm = Integer.parseInt(termStr);
                if (loanTerm <= 0) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Loan term must be greater than 0.",
                        "Invalid Term",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Invalid loan term.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } else if (paymentChoice == 4) {
            String termStr = javax.swing.JOptionPane.showInputDialog(
                this,
                "Enter loan term (years):"
            );
            if (termStr == null) return;
            try {
                loanTerm = Integer.parseInt(termStr);
                if (loanTerm <= 0) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Loan term must be greater than 0.",
                        "Invalid Term",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Invalid loan term.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        boolean confirmed = showBreakdownAndConfirm(
            property,
            paymentChoice,
            bankName,
            loanTerm
        );
        if (!confirmed) return;

        homequest.model.Buyer buyer = homequest.HomeQuest.getBuyer();
        boolean success = buyer.createPurchaseRequest(
            property,
            paymentChoice,
            bankName,
            loanTerm
        );

        if (success) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "<html><h3>Purchase Request Submitted</h3>" +
                    "<p>Property: " +
                    property.getName() +
                    "</p>" +
                    "<p>TCP: ₱" +
                    String.format("%,.2f", property.getTCP()) +
                    "</p>" +
                    "<p>Status: <b>Pending agent approval</b></p></html>",
                "Success",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
            loadAvailableProperties();
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Failed to submit request. Property may no longer be available.",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean showBreakdownAndConfirm(
        homequest.model.Property property,
        int paymentChoice,
        String bankName,
        int loanTerm
    ) {
        double netPrice = property.getBasePrice();
        double vat = homequest.util.FinancialEngine.calculateVAT(
            netPrice,
            false
        );
        double otherCharges =
            homequest.util.FinancialEngine.computeOtherCharges(netPrice);
        double tcp = property.getTCP();
        double agentCut =
            homequest.util.FinancialEngine.calculateAgentCommission(tcp);

        String paymentLabel;
        String financingDetails = "";
        switch (paymentChoice) {
            case 1:
                paymentLabel = "Spot Cash";
                break;
            case 2:
                paymentLabel = "Check";
                break;
            case 3:
                paymentLabel = "Bank Financing";
                double bankMonthly =
                    homequest.util.FinancialEngine.getMonthlyAmortization(
                        tcp,
                        loanTerm
                    );
                financingDetails =
                    "<p>Bank: " +
                    bankName +
                    "</p>" +
                    "<p>Loan Term: " +
                    loanTerm +
                    " years</p>" +
                    "<p>Estimated Monthly: ₱" +
                    String.format("%,.2f", bankMonthly) +
                    "</p>";
                break;
            case 4:
                paymentLabel = "Pag-IBIG Financing";
                double pagibigMonthly =
                    homequest.util.FinancialEngine.getMonthlyAmortization(
                        tcp,
                        loanTerm
                    );
                financingDetails =
                    "<p>Loan Term: " +
                    loanTerm +
                    " years</p>" +
                    "<p>Estimated Monthly: ₱" +
                    String.format("%,.2f", pagibigMonthly) +
                    "</p>";
                break;
            default:
                paymentLabel = "Unknown";
        }

        String breakdown =
            "<html><body style='width: 420px'>" +
            "<h3>Purchase Breakdown</h3>" +
            "<p><b>Property:</b> " +
            property.getName() +
            "</p>" +
            "<p><b>Block/Lot:</b> " +
            property.getBlockLot() +
            "</p>" +
            "<hr>" +
            "<p>Base Price: ₱" +
            String.format("%,.2f", netPrice) +
            "</p>" +
            "<p>VAT: ₱" +
            String.format("%,.2f", vat) +
            "</p>" +
            "<p>Other Charges: ₱" +
            String.format("%,.2f", otherCharges) +
            "</p>" +
            "<p><b>Total Contract Price (TCP): ₱" +
            String.format("%,.2f", tcp) +
            "</b></p>" +
            "<p>Agent Cut (10%): ₱" +
            String.format("%,.2f", agentCut) +
            "</p>" +
            "<hr>" +
            "<p><b>Payment Method:</b> " +
            paymentLabel +
            "</p>" +
            financingDetails +
            "<p>Continue with this purchase request?</p>" +
            "</body></html>";

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            breakdown,
            "Confirm Purchase",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        return confirm == javax.swing.JOptionPane.YES_OPTION;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Content = new javax.swing.JPanel();
        ScrollWrapper = new javax.swing.JScrollPane();
        ButtonWrapper = new javax.swing.JPanel();
        Return = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        Header1 = new javax.swing.JPanel();
        HeaderLabel1 = new javax.swing.JLabel();
        UserType1 = new javax.swing.JLabel();
        UserName1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(600, 500));

        java.awt.GridBagLayout ContentLayout = new java.awt.GridBagLayout();
        ContentLayout.columnWidths = new int[] {0};
        ContentLayout.rowHeights = new int[] {0, 5, 0};
        ContentLayout.columnWeights = new double[] {0.0};
        ContentLayout.rowWeights = new double[] {0.0};
        Content.setLayout(ContentLayout);

        ScrollWrapper.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollWrapper.setHorizontalScrollBar(null);
        ScrollWrapper.setMinimumSize(new java.awt.Dimension(350, 300));
        ScrollWrapper.setName(""); // NOI18N
        ScrollWrapper.setPreferredSize(new java.awt.Dimension(400, 400));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Content.add(ScrollWrapper, gridBagConstraints);

        ButtonWrapper.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        Return.setText("Return");
        Return.addActionListener(this::ReturnActionPerformed);
        ButtonWrapper.add(Return);

        Logout.setText("Logout");
        ButtonWrapper.add(Logout);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(ButtonWrapper, gridBagConstraints);

        Header1.setLayout(new java.awt.GridBagLayout());

        HeaderLabel1.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        HeaderLabel1.setText("HomeQuest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        Header1.add(HeaderLabel1, gridBagConstraints);

        UserType1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UserType1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserType1.setText("Owner");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        Header1.add(UserType1, gridBagConstraints);

        UserName1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UserName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserName1.setText("Owner Name Here");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        Header1.add(UserName1, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Header1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Header1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ReturnActionPerformed(java.awt.event.ActionEvent evt) {
//GEN-FIRST:event_ReturnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReturnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (
            ReflectiveOperationException
            | javax.swing.UnsupportedLookAndFeelException ex
        ) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() ->
            new PurchaseProperty().setVisible(true)
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonWrapper;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Header1;
    private javax.swing.JLabel HeaderLabel1;
    private javax.swing.JButton Logout;
    private javax.swing.JButton Return;
    private javax.swing.JScrollPane ScrollWrapper;
    private javax.swing.JLabel UserName1;
    private javax.swing.JLabel UserType1;
    // End of variables declaration//GEN-END:variables
}
