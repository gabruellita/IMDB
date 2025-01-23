package org.example;

import java.util.Random;

public class Admin<T extends Comparable<T>> extends Staff<T>{
    public Admin(){
        super();
    }
    public Admin(AccountType accountType, String username, Information information){
        super(accountType,username,information);
        this.setExperience(null);
    }
    public String strong_password(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        // Set the default password length
        int length = 6;

        // StringBuilder to build the password
        StringBuilder password = new StringBuilder();

        // Use Random for simplicity (not recommended for cryptographic purposes)
        Random random = new Random();

        // Generate password with the specified length
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }
        return password.toString();
    }

    public static String generateUsername(String name) {
        // Remove spaces and convert to lowercase
        String cleanedName = name.replaceAll("\\s", "").toLowerCase();

        // Append an underscore and a random number for uniqueness
        int randomNumber = (int) (Math.random() * 1000);
        return cleanedName + "_" + randomNumber;
    }
}
