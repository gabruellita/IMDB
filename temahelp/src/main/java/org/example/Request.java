package org.example;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Request implements Subject {
    private RequestTypes type;
    private LocalDateTime createdDate;
    private String description;
    private String username;
    private String to;
    private String actorName;
    private String movieTitle;

    private ArrayList<User> observers = new ArrayList<>();

    public Request(){
        this.type = null;
        this.createdDate = null;
        this.description = null;
        this.username = null;
        this.to = null;
        this.movieTitle = null;
        this.actorName = null;
    }
    public Request(RequestTypes type){
        this.type = type;
    }

    public Request(RequestTypes type, LocalDateTime createdDate, String description,
                   String username, String to, String actorName, String movieTitle){
        this.type = type;
        this.createdDate = createdDate;
        this.description = description;
        this.username = username;
        this.to = to;
        this.movieTitle = movieTitle;
        this.actorName = actorName;
    }

    public RequestTypes getType() {
        return type;
    }

    public void setType(RequestTypes type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public ArrayList<User> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<User> observers) {
        this.observers = observers;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public void displayInfo(){
        System.out.println("Request{");
        System.out.println("Type: " + type);
        System.out.println("Created Date: " + this.createdDate);
        System.out.println("Description: " + description);
        System.out.println("Username: " + username);
        System.out.println("To: " + to);
        if(this.actorName != null)
            System.out.println("Actor Name: " + actorName);
        if(this.movieTitle != null)
            System.out.println("Movie Title: " + movieTitle);
        System.out.println("}");
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
        if(object instanceof Request) {
            Request request = (Request) object;
            User user = IMDB.getInstance().find_user_username(request.getUsername());
            user.update("A fost rezolvat" + request);
        }
    }
    public void notifyObservers(Staff staff, Object object, User user){
        if(object instanceof Request){
            Request request = (Request) object;
            staff.update("Ai primit un request nou de la " + user.getUsername());
        }
    }

    public void notifyObservers_denie(){
        for(User user : this.observers){
            user.update("A fost respins request-ul" + this);
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", createdDate=" + createdDate +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", to='" + to + '\'' +
                ", actorName='" + actorName + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                '}';
    }

}
