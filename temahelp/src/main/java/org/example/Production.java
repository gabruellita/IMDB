package org.example;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "Movie"),
        @JsonSubTypes.Type(value = Series.class, name = "Series")
})

public abstract class Production implements Comparable<Production>, Subject{
    private String title;
    private String type;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String plot;
    private ArrayList<User> observers = new ArrayList<>();
    private double averageRating;

    public Production(){
    }

    public Production(String title,String type, List<String> directors, List<String> actors,
                      List<Genre> genres, List<Rating> ratings, String plot) {
        this.title = title;
        this.type = type;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = calculateAverageRating();
    }
    public Production(String title, String type){
        this.type = type;
        this.title = title;
        this.directors = new ArrayList<String>();
        this.actors = new ArrayList<String>();
        this.genres = new ArrayList<Genre>();
        this.ratings = new ArrayList<Rating>();
        this.plot = null;
        this.averageRating = 0;
    }

    // Abstract method to be implemented by subclasses for displaying specific information
    public abstract void displayInfo();

    // Method to calculate the average rating
    private double calculateAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }

        return sum / ratings.size();
    }
    @Override
    public int compareTo(Production other) {
        return this.title.compareTo(other.title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public void addObserver(User user) {
        this.observers.add(user);
    }

    @Override
    public void removeObserver(User user) {
        this.observers.remove(user);
    }

    @Override
    public void notifyObservers(Object object) throws IOException {
        if(object instanceof Rating){
            Rating rating = (Rating) object;
            if(this.observers!=null){
                for(User user : this.observers){
                    user.update(this.title + "a primit o recenzie nou cu ratingul " + rating.getRating());
                }
            }
        }
    }
}
