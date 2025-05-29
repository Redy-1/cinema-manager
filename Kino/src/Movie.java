public class Movie {
    private String title;
    private int duration;
    private String genre;

    public Movie(String title, int duration, String genre) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public int getDuration() { return duration; }
    public String getGenre() { return genre; }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + duration + " мин)";
    }
}
