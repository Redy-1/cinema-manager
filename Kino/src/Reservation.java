import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private String customerName;
    private Screening screening;
    private List<int[]> seats;

    public Reservation(String customerName, Screening screening) {
        this.customerName = customerName;
        this.screening = screening;
        this.seats = new ArrayList<>();
    }

    public void addSeat(int row, int col) {
        screening.reserveSeat(row, col);
        seats.add(new int[]{row, col});
    }

    public String getCustomerName() { return customerName; }
    public Screening getScreening() { return screening; }
    public List<int[]> getSeats() { return seats; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(customerName + " - " + screening.toString() + " - Места: ");
        for (int[] s : seats)
            sb.append("[").append(s[0]).append(",").append(s[1]).append("] ");
        return sb.toString();
    }
}
