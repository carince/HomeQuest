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
public class AddProperty extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddProperty.class.getName());

    /**
     * Creates new form Main
     */
    public AddProperty() {
        initComponents();
        loadUserData();
        setupEventHandlers();
    }

    private void loadUserData() {
        homequest.model.Owner owner = homequest.HomeQuest.getOwner();
        UserName.setText(owner.getName());
    }

    private void setupEventHandlers() {
        jButton1.addActionListener(e -> submitProperty());
        jButton5.addActionListener(e -> returnToWorkspace());
        Logout.addActionListener(e -> returnToMain());
        
        // Add input filters and document listeners for validation
        setupInputValidation();
    }

    private void setupInputValidation() {
        // Only allow alphanumeric and spaces for Name
        LNFTextField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str != null && str.matches("[a-zA-Z0-9 ]*")) {
                    super.insertString(offs, str, a);
                }
            }
        });

        // Only allow numbers and decimals for Base Price
        LNFTextField1.setDocument(new NumericDocument(15));
        
        // Only allow numbers and decimals for Floor Area
        LNFTextField2.setDocument(new NumericDocument(10));
        
        // Only allow numbers and decimals for Lot Area
        LNFTextField3.setDocument(new NumericDocument(10));
        
        // Only allow alphanumeric and hyphens for Block/Lot
        LNFTextField4.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str != null && str.matches("[a-zA-Z0-9 -]*")) {
                    super.insertString(offs, str, a);
                }
            }
        });
    }

    // Custom document class for numeric input with decimals
    class NumericDocument extends javax.swing.text.PlainDocument {
        private int maxDigits;

        public NumericDocument(int maxDigits) {
            this.maxDigits = maxDigits;
        }

        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
            if (str == null) return;
            
            String newStr = this.getText(0, this.getLength()) + str;
            
            // Check if it matches number pattern (integers and decimals)
            if (newStr.matches("\\d*\\.?\\d*") && this.getLength() + str.length() <= maxDigits) {
                // Prevent multiple decimal points
                if (str.contains(".") && this.getText(0, this.getLength()).contains(".")) {
                    return;
                }
                super.insertString(offs, str, a);
            }
        }
    }

    private void submitProperty() {
        String modelName = LNFTextField.getText().trim();
        String basePriceStr = LNFTextField1.getText().trim();
        String floorAreaStr = LNFTextField2.getText().trim();
        String lotAreaStr = LNFTextField3.getText().trim();
        String blockLot = LNFTextField4.getText().trim();
        
        // Validate all fields are filled
        StringBuilder errors = new StringBuilder();
        
        if (blockLot.isEmpty()) {
            errors.append("• Block/Lot number is required\n");
        } else if (blockLot.length() > 20) {
            errors.append("• Block/Lot must be 20 characters or less\n");
        }
        
        if (modelName.isEmpty()) {
            errors.append("• Property name is required\n");
        } else if (modelName.length() > 50) {
            errors.append("• Property name must be 50 characters or less\n");
        } else if (!modelName.matches("[a-zA-Z0-9 ]+")) {
            errors.append("• Property name can only contain letters, numbers, and spaces\n");
        }
        
        if (basePriceStr.isEmpty()) {
            errors.append("• Base price is required\n");
        } else if (basePriceStr.equals(".")) {
            errors.append("• Base price must be a valid number\n");
        }
        
        if (floorAreaStr.isEmpty()) {
            errors.append("• Floor area is required\n");
        } else if (floorAreaStr.equals(".")) {
            errors.append("• Floor area must be a valid number\n");
        }
        
        if (lotAreaStr.isEmpty()) {
            errors.append("• Lot area is required\n");
        } else if (lotAreaStr.equals(".")) {
            errors.append("• Lot area must be a valid number\n");
        }
        
        if (errors.length() > 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                errors.toString(),
                "Missing or Invalid Fields",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double basePrice = Double.parseDouble(basePriceStr);
            double floorArea = Double.parseDouble(floorAreaStr);
            double lotArea = Double.parseDouble(lotAreaStr);
            
            // Validate numeric ranges
            StringBuilder numErrors = new StringBuilder();
            
            if (basePrice <= 0) {
                numErrors.append("• Base price must be greater than 0\n");
            } else if (basePrice > 999_999_999) {
                numErrors.append("• Base price is too large (max 999,999,999)\n");
            }
            
            if (floorArea <= 0) {
                numErrors.append("• Floor area must be greater than 0 sqm\n");
            } else if (floorArea > 10_000) {
                numErrors.append("• Floor area seems unusually large (max 10,000 sqm)\n");
            }
            
            if (lotArea <= 0) {
                numErrors.append("• Lot area must be greater than 0 sqm\n");
            } else if (lotArea > 50_000) {
                numErrors.append("• Lot area seems unusually large (max 50,000 sqm)\n");
            }
            
            if (lotArea < floorArea) {
                numErrors.append("• Lot area cannot be smaller than floor area\n");
            }
            
            if (numErrors.length() > 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    numErrors.toString(),
                    "Invalid Values",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            homequest.model.Owner owner = homequest.HomeQuest.getOwner();
            homequest.model.Agent agent = homequest.HomeQuest.getAgent();
            java.util.List<homequest.model.Property> allProperties = homequest.HomeQuest.getAllProperties();
            
            homequest.model.HouseAndLot newProperty = owner.addNewProperty(
                blockLot, lotArea, basePrice, modelName, floorArea, allProperties
            );
            
            int assignChoice = javax.swing.JOptionPane.showConfirmDialog(this,
                "Property added successfully!\n\nAssign this property to agent " + agent.getName() + "?",
                "Assign to Agent",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);
            
            if (assignChoice == javax.swing.JOptionPane.YES_OPTION) {
                owner.assignPropertyToAgent(newProperty, agent);
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Property " + blockLot + " has been assigned to " + agent.getName(),
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Property added but not assigned to agent.",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
            
            clearForm();
            
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Invalid numeric value. Please check price and area fields:\n\n" +
                "• Base Price: Must be a valid decimal number\n" +
                "• Floor Area: Must be a valid decimal number\n" +
                "• Lot Area: Must be a valid decimal number",
                "Invalid Input",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "An unexpected error occurred: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        LNFTextField.setText("");
        LNFTextField1.setText("");
        LNFTextField2.setText("");
        LNFTextField3.setText("");
        LNFTextField4.setText("");
    }

    private void returnToWorkspace() {
        homequest.jframe.Owner.Workspace workspace = new homequest.jframe.Owner.Workspace();
        workspace.setVisible(true);
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

        Header = new javax.swing.JPanel();
        HeaderLabel = new javax.swing.JLabel();
        Content = new javax.swing.JPanel();
        ContentScrollPane = new javax.swing.JScrollPane();
        ContentWrapper = new javax.swing.JPanel();
        LNF = new javax.swing.JPanel();
        LNFLabel = new javax.swing.JLabel();
        LNFTextField = new javax.swing.JTextField();
        LNF1 = new javax.swing.JPanel();
        LNFLabel1 = new javax.swing.JLabel();
        LNFTextField1 = new javax.swing.JTextField();
        LNF2 = new javax.swing.JPanel();
        LNFLabel2 = new javax.swing.JLabel();
        LNFTextField2 = new javax.swing.JTextField();
        LNF3 = new javax.swing.JPanel();
        LNFLabel3 = new javax.swing.JLabel();
        LNFTextField3 = new javax.swing.JTextField();
        LNF4 = new javax.swing.JPanel();
        LNFLabel4 = new javax.swing.JLabel();
        LNFTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        ButtonWrapper2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        UserInfo = new javax.swing.JPanel();
        UserIcon = new javax.swing.JLabel();
        UserType = new javax.swing.JLabel();
        UserName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 500));
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

        LNFLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        LNFLabel.setText("Name");

        LNFTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LNFTextField.addActionListener(this::LNFTextFieldActionPerformed);

        javax.swing.GroupLayout LNFLayout = new javax.swing.GroupLayout(LNF);
        LNF.setLayout(LNFLayout);
        LNFLayout.setHorizontalGroup(
            LNFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LNFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LNFTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(LNFLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        LNFLayout.setVerticalGroup(
            LNFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNFLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LNFLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LNFTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        LNFLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12));
        LNFLabel1.setText("Base price (PHP)");

        LNFTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LNFTextField1.addActionListener(this::LNFTextField1ActionPerformed);

        javax.swing.GroupLayout LNF1Layout = new javax.swing.GroupLayout(LNF1);
        LNF1.setLayout(LNF1Layout);
        LNF1Layout.setHorizontalGroup(
            LNF1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LNF1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LNFTextField1)
                    .addComponent(LNFLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addContainerGap())
        );
        LNF1Layout.setVerticalGroup(
            LNF1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LNFLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LNFTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        LNFLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12));
        LNFLabel2.setText("Floor area (sqm)");

        LNFTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LNFTextField2.addActionListener(this::LNFTextField2ActionPerformed);

        javax.swing.GroupLayout LNF2Layout = new javax.swing.GroupLayout(LNF2);
        LNF2.setLayout(LNF2Layout);
        LNF2Layout.setHorizontalGroup(
            LNF2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LNF2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LNFTextField2)
                    .addComponent(LNFLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addContainerGap())
        );
        LNF2Layout.setVerticalGroup(
            LNF2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LNFLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LNFTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        LNFLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12));
        LNFLabel3.setText("Lot Area (sqm)");

        LNFTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LNFTextField3.addActionListener(this::LNFTextField3ActionPerformed);

        javax.swing.GroupLayout LNF3Layout = new javax.swing.GroupLayout(LNF3);
        LNF3.setLayout(LNF3Layout);
        LNF3Layout.setHorizontalGroup(
            LNF3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LNF3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LNFTextField3)
                    .addComponent(LNFLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addContainerGap())
        );
        LNF3Layout.setVerticalGroup(
            LNF3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LNFLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LNFTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        LNFLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12));
        LNFLabel4.setText("Block/Lot");

        LNFTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LNFTextField4.addActionListener(this::LNFTextField4ActionPerformed);

        javax.swing.GroupLayout LNF4Layout = new javax.swing.GroupLayout(LNF4);
        LNF4.setLayout(LNF4Layout);
        LNF4Layout.setHorizontalGroup(
            LNF4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LNF4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LNFTextField4)
                    .addComponent(LNFLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addContainerGap())
        );
        LNF4Layout.setVerticalGroup(
            LNF4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LNF4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LNFLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LNFTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("Add New Property");

        javax.swing.GroupLayout ContentWrapperLayout = new javax.swing.GroupLayout(ContentWrapper);
        ContentWrapper.setLayout(ContentWrapperLayout);
        ContentWrapperLayout.setHorizontalGroup(
            ContentWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LNF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(LNF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(LNF2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(LNF3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(LNF4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(ContentWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContentWrapperLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        ContentWrapperLayout.setVerticalGroup(
            ContentWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContentWrapperLayout.createSequentialGroup()
                .addComponent(LNF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LNF4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LNF3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LNF2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LNF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(ContentWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContentWrapperLayout.createSequentialGroup()
                    .addContainerGap(330, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addContainerGap()))
        );

        ContentScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ContentScrollPane.setViewportView(ContentWrapper);
        ContentScrollPane.setPreferredSize(new java.awt.Dimension(350, 350));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Content.add(ContentScrollPane, gridBagConstraints);

        ButtonWrapper2.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        jButton5.setText("Return");
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
                .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LNFTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LNFTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LNFTextFieldActionPerformed

    private void LNFTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LNFTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LNFTextField1ActionPerformed

    private void LNFTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LNFTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LNFTextField2ActionPerformed

    private void LNFTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LNFTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LNFTextField3ActionPerformed

    private void LNFTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LNFTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LNFTextField4ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new AddProperty().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonWrapper2;
    private javax.swing.JPanel Content;
    private javax.swing.JScrollPane ContentScrollPane;
    private javax.swing.JPanel ContentWrapper;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel HeaderLabel;
    private javax.swing.JPanel LNF;
    private javax.swing.JPanel LNF1;
    private javax.swing.JPanel LNF2;
    private javax.swing.JPanel LNF3;
    private javax.swing.JPanel LNF4;
    private javax.swing.JLabel LNFLabel;
    private javax.swing.JLabel LNFLabel1;
    private javax.swing.JLabel LNFLabel2;
    private javax.swing.JLabel LNFLabel3;
    private javax.swing.JLabel LNFLabel4;
    private javax.swing.JTextField LNFTextField;
    private javax.swing.JTextField LNFTextField1;
    private javax.swing.JTextField LNFTextField2;
    private javax.swing.JTextField LNFTextField3;
    private javax.swing.JTextField LNFTextField4;
    private javax.swing.JButton Logout;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JPanel UserInfo;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    // End of variables declaration//GEN-END:variables
}
