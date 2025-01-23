package org.example;

public class Rating {
    private String username;
    private int rating;
    private String comment;

    public Rating(){
        this.username = null;
        this.rating = 0;
        this.comment = null;
    }

    public Rating(String username, int rating, String comment) {
        this.username = username;
        this.rating = validateRating(rating);
        this.comment = comment;
    }

    // Validare valoare rating între 1 și 10
    private int validateRating(int value) {
        if (value < 1) {
            return 1;
        } else if (value > 10) {
            return 10;
        } else {
            return value;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "{\n" +
                "username='" + username + "\n" +
                ", rating=" + rating + "\n" +
                ", comment='" + comment + "\n" +
                '}';
    }
}
