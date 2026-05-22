package ru.mephi.vikinglambdademo.gui;

import ru.mephi.vikinglambdademo.model.*;
import ru.mephi.vikinglambdademo.service.VikingLambdaService;
import ru.mephi.vikinglambdademo.service.VikingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

// новое окно для дз5 с кнопками вызывающими методы лямбда сервиса
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
        setLayout(new BorderLayout(10,10));

        JPanel buttonPanel = new JPanel(new GridLayout(0,2,5,5));

        buttonPanel.add(createButton("Count older than", e -> countOlderThan()));
        buttonPanel.add(createButton("Count younger than", e -> countYoungerThan()));
        buttonPanel.add(createButton("Count age between", e -> countBetween()));
        buttonPanel.add(createButton("Count age outside", e -> countOutside()));

        buttonPanel.add(createButton("Count by Beard & Hair", e -> countBeardHair()));

        buttonPanel.add(createButton("Count with 1 or 2 Axes", e -> countOneOrTwoAxes()));

        buttonPanel.add(createButton("Random Viking > 180 cm", e -> randomTall()));

        buttonPanel.add(createButton("List Legendary Equipment", e -> listLegendary()));

        buttonPanel.add(createButton("Red-haired sorted by age", e -> redHairedSorted()));

        buttonPanel.add(createButton("Find max ID among vikings", e -> findMaxId()));
        buttonPanel.add(createButton("Get even IDs of vikings", e -> getEvenIds()));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(outputArea);

        add(buttonPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.addActionListener(listener);
        return btn;
    }

    private List<Viking> getVikings() {
        return vikingService.findAll();
    }

    private void setOutput(String s) {
        outputArea.setText(s);
    }

    private void countOlderThan() {
        String input = JOptionPane.showInputDialog(this, "Enter age (greater than):");
        if (input != null) {
            try {
                int age = Integer.parseInt(input);
                long count = lambdaService.countOlderThan(getVikings(), age);
                setOutput("Vikings older than " + age + ": " + count);
            } catch (NumberFormatException ex) {
                setOutput("Invalid number.");
            }
        }
    }

    private void countYoungerThan() {
        String input = JOptionPane.showInputDialog(this, "Enter age (less than):");
        if (input != null) {
            try {
                int age = Integer.parseInt(input);
                long count = lambdaService.countYoungerThan(getVikings(), age);
                setOutput("Vikings younger than " + age + ": " + count);
            } catch (NumberFormatException ex) {
                setOutput("Invalid number.");
            }
        }
    }

    private void countBetween() {
        String minStr = JOptionPane.showInputDialog(this, "Min age:");
        if (minStr == null) return;
        String maxStr = JOptionPane.showInputDialog(this, "Max age:");
        if (maxStr == null) return;
        try {
            int min = Integer.parseInt(minStr);
            int max = Integer.parseInt(maxStr);
            long count = lambdaService.countAgeBetween(getVikings(), min, max);
            setOutput("Vikings with age between " + min + " and " + max + ": " + count);
        } catch (NumberFormatException ex) {
            setOutput("Invalid number.");
        }
    }

    private void countOutside() {
        String minStr = JOptionPane.showInputDialog(this, "Lower bound (exclusive):");
        if (minStr == null) return;
        String maxStr = JOptionPane.showInputDialog(this, "Upper bound (exclusive):");
        if (maxStr == null) return;
        try {
            int min = Integer.parseInt(minStr);
            int max = Integer.parseInt(maxStr);
            long count = lambdaService.countAgeOutside(getVikings(), min, max);
            setOutput("Vikings with age outside [" + min + ", " + max + "]: " + count);
        } catch (NumberFormatException ex) {
            setOutput("Invalid number.");
        }
    }

    private void countBeardHair() {
        BeardStyle beard = (BeardStyle) JOptionPane.showInputDialog(this, "Select beard style:", "Beard",
                JOptionPane.QUESTION_MESSAGE, null, BeardStyle.values(), BeardStyle.LONG);
        if (beard == null) return;
        HairColor hair = (HairColor) JOptionPane.showInputDialog(this, "Select hair color:", "Hair",
                JOptionPane.QUESTION_MESSAGE, null, HairColor.values(), HairColor.Blond);
        if (hair == null) return;
        long count = lambdaService.countByBeardAndHair(getVikings(), beard, hair);
        setOutput("Vikings with beard " + beard + " and hair " + hair + ": " + count);
    }

    private void countOneOrTwoAxes() {
        long count = lambdaService.countWithOneOrTwoAxes(getVikings());
        setOutput("Vikings with exactly 1 or 2 axes: " + count);
    }

    private void randomTall() {
        Optional<Viking> opt = lambdaService.getRandomVikingHeightAbove180(getVikings());
        if (opt.isPresent()) {
            Viking v = opt.get();
            setOutput("Random viking >180cm: " + v.name() + " (age " + v.age() + ", height " + v.heightCm() + " cm)");
        } else {
            setOutput("No vikings taller than 180 cm.");
        }
    }

    private void listLegendary() {
        List<Viking> list = lambdaService.getVikingsWithLegendaryEquipment(getVikings());
        if (list.isEmpty()) {
            setOutput("No vikings with legendary equipment.");
        } else {
            StringBuilder sb = new StringBuilder("Vikings with legendary equipment:\n");
            list.forEach(v -> sb.append(v.name()).append(" (ID ").append(v.id()).append(")\n"));
            setOutput(sb.toString());
        }
    }

    private void redHairedSorted() {
        List<Viking> list = lambdaService.getRedBeardedSortedByAge(getVikings());
        if (list.isEmpty()) {
            setOutput("No red-haired vikings.");
        } else {
            StringBuilder sb = new StringBuilder("Red-haired vikings sorted by age:\n");
            list.forEach(v -> sb.append(v.name()).append(", age ").append(v.age()).append("\n"));
            setOutput(sb.toString());
        }
    }

    private void findMaxId() {
        Integer[] ids = lambdaService.toIdArray(getVikings());
        Optional<Integer> maxId = lambdaService.findMaxId(ids);
        setOutput("Maximum ID among vikings: " + (maxId.isPresent() ? maxId.get() : "none"));
    }

    private void getEvenIds() {
        Integer[] ids = lambdaService.toIdArray(getVikings());
        Integer[] evenIds = lambdaService.getEvenIds(ids); 
        if (evenIds.length == 0) {
            setOutput("No even IDs found.");
        } else {
            setOutput("Even IDs: " + java.util.Arrays.toString(evenIds));
        }
    }
}