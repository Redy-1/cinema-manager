
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class CinemaManager {
    private ArrayList<Movie> movies;
    private ArrayList<Screening> screenings;
    private ArrayList<Reservation> reservations;

    public CinemaManager() {
        movies = new ArrayList<>();
        screenings = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public void saveToFile() throws IOException {
        FileWriter writer = new FileWriter("data/reservations.txt");
        for (Reservation r : reservations) {
            StringBuilder line = new StringBuilder();
            line.append(r.getCustomerName()).append(";")
                .append(r.getScreening().getMovie().getTitle()).append(";")
                .append(r.getScreening().getDateTime().toString()).append(";")
                .append(r.getScreening().getHall());
            for (int[] seat : r.getSeats()) {
                line.append(";").append(seat[0]).append(",").append(seat[1]);
            }
            line.append("\n");
            writer.write(line.toString());
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
        for (Movie m : movies) {
            if (m.getTitle().equalsIgnoreCase(title)) return m;
        }
        return null;
    }
    public Screening findScreeningByTitle(String title) {
        for (Screening screening : screenings) {
            if (screening.getMovie().getTitle().equalsIgnoreCase(title)) {
                return screening;
            }
        }
        return null;
    }


    public ArrayList<Screening> searchByTitle(String title) {
        ArrayList<Screening> result = new ArrayList<>();
        for (Screening s : screenings) {
            if (s.getMovie().getTitle().equalsIgnoreCase(title)) {
                result.add(s);
            }
        }
        return result;
    }

    public ArrayList<Screening> searchByDate(String date) {
        ArrayList<Screening> result = new ArrayList<>();
        for (Screening s : screenings) {
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
        for (Reservation r : reservations) {
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

    public ArrayList<Screening> getScreenings() {
        return screenings;
    }
    public void makeReservation(String customerName, Screening screening, int row, int col) {
        for (Reservation r : reservations) {
            if (r.getCustomerName().equalsIgnoreCase(customerName) && r.getScreening().equals(screening)) {
                r.addSeat(row, col);
                return;
            }
        }
        Reservation newReservation = new Reservation(customerName, screening);
        newReservation.addSeat(row, col);
        reservations.add(newReservation);
    }

}
