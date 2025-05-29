import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GUI extends JFrame {
    private CinemaManager manager;
    private JTextArea outputArea;

    public GUI(CinemaManager manager) {
        this.manager = manager;
        setTitle("Кино Резервации");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JButton searchByTitleBtn = new JButton("Търсене по заглавие");
        JButton searchByDateBtn = new JButton("Търсене по дата");
        JButton sortBtn = new JButton("Сортиране по дата");
        JButton sortByNameBtn = new JButton("Сортиране по име");
        JButton cancelBtn = new JButton("Анулирай резервация");
        JButton showAllBtn = new JButton("Покажи всички прожекции");
        JButton saveBtn = new JButton("Запази резервации");
        JButton reserveBtn = new JButton("Направи резервация");

        panel.add(searchByTitleBtn);
        panel.add(searchByDateBtn);
        panel.add(sortBtn);
        panel.add(sortByNameBtn);
        panel.add(cancelBtn);
        panel.add(showAllBtn);
        panel.add(saveBtn);
        panel.add(reserveBtn);

        add(panel, BorderLayout.SOUTH);

        searchByTitleBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Въведи заглавие на филм:");
            if (title != null) {
                MyList<Screening> result = manager.searchByTitle(title);
                showScreenings(result);
            }
        });

        reserveBtn.addActionListener(e -> {
            String customerName = JOptionPane.showInputDialog("Име на клиента:");
            if (customerName == null || customerName.isBlank()) return;

            String title = JOptionPane.showInputDialog("Заглавие на филм:");
            if (title == null || title.isBlank()) return;

            String rowStr = JOptionPane.showInputDialog("Ред (0-4):");
            if (rowStr == null) return;

            String colStr = JOptionPane.showInputDialog("Колона (0-4):");
            if (colStr == null) return;

            int row, col;
            try {
                row = Integer.parseInt(rowStr);
                col = Integer.parseInt(colStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Моля, въведете валидни числа за ред и колона.");
                return;
            }

            Screening screening = manager.findScreeningByTitle(title);
            if (screening != null) {
                if (!screening.isSeatTaken(row, col)) {
                    manager.makeReservation(customerName, screening, row, col);
                    JOptionPane.showMessageDialog(this, "Резервацията е направена.");
                } else {
                    JOptionPane.showMessageDialog(this, "Мястото вече е заето.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Прожекцията не е намерена.");
            }
        });

        searchByDateBtn.addActionListener(e -> {
            String date = JOptionPane.showInputDialog("Въведи дата (напр. 2025-06-10):");
            if (date != null) {
                MyList<Screening> result = manager.searchByDate(date);
                showScreenings(result);
            }
        });

        sortBtn.addActionListener(e -> {
            manager.sortScreeningsByDateTimeInsertion();
            showScreenings(manager.getScreenings());
        });

        sortByNameBtn.addActionListener(e -> {
            manager.sortScreeningsByMovieTitleInsertion();
            showScreenings(manager.getScreenings());
        });

        cancelBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Име на клиент:");
            int row = Integer.parseInt(JOptionPane.showInputDialog("Ред (0-4):"));
            int col = Integer.parseInt(JOptionPane.showInputDialog("Колона (0-4):"));
            boolean success = manager.cancelReservation(name, row, col);
            if (success) {
                JOptionPane.showMessageDialog(this, "Резервацията е анулирана.");
            } else {
                JOptionPane.showMessageDialog(this, "Не е намерена резервация.");
            }
        });

        showAllBtn.addActionListener(e -> showScreenings(manager.getScreenings()));

        saveBtn.addActionListener(e -> {
            try {
                manager.saveToFile();
                JOptionPane.showMessageDialog(this, "Резервациите са запазени.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Грешка при запис.");
            }
        });

        setVisible(true);
    }

    
    private void showScreenings(MyList<Screening> screenings) {
        outputArea.setText("");
        for (int i = 0; i < screenings.size(); i++) {
            Screening s = screenings.get(i);
            outputArea.append(s.getMovie().getTitle() + " | " + s.getDateTime() + " | " + s.getHall() + "\n");
        }
    }
}
