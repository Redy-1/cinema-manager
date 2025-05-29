import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        CinemaManager manager = new CinemaManager();
        try {
            manager.loadFromFile();
        } catch (Exception e) {
            System.out.println("Неуспешно зареждане на данни.");
        }

        SwingUtilities.invokeLater(() -> new GUI(manager));
    }
}
