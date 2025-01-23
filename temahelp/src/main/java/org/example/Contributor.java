package org.example;

public class Contributor<T extends Comparable<T>> extends Staff<T>{
    public Contributor(){
        super();
    }
    public Contributor(AccountType accountType, String username, Information information){
        super(accountType, username, information);
    }

}
