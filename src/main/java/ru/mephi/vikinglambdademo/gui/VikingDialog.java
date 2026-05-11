package ru.mephi.vikinglambdademo.gui;

import ru.mephi.vikinglambdademo.model.BeardStyle;
import ru.mephi.vikinglambdademo.model.Viking;
import ru.mephi.vikinglambdademo.model.HairColor;
import ru.mephi.vikinglambdademo.model.EquipmentItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VikingDialog extends JDialog {

    private JTextField nameField;
    private JSpinner ageSpinner;
    private JSpinner heightSpinner;
    private JComboBox<HairColor> hairCombo;
    private JComboBox<BeardStyle> beardCombo;
    private DefaultListModel<String> equipmentModel;
    private JList<String> equipmentList;

    private boolean confirmed = false;
    private Viking viking;

    public VikingDialog(Frame owner, String title, Viking initial) {
        super(owner, title, true);
        initComponents();
        if (initial != null) {
            loadViking(initial);
        }
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Age:"), gbc);
        ageSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 120, 1));
        gbc.gridx = 1;
        formPanel.add(ageSpinner, gbc);

        // Height
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Height (cm):"), gbc);
        heightSpinner = new JSpinner(new SpinnerNumberModel(180, 100, 250, 1));
        gbc.gridx = 1;
        formPanel.add(heightSpinner, gbc);

        // Hair color
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Hair color:"), gbc);
        hairCombo = new JComboBox<>(HairColor.values());
        gbc.gridx = 1;
        formPanel.add(hairCombo, gbc);

        // Beard style
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Beard style:"), gbc);
        beardCombo = new JComboBox<>(BeardStyle.values());
        gbc.gridx = 1;
        formPanel.add(beardCombo, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel equipPanel = new JPanel(new BorderLayout());
        equipPanel.setBorder(BorderFactory.createTitledBorder("Equipment"));
        equipmentModel = new DefaultListModel<>();
        equipmentList = new JList<>(equipmentModel);
        equipPanel.add(new JScrollPane(equipmentList), BorderLayout.CENTER);

        JPanel equipButtonPanel = new JPanel();
        JButton addEquipButton = new JButton("Add");
        addEquipButton.addActionListener(this::addEquipment);
        JButton removeEquipButton = new JButton("Remove");
        removeEquipButton.addActionListener(e -> {
            int idx = equipmentList.getSelectedIndex();
            if (idx >= 0) equipmentModel.remove(idx);
        });
        equipButtonPanel.add(addEquipButton);
        equipButtonPanel.add(removeEquipButton);
        equipPanel.add(equipButtonPanel, BorderLayout.SOUTH);

        add(equipPanel, BorderLayout.EAST);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            confirmed = true;
            viking = buildViking();
            setVisible(false);
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEquipment(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Equipment name:");
        if (name != null && !name.trim().isEmpty()) {
            String quality = (String) JOptionPane.showInputDialog(this,
                    "Quality:", "Quality", JOptionPane.QUESTION_MESSAGE,
                    null, new String[]{"Common", "Uncommon", "Rare", "Legendary"}, "Common");
            if (quality != null) {
                equipmentModel.addElement(name + " [" + quality + "]");
            }
        }
    }

    private void loadViking(Viking v) {
        nameField.setText(v.name());
        ageSpinner.setValue(v.age());
        heightSpinner.setValue(v.heightCm());
        hairCombo.setSelectedItem(v.hairColor());
        beardCombo.setSelectedItem(v.beardStyle());
        equipmentModel.clear();
        for (EquipmentItem item : v.equipment()) {
            equipmentModel.addElement(item.name() + " [" + item.quality() + "]");
        }
    }

    private Viking buildViking() {
        String id = (viking != null) ? viking.id() : UUID.randomUUID().toString();
        String name = nameField.getText().trim();
        int age = (Integer) ageSpinner.getValue();
        int height = (Integer) heightSpinner.getValue();
        HairColor hair = (HairColor) hairCombo.getSelectedItem();
        BeardStyle beard = (BeardStyle) beardCombo.getSelectedItem();

        List<EquipmentItem> items = new ArrayList<>();
        for (int i = 0; i < equipmentModel.size(); i++) {
            String entry = equipmentModel.get(i);
            int bracket = entry.lastIndexOf('[');
            if (bracket > 0) {
                String itemName = entry.substring(0, bracket).trim();
                String quality = entry.substring(bracket + 1, entry.length() - 1).trim();
                items.add(new EquipmentItem(itemName, quality));
            }
        }
        return new Viking(id, name, age, height, hair, beard, items);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Viking getViking() {
        return viking;
    }
}