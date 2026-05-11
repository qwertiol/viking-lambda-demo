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

    // Реализовать метод для добавления конкретного викинга
    public void addViking(Viking viking) {
        int row = data.size();
        data.add(viking);
        fireTableRowsInserted(row, row);
    }

    // Реализовать метод для удаления викинга из таблицы
    public void removeViking(String id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id().equals(id)) {
                data.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }

    // Реализовать метод для перезаписи параметров конкретного викинга
    public void updateViking(Viking viking) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id().equals(viking.id())) {
                data.set(i, viking);
                fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    public Viking getVikingAt(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking viking = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> viking.id();
            case 1 -> viking.name();
            case 2 -> viking.age();
            case 3 -> viking.heightCm();
            case 4 -> viking.hairColor();
            case 5 -> viking.beardStyle();
            case 6 -> formatEquipment(viking.equipment());
            default -> "";
        };
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        return equipment.stream()
                .map(item -> item.name() + " [" + item.quality() + "]")
                .collect(Collectors.joining(", "));
    }
}