package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {
    private static List<Request> allRequests = new ArrayList<>();

    // Private constructor to prevent instantiation
    private RequestsHolder() {
    }

    // Method to add a request to the list
    public static void addRequest(Request request) {
        allRequests.add(request);
    }

    // Method to remove a request from the list
    public static void removeRequest(Request request) {
        allRequests.remove(request);
    }

    // Method to display all requests in the list
    public static void displayAllRequests() {
        System.out.println("All Requests:");
        for (Request request : allRequests) {
            request.displayInfo();
            System.out.println(); // Separation between requests
        }
    }
    public static void dispalyMovie(){
        System.out.println("All movies");
        for(Request request : allRequests){
            if(request.getType()==RequestTypes.MOVIE_ISSUE){
                System.out.println(request.getMovieTitle());
            }
        }
    }

    public static void dispalyActor(){
        System.out.println("All actors");
        for(Request request : allRequests){
            if(request.getType()==RequestTypes.ACTOR_ISSUE){
                System.out.println(request.getActorName());
            }
        }
    }
    public static Request find_request_by_date(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String datestr = date.format(formatter);
        for(Request request1 : allRequests){
            String createddatestr = request1.getCreatedDate().format(formatter);
            if(createddatestr.equals(datestr)){
                return request1;
            }
        }
        return null;
    }

    public static List<Request> getAllRequests() {
        return allRequests;
    }

}
