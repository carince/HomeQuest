/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe.Owner;

import homequest.jframe.Agent.*;
import homequest.jframe.*;
import java.awt.Image;
import javax.swing.ImageIcon;


/**
 *
 * @author crnc
 */
public class Workspace extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Workspace.class.getName());

    /**
     * Creates new form Main
     */
    public Workspace() {
        initComponents();
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
        jButton5.addActionListener(e -> viewPropertySalesStatus());
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
        String message = String.format("<html><body style='width: 300px'><h2>Agent Information</h2>" +
            "<p><b>Name:</b> %s</p>" +
            "<p><b>License:</b> %s</p>" +
            "<p><b>Total Listings:</b> %d</p></body></html>",
            agent.getName(), agent.getListings().size());
        
        javax.swing.JOptionPane.showMessageDialog(this, message,
            "Agent Information", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void assignPropertyToAgent() {
        homequest.model.Owner owner = homequest.HomeQuest.getOwner();
        homequest.model.Agent agent = homequest.HomeQuest.getAgent();
        
        if (owner.getProperties().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "You don't have any properties to assign.",
                "No Properties",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] options = new String[owner.getProperties().size()];
        for (int i = 0; i < owner.getProperties().size(); i++) {
            homequest.model.Property prop = owner.getProperties().get(i);
            options[i] = prop.getName() + " - " + prop.getStatus();
        }
        
        String selected = (String) javax.swing.JOptionPane.showInputDialog(this,
            "Select property to assign to " + agent.getName() + ":",
            "Assign Property",
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (selected != null) {
            int index = java.util.Arrays.asList(options).indexOf(selected);
            homequest.model.Property property = owner.getProperties().get(index);
            
            if (owner.assignPropertyToAgent(property, agent)) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Property " + property.getName() + " assigned to " + agent.getName(),
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Failed to assign property.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewPropertySalesStatus() {
        homequest.model.Owner owner = homequest.HomeQuest.getOwner();
        
        StringBuilder message = new StringBuilder("<html><body style='width: 400px'><h2>Property Sales Status</h2>");
        
        if (owner.getProperties().isEmpty()) {
            message.append("<p>No properties owned.</p>");
        } else {
            int available = 0, reserved = 0, sold = 0;
            
            for (homequest.model.Property prop : owner.getProperties()) {
                message.append("<p><b>").append(prop.getName()).append("</b> - ")
                       .append("<span style='color: ");
                
                switch (prop.getStatus()) {
                    case AVAILABLE:
                        message.append("green'>AVAILABLE");
                        available++;
                        break;
                    case RESERVED:
                        message.append("orange'>RESERVED");
                        reserved++;
                        break;
                    case SOLD:
                        message.append("red'>SOLD");
                        sold++;
                        break;
                }
                message.append("</span></p>");
            }
            
            message.append("<hr><p><b>Summary:</b></p>")
                   .append("<p>Total: ").append(owner.getProperties().size())
                   .append(" | Available: ").append(available)
                   .append(" | Reserved: ").append(reserved)
                   .append(" | Sold: ").append(sold).append("</p>");
        }
        message.append("</body></html>");
        
        javax.swing.JOptionPane.showMessageDialog(this, message.toString(),
            "Property Sales Status", javax.swing.JOptionPane.INFORMATION_MESSAGE);
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

        Header = new javax.swing.JPanel();
        HeaderLabel = new javax.swing.JLabel();
        Content = new javax.swing.JPanel();
        ButtonWrapper = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        ButtonWrapper2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        UserInfo = new javax.swing.JPanel();
        UserIcon = new javax.swing.JLabel();
        UserType = new javax.swing.JLabel();
        UserName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(ButtonWrapper, gridBagConstraints);

        ButtonWrapper2.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        jButton5.setText("View Property Sale Status");
        ButtonWrapper2.add(jButton5);

        Logout.setText("Logout");
        ButtonWrapper2.add(Logout);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(ButtonWrapper2, gridBagConstraints);

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
        UserType.setText("Owner");
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
                    .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(() -> new Workspace().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonWrapper;
    private javax.swing.JPanel ButtonWrapper2;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel HeaderLabel;
    private javax.swing.JButton Logout;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JPanel UserInfo;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    // End of variables declaration//GEN-END:variables
}
