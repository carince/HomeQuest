package homequest.util;

import homequest.model.Property;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class PropertyFilterDialog {

    public static final class FilterCriteria {
        private final Set<PropertyStatus> statusFilters = new LinkedHashSet<>();
        private boolean statusFilterActive;
        private Integer block;
        private Integer lot;
        private Double minLotArea;
        private Double maxLotArea;
        private Double minTcp;
        private Double maxTcp;

        public Set<PropertyStatus> getStatusFilters() {
            return statusFilters;
        }

        public boolean isStatusFilterActive() {
            return statusFilterActive;
        }

        public void setStatusFilterActive(boolean statusFilterActive) {
            this.statusFilterActive = statusFilterActive;
        }

        public Integer getBlock() {
            return block;
        }

        public void setBlock(Integer block) {
            this.block = block;
        }

        public Integer getLot() {
            return lot;
        }

        public void setLot(Integer lot) {
            this.lot = lot;
        }

        public Double getMinLotArea() {
            return minLotArea;
        }

        public void setMinLotArea(Double minLotArea) {
            this.minLotArea = minLotArea;
        }

        public Double getMaxLotArea() {
            return maxLotArea;
        }

        public void setMaxLotArea(Double maxLotArea) {
            this.maxLotArea = maxLotArea;
        }

        public Double getMinTcp() {
            return minTcp;
        }

        public void setMinTcp(Double minTcp) {
            this.minTcp = minTcp;
        }

        public Double getMaxTcp() {
            return maxTcp;
        }

        public void setMaxTcp(Double maxTcp) {
            this.maxTcp = maxTcp;
        }

        public void clear() {
            statusFilters.clear();
            statusFilterActive = false;
            block = null;
            lot = null;
            minLotArea = null;
            maxLotArea = null;
            minTcp = null;
            maxTcp = null;
        }

        public FilterCriteria copy() {
            FilterCriteria clone = new FilterCriteria();
            clone.statusFilters.addAll(statusFilters);
            clone.statusFilterActive = statusFilterActive;
            clone.block = block;
            clone.lot = lot;
            clone.minLotArea = minLotArea;
            clone.maxLotArea = maxLotArea;
            clone.minTcp = minTcp;
            clone.maxTcp = maxTcp;
            return clone;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            FilterCriteria other = (FilterCriteria) obj;
            return Objects.equals(statusFilters, other.statusFilters)
                && statusFilterActive == other.statusFilterActive
                && Objects.equals(block, other.block)
                && Objects.equals(lot, other.lot)
                && Objects.equals(minLotArea, other.minLotArea)
                && Objects.equals(maxLotArea, other.maxLotArea)
                && Objects.equals(minTcp, other.minTcp)
                && Objects.equals(maxTcp, other.maxTcp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(statusFilters, statusFilterActive, block, lot, minLotArea, maxLotArea, minTcp, maxTcp);
        }
    }

    private PropertyFilterDialog() {
    }

    public static boolean showFilterDialog(Component parent, FilterCriteria criteria) {
        FilterCriteria previous = criteria.copy();
        boolean allSelected = !criteria.isStatusFilterActive();

        JCheckBox availableCb = new JCheckBox("Available", allSelected || criteria.getStatusFilters().contains(PropertyStatus.AVAILABLE));
        JCheckBox reservedCb = new JCheckBox("Reserved", allSelected || criteria.getStatusFilters().contains(PropertyStatus.RESERVED));
        JCheckBox pendingCb = new JCheckBox("Purchase Pending", allSelected || criteria.getStatusFilters().contains(PropertyStatus.PURCHASE_PENDING));
        JCheckBox soldCb = new JCheckBox("Sold", allSelected || criteria.getStatusFilters().contains(PropertyStatus.SOLD));

        JTextField blockField = new JTextField(toText(criteria.getBlock()));
        JTextField lotField = new JTextField(toText(criteria.getLot()));
        JTextField minLotAreaField = new JTextField(toText(criteria.getMinLotArea()));
        JTextField maxLotAreaField = new JTextField(toText(criteria.getMaxLotArea()));
        JTextField minTcpField = new JTextField(toText(criteria.getMinTcp()));
        JTextField maxTcpField = new JTextField(toText(criteria.getMaxTcp()));

        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Show properties with status:"));
        panel.add(availableCb);
        panel.add(reservedCb);
        panel.add(pendingCb);
        panel.add(soldCb);
        panel.add(new JLabel("Block number (optional):"));
        panel.add(blockField);
        panel.add(new JLabel("Lot number (optional):"));
        panel.add(lotField);
        panel.add(new JLabel("Minimum lot area sqm (optional):"));
        panel.add(minLotAreaField);
        panel.add(new JLabel("Maximum lot area sqm (optional):"));
        panel.add(maxLotAreaField);
        panel.add(new JLabel("Minimum TCP (optional):"));
        panel.add(minTcpField);
        panel.add(new JLabel("Maximum TCP (optional):"));
        panel.add(maxTcpField);

        String[] options = {"Apply", "Clear", "Cancel"};
        int action = JOptionPane.showOptionDialog(
            parent,
            panel,
            "Filter Properties",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (action == 2 || action == JOptionPane.CLOSED_OPTION) {
            return false;
        }

        if (action == 1) {
            if (!criteria.equals(new FilterCriteria())) {
                criteria.clear();
                return true;
            }
            return false;
        }

        Integer blockValue;
        Integer lotValue;
        Double minLotAreaValue;
        Double maxLotAreaValue;
        Double minTcpValue;
        Double maxTcpValue;

        try {
            blockValue = parseInteger(blockField.getText(), "Block");
            lotValue = parseInteger(lotField.getText(), "Lot");
            minLotAreaValue = parseDouble(minLotAreaField.getText(), "Minimum lot area");
            maxLotAreaValue = parseDouble(maxLotAreaField.getText(), "Maximum lot area");
            minTcpValue = parseDouble(minTcpField.getText(), "Minimum TCP");
            maxTcpValue = parseDouble(maxTcpField.getText(), "Maximum TCP");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Invalid Filter", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (minLotAreaValue != null && maxLotAreaValue != null && minLotAreaValue > maxLotAreaValue) {
            JOptionPane.showMessageDialog(parent, "Minimum lot area cannot be greater than maximum lot area.", "Invalid Filter", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (minTcpValue != null && maxTcpValue != null && minTcpValue > maxTcpValue) {
            JOptionPane.showMessageDialog(parent, "Minimum TCP cannot be greater than maximum TCP.", "Invalid Filter", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String validationError = validateCriteria(blockValue, lotValue, minLotAreaValue, maxLotAreaValue, minTcpValue, maxTcpValue);
        if (validationError != null) {
            JOptionPane.showMessageDialog(parent, validationError, "Invalid Filter", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Set<PropertyStatus> selected = new LinkedHashSet<>();
        if (availableCb.isSelected()) {
            selected.add(PropertyStatus.AVAILABLE);
        }
        if (reservedCb.isSelected()) {
            selected.add(PropertyStatus.RESERVED);
        }
        if (pendingCb.isSelected()) {
            selected.add(PropertyStatus.PURCHASE_PENDING);
        }
        if (soldCb.isSelected()) {
            selected.add(PropertyStatus.SOLD);
        }

        criteria.getStatusFilters().clear();
        if (selected.size() == 4) {
            criteria.setStatusFilterActive(false);
        } else {
            criteria.setStatusFilterActive(true);
            criteria.getStatusFilters().addAll(selected);
        }
        criteria.setBlock(blockValue);
        criteria.setLot(lotValue);
        criteria.setMinLotArea(minLotAreaValue);
        criteria.setMaxLotArea(maxLotAreaValue);
        criteria.setMinTcp(minTcpValue);
        criteria.setMaxTcp(maxTcpValue);

        if (criteria.equals(previous)) {
            return false;
        }

        return true;
    }

    public static List<Property> filterProperties(List<Property> source, FilterCriteria criteria) {
        if (criteria == null) {
            return source;
        }

        List<Property> filtered = new ArrayList<>();
        for (Property property : source) {
            if (criteria.isStatusFilterActive() && !criteria.getStatusFilters().contains(property.getStatus())) {
                continue;
            }

            Integer propertyBlock = safeParseInt(property.getBlock());
            Integer propertyLot = safeParseInt(property.getLot());
            if (criteria.getBlock() != null && !criteria.getBlock().equals(propertyBlock)) {
                continue;
            }
            if (criteria.getLot() != null && !criteria.getLot().equals(propertyLot)) {
                continue;
            }

            if (criteria.getMinLotArea() != null && property.getLotArea() < criteria.getMinLotArea()) {
                continue;
            }
            if (criteria.getMaxLotArea() != null && property.getLotArea() > criteria.getMaxLotArea()) {
                continue;
            }

            double tcp = property.getTCP();
            if (criteria.getMinTcp() != null && tcp < criteria.getMinTcp()) {
                continue;
            }
            if (criteria.getMaxTcp() != null && tcp > criteria.getMaxTcp()) {
                continue;
            }

            filtered.add(property);
        }
        return filtered;
    }

    private static String toText(Number value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static Integer parseInteger(String raw, String label) {
        String value = raw == null ? "" : raw.trim();
        if (value.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value.replace(",", ""));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a valid number.");
        }
    }

    private static Double parseDouble(String raw, String label) {
        String value = raw == null ? "" : raw.trim();
        if (value.isEmpty()) {
            return null;
        }
        try {
            return Double.valueOf(value.replace(",", ""));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a valid number.");
        }
    }

    private static Integer safeParseInt(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(raw.trim().replace(",", ""));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static String validateCriteria(
        Integer block,
        Integer lot,
        Double minLotArea,
        Double maxLotArea,
        Double minTcp,
        Double maxTcp
    ) {
        if (block != null && block <= 0) {
            return "Block must be greater than 0.";
        }
        if (lot != null && lot <= 0) {
            return "Lot must be greater than 0.";
        }

        if (minLotArea != null && minLotArea < 0) {
            return "Minimum lot area cannot be negative.";
        }
        if (maxLotArea != null && maxLotArea < 0) {
            return "Maximum lot area cannot be negative.";
        }

        if (minTcp != null && minTcp < 0) {
            return "Minimum TCP cannot be negative.";
        }
        if (maxTcp != null && maxTcp < 0) {
            return "Maximum TCP cannot be negative.";
        }

        return null;
    }
}
