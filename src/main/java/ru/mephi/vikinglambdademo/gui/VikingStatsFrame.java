package ru.mephi.vikinglambdademo.gui;

import ru.mephi.vikinglambdademo.model.*;
import ru.mephi.vikinglambdademo.service.VikingLambdaService;
import ru.mephi.vikinglambdademo.service.VikingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class VikingStatsFrame extends JFrame {
    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private JTextArea outputArea;

    public VikingStatsFrame(VikingService vikingService, VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;
        setTitle("Viking Statistics & Lambda Operations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        buttonPanel.add(new JButton("Count older than 30"));
        buttonPanel.add(new JButton("Count younger than 30"));
        buttonPanel.add(new JButton("Count age between 20-40"));
        buttonPanel.add(new JButton("Count age outside 20-40"));
        buttonPanel.add(new JButton("Count by Beard & Hair"));
        buttonPanel.add(new JButton("Count with 1 Axe"));
        buttonPanel.add(new JButton("Count with 2 Axes"));
        buttonPanel.add(new JButton("Random Viking > 180 cm"));
        buttonPanel.add(new JButton("List Legendary Equipment"));
        buttonPanel.add(new JButton("Red-haired sorted by age"));
        buttonPanel.add(new JButton("Demo Integer Array Ops"));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.addActionListener(e -> handleButton(btn.getText()));
            }
        }
    }

    private void handleButton(String command) {
        List<Viking> vikings = vikingService.findAll();
        StringBuilder sb = new StringBuilder();
        switch (command) {
            case "Count older than 30":
                long older = lambdaService.countOlderThan(vikings, 30);
                sb.append("Vikings older than 30: ").append(older);
                break;
            case "Count younger than 30":
                long younger = lambdaService.countYoungerThan(vikings, 30);
                sb.append("Vikings younger than 30: ").append(younger);
                break;
            case "Count age between 20-40":
                long between = lambdaService.countAgeBetween(vikings, 20, 40);
                sb.append("Vikings age between 20 and 40: ").append(between);
                break;
            case "Count age outside 20-40":
                long outside = lambdaService.countAgeOutside(vikings, 20, 40);
                sb.append("Vikings age outside 20-40: ").append(outside);
                break;
            case "Count by Beard & Hair":
                BeardStyle beard = (BeardStyle) JOptionPane.showInputDialog(this,
                        "Select beard style:", "Beard",
                        JOptionPane.QUESTION_MESSAGE, null, BeardStyle.values(), BeardStyle.LONG);
                HairColor hair = (HairColor) JOptionPane.showInputDialog(this,
                        "Select hair color:", "Hair",
                        JOptionPane.QUESTION_MESSAGE, null, HairColor.values(), HairColor.Blond);
                if (beard != null && hair != null) {
                    long count = lambdaService.countByBeardAndHair(vikings, beard, hair);
                    sb.append("Count with beard ").append(beard).append(" and hair ").append(hair).append(": ").append(count);
                } else sb.append("Cancelled.");
                break;
            case "Count with 1 Axe":
                long oneAxe = lambdaService.countWithAxes(vikings, 1);
                sb.append("Vikings with exactly 1 axe: ").append(oneAxe);
                break;
            case "Count with 2 Axes":
                long twoAxes = lambdaService.countWithAxes(vikings, 2);
                sb.append("Vikings with exactly 2 axes: ").append(twoAxes);
                break;
            case "Random Viking > 180 cm":
                Optional<Viking> randomTall = lambdaService.getRandomVikingHeightAbove180(vikings);
                if (randomTall.isPresent()) {
                    sb.append("Random tall viking (>180cm): ").append(vikingToString(randomTall.get()));
                } else {
                    sb.append("No vikings taller than 180 cm.");
                }
                break;
            case "List Legendary Equipment":
                List<Viking> legendary = lambdaService.getVikingsWithLegendaryEquipment(vikings);
                if (legendary.isEmpty()) {
                    sb.append("No vikings with legendary equipment.");
                } else {
                    sb.append("Vikings with legendary equipment:\n");
                    legendary.forEach(v -> sb.append(vikingToString(v)).append("\n"));
                }
                break;
            case "Red-haired sorted by age":
                List<Viking> redSorted = lambdaService.getRedHairedSortedByAge(vikings);
                if (redSorted.isEmpty()) {
                    sb.append("No red-haired vikings.");
                } else {
                    sb.append("Red-haired vikings sorted by age:\n");
                    redSorted.forEach(v -> sb.append(vikingToString(v)).append("\n"));
                }
                break;
            case "Demo Integer Array Ops":
                List<Integer> ids = List.of(5, 12, 7, 24, 33, 8, 15);
                Optional<Integer> maxId = lambdaService.findMaxId(ids);
                List<Integer> evenIds = lambdaService.getEvenIds(ids);
                sb.append("Array: ").append(ids).append("\n");
                sb.append("Max ID: ").append(maxId.orElse(null)).append("\n");
                sb.append("Even IDs: ").append(evenIds);
                break;
            default:
                sb.append("Unknown command");
        }
        outputArea.setText(sb.toString());
    }

    private String vikingToString(Viking v) {
        return String.format("%s (age %d, height %d cm, %s hair, %s beard)",
                v.name(), v.age(), v.heightCm(), v.hairColor(), v.beardStyle());
    }
}