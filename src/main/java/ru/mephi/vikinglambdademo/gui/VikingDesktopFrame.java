package ru.mephi.vikinglambdademo.gui;

import ru.mephi.vikinglambdademo.model.Viking;
import ru.mephi.vikinglambdademo.service.VikingService;
import ru.mephi.vikinglambdademo.service.VikingLambdaService;

import javax.swing.*;
import java.awt.*;

public class VikingDesktopFrame extends JFrame {
    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final JTable vikingTable;

    public VikingDesktopFrame(VikingService vikingService, VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton createRandom = new JButton("Create random viking");
        createRandom.addActionListener(e -> onCreateRandomViking());
        JButton createCustom = new JButton("Create custom viking");
        createCustom.addActionListener(e -> onAddCustomViking());
        JButton delete = new JButton("Delete selected viking");
        delete.addActionListener(e -> onDeleteSelected());
        JButton edit = new JButton("Edit selected viking");
        edit.addActionListener(e -> onEditSelected());
        JButton massGen = new JButton("Mass generate");
        massGen.addActionListener(e -> onMassGenerate());
        JButton stats = new JButton("Statistics");
        stats.addActionListener(e -> openStatsFrame());

        bottom.add(createRandom);
        bottom.add(createCustom);
        bottom.add(delete);
        bottom.add(edit);
        bottom.add(massGen);
        bottom.add(stats);
        add(bottom, BorderLayout.SOUTH);
    }

    private void onCreateRandomViking() {
        Viking v = vikingService.createRandomViking();
        tableModel.addViking(v);
    }

    private void onAddCustomViking() {
        VikingDialog dlg = new VikingDialog(this, "Add new Viking", null);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            Viking v = dlg.getViking();
            vikingService.addViking(v);
            tableModel.addViking(v);
        }
    }

    private void onDeleteSelected() {
        int row = vikingTable.getSelectedRow();
        if (row >= 0) {
            Viking v = tableModel.getVikingAt(row);
            if (JOptionPane.showConfirmDialog(this, "Delete " + v.name() + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                vikingService.deleteViking(v.id());
                tableModel.removeViking(v.id());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a viking to delete.");
        }
    }

    private void onEditSelected() {
        int row = vikingTable.getSelectedRow();
        if (row >= 0) {
            Viking original = tableModel.getVikingAt(row);
            VikingDialog dlg = new VikingDialog(this, "Edit Viking", original);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                Viking updated = dlg.getViking();
                vikingService.updateViking(original.id(), updated);
                tableModel.updateViking(updated);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a viking to edit.");
        }
    }

    private void onMassGenerate() {
        String input = JOptionPane.showInputDialog(this, "Number of vikings to generate:");
        if (input != null) {
            try {
                int count = Integer.parseInt(input);
                if (count > 0) {
                    vikingService.generateMassVikings(count);
                    refreshTableFromService();
                    JOptionPane.showMessageDialog(this, "Generated " + count + " vikings.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        }
    }

    private void refreshTableFromService() {
        tableModel.clear();
        vikingService.findAll().forEach(tableModel::addViking);
    }

    private void openStatsFrame() {
        new VikingStatsFrame(vikingService, lambdaService).setVisible(true);
    }

    public void addNewViking(Viking v) { tableModel.addViking(v); }
    public void removeVikingById(int id) { tableModel.removeViking(id); }
    public void updateViking(Viking v) { tableModel.updateViking(v); }
}