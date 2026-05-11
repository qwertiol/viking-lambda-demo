package ru.mephi.vikinglambdademo.gui;

import ru.mephi.vikinglambdademo.model.Viking;
import ru.mephi.vikinglambdademo.service.VikingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.UUID;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final JTable vikingTable;

    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 450));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(e -> onCreateRandomViking());

        JButton addButton = new JButton("Create custom viking");
        addButton.addActionListener(this::onAddCustomViking);

        JButton deleteButton = new JButton("Delete selected viking");
        deleteButton.addActionListener(e -> onDeleteSelected());

        JButton editButton = new JButton("Edit selected viking");
        editButton.addActionListener(e -> onEditSelected());

        bottomPanel.add(createButton);
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(editButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onCreateRandomViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void onAddCustomViking(ActionEvent e) {
        VikingDialog dialog = new VikingDialog(this, "Add new Viking", null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Viking newViking = dialog.getViking();
            vikingService.addViking(newViking);
            tableModel.addViking(newViking);
        }
    }

    private void onDeleteSelected() {
        int selectedRow = vikingTable.getSelectedRow();
        if (selectedRow >= 0) {
            Viking viking = tableModel.getVikingAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete " + viking.name() + "?",
                    "Confirm deletion",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                vikingService.deleteViking(viking.id());
                tableModel.removeViking(viking.id());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a viking to delete.");
        }
    }

    private void onEditSelected() {
        int selectedRow = vikingTable.getSelectedRow();
        if (selectedRow >= 0) {
            Viking original = tableModel.getVikingAt(selectedRow);
            VikingDialog dialog = new VikingDialog(this, "Edit Viking", original);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Viking updated = dialog.getViking();
                vikingService.updateViking(original.id(), updated);
                tableModel.updateViking(updated);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a viking to edit.");
        }
    }

    // Методы для вызова из VikingListener
    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }

    public void removeVikingById(String id) {
        tableModel.removeViking(id);
    }

    public void updateViking(Viking viking) {
        tableModel.updateViking(viking);
    }
}