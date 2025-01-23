package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor {
    private String name;
    private List<Map<String, String>> performances; // Perechi de tipul Name:Type pentru fiecare rol
    private String biography;



    public Actor(){
        this.name = null;
        this.performances = null;
        this.biography = null;
    }

    public Actor(String name, List<Map<String, String>> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public Actor(String name){
        this.name = name;
        this.biography = null;
        this.performances = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Map<String, String>> performances) {
        this.performances = performances;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void displayInfo() {
        System.out.println("Actor Name: " + name);
        System.out.println("Biography: " + biography);
        System.out.println("Performances:[");

        for (Map<String, String> performance : performances) {
            System.out.println("{");
            for (Map.Entry<String, String> entry : performance.entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("}");
        }
        System.out.println("]");
    }
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("  \"name\": \"").append(name).append("\",\n");
        stringBuilder.append("  \"performances\": [\n");

        for (Map<String, String> performance : performances) {
            stringBuilder.append("    {\n");
            for (Map.Entry<String, String> entry : performance.entrySet()) {
                stringBuilder.append("      \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\",\n");
            }
            stringBuilder.append("    },\n");
        }

        stringBuilder.append("  ],\n");
        stringBuilder.append("  \"biography\": \"").append(biography).append("\"\n");
        stringBuilder.append("}");

        return stringBuilder.toString();

    }
}
