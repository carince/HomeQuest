/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe.Owner;

import homequest.jframe.*;
import homequest.jframe.Agent.*;
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
public class ViewProperties extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(ViewProperties.class.getName());
    private final homequest.util.PropertyFilterDialog.FilterCriteria activeFilters = new homequest.util.PropertyFilterDialog.FilterCriteria();

    /**
     * Creates new form Main
     */
    public ViewProperties() {
        initComponents();
        loadUserData();
        loadOwnerProperties();
        setupEventHandlers();
    }

    private void loadUserData() {
        homequest.model.Owner owner = homequest.HomeQuest.getOwner();
        UserName.setText(owner.getName());
    }

    private void setupEventHandlers() {
        javax.swing.JButton filterButton = new javax.swing.JButton("Filter");
        filterButton.addActionListener(e -> showFilterDialog());
        ButtonWrapper.setLayout(new java.awt.GridLayout(1, 3, 20, 0));
        ButtonWrapper.add(filterButton, 0);
        Return.addActionListener(e -> returnToWorkspace());
        Logout.addActionListener(e -> returnToMain());
    }

    private void showFilterDialog() {
        if (homequest.util.PropertyFilterDialog.showFilterDialog(this, activeFilters)) {
            loadOwnerProperties();
        }
    }

    private void returnToWorkspace() {
        homequest.jframe.Owner.Workspace workspace =
            new homequest.jframe.Owner.Workspace();
        workspace.setVisible(true);
        this.dispose();
    }

    private void returnToMain() {
        homequest.jframe.Main main = new homequest.jframe.Main();
        main.setVisible(true);
        this.dispose();
    }

    private void loadOwnerProperties() {
        homequest.model.Owner owner = homequest.HomeQuest.getOwner();
        java.util.List<homequest.model.Property> properties =
            owner.getProperties();
        properties = homequest.util.PropertyFilterDialog.filterProperties(properties, activeFilters);

        JPanel container = new JPanel();
        container.setLayout(
            new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS)
        );
        container.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );
        container.setOpaque(false);

        if (properties.isEmpty()) {
            JLabel emptyLabel = new JLabel("No properties owned.");
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
            for (int i = 0; i < properties.size(); i++) {
                homequest.model.Property property = properties.get(i);
                JPanel panel = createPropertyPanel(property, i + 1);
                container.add(panel);
                if (i < properties.size() - 1) {
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

        java.awt.Color bgColor;
        switch (property.getStatus()) {
            case AVAILABLE:
                bgColor = new java.awt.Color(220, 255, 220);
                break;
            case RESERVED:
                bgColor = new java.awt.Color(255, 250, 205);
                break;
            case SOLD:
                bgColor = new java.awt.Color(255, 220, 220);
                break;
            default:
                bgColor = java.awt.Color.LIGHT_GRAY;
        }

        panel.setBackground(bgColor);
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setLayout(new java.awt.BorderLayout(10, 5));
        panel.setPreferredSize(new java.awt.Dimension(400, 125));
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 125));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(
            "<html><b>#" +
                index +
                ": " +
                property.getName() +
                "</b><br>" +
                "Block: " + property.getBlock() + " &nbsp; Lot: " + property.getLot() + "<br>" +
                "Lot Area: " + String.format("%,.2f", property.getLotArea()) + " sqm<br>" +
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

        panel.add(label, java.awt.BorderLayout.CENTER);

        return panel;
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
                    .addComponent(Content, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
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
            new ViewProperties().setVisible(true)
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonWrapper;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel HeaderLabel;
    private javax.swing.JButton Logout;
    private javax.swing.JButton Return;
    private javax.swing.JScrollPane ScrollWrapper;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserType;
    // End of variables declaration//GEN-END:variables
}
