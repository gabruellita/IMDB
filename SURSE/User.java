package org.example;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "userType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Regular.class, name = "Regular"),
        @JsonSubTypes.Type(value = Contributor.class, name = "Contributor"),
        @JsonSubTypes.Type(value = Admin.class, name = "Admin")
})

public abstract class User<T extends Comparable<T>> implements Observer {
    private Information information;
    private AccountType userType;
    private String username;
    private String experience;
    private List<String> notifications;
    private List<String> favoriteActors;
    private List<String> favoriteProductions;

    private final Comparator<Object> nameComparator = (o1, o2) -> {
        if (o1 instanceof Production && o2 instanceof Production) {
            return ((Production) o1).getTitle().compareTo(((Production) o2).getTitle());
        } else if (o1 instanceof Actor && o2 instanceof Actor) {
            return ((Actor) o1).getName().compareTo(((Actor) o2).getName());
        } else if(o1 instanceof Production && o2 instanceof  Actor) {
            return ((Production) o1).getTitle().compareTo(((Actor) o2).getName());
        } else if (o1 instanceof Actor && o2 instanceof Production) {
            return ((Actor) o1).getName().compareTo(((Production) o2).getTitle());
        } else {
            throw new IllegalArgumentException("Incompatible types for comparison");
        }
    };
    private SortedSet<T> favorites = new TreeSet<>(nameComparator);

    public User(AccountType accountType, String username, Information information){
        this.userType = accountType;
        this.username = username;
        this.experience = "0";
        this.information = information;
        this.favorites = new TreeSet<>();
        this.notifications = new ArrayList<>();
    }
    public User(){
        this.information = null;
        this.userType = null;
        this.experience = null;
        this.username = null;
        this.notifications = null;
        this.favoriteActors = null;
        this.favoriteProductions = null;
    }
    public User(Information information, AccountType userType, String username,
                String experience, List<String> notifications,
                ArrayList<String> favoriteActors, ArrayList<String> favoriteProductions){
        this.information = information;
        this.userType = userType;
        this.experience = experience;
        this.username = username;
        this.notifications = notifications;
        this.favoriteActors = favoriteActors;
        this.favoriteProductions = favoriteProductions;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public AccountType getUserType() {
        return userType;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public List<String> getFavoriteActors() {
        return favoriteActors;
    }

    public void setFavoriteActors(List<String> favoriteActors) {
        this.favoriteActors = favoriteActors;
    }

    public List<String> getFavoriteProductions() {
        return favoriteProductions;
    }

    public void setFavoriteProductions(List<String> favoriteProductions) {
        this.favoriteProductions = favoriteProductions;
    }

    public SortedSet<T> getFavorites() {
        return favorites;
    }

    public void setFavorites(SortedSet<T> favorites) {
        this.favorites = favorites;
    }

    public Comparator<Object> getNameComparator() {
        return nameComparator;
    }

    public Actor find_actor_fav(String name){
        for (Object o : this.favorites){
            if(o instanceof Actor){
                Actor actor = (Actor) o;
                if(actor.getName().equals(name)){
                    return actor;
                }
            }
        }
        return  null;
    }

    public Production find_prod_fav(String name){
        for (Object o : this.favorites){
            if(o instanceof Production){
                Production production = (Production) o;
                if(production.getTitle().equals(name)){
                    return production;
                }
            }
        }
        return  null;
    }

    public void afisare_fav(){
        for (Object o : this.favorites){
            if(o instanceof Production){
                Production production = (Production) o;
                System.out.println(production.getTitle());
            }
            if(o instanceof Actor){
                Actor actor = (Actor) o;
                System.out.println(actor.getName());
            }
        }
    }

    public static class Information{
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private String birthDate;

        public Information(){}

        private Information(InformationBuilder builder){
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.gender = builder.gender;
            this.age = builder.age;
            this.birthDate = builder.birthDate;
        }
        public static class InformationBuilder{
            private Credentials credentials;
            private String name;
            private String country;
            private String gender;
            private int age;
            private String birthDate;
            public InformationBuilder(Credentials credentials){
                this.credentials = credentials;
            }
            public InformationBuilder name(String name){
                this.name = name;
                return this;
            }
            public InformationBuilder country(String country){
                this.country = country;
                return this;
            }
            public InformationBuilder gender(String gender){
                this.gender = gender;
                return this;
            }
            public InformationBuilder age(int age){
                this.age = age;
                return this;
            }
            public InformationBuilder birthDate(String birthDate){
                this.birthDate = birthDate;
                return this;
            }
            public Information build(){
                return  new Information(this);
            }
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        @Override
        public String toString() {
            return "Information{\n" +
                    "credentials=" + credentials + "\n" +
                    ", nume='" + name + "\n" +
                    ", country='" + country + "\n" +
                    ", age=" + age + "\n" +
                    ", gender='" + gender + "\n" +
                    ", birthDate=" + birthDate + "\n" +
                    '}' + "\n";
        }
    }

    @Override
    public String toString() {
        return "User{\n" +
                "information=" + information +
                ", userType=" + userType + "\n" +
                ", username='" + username + "\n" +
                ", experience=" + experience + "\n" +
                ", notifications=" + notifications + "\n" +
                ", favoriteActors=" + favoriteActors + "\n" +
                ", favoriteProductions=" + favoriteProductions + "\n" +
                '}' + "\n";
    }
    public void displayInfo(){
        System.out.println("username: " + getUsername());
        System.out.println("experience: " + getExperience());
        System.out.println("Information: {");
        System.out.println(getInformation());
        System.out.println("}");
        System.out.println("usertype: " + getUserType());
        System.out.println("Favorites{");
        for(Object obj : getFavorites()){
            if(obj instanceof Production){
                Production prod = (Production) obj;
                System.out.println(prod.getTitle() + " (prodction)");
            }
            if(obj instanceof Actor){
                Actor actor = (Actor) obj;
                System.out.println(actor.getName() + "(actor)");
            }
        }
        System.out.println("}");
    }

    @Override
    public void update(String text) {
        if(this.notifications == null){
            this.notifications = new ArrayList<>();
        }
        this.notifications.add(text);
    }

    public void increase_experience(ExperienceStrategy strategy){
        if(!this.experience.matches("-?\\d+(\\.\\d+)?")){
            System.out.println("nu este numar experienta");
            return;
        }
        int exp = Integer.parseInt(this.experience);
        exp = exp + strategy.calculateExperience();
        this.experience  = String.valueOf(exp);
    }
}
