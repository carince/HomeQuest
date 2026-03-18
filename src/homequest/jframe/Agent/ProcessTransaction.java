/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe.Agent;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author crnc
 */
public class ProcessTransaction extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProcessTransaction.class.getName());

    /**
     * Creates new form Main
     */
    public ProcessTransaction() {
        initComponents();
        loadUserData();
        loadPendingRequests();
        setupEventHandlers();
    }

    private void loadUserData() {
        homequest.model.Agent agent = homequest.HomeQuest.getAgent();
        UserName1.setText(agent.getName());
    }

    private void setupEventHandlers() {
        Return.addActionListener(e -> returnToWorkspace());
        Logout.addActionListener(e -> returnToMain());
    }

    private void returnToWorkspace() {
        homequest.jframe.Agent.Workspace workspace = new homequest.jframe.Agent.Workspace();
        workspace.setVisible(true);
        this.dispose();
    }

    private void returnToMain() {
        homequest.jframe.Main main = new homequest.jframe.Main();
        main.setVisible(true);
        this.dispose();
    }

    private void loadPendingRequests() {
        homequest.model.Agent agent = homequest.HomeQuest.getAgent();
        java.util.List<homequest.model.Property> allProperties = homequest.HomeQuest.getAllProperties();
        java.util.List<homequest.model.Property> pendingProps = agent.getPendingPurchaseRequests(allProperties);

        JPanel container = new JPanel();
        container.setLayout(new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS));
        container.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        container.setOpaque(false);

        if (pendingProps.isEmpty()) {
            JLabel emptyLabel = new JLabel("No pending purchase requests.");
            emptyLabel.setFont(new java.awt.Font("Segoe UI", 0, 18));
            emptyLabel.setForeground(java.awt.Color.GRAY);
            JPanel emptyPanel = new JPanel();
            emptyPanel.add(emptyLabel);
            emptyPanel.setPreferredSize(new java.awt.Dimension(400, 100));
            emptyPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 100));
            emptyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            container.add(emptyPanel);
        } else {
            for (int i = 0; i < pendingProps.size(); i++) {
                homequest.model.Property property = pendingProps.get(i);
                JPanel panel = createRequestPanel(property, i + 1);
                container.add(panel);
                if (i < pendingProps.size() - 1) {
                    container.add(javax.swing.Box.createVerticalStrut(8));
                }
            }
        }

        ScrollWrapper.setViewportView(container);
        ScrollWrapper.revalidate();
        ScrollWrapper.repaint();
    }

    private JPanel createRequestPanel(homequest.model.Property property, int index) {
        JPanel panel = new JPanel();
        panel.setBackground(new java.awt.Color(255, 250, 205));
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setLayout(new java.awt.BorderLayout(10, 5));
        panel.setPreferredSize(new java.awt.Dimension(400, 165));
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 165));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String paymentMethod = getPaymentMethodText(property);
        
        JLabel infoLabel = new JLabel("<html><b>#" + index + ": " + property.getName() + "</b><br>" +
            "Block: " + property.getBlock() + " &nbsp; Lot: " + property.getLot() + "<br>" +
            "Buyer: " + property.getPendingBuyer().getName() + "<br>" +
                "TCP: ₱" + String.format("%,.2f", property.getTCP()) + "<br>" +
                "Payment: " + paymentMethod + "</html>");
        infoLabel.setFont(new java.awt.Font("Segoe UI", 0, 12));
        infoLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        javax.swing.JButton approveBtn = new javax.swing.JButton("Approve");
        approveBtn.setBackground(new java.awt.Color(144, 238, 144));
        approveBtn.addActionListener(e -> handleApprove(property));

        javax.swing.JButton rejectBtn = new javax.swing.JButton("Reject");
        rejectBtn.setBackground(new java.awt.Color(255, 182, 193));
        rejectBtn.addActionListener(e -> handleReject(property));

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);

        panel.add(infoLabel, java.awt.BorderLayout.CENTER);
        panel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        return panel;
    }

    private String getPaymentMethodText(homequest.model.Property property) {
        switch (property.getPendingPaymentMethod()) {
            case 1:
                return "Spot Cash";
            case 2:
                return "Check";
            case 3:
                return "Bank Financing (" + property.getPendingBankName() + ", " + property.getPendingLoanTerm() + " years)";
            case 4:
                return "Pag-IBIG Financing (" + property.getPendingLoanTerm() + " years)";
            default:
                return "Unknown";
        }
    }

    private void handleApprove(homequest.model.Property property) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Approve purchase request for " + property.getName() + "?",
                "Confirm Approval",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            homequest.model.Agent agent = homequest.HomeQuest.getAgent();
            int paymentMethod = property.getPendingPaymentMethod();
            agent.approveTransaction(property);
            
            boolean isInstallment = (paymentMethod == 3 || paymentMethod == 4);
            String finalStatus = isInstallment ? "RESERVED (installment ongoing)" : "SOLD";
            String note = isInstallment ? "\nBuyer will continue making monthly payments." : "";
            
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Transaction approved successfully!\nProperty: " + property.getName() + "\nStatus: " + finalStatus + note,
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            loadPendingRequests();
        }
    }

    private void handleReject(homequest.model.Property property) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Reject purchase request for " + property.getName() + "?\nProperty will return to AVAILABLE status.",
                "Confirm Rejection",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            homequest.model.Agent agent = homequest.HomeQuest.getAgent();
            agent.rejectTransaction(property);
            
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Request rejected. Property returned to AVAILABLE status.",
                    "Rejected",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            loadPendingRequests();
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
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
        java.awt.EventQueue.invokeLater(() -> new ProcessTransaction().setVisible(true));
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
