/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe.Buyer;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author crnc
 */
public class ViewPurchaseHistory extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ViewPurchaseHistory.class.getName());

    /**
     * Creates new form Main
     */
    public ViewPurchaseHistory() {
        initComponents();
        loadUserData();
        loadPurchaseHistory();
        setupEventHandlers();
    }

    private void loadUserData() {
        homequest.model.Buyer buyer = homequest.HomeQuest.getBuyer();
        UserName.setText(buyer.getName());
    }

    private void setupEventHandlers() {
        Return.addActionListener(e -> returnToWorkspace());
        Logout.addActionListener(e -> returnToMain());
    }

    private void returnToWorkspace() {
        homequest.jframe.Buyer.Workspace workspace = new homequest.jframe.Buyer.Workspace();
        workspace.setVisible(true);
        this.dispose();
    }

    private void returnToMain() {
        homequest.jframe.Main main = new homequest.jframe.Main();
        main.setVisible(true);
        this.dispose();
    }

    private void loadPurchaseHistory() {
        homequest.model.Buyer buyer = homequest.HomeQuest.getBuyer();
        java.util.List<homequest.transaction.Transaction> history = buyer.getPurchaseHistory();

        JPanel container = new JPanel();
        container.setLayout(new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS));
        container.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        container.setOpaque(false);

        if (history.isEmpty()) {
            JLabel emptyLabel = new JLabel("No purchase history yet.");
            emptyLabel.setFont(new java.awt.Font("Segoe UI", 0, 18));
            emptyLabel.setForeground(java.awt.Color.GRAY);
            JPanel emptyPanel = new JPanel();
            emptyPanel.add(emptyLabel);
            emptyPanel.setPreferredSize(new java.awt.Dimension(400, 100));
            emptyPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 100));
            emptyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            container.add(emptyPanel);
        } else {
            for (int i = 0; i < history.size(); i++) {
                homequest.transaction.Transaction transaction = history.get(i);
                JPanel panel = createTransactionPanel(transaction, i + 1);
                container.add(panel);
                if (i < history.size() - 1) {
                    container.add(javax.swing.Box.createVerticalStrut(8));
                }
            }
        }

        ScrollWrapper.setViewportView(container);
        ScrollWrapper.revalidate();
        ScrollWrapper.repaint();
    }

    private JPanel createTransactionPanel(homequest.transaction.Transaction transaction, int index) {
        JPanel panel = new JPanel();

        panel.setBackground(new java.awt.Color(235, 245, 255));
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setLayout(new java.awt.BorderLayout(10, 5));
        panel.setPreferredSize(new java.awt.Dimension(400, 130));
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 130));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        homequest.model.Property property = transaction.getTargetProperty();
        String paymentType = transaction.getClass().getSimpleName();

        JLabel label = new JLabel("<html><b>#" + index + " - Transaction ID: " + transaction.getTransactionID() + "</b><br>" +
                "Property: " + property.getName() + "<br>" +
                "Payment Type: " + paymentType + "<br>" +
                "Status: <b>" + property.getStatus() + "</b></html>");
        label.setFont(new java.awt.Font("Segoe UI", 0, 14));
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(label, java.awt.BorderLayout.CENTER);

        if (transaction instanceof homequest.transaction.Bank || transaction instanceof homequest.transaction.PagIbig) {
            javax.swing.JButton duesButton = new javax.swing.JButton("View Dues / Pay");
            duesButton.addActionListener(e -> openInstallmentDuesDialog(transaction));

            JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            buttonPanel.setOpaque(false);
            buttonPanel.add(duesButton);
            panel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        }

        return panel;
    }

    private void openInstallmentDuesDialog(homequest.transaction.Transaction transaction) {
        int termMonths;
        double monthlyDue;
        String paymentType;

        if (transaction instanceof homequest.transaction.Bank) {
            homequest.transaction.Bank bankTransaction = (homequest.transaction.Bank) transaction;
            termMonths = bankTransaction.getTermMonths();
            monthlyDue = bankTransaction.getMonthlyInstallment();
            paymentType = "Bank Financing";
        } else if (transaction instanceof homequest.transaction.PagIbig) {
            homequest.transaction.PagIbig pagIbigTransaction = (homequest.transaction.PagIbig) transaction;
            termMonths = pagIbigTransaction.getTermMonths();
            monthlyDue = pagIbigTransaction.getMonthlyInstallment();
            paymentType = "Pag-IBIG Financing";
        } else {
            return;
        }

        int paidCount = transaction.getPayments().size();
        homequest.model.Buyer buyer = homequest.HomeQuest.getBuyer();

        String[] columns = {"Due #", "Amount", "Status"};
        Object[][] rows = new Object[termMonths][3];
        for (int month = 1; month <= termMonths; month++) {
            rows[month - 1][0] = "Month " + month;
            rows[month - 1][1] = "₱" + String.format("%,.2f", monthlyDue);
            rows[month - 1][2] = month <= paidCount ? "PAID" : "PENDING";
        }

        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        javax.swing.JTable duesTable = new javax.swing.JTable(model);
        duesTable.setRowHeight(24);
        duesTable.getTableHeader().setReorderingAllowed(false);

        javax.swing.JScrollPane tableScroll = new javax.swing.JScrollPane(duesTable);
        tableScroll.setPreferredSize(new java.awt.Dimension(450, 220));

        String summary = "<html><b>Payment Type:</b> " + paymentType + "<br>" +
                "<b>Monthly Due:</b> ₱" + String.format("%,.2f", monthlyDue) + "<br>" +
                "<b>Paid:</b> " + paidCount + " / " + termMonths + " months<br>" +
                "<b>Wallet Balance:</b> ₱" + String.format("%,.2f", buyer.getWalletBalance()) + "</html>";

        JPanel content = new JPanel(new java.awt.BorderLayout(10, 10));
        content.add(new JLabel(summary), java.awt.BorderLayout.NORTH);
        content.add(tableScroll, java.awt.BorderLayout.CENTER);

        if (paidCount >= termMonths) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    content,
                    "Installment Dues - Fully Paid",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = javax.swing.JOptionPane.showOptionDialog(
                this,
                content,
                "Installment Dues",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Pay Next Due", "Close"},
                "Pay Next Due");

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            boolean paid = false;
            if (transaction instanceof homequest.transaction.Bank) {
                paid = ((homequest.transaction.Bank) transaction).payNextDue();
            } else if (transaction instanceof homequest.transaction.PagIbig) {
                paid = ((homequest.transaction.PagIbig) transaction).payNextDue();
            }

            if (paid) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Monthly due paid successfully!",
                        "Payment Success",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Unable to pay due. You may have insufficient wallet balance or all dues are already paid.",
                        "Payment Failed",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            loadPurchaseHistory();
        }
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

        Header = new javax.swing.JPanel();
        HeaderLabel = new javax.swing.JLabel();
        Content = new javax.swing.JPanel();
        ScrollWrapper = new javax.swing.JScrollPane();
        ButtonWrapper = new javax.swing.JPanel();
        Return = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        UserInfo = new javax.swing.JPanel();
        UserIcon = new javax.swing.JLabel();
        UserType = new javax.swing.JLabel();
        UserName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HomeQuest");
        setResizable(false);
        setSize(new java.awt.Dimension(600, 500));

        Header.setLayout(new java.awt.GridBagLayout());

        HeaderLabel.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        HeaderLabel.setText("HomeQuest");
        Header.add(HeaderLabel, new java.awt.GridBagConstraints());

        java.awt.GridBagLayout ContentLayout = new java.awt.GridBagLayout();
        ContentLayout.columnWidths = new int[] {0, 5, 0};
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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        Content.add(ScrollWrapper, gridBagConstraints);

        ButtonWrapper.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        Return.setText("Return");
        Return.addActionListener(this::ReturnActionPerformed);
        ButtonWrapper.add(Return);

        Logout.setText("Logout");
        ButtonWrapper.add(Logout);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(ButtonWrapper, gridBagConstraints);

        UserInfo.setMaximumSize(new java.awt.Dimension(199, 96));
        UserInfo.setMinimumSize(new java.awt.Dimension(199, 196));
        UserInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/homequest/jframe/pfp.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_FAST);
        UserIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserIcon.setIcon(new ImageIcon(scaledImage));
        UserIcon.setMaximumSize(new java.awt.Dimension(100, 100));
        UserIcon.setMinimumSize(new java.awt.Dimension(100, 100));
        UserIcon.setPreferredSize(new java.awt.Dimension(100, 100));
        UserInfo.add(UserIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 120));

        UserType.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UserType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserType.setText("Buyer");
        UserInfo.add(UserType, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 199, 30));

        UserName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserName.setText("Owner Name Here");
        UserInfo.add(UserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, -1, 30));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(UserInfo, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                    .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReturnActionPerformed
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ViewPurchaseHistory().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonWrapper;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel HeaderLabel;
    private javax.swing.JButton Logout;
    private javax.swing.JButton Return;
    private javax.swing.JScrollPane ScrollWrapper;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JPanel UserInfo;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserType;
    // End of variables declaration//GEN-END:variables
}
