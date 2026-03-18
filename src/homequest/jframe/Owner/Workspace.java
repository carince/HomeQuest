/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe.Owner;

import homequest.jframe.*;
import homequest.jframe.Agent.*;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author crnc
 */
public class Workspace extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(Workspace.class.getName());

    /**
     * Creates new form Main
     */
    public Workspace() {
        initComponents();
        setTitle("HomeQuest - Owner");
        loadUserData();
        setupEventHandlers();
    }

    private void loadUserData() {
        homequest.model.Owner owner = homequest.HomeQuest.getOwner();
        UserName.setText(owner.getName());
    }

    private void setupEventHandlers() {
        jButton1.addActionListener(e -> openViewProperties());
        jButton2.addActionListener(e -> openAddProperty());
        jButton4.addActionListener(e -> assignPropertyToAgent());
        jButton5.addActionListener(e -> openGenerateReport());
        Logout.addActionListener(e -> returnToMain());
    }

    private void openViewProperties() {
        ViewProperties viewProperties = new ViewProperties();
        viewProperties.setVisible(true);
        this.dispose();
    }

    private void openAddProperty() {
        AddProperty addProperty = new AddProperty();
        addProperty.setVisible(true);
        this.dispose();
    }

    private void viewAgentInformation() {
        homequest.model.Agent agent = homequest.HomeQuest.getAgent();
        String message = String.format(
            "<html><body style='width: 300px'><h2>Agent Information</h2>" +
                "<p><b>Name:</b> %s</p>" +
                "<p><b>License:</b> %s</p>" +
                "<p><b>Total Listings:</b> %d</p></body></html>",
            agent.getName(),
            agent.getListings().size()
        );

        javax.swing.JOptionPane.showMessageDialog(
            this,
            message,
            "Agent Information",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void assignPropertyToAgent() {
        try {
            homequest.model.Owner owner = homequest.HomeQuest.getOwner();
            homequest.model.Agent agent = homequest.HomeQuest.getAgent();

            if (owner == null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Owner data is not available.",
                    "Assignment Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (agent == null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Agent data is not available.",
                    "Assignment Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (owner.getProperties().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "You don't have any properties to assign.",
                    "No Properties",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            java.util.List<homequest.model.Property> assignableProperties =
                owner
                    .getProperties()
                    .stream()
                    .filter(prop -> !agent.getListings().contains(prop))
                    .toList();

            if (assignableProperties.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "All your properties are already assigned to " +
                    agent.getName() +
                    ".",
                    "No Assignable Properties",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            String[] options = new String[assignableProperties.size()];
            for (int i = 0; i < assignableProperties.size(); i++) {
                homequest.model.Property prop = assignableProperties.get(i);
                options[i] = prop.getName() + " - " + prop.getStatus();
            }

            String selected = (String) javax.swing.JOptionPane.showInputDialog(
                this,
                "Select property to assign to " + agent.getName() + ":",
                "Assign Property",
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (selected == null) {
                return;
            }

            int index = java.util.Arrays.asList(options).indexOf(selected);
            if (index < 0 || index >= assignableProperties.size()) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Invalid property selection. Please try again.",
                    "Assignment Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            homequest.model.Property property = assignableProperties.get(index);
            String blockReason = owner.getAssignmentBlockReason(property, agent);

            if (blockReason != null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    blockReason,
                    "Assignment Blocked",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (owner.assignPropertyToAgent(property, agent)) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Property " +
                        property.getName() +
                        " assigned to " +
                        agent.getName(),
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Unable to assign property due to a validation error.",
                    "Assignment Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to assign property to agent", ex);
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Unexpected error while assigning property: " + ex.getMessage(),
                "Assignment Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openGenerateReport() {
        GenerateReport report = new GenerateReport();
        report.setVisible(true);
        this.dispose();
    }

    private void returnToMain() {
        homequest.jframe.Main main = new homequest.jframe.Main();
        main.setVisible(true);
        this.dispose();
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
        ButtonWrapper = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        ButtonWrapper2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        Header = new javax.swing.JPanel();
        HeaderLabel = new javax.swing.JLabel();
        UserType = new javax.swing.JLabel();
        UserName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(600, 500));

        java.awt.GridBagLayout ContentLayout = new java.awt.GridBagLayout();
        ContentLayout.columnWidths = new int[] {0};
        ContentLayout.rowHeights = new int[] {0, 5, 0};
        ContentLayout.columnWeights = new double[] {0.0};
        ContentLayout.rowWeights = new double[] {0.0};
        Content.setLayout(ContentLayout);

        ButtonWrapper.setLayout(new java.awt.GridLayout(4, 1, 0, 20));

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton1.setText("View All My Properties");
        ButtonWrapper.add(jButton1);

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton2.setText("Add New Property");
        ButtonWrapper.add(jButton2);

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton4.setText("Assign Property to Agent");
        ButtonWrapper.add(jButton4);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(ButtonWrapper, gridBagConstraints);

        ButtonWrapper2.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        jButton5.setText("Generate Report");
        ButtonWrapper2.add(jButton5);

        Logout.setText("Logout");
        ButtonWrapper2.add(Logout);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(ButtonWrapper2, gridBagConstraints);

        Header.setLayout(new java.awt.GridBagLayout());

        HeaderLabel.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        HeaderLabel.setText("HomeQuest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        Header.add(HeaderLabel, gridBagConstraints);

        UserType.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UserType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserType.setText("Owner");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        Header.add(UserType, gridBagConstraints);

        UserName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserName.setText("Owner Name Here");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        Header.add(UserName, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Content, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                    .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new Workspace().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonWrapper;
    private javax.swing.JPanel ButtonWrapper2;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel HeaderLabel;
    private javax.swing.JButton Logout;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    // End of variables declaration//GEN-END:variables
}
