/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe.Agent;

import homequest.jframe.*;
import homequest.jframe.Agent.*;
import homequest.jframe.Owner.*;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author crnc
 */
public class ViewListings extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(ViewListings.class.getName());
    private final homequest.util.PropertyFilterDialog.FilterCriteria activeFilters = new homequest.util.PropertyFilterDialog.FilterCriteria();
    private final List<String> imagePool = discoverImageFiles();
    private final Map<String, ImageIcon> imageCache = new HashMap<>();

    /**
     * Creates new form Main
     */
    public ViewListings() {
        initComponents();
        setTitle("HomeQuest - Agent / View Listings");
        loadUserData();
        loadAgentListings();
        setupEventHandlers();
    }

    private void loadUserData() {
        homequest.model.Agent agent = homequest.HomeQuest.getAgent();
        UserName1.setText(agent.getName());
        UserType1.setText("Agent");
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
            loadAgentListings();
        }
    }

    private void returnToWorkspace() {
        homequest.jframe.Agent.Workspace workspace =
            new homequest.jframe.Agent.Workspace();
        workspace.setVisible(true);
        this.dispose();
    }

    private void returnToMain() {
        homequest.jframe.Main main = new homequest.jframe.Main();
        main.setVisible(true);
        this.dispose();
    }

    private void loadAgentListings() {
        homequest.model.Agent agent = homequest.HomeQuest.getAgent();
        java.util.List<homequest.model.Property> listings = agent.getListings();
        listings = homequest.util.PropertyFilterDialog.filterProperties(listings, activeFilters);

        JPanel container = new JPanel();
        container.setLayout(
            new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS)
        );
        container.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );
        container.setOpaque(false);

        if (listings.isEmpty()) {
            JLabel emptyLabel = new JLabel("No properties in your listings.");
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
            for (int i = 0; i < listings.size(); i++) {
                homequest.model.Property property = listings.get(i);
                String imageName = getImageNameForProperty(property, i);
                JPanel panel = createPropertyPanel(property, i + 1, imageName);
                container.add(panel);
                if (i < listings.size() - 1) {
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
        int index,
        String imageName
    ) {
        JPanel panel = new JPanel();
        panel.setBackground(java.awt.Color.lightGray);
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setLayout(new java.awt.BorderLayout(10, 5));
        panel.setPreferredSize(new java.awt.Dimension(400, 165));
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 165));
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
                "Status: " +
                property.getStatus() +
                "</html>"
        );
        label.setFont(new java.awt.Font("Segoe UI", 0, 14));
        label.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        panel.add(label, java.awt.BorderLayout.CENTER);

        javax.swing.JButton showImageButton = new javax.swing.JButton("Show Image");
        showImageButton.addActionListener(e -> showImagePopup(property.getName(), imageName));
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(showImageButton);
        panel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        return panel;
    }

    private String getImageNameForProperty(homequest.model.Property property, int index) {
        if (imagePool.isEmpty()) {
            return null;
        }

        String propertyName = property.getName().toLowerCase().trim();
        for (String imageName : imagePool) {
            String imageNameWithoutExt = imageName.substring(0, imageName.lastIndexOf('.')).toLowerCase();
            if (propertyName.equals(imageNameWithoutExt)) {
                return imageName;
            }
        }

        for (String imageName : imagePool) {
            String imageNameWithoutExt = imageName.substring(0, imageName.lastIndexOf('.')).toLowerCase();
            if (propertyName.startsWith(imageNameWithoutExt)) {
                return imageName;
            }
        }

        int safeIndex = Math.abs(index) % imagePool.size();
        return imagePool.get(safeIndex);
    }

    private ImageIcon loadImageIcon(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            return null;
        }

        ImageIcon cached = imageCache.get(imageName);
        if (cached != null) {
            return cached;
        }

        URL imageUrl = getClass().getResource("/homequest/jframe/imgs/" + imageName);
        if (imageUrl == null) {
            return null;
        }

        ImageIcon original = new ImageIcon(imageUrl);
        Image scaled = original.getImage().getScaledInstance(520, 320, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);
        imageCache.put(imageName, scaledIcon);
        return scaledIcon;
    }

    private void showImagePopup(String propertyName, String imageName) {
        ImageIcon icon = loadImageIcon(imageName);
        if (icon == null) {
            JOptionPane.showMessageDialog(
                this,
                "No image found for " + propertyName + ".",
                "Image Not Found",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JOptionPane optionPane = new JOptionPane(imageLabel, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, propertyName + " - Property Image");
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private List<String> discoverImageFiles() {
        List<String> discovered = new ArrayList<>();
        try {
            URL directoryUrl = getClass().getResource("/homequest/jframe/imgs");
            if (directoryUrl == null) {
                return discovered;
            }

            if ("file".equals(directoryUrl.getProtocol())) {
                File directory = new File(directoryUrl.toURI());
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && isImageFile(file.getName())) {
                            discovered.add(file.getName());
                        }
                    }
                }
            } else if ("jar".equals(directoryUrl.getProtocol())) {
                JarURLConnection connection = (JarURLConnection) directoryUrl.openConnection();
                String prefix = connection.getEntryName();
                Enumeration<JarEntry> entries = connection.getJarFile().entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (!entry.isDirectory() && name.startsWith(prefix + "/")) {
                        String fileName = name.substring(prefix.length() + 1);
                        if (!fileName.contains("/") && isImageFile(fileName)) {
                            discovered.add(fileName);
                        }
                    }
                }
            }
        } catch (URISyntaxException | IOException ex) {
            logger.log(java.util.logging.Level.WARNING, "Unable to scan images directory", ex);
        }

        Collections.sort(discovered);
        return discovered;
    }

    private boolean isImageFile(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg")
            || lower.endsWith(".jpeg")
            || lower.endsWith(".png")
            || lower.endsWith(".gif")
            || lower.endsWith(".bmp");
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
            new ViewListings().setVisible(true)
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
