package org.example;

public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager{
    public Regular(){
        super();
    }
    public Regular(AccountType accountType, String username, Information information){
        super(accountType, username, information);
    }
    @Override
    public void createRequest(Request request) {
        RequestsHolder.addRequest(request);
    }

    @Override
    public void removeRequest(Request request) {
        RequestsHolder.removeRequest(request);
    }
    public void addRating(Rating r, Production p){
    }
}
