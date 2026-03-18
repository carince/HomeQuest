/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package homequest.jframe;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author crnc
 */
public class ViewProperties extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ViewProperties.class.getName());
    private final homequest.util.PropertyFilterDialog.FilterCriteria activeFilters = new homequest.util.PropertyFilterDialog.FilterCriteria();
    private final List<String> imagePool = discoverImageFiles();
    private final Map<String, ImageIcon> imageCache = new HashMap<>();

    /**
     * Creates new form ViewProperties
     */
    public ViewProperties() {
        initComponents();
        setTitle("HomeQuest - View Properties");
        loadPropertyImages();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> showFilterDialog());
        buttonWrapper.setLayout(new GridLayout(1, 3, 20, 0));
        buttonWrapper.add(filterButton, 0);

        returnButton.addActionListener(e -> {
            Main main = new Main();
            main.setVisible(true);
            this.dispose();
        });

        logoutButton.addActionListener(e -> {
            Main main = new Main();
            main.setVisible(true);
            this.dispose();
        });
    }

    private void showFilterDialog() {
        if (homequest.util.PropertyFilterDialog.showFilterDialog(this, activeFilters)) {
            loadPropertyImages();
        }
    }

    private void loadPropertyImages() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        container.setOpaque(false);

        java.util.List<homequest.model.Property> properties = homequest.HomeQuest.getAllProperties();
        properties = homequest.util.PropertyFilterDialog.filterProperties(properties, activeFilters);

        if (properties.isEmpty()) {
            JLabel emptyLabel = new JLabel("No property data available.");
            emptyLabel.setFont(new java.awt.Font("Segoe UI", 0, 18));
            emptyLabel.setForeground(java.awt.Color.GRAY);
            JPanel emptyPanel = new JPanel();
            emptyPanel.add(emptyLabel);
            emptyPanel.setPreferredSize(new Dimension(400, 100));
            emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            emptyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            container.add(emptyPanel);
        } else {
            for (int i = 0; i < properties.size(); i++) {
                homequest.model.Property property = properties.get(i);
                String imageName = getImageNameForProperty(property, i);
                JPanel card = createImageCard(imageName, property);
                container.add(card);
                if (i < properties.size() - 1) {
                    container.add(Box.createVerticalStrut(8));
                }
            }
        }

        scrollWrapper.setViewportView(container);
        scrollWrapper.revalidate();
        scrollWrapper.repaint();
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

    private JPanel createImageCard(String imageName, homequest.model.Property property) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEtchedBorder());
        card.setBackground(new java.awt.Color(235, 245, 255));
        card.setPreferredSize(new Dimension(400, 180));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = new JLabel("<html><b>" + property.getName() + "</b><br>" +
                "Block: " + property.getBlock() + " &nbsp; Lot: " + property.getLot() + "<br>" +
                "Lot Area: " + String.format("%,.2f", property.getLotArea()) + " sqm<br>" +
                "TCP: ₱" + String.format("%,.2f", property.getTCP()) + "<br>" +
                "Status: <b>" + property.getStatus() + "</b></html>");
        infoLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton showImageButton = new JButton("Show Image");
        showImageButton.addActionListener(e -> showImagePopup(property.getName(), imageName));
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(showImageButton);

        card.add(infoLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        return card;
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
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        header = new JPanel();
        headerLabel = new JLabel();
        content = new JPanel();
        scrollWrapper = new JScrollPane();
        buttonWrapper = new JPanel();
        returnButton = new JButton();
        logoutButton = new JButton();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HomeQuest");
        setResizable(false);
        setSize(new Dimension(600, 500));

        header.setLayout(new GridBagLayout());
        headerLabel.setFont(new java.awt.Font("Segoe UI", 0, 48));
        headerLabel.setText("HomeQuest");
        header.add(headerLabel, new GridBagConstraints());

        GridBagLayout contentLayout = new GridBagLayout();
        contentLayout.columnWidths = new int[] {0};
        contentLayout.rowHeights = new int[] {0, 5, 0};
        contentLayout.columnWeights = new double[] {1.0};
        contentLayout.rowWeights = new double[] {1.0, 0.0, 0.0};
        content.setLayout(contentLayout);

        scrollWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollWrapper.setMinimumSize(new Dimension(350, 300));
        scrollWrapper.setPreferredSize(new Dimension(400, 400));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        content.add(scrollWrapper, gridBagConstraints);

        buttonWrapper.setLayout(new GridLayout(1, 2, 20, 0));
        returnButton.setText("Return");
        logoutButton.setText("Logout");
        buttonWrapper.add(returnButton);
        buttonWrapper.add(logoutButton);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        content.add(buttonWrapper, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                        .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                    .addContainerGap()));

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
        java.awt.EventQueue.invokeLater(() -> new ViewProperties().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel buttonWrapper;
    private JPanel content;
    private JPanel header;
    private JLabel headerLabel;
    private JButton logoutButton;
    private JButton returnButton;
    private JScrollPane scrollWrapper;
    // End of variables declaration//GEN-END:variables
}
