package homequest.jframe.Owner;

import homequest.HomeQuest;
import homequest.model.Owner;
import homequest.model.Property;
import homequest.util.PropertyStatus;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class GenerateReport extends JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(GenerateReport.class.getName());

    private JPanel header;
    private JLabel headerLabel;
    private JLabel userType;
    private JLabel userName;
    private JPanel content;
    private JScrollPane tableScroll;
    private JTable reportTable;
    private JPanel summaryPanel;
    private JLabel totalLotsLabel;
    private JLabel availableLabel;
    private JLabel reservedLabel;
    private JLabel purchasePendingLabel;
    private JLabel soldLabel;
    private JPanel buttonWrapper;
    private JButton returnButton;
    private JButton logoutButton;

    public GenerateReport() {
        initComponents();
        setTitle("HomeQuest - Owner / Generate Report");
        loadUserData();
        loadReportData();
        setupEventHandlers();
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        header = new JPanel();
        headerLabel = new JLabel();
        userType = new JLabel();
        userName = new JLabel();
        content = new JPanel();
        tableScroll = new JScrollPane();
        reportTable = new JTable();
        summaryPanel = new JPanel();
        totalLotsLabel = new JLabel();
        availableLabel = new JLabel();
        reservedLabel = new JLabel();
        purchasePendingLabel = new JLabel();
        soldLabel = new JLabel();
        buttonWrapper = new JPanel();
        returnButton = new JButton();
        logoutButton = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(600, 500));

        header.setLayout(new GridBagLayout());

        headerLabel.setFont(new java.awt.Font("Segoe UI", 0, 48));
        headerLabel.setText("HomeQuest");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        header.add(headerLabel, gridBagConstraints);

        userType.setFont(new java.awt.Font("Segoe UI", 0, 24));
        userType.setHorizontalAlignment(SwingConstants.CENTER);
        userType.setText("Owner");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        header.add(userType, gridBagConstraints);

        userName.setFont(new java.awt.Font("Segoe UI", 0, 24));
        userName.setHorizontalAlignment(SwingConstants.CENTER);
        userName.setText("Owner Name Here");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        header.add(userName, gridBagConstraints);

        GridBagLayout contentLayout = new GridBagLayout();
        contentLayout.columnWidths = new int[] {0};
        contentLayout.rowHeights = new int[] {0, 0, 0};
        contentLayout.columnWeights = new double[] {1.0};
        contentLayout.rowWeights = new double[] {1.0, 0.0, 0.0};
        content.setLayout(contentLayout);

        reportTable.setModel(
            new DefaultTableModel(
                new Object[][] {},
                new String[] {"BLOCK", "LOT", "NAME", "STATUS", "TCP"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            }
        );
        reportTable.setRowHeight(24);
        reportTable.getTableHeader().setReorderingAllowed(false);
        reportTable.setAutoCreateRowSorter(true);
        tableScroll.setViewportView(reportTable);
        tableScroll.setPreferredSize(new Dimension(760, 300));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 6, 10);
        content.add(tableScroll, gridBagConstraints);

        summaryPanel.setLayout(new GridLayout(0, 1, 0, 4));
        summaryPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder("Summary"),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        summaryPanel.add(totalLotsLabel);
        summaryPanel.add(availableLabel);
        summaryPanel.add(reservedLabel);
        summaryPanel.add(purchasePendingLabel);
        summaryPanel.add(soldLabel);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 6, 10);
        content.add(summaryPanel, gridBagConstraints);

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
                        .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                    .addContainerGap())
        );

        pack();
    }

    private void loadUserData() {
        Owner owner = HomeQuest.getOwner();
        userName.setText(owner.getName());
        userType.setText("Owner");
    }

    private void setupEventHandlers() {
        returnButton.addActionListener(e -> returnToWorkspace());
        logoutButton.addActionListener(e -> returnToMain());
    }

    private void loadReportData() {
        Owner owner = HomeQuest.getOwner();
        List<Property> properties = owner.getProperties();

        DefaultTableModel model = (DefaultTableModel) reportTable.getModel();
        model.setRowCount(0);

        Map<PropertyStatus, Integer> statusCounts = new EnumMap<>(PropertyStatus.class);
        for (PropertyStatus status : PropertyStatus.values()) {
            statusCounts.put(status, 0);
        }

        for (Property property : properties) {
            model.addRow(
                new Object[] {
                    safeValue(property.getBlock()),
                    safeValue(property.getLot()),
                    safeValue(property.getName()),
                    property.getStatus().name(),
                    String.format("₱%,.2f", property.getTCP())
                }
            );

            PropertyStatus status = property.getStatus();
            statusCounts.put(status, statusCounts.get(status) + 1);
        }

        totalLotsLabel.setText("Total Lots: " + properties.size());
        availableLabel.setText("Available: " + statusCounts.get(PropertyStatus.AVAILABLE));
        reservedLabel.setText("Reserved: " + statusCounts.get(PropertyStatus.RESERVED));
        purchasePendingLabel.setText("Purchase Pending: " + statusCounts.get(PropertyStatus.PURCHASE_PENDING));
        soldLabel.setText("Sold: " + statusCounts.get(PropertyStatus.SOLD));
    }

    private String safeValue(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private void returnToWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setVisible(true);
        this.dispose();
    }

    private void returnToMain() {
        homequest.jframe.Main main = new homequest.jframe.Main();
        main.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
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

        java.awt.EventQueue.invokeLater(() -> new GenerateReport().setVisible(true));
    }
}
