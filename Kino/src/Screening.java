import java.time.LocalDateTime;

public class Screening {
    private Movie movie;
    private LocalDateTime dateTime;
    private String hall;
    private boolean[][] seats = new boolean[5][5]; 

    public Screening(Movie movie, LocalDateTime dateTime, String hall) {
        this.movie = movie;
        this.dateTime = dateTime;
        this.hall = hall;
    }

    public Movie getMovie() { return movie; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getHall() { return hall; }

    public boolean[][] getSeats() { return seats; }

    public boolean isSeatTaken(int row, int col) {
        return seats[row][col];
    }

    public boolean reserveSeat(int row, int col) {
        if (!seats[row][col]) {
            seats[row][col] = true;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return movie.getTitle() + " - " + dateTime.toString() + " - Зала " + hall;
    }

	public boolean cancelSeat(int row, int col) {
	    if (seats[row][col]) {
	        seats[row][col] = false;
	        return true;
	    }
	    return false;
	}
}
