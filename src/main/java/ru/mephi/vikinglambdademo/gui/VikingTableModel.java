package ru.mephi.vikinglambdademo.gui;

import ru.mephi.vikinglambdademo.model.EquipmentItem;
import ru.mephi.vikinglambdademo.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Name", "Age", "Height (cm)", "Hair color", "Beard style", "Equipment"};
    private final List<Viking> data = new ArrayList<>();

    public void addViking(Viking viking) {
        data.add(viking);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    // новый метод дз4
    public void removeViking(int id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id() == id) {
                data.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }
    
    // новый метод дз4  
    public void updateViking(Viking viking) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id() == viking.id()) {
                data.set(i, viking);
                fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    // новый метод дз4
    public void clear() {
        data.clear();
        fireTableDataChanged();
    }

    public Viking getVikingAt(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() { return data.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking v = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> v.id();
            case 1 -> v.name();
            case 2 -> v.age();
            case 3 -> v.heightCm();
            case 4 -> v.hairColor();
            case 5 -> v.beardStyle();
            case 6 -> v.equipment().stream()
                    .map(item -> item.name() + " [" + item.quality() + "]")
                    .collect(Collectors.joining(", "));
            default -> "";
        };
    }
}