import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class CinemaManager {
    private MyList<Movie> movies;
    private MyList<Screening> screenings;
    private MyList<Reservation> reservations;

    public CinemaManager() {
        movies = new MyList<>();
        screenings = new MyList<>();
        reservations = new MyList<>();
    }

    public void saveToFile() throws IOException {
        FileWriter writer = new FileWriter("data/reservations.txt");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            for (int[] seat : r.getSeats()) {
                writer.write(r.getCustomerName() + ";" +
                             r.getScreening().getMovie().getTitle() + ";" +
                             r.getScreening().getDateTime().toString() + ";" +
                             r.getScreening().getHall() + ";" +
                             seat[0] + ";" + seat[1] + "\n");
            }
        }
        writer.close();
    }

    public void loadFromFile() throws IOException {
        movies.clear();
        screenings.clear();
        Map<String, Movie> movieMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/movies.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                Movie m = new Movie(parts[0], Integer.parseInt(parts[1]), parts[2]);
                movies.add(m);
                movieMap.put(parts[0], m);
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader("data/screenings.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                Movie m = movieMap.get(parts[0]);
                Screening s = new Screening(m, LocalDateTime.parse(parts[1]), parts[2]);
                screenings.add(s);
            }
        }
    }

    public Movie getMovieByTitle(String title) {
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            if (m.getTitle().equalsIgnoreCase(title)) return m;
        }
        return null;
    }

    public Screening findScreeningByTitle(String title) {
        for (int i = 0; i < screenings.size(); i++) {
            Screening screening = screenings.get(i);
            if (screening.getMovie().getTitle().equalsIgnoreCase(title)) {
                return screening;
            }
        }
        return null;
    }

    public MyList<Screening> searchByTitle(String title) {
        MyList<Screening> result = new MyList<>();
        for (int i = 0; i < screenings.size(); i++) {
            Screening s = screenings.get(i);
            if (s.getMovie().getTitle().equalsIgnoreCase(title)) {
                result.add(s);
            }
        }
        return result;
    }

    public MyList<Screening> searchByDate(String date) {
        MyList<Screening> result = new MyList<>();
        for (int i = 0; i < screenings.size(); i++) {
            Screening s = screenings.get(i);
            if (s.getDateTime().toString().startsWith(date)) {
                result.add(s);
            }
        }
        return result;
    }

    public void sortScreeningsByDateTimeInsertion() {
        for (int i = 1; i < screenings.size(); i++) {
            Screening key = screenings.get(i);
            int j = i - 1;
            while (j >= 0 && screenings.get(j).getDateTime().isAfter(key.getDateTime())) {
                screenings.set(j + 1, screenings.get(j));
                j--;
            }
            screenings.set(j + 1, key);
        }
    }

    public void sortScreeningsByMovieTitleInsertion() {
        for (int i = 1; i < screenings.size(); i++) {
            Screening key = screenings.get(i);
            int j = i - 1;
            while (j >= 0 && screenings.get(j).getMovie().getTitle().compareTo(key.getMovie().getTitle()) > 0) {
                screenings.set(j + 1, screenings.get(j));
                j--;
            }
            screenings.set(j + 1, key);
        }
    }

    public boolean cancelReservation(String name, int row, int col) {
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            if (r.getCustomerName().equalsIgnoreCase(name)) {
                for (int[] seat : r.getSeats()) {
                    if (seat[0] == row && seat[1] == col) {
                        r.getScreening().cancelSeat(row, col);
                        r.getSeats().remove(seat);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void makeReservation(String name, Screening screening, int row, int col) {
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            if (r.getCustomerName().equalsIgnoreCase(name) &&
                r.getScreening().equals(screening)) {
                r.addSeat(row, col);
                screening.reserveSeat(row, col);
                return;
            }
        }

        Reservation newRes = new Reservation(name, screening);
        newRes.addSeat(row, col);
        screening.reserveSeat(row, col);
        reservations.add(newRes);
    }

    public MyList<Screening> getScreenings() {
        return screenings;
    }
}