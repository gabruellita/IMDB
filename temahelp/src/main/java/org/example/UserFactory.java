package org.example;

public class UserFactory {
    public static User factory(AccountType accountType, String username, User.Information information ){
        if(accountType == AccountType.Admin){
            return new Admin(accountType,username,information);
        }
        if(accountType == AccountType.Contributor){
            return new Contributor(accountType,username,information);
        }
        if(accountType == AccountType.Regular){
            return new Regular(accountType,username, information);
        }
        System.out.println("Acest tip de utilizator nu exisista");
        return null;
    }
}
