package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
///import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Series extends Production {
    private int releaseYear;
    private int numSeasons;
    private Map<String, List<Episode>> seasons;

    public Series() {
        super();
        this.numSeasons = 0;
        this.releaseYear = 0;
        this.seasons = null;
    }

    public Series(@JsonProperty("title") String title,
                  @JsonProperty("type") String type,
                  @JsonProperty("directors") List<String> directors,
                  @JsonProperty("actors") List<String> actors,
                  @JsonProperty("genres") List<Genre> genres,
                  @JsonProperty("ratings") List<Rating> ratings,
                  @JsonProperty("plot") String plot,
                  @JsonProperty("releaseYear") int releaseYear,
                  @JsonProperty("numSeasons") int numSeasons,
                  @JsonProperty("seasons") Map<String, List<Episode>> seasons) {
        super(title, type, directors, actors, genres, ratings, plot);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.seasons = seasons;
    }

    public Series(String title, String type){
        super(title, type);
        this.numSeasons = 0;
        this.releaseYear = 0;
        this.seasons = new HashMap<>();
    }

    public void displayInfo() {
        System.out.println("Title: " + getTitle());
        System.out.println("Type: " + getType());
        System.out.println("Directors: " + getDirectors());
        System.out.println("Actors: " + getActors());
        System.out.println("Genres: " + getGenres());
        System.out.println("User Ratings: " + getRatings());
        System.out.println("Plot: " + getPlot());
        System.out.println("Release Year: " + releaseYear);
        System.out.println("Number of Seasons: " + numSeasons);

        if (seasons != null) {
            System.out.println("Seasons:");
            for (Map.Entry<String, List<Episode>> seasonEntry : seasons.entrySet()) {
                String seasonName = seasonEntry.getKey();
                List<Episode> episodes = seasonEntry.getValue();
                System.out.println("  Season: " + seasonName);
                for (Episode episode : episodes) {
                    System.out.println(episode);
                    System.out.println(); // Separare între episoade
                }
            }
        }
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("  \"title\": \"").append(getTitle()).append("\",\n");
        stringBuilder.append("  \"type\": \"").append(getType()).append("\",\n");
        stringBuilder.append("  \"directors\": ").append(getDirectors()).append(",\n");
        stringBuilder.append("  \"actors\": ").append(getActors()).append(",\n");
        stringBuilder.append("  \"genres\": [").append(formatGenres()).append("],\n");
        stringBuilder.append("  \"ratings\": ").append(getRatings()).append(",\n");
        stringBuilder.append("  \"plot\": \"").append(getPlot()).append("\",\n");
        stringBuilder.append("  \"averageRating\": ").append(getAverageRating()).append(",\n");
        stringBuilder.append("  \"releaseYear\": ").append(releaseYear).append(",\n");
        stringBuilder.append("  \"numSeasons\": ").append(numSeasons).append(",\n");
        stringBuilder.append("  \"seasons\": ").append(formatSeasons()).append("\n");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private String formatGenres() {
        StringBuilder formattedGenres = new StringBuilder();
        for (Genre genre : getGenres()) {
            formattedGenres.append("\"").append(genre).append("\", ");
        }
        if (formattedGenres.length() > 0) {
            formattedGenres.setLength(formattedGenres.length() - 2); // Remove trailing comma and space
        }
        return formattedGenres.toString();
    }

    private String formatSeasons() {
        StringBuilder formattedSeasons = new StringBuilder("{\n");
        for (Map.Entry<String, List<Episode>> seasonEntry : seasons.entrySet()) {
            String seasonName = seasonEntry.getKey();
            List<Episode> episodes = seasonEntry.getValue();
            formattedSeasons.append("    \"").append(seasonName).append("\": [\n");
            for (Episode episode : episodes) {
                formattedSeasons.append("      {\n");
                formattedSeasons.append("        \"episodeName\": \"").append(episode.getEpisodeName()).append("\",\n");
                formattedSeasons.append("        \"duration\": \"").append(episode.getDuration()).append("\"\n");
                formattedSeasons.append("      },\n");
            }
            if (!episodes.isEmpty()) {
                formattedSeasons.setLength(formattedSeasons.length() - 2); // Remove trailing comma and space
            }
            formattedSeasons.append("    ],\n");
        }
        if (!seasons.isEmpty()) {
            formattedSeasons.setLength(formattedSeasons.length() - 2); // Remove trailing comma and space
        }
        formattedSeasons.append("  }");
        return formattedSeasons.toString();
    }

}
