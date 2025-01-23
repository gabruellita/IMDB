package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Movie extends Production{
    private String duration;
    private int releaseYear;

    public Movie(){
        super();
        this.duration = null;
        this.releaseYear = 0;
    }
    public Movie(@JsonProperty("title") String title,
                 @JsonProperty("type") String type,
                 @JsonProperty("directors") List<String> directors,
                 @JsonProperty("actors") List<String> actors,
                 @JsonProperty("genres") List<Genre> genres,
                 @JsonProperty("ratings") List<Rating> ratings,
                 @JsonProperty("plot") String plot,
                 @JsonProperty("duration") String duration,
                 @JsonProperty("releaseYear") int releaseYear){
        super(title, type, directors, actors, genres, ratings, plot);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public Movie(String title, String type){
        super(title, type);
        this.duration = null;
        this.releaseYear = 0;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + getTitle());
        System.out.println("Type: " + getType());
        System.out.println("Directors: " + getDirectors());
        System.out.println("Actors: " + getActors());
        System.out.println("Genres: " + getGenres());
        System.out.println("Ratings: " + getRatings());
        System.out.println("Plot: " + getPlot());
        System.out.println("Duration: " + this.duration);
        System.out.println("Average Rating: " + getAverageRating());
        System.out.println("Release Year: " + this.releaseYear);
        // poți adăuga și alte detalii specifice clasei Movie
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("  \"title\": \"").append(getTitle()).append("\",\n");
        stringBuilder.append("  \"type\": \"").append(getType()).append("\",\n");
        stringBuilder.append("  \"directors\": ").append(",\n");
        for(String director: getDirectors()){
            stringBuilder.append(director).append("\n");
        }

        stringBuilder.append("  \"actors\": ").append(",\n");
        for(String actor: getActors()){
            stringBuilder.append(actor).append("\n");
        }

        stringBuilder.append("  \"genres\": ").append(",\n");
        for(Genre genre : getGenres()){
            stringBuilder.append(genre).append("\n");
        }

        stringBuilder.append("  \"ratings\": ").append(",\n");
        for(Rating rating : getRatings()){
            stringBuilder.append(rating).append("\n");
        }

        stringBuilder.append("  \"plot\": \"").append(getPlot()).append("\",\n");
        stringBuilder.append("  \"averageRating\": ").append(getAverageRating()).append(",\n");
        stringBuilder.append("  \"duration\": \"").append(duration).append("\",\n");
        stringBuilder.append("  \"releaseYear\": ").append(releaseYear).append("\n");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
