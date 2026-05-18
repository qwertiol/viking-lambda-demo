package ru.mephi.vikinglambdademo.gui;

import ru.mephi.vikinglambdademo.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// новое окно для дз5
public class VikingDialog extends JDialog {
    private JTextField nameField;
    private JSpinner ageSpinner, heightSpinner;
    private JComboBox<HairColor> hairCombo;
    private JComboBox<BeardStyle> beardCombo;
    private DefaultListModel<String> equipmentModel;
    private JList<String> equipmentList;
    private boolean confirmed = false;
    private Viking viking;
    private int existingId;

    public VikingDialog(Frame owner, String title, Viking initial) {
        super(owner, title, true);
        initComponents();
        if (initial != null) {
            existingId = initial.id();
            loadViking(initial);
        } else {
            existingId = -1;
        }
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx=1; form.add(nameField, gbc);

        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Age:"), gbc);
        ageSpinner = new JSpinner(new SpinnerNumberModel(25,1,120,1));
        gbc.gridx=1; form.add(ageSpinner, gbc);

        gbc.gridx=0; gbc.gridy=2; form.add(new JLabel("Height (cm):"), gbc);
        heightSpinner = new JSpinner(new SpinnerNumberModel(180,100,250,1));
        gbc.gridx=1; form.add(heightSpinner, gbc);

        gbc.gridx=0; gbc.gridy=3; form.add(new JLabel("Hair color:"), gbc);
        hairCombo = new JComboBox<>(HairColor.values());
        gbc.gridx=1; form.add(hairCombo, gbc);

        gbc.gridx=0; gbc.gridy=4; form.add(new JLabel("Beard style:"), gbc);
        beardCombo = new JComboBox<>(BeardStyle.values());
        gbc.gridx=1; form.add(beardCombo, gbc);

        add(form, BorderLayout.CENTER);

        JPanel equipPanel = new JPanel(new BorderLayout());
        equipPanel.setBorder(BorderFactory.createTitledBorder("Equipment"));
        equipmentModel = new DefaultListModel<>();
        equipmentList = new JList<>(equipmentModel);
        equipPanel.add(new JScrollPane(equipmentList), BorderLayout.CENTER);
        JPanel equipButtons = new JPanel();
        JButton addEq = new JButton("Add");
        addEq.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Equipment name:");
            if (name != null && !name.trim().isEmpty()) {
                String quality = (String) JOptionPane.showInputDialog(this, "Quality:", "Quality",
                        JOptionPane.QUESTION_MESSAGE, null, new String[]{"Common","Uncommon","Rare","Legendary"}, "Common");
                if (quality != null) {
                    equipmentModel.addElement(name + " [" + quality + "]");
                }
            }
        });
        JButton removeEq = new JButton("Remove");
        removeEq.addActionListener(e -> {
            int idx = equipmentList.getSelectedIndex();
            if (idx>=0) equipmentModel.remove(idx);
        });
        equipButtons.add(addEq);
        equipButtons.add(removeEq);
        equipPanel.add(equipButtons, BorderLayout.SOUTH);
        add(equipPanel, BorderLayout.EAST);

        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            confirmed = true;
            viking = buildViking();
            setVisible(false);
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> setVisible(false));
        buttons.add(ok);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);
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
        int id = (existingId != -1) ? existingId : (int)(System.currentTimeMillis() & Integer.MAX_VALUE);
        if (existingId == -1) {
            id = (int)(System.currentTimeMillis() % Integer.MAX_VALUE);
        }
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
                String quality = entry.substring(bracket+1, entry.length()-1).trim();
                items.add(new EquipmentItem(itemName, quality));
            }
        }
        return new Viking(id, name, age, height, hair, beard, items);
    }

    public boolean isConfirmed() { return confirmed; }
    public Viking getViking() { return viking; }
}