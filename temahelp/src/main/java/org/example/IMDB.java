package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class IMDB {
    private static IMDB instance  = null;
    private List<User> users;
    private List<Actor> actors;
    private List<Request> requests;
    private List<Production> productions;
    private IMDB() throws IOException {
        /// citire din fisiere JSON
        File actorsFile = new File("C:\\Users\\gabri\\IdeaProjects\\temahelp\\src\\main\\resources\\input\\actors.json");
        File productionFile = new File("C:\\Users\\gabri\\IdeaProjects\\temahelp\\src\\main\\resources\\input\\production.json");
        File requestsFile = new File("C:\\Users\\gabri\\IdeaProjects\\temahelp\\src\\main\\resources\\input\\requests.json");
        File accountsFile = new File("C:\\Users\\gabri\\IdeaProjects\\temahelp\\src\\main\\resources\\input\\accounts.json");


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        actors = objectMapper.readValue(actorsFile, new TypeReference<List<Actor>>() {});
        productions = objectMapper.readValue(productionFile, new TypeReference<List<Production>>() {});
        requests = objectMapper.readValue(requestsFile, new TypeReference<List<Request>>() {});
        users = objectMapper.readValue(accountsFile, new TypeReference<List<User>>() {});
        for(User user : users){
            if(user instanceof Regular){
                user.setUserType(AccountType.Regular);
            }
            if(user instanceof Contributor){
                user.setUserType(AccountType.Contributor);
            }
            if(user instanceof Admin){
                user.setUserType(AccountType.Admin);
            }
        }
        for(User user : users){
            if(user.getFavoriteProductions() != null){
                for(Object object : user.getFavoriteProductions()) {
                    String prodName = (String) object;
                    Production find =findProduction(prodName);
                    if(find != null){
                        user.getFavorites().add(find);
                    }
                }
            }
            if(user.getFavoriteActors() != null){
                for(Object object : user.getFavoriteActors()){
                    String actorName = (String) object;
                    Actor find = findActor(actorName);
                    if(find != null){
                        user.getFavorites().add(find);
                    }
                }
            }
            if(user instanceof Staff){
                Staff userhelp = (Staff) user;
                if(userhelp.getProductionsContribution() != null){
                    for(Object object : userhelp.getProductionsContribution()) {
                        String prodName = (String) object;
                        Production find = findProduction(prodName);
                        if(find != null){
                            userhelp.getContributions().add(find);
                            find.addObserver(userhelp);
                        }
                    }
                }
                if(userhelp.getActorsContribution() != null){
                    for(Object object : userhelp.getActorsContribution()){
                        String actorName = (String) object;
                        Actor find = findActor(actorName);
                        if(find != null){
                            userhelp.getContributions().add(find);
                        }
                    }
                }
            }
        }
    }
    public static IMDB getInstance() throws IOException {
        if(instance == null){
            instance = new IMDB();
        }
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    ///funcie de gasire productie dupa nume
    public Production findProduction(String productionName){
        Production find = null;
        for(Production production : this.productions){
            if(production.getTitle().equals(productionName)){
                find = production;
                break;
            }
        }
        return find;
    }
    public Actor findActor(String actorName){
        Actor find = null;
        for(Actor actor : this.actors){
            if(actor.getName().equals(actorName)) {
                find = actor;
                break;
            }
        }
        return find;
    }
    private void afisareProductii(){
        for(Production production : this.productions){
            production.displayInfo();
        }
    }
    private void afisareActors(){
        for (Actor actor : this.actors){
            actor.displayInfo();
        }
    }
    private void afisareRequests(){
        for (Request request : this.requests){
            request.displayInfo();
        }
    }
    public void afisareuser(){
        for(User user : this.users){
            user.displayInfo();
        }
    }
    public User findUserbyEmail(String email){
        for(User user : this.users){
            if(Objects.equals(user.getInformation().getCredentials().getEmail(), email)){
                return user;
            }
        }

        return null;
    }

    private void vizualizareNotificari(User user){
        if(user.getNotifications() == null){
            System.out.println("Nu ai notifacri momentan");
            return;
        }
        for(Object object : user.getNotifications()){
            String text = (String) object;
            System.out.println(text);
        }
    }

    private void rulareSpeficAccount(User user) throws Exception {
        if(user.getUserType() == AccountType.Admin){
            CLIAdmin((Admin) user);
        } else if(user.getUserType() == AccountType.Contributor){
            CLIContributor((Contributor) user);
        } else if(user.getUserType() == AccountType.Regular){
            CLIRegular((Regular) user);
        } else {
            throw new Exception("User nu are un tip!!!");
        }

    }

    private boolean checkNumber(String string){
        String numbers = "123456789";
        for(int i = 0; i < string.length(); i++){
            char carac = string.charAt(i);
            if(!numbers.contains(String.valueOf(carac))){
                return false;
            }
        }
        return true;
    }

    public User find_user_username(String username){
        for (User user : this.users){
            if(Objects.equals(user.getUsername(), username)){
                return user;
            }
        }
        return null;
    }

    private void searchActorOrprod(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ce cauti? Actor sau Productie?");
        System.out.print("Alege optiune: ");
        String choise = scanner.nextLine();
        if(Objects.equals(choise,"Actor")){
            System.out.print("\nIntrodu  numele actorului cautat ");
            String actorfind = scanner.nextLine();
            Actor actor = findActor(actorfind);
            if(actor != null){
                actor.displayInfo();
                return;
            } else {
                System.out.println("Nume gresit de actor!!!");
                System.out.print("Doriti recautare?[yes] ");
                String temp = scanner.nextLine();
                if(Objects.equals(temp, "yes")){
                    searchActorOrprod();
                } else {
                    return;
                }
            }
        }
        if(Objects.equals(choise, "Productie")){
            System.out.print("Introdu numele productiei ");
            String prodfind = scanner.nextLine();
            Production prod = findProduction(prodfind);
            if(prod != null){
                prod.displayInfo();
                return;
            } else {
                System.out.println("Nume gresit de productie!!!");
                System.out.print("Doriti recautare?[yes] ");
                String temp = scanner.nextLine();
                if(Objects.equals(temp, "yes")){
                    searchActorOrprod();
                } else {
                    return;
                }
            }
        }
        System.out.println("Optiune scrisa gresit!!!");
        System.out.print("Doriti reintroducere?[yes] ");
        String temp = scanner.nextLine();
        if(Objects.equals(temp, "yes")){
            searchActorOrprod();
        } else {
            return;
        }
    }

    private void logout() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Doresti logarea cu un alt cont?[yes] ");
        String optiune = scanner.nextLine();
        if(Objects.equals(optiune, "yes")){
            rulareInCLI();
        }
        return;
    }

    private void add_or_del_fav(User user){
        System.out.println("ATENTIE introducerea gresita a numelui sau a optiuni va duce la necesitatea de introducere a lor de la inceput!!!");
        System.out.print("Doresti adaugare sau stergere \n  Adaugare \n  Stergere \n Optinea aleasa este: ");
        Scanner scanner = new Scanner(System.in);
        String optiune = scanner.nextLine();
        if(Objects.equals(optiune, "Adaugare")){
            System.out.print("Ce doriti sa adaugati Actor sau Productie? ");
            String optiune2 = scanner.nextLine();

            if(Objects.equals(optiune2, "Productie")){
                System.out.print("Introduceti numele productiei ");
                String prodname = scanner.nextLine();
                Production prod = findProduction(prodname);
                if(prod == null){
                    System.out.println("Productia nu se afla in sistem");
                    System.out.print("Doresti reincercarea adaugari?[yes] ");
                    String optiune3 = scanner.nextLine();
                    if(Objects.equals(optiune3, "yes")){
                        add_or_del_fav(user);
                    }
                    return;
                }
                user.getFavorites().add(prod);
                System.out.println("Productia a fost adaugata in lista de favorite");
                System.out.print("Doresti inca o adaugare?[yes] ");
                String optiune3 = scanner.nextLine();
                if(Objects.equals(optiune3, "yes")){
                    add_or_del_fav(user);
                }
                return;
            }
            if(Objects.equals(optiune2, "Actor")){
                System.out.print("Introduceti numele actorului ");
                String actname = scanner.nextLine();
                Actor actor = findActor(actname);
                if(actor == null){
                    System.out.println("Actorul nu se afla in sistem");
                    System.out.print("Doresti reincercarea adaugari?[yes] ");
                    String optiune3 = scanner.nextLine();
                    if(Objects.equals(optiune3, "yes")){
                        add_or_del_fav(user);
                    }
                    return;
                }
                user.getFavorites().add(actor);
                System.out.println("Actorul a fost adaugat in lista de favorite");
                System.out.print("Doresti inca o adaugare?[yes] ");
                String optiune3 = scanner.nextLine();
                if(Objects.equals(optiune3, "yes")){
                    add_or_del_fav(user);
                }
                return;
            }

        }
        if(Objects.equals(optiune, "Stergere")){
            afisare_fav(user);
            System.out.print("Ce doriti sa stergeti Actor sau Productie? ");
            String optiune2 = scanner.nextLine();
            if(Objects.equals(optiune2, "Productie")){
                System.out.print("Introduceti numele productiei ");
                String prodname = scanner.nextLine();
                Production prod = user.find_prod_fav(prodname);
                if(prod == null){
                    System.out.println("Actorul nu se afla in lista de favorite");
                    System.out.print("Doresti reincercarea stergeri?[yes] ");
                    String optiune3 = scanner.nextLine();
                    if(Objects.equals(optiune3, "yes")){
                        add_or_del_fav(user);
                    }
                    return;
                }
                user.getFavorites().remove(prod);
                System.out.println("Productia a fost scoasa din lista de favorite");
                System.out.print("Doresti inca o stergeri?[yes] ");
                String optiune3 = scanner.nextLine();
                if(Objects.equals(optiune3, "yes")){
                    add_or_del_fav(user);
                }
                return;
            }
            if(Objects.equals(optiune2, "Actor")){
                System.out.print("Introduceti numele actorului ");
                String actname = scanner.nextLine();
                Actor actor = user.find_actor_fav(actname);
                if(actor == null){
                    System.out.println("Actorul nu se afla in lista de favorite");
                    System.out.print("Doresti reincercarea stergeri?[yes/no] ");
                    String optiune3 = scanner.nextLine();
                    if(Objects.equals(optiune3, "yes")){
                        add_or_del_fav(user);
                    }
                    return;
                }
                user.getFavorites().remove(actor);
                System.out.println("Productia a fost scoasa din lista de favorite");
                System.out.print("Doresti inca o stergeri?[yes] ");
                String optiune3 = scanner.nextLine();
                if(Objects.equals(optiune3, "yes")){
                    add_or_del_fav(user);
                }
                return;
            }
        }
        System.out.println("Optiune scrisa gresit!!!");
        System.out.print("Doriti reintroducere?[yes/no] ");
        String temp = scanner.nextLine();
        if(Objects.equals(temp, "yes")){
            add_or_del_fav(user);
        } else {
            return;
        }

    }

    public void add_del_sistem(Staff staff){
        System.out.println("ATENTIE introducerea gresita a numelui sau a optiuni va duce la necesitatea de introducere a lor de la inceput!!!");
        System.out.print("Doresti adaugare sau stergere \n  Adaugare \n  Stergere \n Optinea aleasa este: ");
        Scanner scanner = new Scanner(System.in);
        String optiune = scanner.nextLine();
        if(Objects.equals(optiune, "Adaugare")){
            System.out.print("Ce doriti sa adaugati Actor sau Productie? ");
            String optiune2 = scanner.nextLine();
            if(Objects.equals(optiune2, "Productie")){
                System.out.print("Introduceti numele productiei ");
                String prodname = scanner.nextLine();
                System.out.print("\n Este Movie sau Series?");
                String type = scanner.nextLine();
                if(Objects.equals(type, "Movie")){
                    Movie movie = new Movie(prodname, type);
                    movie.addObserver(staff);
                    System.out.print("\n Doresti adaugarea la mai multe informatii despre "+ prodname + " ?[yes/no]");
                    String optiuneYes = scanner.nextLine();
                    if(Objects.equals(optiuneYes, "yes")){
                        System.out.print("\n Doresti adaugarea unui plot?[yes/no]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Plotu este: ");
                            String plot = scanner.nextLine();
                            movie.setPlot(plot);
                        }
                        System.out.print("\n Doresti adaugarea actorilor?[yes/no]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Introdu actori (introducerea se opreste la introducerea cuvantului no):");
                            System.out.print("\n Actor:");
                            String actorName = scanner.nextLine();
                            while(!Objects.equals(actorName, "no")){
                                movie.getActors().add(actorName);
                                System.out.print("\n Actor:");
                                actorName = scanner.nextLine();
                            }
                        }
                        System.out.print("\n Doresti adaugarea directorilor?[yes/no]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Introdu directori (introducerea se opreste la introducerea cuvantului no):");
                            System.out.print("\n Director:");
                            String directorName = scanner.nextLine();
                            while(!Objects.equals(directorName, "no")){
                                movie.getDirectors().add(directorName);
                                System.out.print("\n Director:");
                                directorName = scanner.nextLine();
                            }
                        }
                        System.out.print("\n Doresti adaugarea genurilor?[yes/no]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Introdu genuri (introducerea se opreste la introducerea cuvantului no):");
                            System.out.print("\n Genre:");
                            String genreString = scanner.nextLine();
                            while(!Objects.equals(genreString, "no")){
                                Genre genre = Genre.valueOf(genreString);
                                movie.getGenres().add(genre);
                                System.out.print("\n Genre:");
                                genreString = scanner.nextLine();
                            }
                        }
                        System.out.print("\n Doresti adaugarea duratei?[yes/no]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Durata: ");
                            String duration = scanner.nextLine();
                            movie.setDuration(duration);
                        }
                        System.out.print("\n Doresti adaugarea anului aparitiei?[yes/no] ");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Anul aparitiei: ");
                            String yearString = scanner.nextLine();
                            while(!checkNumber(yearString)){
                                System.out.print("\n Nu ai introdus un an valid doar din cifre reintrodu: ");
                                yearString = scanner.nextLine();
                            }
                            int realseyear = Integer.parseInt(yearString);
                            movie.setReleaseYear(realseyear);
                        }
                        System.out.println("Informatii adaugate cu succes");
                    }

                    getProductions().add(movie);
                    staff.getContributions().add(movie);
                    if(staff instanceof Contributor){
                        ExperienceStrategy addSystemExperience = new AddSystemExperience();
                        staff.increase_experience(addSystemExperience);
                    }
                    System.out.println("\n Film adaugat cu succes");
                    return;
                }
                if(Objects.equals(type, "Series")){
                    Series series = new Series(prodname, type);
                    series.addObserver(staff);
                    System.out.print("\n Doresti adaugarea la mai multe informatii despre "+ prodname + " ?[yes/no]");
                    String optiuneYes = scanner.nextLine();
                    if(Objects.equals(optiuneYes, "yes")){
                        System.out.print("\n Doresti adaugarea unui plot?[yes]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Plotul este: ");
                            String plot = scanner.nextLine();
                            series.setPlot(plot);
                        }
                        System.out.print("\n Doresti adaugarea actorilor?[yes/no]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Introdu actori (introducerea se opreste la introducerea cuvantului no):");
                            System.out.print("\n Actor:");
                            String actorName = scanner.nextLine();
                            while(!Objects.equals(actorName, "no")){
                                series.getActors().add(actorName);
                                System.out.print("\n Actor:");
                                actorName = scanner.nextLine();
                            }
                        }
                        System.out.print("\n Doresti adaugarea directorilor?[yes]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Introdu directori (introducerea se opreste la introducerea cuvantului no):");
                            System.out.print("\n Director:");
                            String directorName = scanner.nextLine();
                            while(!Objects.equals(directorName, "no")){
                                series.getDirectors().add(directorName);
                                System.out.print("\n Director:");
                                directorName = scanner.nextLine();
                            }
                        }
                        System.out.print("\n Doresti adaugarea genurilor?[yes]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Introdu genuri (introducerea se opreste la introducerea cuvantului no):");
                            System.out.print("\n Genre:");
                            String genreString = scanner.nextLine();
                            while(!Objects.equals(genreString, "no")){
                                Genre genre = Genre.valueOf(genreString);
                                series.getGenres().add(genre);
                                System.out.print("\n Genre:");
                                genreString = scanner.nextLine();
                            }
                        }
                        System.out.print("\n Doresti adaugarea anului aparitiei?[yes]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Anul aparitiei: ");
                            String yearString = scanner.nextLine();
                            while(!checkNumber(yearString)){
                                System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                                yearString = scanner.nextLine();
                            }
                            int realseyear = Integer.parseInt(yearString);
                            series.setReleaseYear(realseyear);
                        }

                        System.out.print("\n Doresti adaugarea sezoanelor?[yes]");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            System.out.print("\n Numar sezoane: ");
                            String numSeasonsString = scanner.nextLine();
                            while(!checkNumber(numSeasonsString)){
                                System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                                numSeasonsString = scanner.nextLine();
                            }
                            int numSeasons = Integer.parseInt(numSeasonsString);
                            series.setNumSeasons(numSeasons);
                            int i = 1;
                            while(numSeasons > 0){
                                ArrayList<Episode> listOfEpisodes = new ArrayList<Episode>();
                                String seasonName = "Season " + i;
                                System.out.print("\n Numar episoade pentru sezonul " + i + " : ");
                                i++;
                                String numEpStr = scanner.nextLine();
                                while(!checkNumber(numEpStr)){
                                    System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                                    numEpStr = scanner.nextLine();
                                }
                                int numEp = Integer.parseInt(numEpStr);
                                while(numEp > 0){
                                    Episode episode;
                                    System.out.print("\n Episode name: ");
                                    String epName = scanner.nextLine();
                                    System.out.print("\n Episode duration: ");
                                    String epDuration = scanner.nextLine();
                                    episode = new Episode(epName,epDuration);
                                    listOfEpisodes.add(episode);
                                    numEp--;
                                }
                                series.getSeasons().put(seasonName,listOfEpisodes);
                                numSeasons--;
                            }
                        }

                        System.out.println("Informatii adaugate cu succes");
                    }

                    getProductions().add(series);
                    staff.getContributions().add(series);
                    if(staff instanceof Contributor){
                        ExperienceStrategy addSystemExperience = new AddSystemExperience();
                        staff.increase_experience(addSystemExperience);
                    }
                    System.out.println("\n Series adaugat cu succes");
                    return;
                }

                System.out.println("Optiune scrisa gresit!!!");
                System.out.print("Doriti reintroducere?[yes] ");
                String temp = scanner.nextLine();
                if(Objects.equals(temp, "yes")){
                    add_del_sistem(staff);
                }
                return;
            }
            if(Objects.equals(optiune2, "Actor")){
                System.out.print("Introduceti numele actorului ");
                String actname = scanner.nextLine();
                Actor actor = new Actor(actname);
                System.out.print("\n Doresti adaugarea la mai multe informatii despre "+ actname + " ?[yes] ");
                String optiuneYes = scanner.nextLine();
                if(Objects.equals(optiuneYes, "yes")){
                    System.out.print("\n Doresti adaugarea biografiei?[yes]");
                    optiuneYes = scanner.nextLine();
                    if(Objects.equals(optiuneYes, "yes")){
                        System.out.print("Biografia: ");
                        String biography = scanner.nextLine();
                        actor.setBiography(biography);
                    }
                    System.out.print("\n Doresti adaugarea performantei?[yes] ");
                    optiuneYes = scanner.nextLine();
                    if(Objects.equals(optiuneYes, "yes")){
                        System.out.print("\n Numar performante: ");
                        String numPerStr = scanner.nextLine();
                        while(!checkNumber(numPerStr)){
                            System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                            numPerStr = scanner.nextLine();
                        }
                        int numPer = Integer.parseInt(numPerStr);
                        while(numPer > 0 ){
                            System.out.print("\n Numele performantei: ");
                            String namePerf = scanner.nextLine();
                            System.out.print("Tipul performantei: ");
                            String typePerf = scanner.nextLine();
                            Map<String, String> map1 = new HashMap<>();
                            map1.put("title", namePerf);
                            map1.put("type", typePerf);
                            actor.getPerformances().add(map1);
                            numPer--;
                        }
                    }
                    System.out.println("Informatii adaugate cu succes");
                }
                getActors().add(actor);
                staff.getContributions().add(actor);
                if(staff instanceof Contributor){
                    ExperienceStrategy addSystemExperience = new AddSystemExperience();
                    staff.increase_experience(addSystemExperience);
                }
                System.out.println("\n Actor adaugat cu succes");
                return;
            }
            System.out.println("Optiune scrisa gresit!!!");
            System.out.print("Doriti reintroducere?[yes] ");
            String temp = scanner.nextLine();
            if(Objects.equals(temp, "yes")){
                add_del_sistem(staff);
            }
            return;
        }
        if(Objects.equals(optiune, "Stergere")){
            System.out.print("Ce doriti sa stergeti Actor sau Productie? ");
            String optiune2 = scanner.nextLine();
            if(Objects.equals(optiune2, "Productie")){
                afisare_prod_name(staff);
                System.out.print("Introduceti numele productiei ");
                String prodname = scanner.nextLine();
                Production prod = findProduction(prodname);
                if(prod == null){
                    System.out.println("Productia nu se afla in sistem");
                    System.out.print("Doresti reincercarea stergeri?[yes] ");
                    String optiune3 = scanner.nextLine();
                    if(Objects.equals(optiune3, "yes")){
                        add_del_sistem(staff);
                    }
                    return;
                }
                if(!staff.getContributions().contains(prod)){
                    System.out.println("Nu ai acces la aceasta productie pentru a fi stearsa");
                    return;
                }
                getProductions().remove(prod);
                System.out.println("Productia a fost scoasa din sistem");
                System.out.print("Doresti inca o stergeri?[yes] ");
                String optiune3 = scanner.nextLine();
                if(Objects.equals(optiune3, "yes")){
                    add_del_sistem(staff);
                }
                return;
            }
            if(Objects.equals(optiune2, "Actor")){
                afisare_actor_name(staff);
                System.out.print("Introduceti numele actorului ");
                String actname = scanner.nextLine();
                Actor actor = findActor(actname);
                if(actor == null){
                    System.out.println("Actorul nu se afla in sitem");
                    System.out.print("Doresti reincercarea stergeri?[yes] ");
                    String optiune3 = scanner.nextLine();
                    if(Objects.equals(optiune3, "yes")){
                        add_del_sistem(staff);
                    }
                    return;
                }
                if(!staff.getContributions().contains(actor)){
                    System.out.println("Nu ai acces la aceasta productie pentru a fi stearsa");
                    return;
                }
                getActors().remove(actor);
                System.out.println("Actorul a fost scoas din sistem");
                System.out.print("Doresti inca o stergeri?[yes] ");
                String optiune3 = scanner.nextLine();
                if(Objects.equals(optiune3, "yes")){
                    add_del_sistem(staff);
                }
                return;
            }
        }
        System.out.println("Optiune scrisa gresit!!!");
        System.out.print("Doriti reintroducere?[yes] ");
        String temp = scanner.nextLine();
        if(Objects.equals(temp, "yes")){
            add_del_sistem(staff);
        } else {
            return;
        }
    }

    private void updateMovie(Staff staff){
        System.out.println("Filme de updatat de " + staff.getUsername());
        for(Object object : staff.getContributions()){
            if(object instanceof Movie) {
                Production production = (Production) object;
                System.out.println(production.getTitle());
            }
        }
        if(staff instanceof Admin){
            RequestsHolder.dispalyActor();
        }
        System.out.print("\n Numele filmului de updatat: ");
        Scanner scanner = new Scanner(System.in);
        String movieName = scanner.nextLine();
        Movie movie = (Movie) findProduction(movieName);
        if(movie == null){
            System.out.println("Filmul nu se afla in sistem.");
            System.out.print("\n Doriti updatarea altui film?[yes]");
            String optiune = scanner.nextLine();
            if(Objects.equals(optiune, "yes")){
                updateMovie(staff);
            }
            return;
        }
        if(!staff.getContributions().contains(movie) && staff instanceof Contributor){
            System.out.println("Acest film nu a fost adaugat de tine");
            return;
        }
        System.out.print("\n Doresti updatarea informatiilor despre "+ movieName + " ?[yes/no]");
        String optiuneYes = scanner.nextLine();
        if(Objects.equals(optiuneYes, "yes")){
            System.out.print("\n Doresti updatarea plotului?[yes]");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Plotu este: ");
                String plot = scanner.nextLine();
                movie.setPlot(plot);
            }
            System.out.print("\n Doresti updatarea actorilor?[yes]");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Introdu actori (introducerea se opreste la introducerea cuvantului no):");
                System.out.print("\n Actor:");
                String actorName = scanner.nextLine();
                while(!Objects.equals(actorName, "no")){
                    movie.getActors().add(actorName);
                    System.out.print("\n Actor:");
                    actorName = scanner.nextLine();
                }
                System.out.print("\n Doresti si stergerea actorilor?[yes] ");
                optiuneYes = scanner.nextLine();
                if(Objects.equals(optiuneYes, "yes")){
                    System.out.print("\n Introdu actori (introducerea se opreste la introducerea cuvantului no):");
                    System.out.print("\n Actor:");
                    actorName = scanner.nextLine();
                    while(!Objects.equals(actorName, "no")){
                        movie.getActors().remove(actorName);
                        System.out.print("\n Actor:");
                        actorName = scanner.nextLine();
                    }
                }
            }

            }
            System.out.print("\n Doresti updatarea directorilor?[yes]");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Introdu directori (introducerea se opreste la introducerea cuvantului no):");
                System.out.print("\n Director:");
                String directorName = scanner.nextLine();
                while(!Objects.equals(directorName, "no")){
                    movie.getDirectors().add(directorName);
                    System.out.print("\n Director:");
                    directorName = scanner.nextLine();
                }
                System.out.print("\n Doresti si stergerea directorilor?[yes] ");
                optiuneYes = scanner.nextLine();
                if(Objects.equals(optiuneYes, "yes")){
                    System.out.print("\n Introdu directori (introducerea se opreste la introducerea cuvantului no):");
                    System.out.print("\n Director:");
                    directorName = scanner.nextLine();
                    while(!Objects.equals(directorName, "no")){
                        movie.getActors().remove(directorName);
                        System.out.print("\n Director:");
                        directorName = scanner.nextLine();
                    }
                }
            }
            System.out.print("\n Doresti updatarea genurilor?[yes/no]");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Introdu genuri (introducerea se opreste la introducerea cuvantului no):");
                System.out.print("\n Genre:");
                String genreString = scanner.nextLine();
                while(!Objects.equals(genreString, "no")) {
                    Genre genre = null;
                    try {
                        genre = Genre.valueOf(genreString);
                        movie.getGenres().add(genre);
                        System.out.print("\n Genre:");
                        genreString = scanner.nextLine();
                    }catch (IllegalArgumentException e) {
                        System.out.println("nu este gen valabil");
                        System.out.print("\n Doresti reincercare pentru updatare?[yes] ");
                        optiuneYes = scanner.nextLine();
                        if(Objects.equals(optiuneYes, "yes")){
                            updateMovie(staff);
                        }
                        return;
                    }
                }
                System.out.print("\n Doresti si stergerea genurilor?[yes/no] ");
                optiuneYes = scanner.nextLine();
                if(Objects.equals(optiuneYes, "yes")){
                    System.out.print("\n Introdu genuri (introducerea se opreste la introducerea cuvantului no):");
                    System.out.print("\n Genre:");
                    genreString = scanner.nextLine();
                    while(!Objects.equals(genreString, "no")){
                        Genre genre = null;
                        try {
                            genre = Genre.valueOf(genreString);
                            movie.getGenres().remove(genre);
                            System.out.print("\n Genre:");
                            genreString = scanner.nextLine();
                        }catch (IllegalArgumentException e) {
                            System.out.println("nu este gen valabil");
                            System.out.print("\n Doresti reincercare pentru updatare?[yes/no] ");
                            optiuneYes = scanner.nextLine();
                            if(Objects.equals(optiuneYes, "yes")){
                                updateMovie(staff);
                            }
                            return;
                        }
                    }
                }
            }
            System.out.print("\n Doresti updatarea duratei?[yes]");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Durata: ");
                String duration = scanner.nextLine();
                movie.setDuration(duration);
            }
            System.out.print("\n Doresti updatarea anului aparitiei?[yes] ");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Anul aparitiei: ");
                String yearString = scanner.nextLine();
                while(!checkNumber(yearString)){
                    System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                    yearString = scanner.nextLine();
                }
                int realseyear = Integer.parseInt(yearString);
                movie.setReleaseYear(realseyear);
            }
            System.out.println("Informatii updatate  cu succes");

        return;

    }

    private void updateSeries(Staff staff){
        System.out.println("Series de updatat de " + staff.getUsername());
        for(Object object : staff.getContributions()){
            if(object instanceof Series) {
                Production production = (Production) object;
                System.out.println(production.getTitle());
            }
        }
        System.out.print("\n Numele seriei de updatat: ");
        Scanner scanner = new Scanner(System.in);
        String seriesName = scanner.nextLine();
        Series series = (Series) findProduction(seriesName);
        if(series == null){
            System.out.println("Filmul nu se afla in sistem.");
            System.out.print("\n Doriti updatarea altui film?[yes]");
            String optiune = scanner.nextLine();
            if(Objects.equals(optiune, "yes")){
                updateSeries(staff);
            }
            return;
        }
        if(!staff.getContributions().contains(series) && staff instanceof Contributor){
            System.out.println("Acest serial nu a fost adaugat de tine");
            return;
        }
        System.out.print("\n Doresti updatarea informatiilor despre "+ seriesName + " ?[yes/no]");
        String optiuneYes = scanner.nextLine();
        if(Objects.equals(optiuneYes, "yes")){
            System.out.print("\n Doresti updatarea plotului?[yes]");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Plotu este: ");
                String plot = scanner.nextLine();
                series.setPlot(plot);
            }
            System.out.print("\n Doresti updatarea actorilor actorilor?[yes/no]");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Introdu actori (introducerea se opreste la introducerea cuvantului no):");
                System.out.print("\n Actor:");
                String actorName = scanner.nextLine();
                while(!Objects.equals(actorName, "no")){
                    series.getActors().add(actorName);
                    System.out.print("\n Actor:");
                    actorName = scanner.nextLine();
                }
                System.out.print("\n Doresti si stergerea actorilor?[yes] ");
                optiuneYes = scanner.nextLine();
                if(Objects.equals(optiuneYes, "yes")){
                    System.out.print("\n Introdu actori (introducerea se opreste la introducerea cuvantului no):");
                    System.out.print("\n Actor:");
                    actorName = scanner.nextLine();
                    while(!Objects.equals(actorName, "no")){
                        series.getActors().remove(actorName);
                        System.out.print("\n Actor:");
                        actorName = scanner.nextLine();
                    }
                }
            }

        }
        System.out.print("\n Doresti updatarea directorilor?[yes]");
        optiuneYes = scanner.nextLine();
        if(Objects.equals(optiuneYes, "yes")){
            System.out.print("\n Introdu directori (introducerea se opreste la introducerea cuvantului no):");
            System.out.print("\n Director:");
            String directorName = scanner.nextLine();
            while(!Objects.equals(directorName, "no")){
                series.getDirectors().add(directorName);
                System.out.print("\n Director:");
                directorName = scanner.nextLine();
            }
            System.out.print("\n Doresti si stergerea directorilor?[yes] ");
            optiuneYes = scanner.nextLine();
            if(Objects.equals(optiuneYes, "yes")){
                System.out.print("\n Introdu directori (introducerea se opreste la introducerea cuvantului no):");
                System.out.print("\n Director:");
                directorName = scanner.nextLine();
                while(!Objects.equals(directorName, "no")){
                    series.getActors().remove(directorName);
                    System.out.print("\n Director:");
                    directorName = scanner.nextLine();
                }
            }
        }
        System.out.print("\n Doresti updatarea genurilor?[yes/no]");
        optiuneYes = scanner.nextLine();
        if(Objects.equals(optiuneYes, "yes")) {
            System.out.print("\n Introdu genuri (introducerea se opreste la introducerea cuvantului no):");
            System.out.print("\n Genre:");
            String genreString = scanner.nextLine();
            while (!Objects.equals(genreString, "no")) {
                Genre genre = null;
                try {
                    genre = Genre.valueOf(genreString);
                    series.getGenres().add(genre);
                    System.out.print("\n Genre:");
                    genreString = scanner.nextLine();
                } catch (IllegalArgumentException e) {
                    System.out.println("nu este gen valabil");
                    System.out.print("\n Doresti reincercare pentru updatare?[yes] ");
                    optiuneYes = scanner.nextLine();
                    if (Objects.equals(optiuneYes, "yes")) {
                        updateSeries(staff);
                    }
                }
            }
            System.out.print("\n Doresti si stergerea genurilor?[yes/no] ");
            optiuneYes = scanner.nextLine();
            if (Objects.equals(optiuneYes, "yes")) {
                System.out.print("\n Introdu genuri (introducerea se opreste la introducerea cuvantului no):");
                System.out.print("\n Genre:");
                genreString = scanner.nextLine();
                while (!Objects.equals(genreString, "no")) {
                    Genre genre;
                    try {
                        genre = Genre.valueOf(genreString);
                        series.getGenres().remove(genre);
                        System.out.print("\n Genre:");
                        genreString = scanner.nextLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println("nu este gen valabil");
                        System.out.print("\n Doresti reincercare pentru updatare?[yes] ");
                        optiuneYes = scanner.nextLine();
                        if (Objects.equals(optiuneYes, "yes")) {
                            updateSeries(staff);
                        }
                    }
                }
            }
        }
        System.out.print("\n Doresti adaugarea anului aparitiei?[yes]");
        optiuneYes = scanner.nextLine();
        if(Objects.equals(optiuneYes, "yes")){
            System.out.print("\n Anul aparitiei: ");
            String yearString = scanner.nextLine();
            while(!checkNumber(yearString)){
                System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                yearString = scanner.nextLine();
            }
            int realseyear = Integer.parseInt(yearString);
            series.setReleaseYear(realseyear);
            }

        System.out.print("\n Doresti adaugarea sezoanelor?[yes]");
        optiuneYes = scanner.nextLine();
        if(Objects.equals(optiuneYes, "yes")){
            System.out.print("\n Numar sezoane de adaugat: ");
            String numSeasonsString = scanner.nextLine();
            while(!checkNumber(numSeasonsString)){
                System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                numSeasonsString = scanner.nextLine();
            }
            int numSeasons = Integer.parseInt(numSeasonsString);
            int i = series.getNumSeasons() + 1;
            series.setNumSeasons(numSeasons + series.getNumSeasons());
            while(numSeasons > 0){
                ArrayList<Episode> listOfEpisodes = new ArrayList<Episode>();
                String seasonName = "Season " + i;
                System.out.print("\n Numar episoade pentru sezonul " + i + " : ");
                i++;
                String numEpStr = scanner.nextLine();
                while(!checkNumber(numEpStr)){
                    System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                    numEpStr = scanner.nextLine();
                }
                int numEp = Integer.parseInt(numEpStr);
                while(numEp > 0){
                    Episode episode;
                    System.out.print("\n Episode name: ");
                    String epName = scanner.nextLine();
                    System.out.print("\n Episode duration: ");
                    String epDuration = scanner.nextLine();
                    episode = new Episode(epName,epDuration);
                    listOfEpisodes.add(episode);
                    numEp--;
                }
                series.getSeasons().put(seasonName,listOfEpisodes);
                numSeasons--;
                }
            }
        System.out.println("Informatii adaugate cu succes");

    }

    private void updateActor(Staff staff) {
        afisare_actor_name(staff);
        if(staff instanceof Admin){
            RequestsHolder.dispalyActor();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti numele actorului ");
        String actname = scanner.nextLine();
        Actor actor = findActor(actname);
        if(actor == null){
            System.out.println("Actorul nu se afla in sistem.");
            System.out.print("\n Doriti updatarea altui actor?[yes]");
            String optiune = scanner.nextLine();
            if(Objects.equals(optiune, "yes")){
                updateMovie(staff);
            }
            return;
        }
        if(!staff.getContributions().contains(actor) && staff instanceof Contributor){
            System.out.println("Acest actor nu a fost adaugat de tine");
            return;
        }
        System.out.print("\n Doresti updatarea la mai multe informatii despre " + actname + " ?[yes] ");
        String optiuneYes = scanner.nextLine();
        if (Objects.equals(optiuneYes, "yes")) {
            System.out.print("\n Doresti updatarea biografiei?[yes]");
            optiuneYes = scanner.nextLine();
            if (Objects.equals(optiuneYes, "yes")) {
                System.out.print("Biografia: ");
                String biography = scanner.nextLine();
                actor.setBiography(biography);
            }
            System.out.print("\n Doresti updatarea performantei?[yes] ");
            optiuneYes = scanner.nextLine();
            if (Objects.equals(optiuneYes, "yes")) {
                System.out.print("\n Numar performante de adaugat: ");
                String numPerStr = scanner.nextLine();
                while(!checkNumber(numPerStr)){
                    System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                    numPerStr = scanner.nextLine();
                }
                int numPer = Integer.parseInt(numPerStr);
                while (numPer > 0) {
                    System.out.print("\n Numele performantei: ");
                    String namePerf = scanner.nextLine();
                    System.out.print("Tipul performantei: ");
                    String typePerf = scanner.nextLine();
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("title", namePerf);
                    map1.put("type", typePerf);
                    actor.getPerformances().add(map1);
                    numPer--;
                }
            }
            System.out.println("Informatii updatate cu succes");
        }
    }

    private void afisare_prod_name(Staff staff){
        System.out.println("Productie adaugate de " + staff.getUsername());
        for(Object object : staff.getContributions()){
            if(object instanceof Production) {
                Production production = (Production) object;
                System.out.println(production.getTitle());
            }
        }
    }

    private void afisare_prod_sistem(){
        System.out.println("Productiile din sistem");
        for(Object object : this.productions){
            if(object instanceof Production) {
                Production production = (Production) object;
                System.out.println(production.getTitle() + "(Production)");
            }
            if(object instanceof Actor){
                Actor actor = (Actor) object;
                System.out.println(actor.getName() + "(Actor)");
            }
        }
    }

    private void afisare_fav(User user){
        System.out.println("Favoritele utilizatorului " + user.getUsername());
        for(Object object : user.getFavorites()){
            if(object instanceof Production) {
                Production production = (Production) object;
                System.out.println(production.getTitle() + "(Production)");
            }
            if(object instanceof Actor){
                Actor actor = (Actor) object;
                System.out.println(actor.getName() + "(Actor)");
            }
        }
    }

    private void afisare_actor_name(Staff staff){
        System.out.println("Actori adaugati de " + staff.getUsername());
        for(Object object : staff.getContributions()){
            if(object instanceof Actor) {
                Actor actor = (Actor) object;
                System.out.println(actor.getName());
            }
        }
    }

    private void afisare_user_username(Admin admin){
        System.out.println("Useri din sistem");
        for(User user : this.users){
            System.out.println(user.getUsername());
        }
    }

    private void stergere_rating_by_user(User user){
        for(Production production : this.productions){
            List<Rating> removeRatings = new ArrayList<>();
            for(Rating rating : production.getRatings()){
                if(Objects.equals(rating.getUsername(), user.getUsername())){
                    ///production.getRatings().remove(rating);
                    removeRatings.add(rating);
                }
            }
            production.getRatings().removeAll(removeRatings);
        }
    }
    private void mutare_request_to_admin(User user){
        if(user instanceof Staff){
            Staff staff = (Staff) user;
            for(Object object : staff.getAssignedRequests()){
                if(object instanceof Request){
                    Request request = (Request) object;
                    request.setTo("Admin");
                    RequestsHolder.addRequest(request);
                }
            }
        }
    }
    private void del_all_requests_user(User user){
        if(user instanceof Regular || user instanceof Contributor){
            List<Request> removeRequestImdb = new ArrayList<>();
            for(Request request : this.requests){
                if(Objects.equals(request.getUsername(), user.getUsername())){
                    ///this.requests.remove(request);
                    removeRequestImdb.add(request);
                }
            }
            this.requests.removeAll(removeRequestImdb);
            List<Request> removeRequests = new ArrayList<>();
            for(Request request : RequestsHolder.getAllRequests()){
                if(Objects.equals(request.getUsername(), user.getUsername())){
                    ///RequestsHolder.getAllRequests().remove(request);
                    removeRequests.add(request);
                }
            }
            RequestsHolder.getAllRequests().removeAll(removeRequests);
            for(User user1 : this.users){
                if(user1 instanceof Staff){
                    Staff staff = (Staff) user1;
                    List<Request> removeRequestsstaff = new ArrayList<>();
                    for(Object object : staff.getAssignedRequests()){
                        if(object instanceof Request){
                            Request request = (Request) object;
                            if(Objects.equals(request.getUsername(), user.getUsername())){
                                ///staff.getAssignedRequests().remove(request);
                                removeRequestsstaff.add(request);
                            }
                        }
                    }
                    staff.getAssignedRequests().removeAll(removeRequestsstaff);
                }
            }
        }
    }
    private void add_delete_user(Admin admin){
        System.out.print("\n Doriti stergere sau adaugare? ");
        Scanner scanner = new Scanner(System.in);
        String optiune_stergere_adaugare = scanner.nextLine();
        if(Objects.equals(optiune_stergere_adaugare,"stergere")){
            afisare_user_username(admin);
            System.out.print("Username pentru utilizator de sters: ");
            String username_del = scanner.nextLine();
            User user_del = find_user_username(username_del);
            if(user_del == null){
                System.out.println("Acest username nu are un cont legat de el");
                System.out.print("\n Doriti reincercare pentru stergere sau adaugare?[yes/no]");
                String optine_yes = scanner.nextLine();
                if(Objects.equals(optine_yes, "yes")){
                    add_delete_user(admin);
                    return;
                }
                return;
            }
            stergere_rating_by_user(user_del);
            del_all_requests_user(user_del);
            mutare_request_to_admin(user_del);
            this.users.remove(user_del);
            System.out.println("Utilizator sters cu succes");
            System.out.print("\n Doriti inca o stergere sau adaugare?[yes]");
            String optine_yes = scanner.nextLine();
            if(Objects.equals(optine_yes, "yes")){
                add_delete_user(admin);
                return;
            }
            return;
        }
        if(Objects.equals(optiune_stergere_adaugare, "adaugare")){
            System.out.print("Email pentru utilizator de adaugat: ");
            String email = scanner.nextLine();
            System.out.print("Nume pentru utilizator de adaugat");
            String name = scanner.nextLine();
            System.out.print("Vrei adaugare de mai multe informatii?[yes]");
            String optiune_yes = scanner.nextLine();
            Credentials credentials = new Credentials(email,admin.strong_password());
            User.Information information ;
            User.Information.InformationBuilder builder = new User.Information.InformationBuilder(credentials);
            if(Objects.equals(optiune_yes, "yes")){
                String optiunea_yes2;
                System.out.print("Vrei adaugarea genului?[yes]");
                optiunea_yes2 = scanner.nextLine();
                if(Objects.equals(optiunea_yes2, "yes")){
                    System.out.print("Genul este: ");
                    String gender = scanner.nextLine();
                    builder = builder.gender(gender);
                }
                System.out.print("Vrei adaugarea tari?[yes]");
                optiunea_yes2 = scanner.nextLine();
                if(Objects.equals(optiunea_yes2, "yes")){
                    System.out.print("Tara este: ");
                    String country = scanner.nextLine();
                    builder = builder.country(country);
                }
                System.out.print("Vrei adaugarea datei de nastere?[yes]");
                optiunea_yes2 = scanner.nextLine();
                if(Objects.equals(optiunea_yes2, "yes")){
                    System.out.print("Data este: ");
                    String date = scanner.nextLine();
                    builder = builder.birthDate(date);
                }
                System.out.print("Vrei adaugarea varstei?[yes]");
                optiunea_yes2 = scanner.nextLine();
                if(Objects.equals(optiunea_yes2, "yes")){
                    System.out.print("Varsta este: ");
                    String agestr = scanner.nextLine();
                    int age;
                    while(!checkNumber(agestr)){
                        System.out.print("Nu ai introdus o varsta valida reintrodu: ");
                        agestr = scanner.nextLine();
                    }
                    age = Integer.parseInt(agestr);
                    builder = builder.age(age);
                }
            }
            builder = builder.name(name);
            information = builder.build();
            System.out.println("Ce tip de cont doresti sa creati?");
            System.out.println("Admin\nContributor\nRegular");
            System.out.print("Tipul dorit este: ");
            String optiune_account_type = scanner.nextLine();
            if(Objects.equals(optiune_account_type, "Admin")){
                Admin admin1 = (Admin) UserFactory.factory(AccountType.Admin,admin.generateUsername(name),information);
                this.users.add(admin1);
                System.out.println("User admin creat cu succes ");
                System.out.println("email: " + email);
                System.out.println("password: " + information.getCredentials().getPassword());
                return;
            }
            if(Objects.equals(optiune_account_type, "Contributor")){
                Contributor contributor = (Contributor) UserFactory.factory(AccountType.Contributor,admin.generateUsername(name),information);
                this.users.add(contributor);
                System.out.println("User contributor creat cu succes ");
                System.out.println("email: " + email);
                System.out.println("password: " + information.getCredentials().getPassword());
                return;
            }
            if(Objects.equals(optiune_account_type, "Regular")){
                Regular regular = (Regular) UserFactory.factory(AccountType.Regular,admin.generateUsername(name),information);
                this.users.add(regular);
                System.out.println("User regular creat cu succes ");
                System.out.println("email: " + email);
                System.out.println("password: " + information.getCredentials().getPassword());
                return;
            }
            System.out.println("nu ai introdus bine datele");
        }
    }

    private void afisare_ratings_given(Regular regular){
        for(Production production : this.productions){
            for(Rating rating : production.getRatings()){
                if(Objects.equals(rating.getUsername(), regular.getUsername())){
                    System.out.println(production.getTitle());
                }
            }
        }
    }

    private Rating find_rating_by_username(Production production, String username){
            for(Rating rating : production.getRatings()){
                if(Objects.equals(rating.getUsername(), username)){
                    return rating;
                }
            }
        return null;
    }
    private void add_del_rating(Regular regular) throws IOException {
        System.out.print("\n Doresti adaugare sau stergere de rating?(stergere/adaugare)");
        Scanner scanner = new Scanner(System.in);
        String optiune_stergere_adugare = scanner.nextLine();
        if(Objects.equals(optiune_stergere_adugare, "adaugare")){
            afisare_prod_sistem();
            System.out.print("\n Numele productiei de adaugat rating");
            String prodname = scanner.nextLine();
            Production production = findProduction(prodname);
            System.out.print("Introduceti nota filmui(de la 1 pana la 10) ");
            String nrStr = scanner.nextLine();
            while(!checkNumber(nrStr)){
                System.out.print("\n Nu ai introdus un numar valid doar din cifre reintrodu: ");
                nrStr = scanner.nextLine();
            }
            int nr = Integer.parseInt(nrStr);
            System.out.print("Introduceti un comentariu ");
            String com = scanner.nextLine();
            Rating rating = new Rating(regular.getUsername(),nr,com);
            production.getRatings().add(rating);
            production.notifyObservers(rating);
            production.addObserver(regular);
            ExperienceStrategy RatingExperience = new RatingExperience();
            regular.increase_experience(RatingExperience);
            System.out.println("Rating adaugat cu succes");
            return;
        }
        if(Objects.equals(optiune_stergere_adugare, "stergere")){
            afisare_ratings_given(regular);
            System.out.print("\n Din ce productie  doresti stergerea ratingului");
            String prodname = scanner.nextLine();
            Production production = findProduction(prodname);
            if(production == null){
                System.out.println("Productia nu se afla in sistem");
                System.out.print("\n Vrei sa reintroduci Productia?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    add_del_rating(regular);
                }
                return;
            }
            Rating rating = find_rating_by_username(production,regular.getUsername());
            if(rating == null){
                System.out.println("Nu ai dat rating la aceasta productie");
                System.out.print("\n Vrei sa refaci stergerea?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    add_del_rating(regular);
                }
                return;
            }
            production.getRatings().remove(rating);
            production.removeObserver(regular);
            System.out.println("Ratingul a fost sters cu succes");
            return;
        }
        System.out.print("\nOptiunea a fost introdusa gresit doresti refacere?[yes]");
        String optiune_yes = scanner.nextLine();
        if(Objects.equals(optiune_yes, "yes")){
            add_del_rating(regular);
        }
    }

    private void afisare_requests_by_specific_user(String username){
        for(Request request : this.requests){
            if(Objects.equals(request.getUsername(), username)){
                System.out.println(request);
            }
        }
    }

    private boolean isLocalDateTime(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
            // Dacă parsarea nu generează o excepție, șirul este de tip LocalDateTime.
            return true;
        } catch (DateTimeParseException e) {
            // Excepție generată dacă șirul nu poate fi parsat în LocalDateTime.
            return false;
        }
    }

    private Request find_request_by_date(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String datestr = date.format(formatter);
        for(Request request1 : this.requests){
            String createddatestr = request1.getCreatedDate().format(formatter);
            if(createddatestr.equals(datestr)){
                return request1;
            }
        }
        return null;
    }

    private Staff find_staff_by_actor_added(String actname){
        for(User user : this.users){
            if(user instanceof Staff){
                Staff staff = (Staff) user;
                for(Object object : staff.getContributions()){
                    if(object instanceof Actor){
                        Actor actor = (Actor) object;
                        if(Objects.equals(actor.getName(), actname)){
                            return staff;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Staff find_staff_by_movie_added(String prodname){
        for(User user : this.users){
            if(user instanceof Staff){
                Staff staff = (Staff) user;
                for(Object object : staff.getContributions()){
                    if(object instanceof Production){
                        Production actor = (Production) object;
                        if(Objects.equals(actor.getTitle(), prodname)){
                            return staff;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void add_del_request(User user){
        System.out.print("\n Doresti adaugare sau stergere de request?");
        Scanner scanner = new Scanner(System.in);
        String optiune_stergere_adaugare = scanner.nextLine();
        if(Objects.equals(optiune_stergere_adaugare, "stergere")){
            System.out.println("Request-urile facute: ");
            afisare_requests_by_specific_user(user.getUsername());
            System.out.print("\n Ce request doriti sa stergeti? (alegeti in functie de date creatiei)");
            String dateStr = scanner.nextLine();
            while(!isLocalDateTime(dateStr)){
                System.out.println("Nu ati introdus un tip corect de data (yyyy-MM-dd'T'HH:mm:ss)");
                System.out.print("\nDoriti sa continuati stergere de la inceput?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    add_del_request(user);
                }
                System.out.print("Reintroduceti data: ");
                dateStr = scanner.nextLine();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dateStr,formatter);
            Request request = find_request_by_date(date);
            this.requests.remove(request);
            assert request != null;
            if(Objects.equals(request.getTo(), "Admin")){
                RequestsHolder.removeRequest(request);
                System.out.println("Request sters cu succes.");
                return;
            } else {
                User contribuitor = find_user_username(request.getTo());
                Contributor contribuitor2 = (Contributor) contribuitor;
                contribuitor2.getAssignedRequests().remove(request);
                System.out.println("Request sters cu succes.");
                return;
            }
        }
        if(Objects.equals(optiune_stergere_adaugare, "adaugare")){
            System.out.println("Tipuri de request:\nDELETE_ACCOUNT\nACTOR_ISSUE\nMOVIE_ISSUE\nOTHERS");
            System.out.print("Ce tip doresti? ");
            String tip_request = scanner.nextLine();
            if(Objects.equals(tip_request, "DELETE_ACCOUNT")){
                System.out.print("\nIntroduce o descriere a request-ului");
                String description = scanner.nextLine();
                Request request = new Request(RequestTypes.DELETE_ACCOUNT);
                request.setDescription(description);
                request.setUsername(user.getUsername());
                request.setTo("Admin");
                ZoneId zoneid = ZoneId.of("Europe/Bucharest");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneid);
                LocalDateTime date = zonedDateTime.toLocalDateTime();
                DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = date.format(originalFormatter);
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime newFormattedDateTime = LocalDateTime.parse(LocalDateTime.parse(formattedDateTime, originalFormatter)
                        .format(newFormatter));
                request.setCreatedDate(newFormattedDateTime);
                request.addObserver(user);
                this.requests.add(request);
                RequestsHolder.addRequest(request);
                System.out.println("Request creat cu succes");
                return;
            }
            if(Objects.equals(tip_request, "OTHERS")){
                System.out.print("\nIntroduce o descriere a request-ului");
                String description = scanner.nextLine();
                Request request = new Request(RequestTypes.OTHERS);
                request.setDescription(description);
                request.setUsername(user.getUsername());
                request.setTo("Admin");
                ZoneId zoneid = ZoneId.of("Europe/Bucharest");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneid);
                LocalDateTime date = zonedDateTime.toLocalDateTime();
                DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = date.format(originalFormatter);
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime newFormattedDateTime = LocalDateTime.parse(LocalDateTime.parse(formattedDateTime, originalFormatter)
                        .format(newFormatter));
                System.out.println(newFormattedDateTime);
                request.setCreatedDate(newFormattedDateTime);
                request.addObserver(user);
                this.requests.add(request);
                RequestsHolder.addRequest(request);
                System.out.println("Request creat cu succes");
                return;
            }
            if(Objects.equals(tip_request, "ACTOR_ISSUE")){
                System.out.print("\nIntroduce o descriere a request-ului");
                String description = scanner.nextLine();
                System.out.print("\nIntroduceti numele actorului");
                String actname = scanner.nextLine();
                Request request = new Request(RequestTypes.ACTOR_ISSUE);
                request.setDescription(description);
                request.setActorName(actname);
                request.setUsername(user.getUsername());
                ZoneId zoneid = ZoneId.of("Europe/Bucharest");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneid);
                LocalDateTime date = zonedDateTime.toLocalDateTime();
                DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = date.format(originalFormatter);
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime newFormattedDateTime = LocalDateTime.parse(LocalDateTime.parse(formattedDateTime, originalFormatter)
                        .format(newFormatter));
                request.setCreatedDate(newFormattedDateTime);
                request.addObserver(user);
                Actor actor = findActor(actname);
                Staff staff = find_staff_by_actor_added(actname);
                if(actor == null){
                    System.out.print("\n Acest actor nu se afla in sistem doresti reluarea actiuni?[yes]");
                    String optiune_yes = scanner.nextLine();
                    if(Objects.equals(optiune_yes, "yes")){
                        add_del_request(user);
                        return;
                    }
                    return;
                }
                if(staff == null){
                    request.setTo("Admin");
                    RequestsHolder.addRequest(request);
                }else {
                    if(user instanceof Staff){
                        Staff staff1 = (Staff) user;
                        if(staff1.getContributions().contains(actor)){
                            System.out.println("Nu ai voie sa faci un request la un actor adaugat de tine");
                            return;
                        }

                    }
                    request.setTo(staff.getUsername());
                    request.notifyObservers(staff,request,user);
                    staff.getAssignedRequests().add(request);
                }
                this.requests.add(request);
                System.out.println("Request creat cu succes");
                return;
            }
            if(Objects.equals(tip_request, "MOVIE_ISSUE")){
                System.out.print("\nIntroduce o descriere a request-ului");
                String description = scanner.nextLine();
                System.out.print("\nIntroduceti numele filmului: ");
                String prodname = scanner.nextLine();
                Request request = new Request(RequestTypes.MOVIE_ISSUE);
                request.setDescription(description);
                request.setMovieTitle(prodname);
                request.setUsername(user.getUsername());
                ZoneId zoneid = ZoneId.of("Europe/Bucharest");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneid);
                LocalDateTime date = zonedDateTime.toLocalDateTime();
                DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = date.format(originalFormatter);
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime newFormattedDateTime = LocalDateTime.parse(LocalDateTime.parse(formattedDateTime, originalFormatter)
                        .format(newFormatter));
                request.setCreatedDate(newFormattedDateTime);
                request.addObserver(user);
                Production production = findProduction(prodname);
                Staff staff = find_staff_by_movie_added(prodname);
                if(production == null){
                    System.out.print("\n Acest film nu se afla in sistem doresti reluarea actiuni?[yes]");
                    String optiune_yes = scanner.nextLine();
                    if(Objects.equals(optiune_yes, "yes")){
                        add_del_request(user);
                        return;
                    }
                    return;
                }
                if(staff == null){
                    request.setTo("Admin");
                    RequestsHolder.addRequest(request);
                } else {
                    if(user instanceof Staff){
                        Staff staff2 = (Staff) user;
                        if(staff2.getContributions().contains(production)){
                            System.out.println("Nu ai voie sa faci un request la o productie adaugat de tine");
                            return;
                        }

                    }
                    request.setTo(staff.getUsername());
                    request.notifyObservers(staff,request,user);
                    staff.getAssignedRequests().add(request);
                }
                this.requests.add(request);
                System.out.println("Request creat cu succes");
                return;
            }
            System.out.println("Optiune introdusa gresit");
            System.out.print("Doresti reluarea actiuni?[yes/no]");
            String optiune_yes = scanner.nextLine();
            if(Objects.equals(optiune_yes, "yes")){
                add_del_request(user);
                return;
            }
            return;
        }
        System.out.print("\nOptiunea a fost introdusa gresit doresti refacere?[yes/no]");
        String optiune_yes = scanner.nextLine();
        if(Objects.equals(optiune_yes, "yes")){
            add_del_request(user);
        }
    }

    private void view_requstes(Staff staff){
        if(staff instanceof Contributor){
            for(Object object : staff.getAssignedRequests()){
                if(object instanceof Request){
                    Request request = (Request) object;
                    request.displayInfo();
                }
            }
            return;
        }
        if(staff instanceof Admin){
            System.out.println("Ce tip de vizualizare doresti?");
            System.out.println("1) Toate\n2)Doar pentru admini\n3)Doar cele atribuite tie ");
            System.out.print("\n Optiunea aleasa este: ");
            Scanner scanner = new Scanner(System.in);
            String optiune_vizualizare = scanner.nextLine();
            if(Objects.equals(optiune_vizualizare, "1")){
                RequestsHolder.displayAllRequests();
                for(Object object : staff.getAssignedRequests()){
                    if(object instanceof Request){
                        Request request = (Request) object;
                        request.displayInfo();
                    }
                }
                return;
            }
            if(Objects.equals(optiune_vizualizare, "2")){
                RequestsHolder.displayAllRequests();
                return;
            }
            if(Objects.equals(optiune_vizualizare, "3")){
                for(Object object : staff.getAssignedRequests()){
                    if(object instanceof Request){
                        Request request = (Request) object;
                        request.displayInfo();
                    }
                }
                return;
            }
            System.out.println("Nu ai introdus o optiune valida");
            System.out.print("Doresti reintroducerea optiunilor?[yes] ");
            String optiune_yes = scanner.nextLine();
            if(Objects.equals(optiune_yes, "yes")){
                view_requstes(staff);
            }
        }
    }

    private void solve_request(Staff staff) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n Alege una dintre optiunile\n View\n Solve\n Denie\n Exit\n Optiunea aleasa este: ");
        String optiune_initiala = scanner.nextLine();
        if(Objects.equals(optiune_initiala, "View")){
            view_requstes(staff);
            solve_request(staff);
            return;
        }
        if(Objects.equals(optiune_initiala, "Solve")){
            System.out.print("\n Ce request doriti sa rezolvati? (alegeti in functie de date creatiei)");
            String dateStr = scanner.nextLine();
            while(!isLocalDateTime(dateStr)){
                System.out.println("Nu ati introdus un tip corect de data (yyyy-MM-dd'T'HH:mm:ss)");
                System.out.print("\nDoriti sa continuati rezolvarea de la inceput?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    solve_request(staff);
                    return;
                }
                System.out.print("Reintroduceti data: ");
                dateStr = scanner.nextLine();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dateStr,formatter);
            System.out.println(date);
            Request request1;
            if(staff instanceof Admin){
                System.out.print("Request-ul este destianat adminilor?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    request1 = RequestsHolder.find_request_by_date(date);
                    RequestsHolder.removeRequest(request1);
                } else {
                    request1 = staff.find_request_by_date(date);
                    staff.getAssignedRequests().remove(request1);
                }
            } else {
                request1 = staff.find_request_by_date(date);
                staff.getAssignedRequests().remove(request1);
            }
            Request request = request1;
            if(request == null){
                System.out.println("Acest request nu se afla in sistem");
                System.out.print("Doresti sa o iei de la inceput?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    solve_request(staff);
                    return;
                }
                System.out.println("nu ai rezolvat nimic");
                return;
            }
            request.notifyObservers(request);
            User user = find_user_username(request.getUsername());
            if(user instanceof Regular || user instanceof Contributor){
                ExperienceStrategy strategy = new IssueExperience();
                user.increase_experience(strategy);
            }
            this.requests.remove(request);
            System.out.println("Request marcat ca rezolvat cu succes");
            return;
        }
        if(Objects.equals(optiune_initiala, "Denie")){
            System.out.print("\n Ce request doriti sa stergeti? (alegeti in functie de date creatiei)");
            String dateStr = scanner.nextLine();
            while(!isLocalDateTime(dateStr)){
                System.out.println("Nu ati introdus un tip corect de data (yyyy-MM-dd' 'HH:mm:ss)");
                System.out.print("\nDoriti sa continuati rezolvarea de la inceput?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    solve_request(staff);
                }
                System.out.print("Reintroduceti data: ");
                dateStr = scanner.nextLine();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dateStr,formatter);
            Request request1;
            if(staff instanceof Admin){
                System.out.print("Request-ul este destianat adminilor?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    request1 = RequestsHolder.find_request_by_date(date);
                    RequestsHolder.removeRequest(request1);
                } else {
                    request1 = staff.find_request_by_date(date);
                    staff.getAssignedRequests().remove(request1);
                }
            } else {
                request1 = staff.find_request_by_date(date);
                staff.getAssignedRequests().remove(request1);
            }
            Request request = request1;
            if(request == null){
                System.out.println("Acest request nu se afla in sistem");
                System.out.print("Doresti sa o iei de la inceput?[yes]");
                String optiune_yes = scanner.nextLine();
                if(Objects.equals(optiune_yes, "yes")){
                    solve_request(staff);
                    return;
                }
                System.out.println("nu ai rezolvat nimic");
                return;
            }
            request.notifyObservers_denie();
            this.requests.remove(request);
            staff.getAssignedRequests().remove(request);
            System.out.println("Request marcat ca respins cu succes");
            return;
        }
        if(Objects.equals(optiune_initiala, "Exit")){
            return;
        }
        System.out.println("Nu ai introdus o optiune valida");
        System.out.print("Doresti reintroducerea optiunilor?[yes] ");
        String optiune_yes = scanner.nextLine();
        if(Objects.equals(optiune_yes, "yes")){
            view_requstes(staff);
        }
    }

    private void afisare_prod_gen(Genre genre){
        if(this.productions == null){
            System.out.println("Nu exisista productii");
            return;
        }
        for(Production production : this.productions){
            if(production.getGenres().contains(genre)){
                production.displayInfo();
            }
        }
    }

    private void afisare_dupa_gen(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ce gen doresti?");
        System.out.println(" 1)Comedy");
        System.out.println(" 2)Drama");
        System.out.println(" 3)Horror");
        System.out.println(" 4)SF");
        System.out.println(" 5)Romance");
        System.out.println(" 6)Fantasy");
        System.out.println(" 7)Mystery");
        System.out.println(" 8)Thriller");
        System.out.println(" 9)Crime");
        System.out.println(" 10)Biography");
        System.out.println(" 11)War");
        System.out.println(" 12)Action");
        System.out.println(" 13)Adventure");
        System.out.println(" 14)Cooking");
        System.out.print("Alege genul(introdu numarul respectiv intre (1,14)):");
        String optiune_gen = scanner.nextLine();
        switch (optiune_gen){
            case "1":
                afisare_prod_gen(Genre.Comedy);
                break;
            case "2":
                afisare_prod_gen(Genre.Drama);
                break;
            case "3":
                afisare_prod_gen(Genre.Horror);
                break;
            case "4":
                afisare_prod_gen(Genre.SF);
                break;
            case "5":
                afisare_prod_gen(Genre.Romance);
                break;
            case "6":
                afisare_prod_gen(Genre.Fantasy);
                break;
            case "7":
                afisare_prod_gen(Genre.Mystery);
                break;
            case "8":
                afisare_prod_gen(Genre.Thriller);
                break;
            case "9":
                afisare_prod_gen(Genre.Crime);
                break;
            case "10":
                afisare_prod_gen(Genre.Biography);
                break;
            case "11":
                afisare_prod_gen(Genre.War);
                break;
            case "12":
                afisare_prod_gen(Genre.Action);
                break;
            case "13":
                afisare_prod_gen(Genre.Adventure);
                break;
            case "14":
                afisare_prod_gen(Genre.Cooking);
                break;
            default:
                System.out.println("Nu ai introdus un numar valid!!!");
                afisare_dupa_gen();
        }
    }

    private void afisare_prod_nr_ratings(int nr){
        if(this.productions == null){
            System.out.println("Nu exisista productii");
            return;
        }
        for(Production production : this.productions){
            if(production.getRatings().size() >= nr){
                production.displayInfo();
            }
        }
    }

    private void afisare_prod_rating(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Numarul minim de rating-uri:");
        String intstr = scanner.nextLine();
        while(!checkNumber(intstr)){
            System.out.println("Nu ai introdus un numar");
            System.out.print("Numar nou: ");
            intstr = scanner.nextLine();
        }
        int nr = Integer.parseInt(intstr);
        afisare_prod_nr_ratings(nr);
    }

    private void afisare_prod(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ce tip de afisare doresti?");
        System.out.println(" 1) Toate");
        System.out.println(" 2) Dupa un gen ales ");
        System.out.println(" 3) Dupa un numar de rating-uri date");
        System.out.print("Tipul ales: ");
        String optiune = scanner.nextLine();
        if(Objects.equals(optiune, "1")){
            afisareProductii();
            return;
        }
        if(Objects.equals(optiune, "2")){
            afisare_dupa_gen();
            return;
        }
        if(Objects.equals(optiune, "3")){
            afisare_prod_rating();
            return;
        }
        System.out.println("Nu ai ales o optiune valida");
        System.out.print("Doresti reintroducerea optiunilor?[yes] ");
        String optiune_yes = scanner.nextLine();
        if(Objects.equals(optiune_yes, "yes")){
            afisare_prod();
        }
    }

    private void afisare_actors_string(String s){
        if(this.actors == null){
            System.out.println("Nu exisista actori adaugati");
            return;
        }
        for(Actor actor : this.actors){
            if(actor.getName().contains(s)){
                actor.displayInfo();
            }
        }
    }

    private void afisare_actor_nume(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Dupa ce nume doresti filtrarea: ");
        String s = scanner.nextLine();
        afisare_actors_string(s);
    }

    private void afisare_actors(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ce tip de afisare doresti?");
        System.out.println(" 1) Toate");
        System.out.println(" 2) Dupa nume ");
        System.out.print("Tipul ales: ");
        String optiune = scanner.nextLine();
        if(Objects.equals(optiune, "1")){
            afisareActors();
            return;
        }
        if(Objects.equals(optiune, "2")){
            afisare_actor_nume();
            return;
        }
        System.out.println("Nu ai ales o optiune valida");
        System.out.print("Doresti reintroducerea optiunilor?[yes] ");
        String optiune_yes = scanner.nextLine();
        if(Objects.equals(optiune_yes, "yes")){
            afisare_actors();
        }
    }

    public void CLIAdmin(Admin admin) throws Exception {
        System.out.println("Username: " + admin.getUsername());
        System.out.println("User experience: -");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action :");
        System.out.println("1) View productions details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifactions");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Add/Delete actor/movie/series to/from favorites");
        System.out.println("6) Add/Delete user");
        System.out.println("7) Add/Delete actor/movie/series to/from system");
        System.out.println("8) Update Movie Details");
        System.out.println("9) Update Series Details");
        System.out.println("10) Update Actor Details");
        System.out.println("11) Solve a request");
        System.out.println("12) Logout");
        System.out.print("Optiunea aleasa este : ");
        String optiune = scanner.nextLine();

        switch (optiune){
            case "1":
                afisare_prod();
                CLIAdmin(admin);
                break;
            case "2":
                afisare_actors();
                CLIAdmin(admin);
                break;
            case "3":
                vizualizareNotificari(admin);
                CLIAdmin(admin);
                break;
            case "4":
                searchActorOrprod();
                CLIAdmin(admin);
                break;
            case "5":
                add_or_del_fav(admin);
                CLIAdmin(admin);
                break;
            case "6":
                add_delete_user(admin);
                CLIAdmin(admin);
                break;
            case "7":
                add_del_sistem(admin);
                CLIAdmin(admin);
                break;
            case "8":
                updateMovie(admin);
                CLIAdmin(admin);
                break;
            case "9":
                updateSeries(admin);
                CLIAdmin(admin);
                break;
            case "10":
                updateActor(admin);
                CLIAdmin(admin);
                break;
            case "11":
                solve_request(admin);
                CLIAdmin(admin);
                break;
            case "12":
                logout();
                break;
            default:
                System.out.println("Nu ai introdus o optiune valida");
                CLIAdmin(admin);
        }

    }
    public void CLIContributor(Contributor contributor) throws Exception {
        System.out.println("Username: " + contributor.getUsername());
        System.out.println("User experience: " + contributor.getExperience());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action :");
        System.out.println("1) View productions details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifactions");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Add/Delete actor/movie/series to/from favorites");
        System.out.println("6) Add/Delete Request");
        System.out.println("7) Add/Delete actor/movie/series to/from system");
        System.out.println("8) Update Movie Details");
        System.out.println("9) Update Series Details");
        System.out.println("10) Update Actor Details");
        System.out.println("11) Solve a request");
        System.out.println("12) Logout");
        System.out.print("Optiunea aleasa este : ");
        String optiune = scanner.nextLine();

        switch (optiune){
            case "1":
                afisare_prod();
                CLIContributor(contributor);
                break;
            case "2":
                afisare_actors();
                CLIContributor(contributor);
                break;
            case "3":
                vizualizareNotificari(contributor);
                CLIContributor(contributor);
                break;
            case "4":
                searchActorOrprod();
                CLIContributor(contributor);
                break;
            case "5":
                add_or_del_fav(contributor);
                CLIContributor(contributor);
                break;
            case "6":
                add_del_request(contributor);
                CLIContributor(contributor);
                break;
            case "7":
                add_del_sistem(contributor);
                CLIContributor(contributor);
                break;
            case "8":
                updateMovie(contributor);
                CLIContributor(contributor);
                break;
            case "9":
                updateSeries(contributor);
                CLIContributor(contributor);
                break;
            case "10":
                updateActor(contributor);
                CLIContributor(contributor);
                break;
            case "11":
                solve_request(contributor);
                CLIContributor(contributor);
                break;
            case "12":
                logout();
                break;
            default:
                System.out.println("Nu ai introdus o optiune valida");
                CLIContributor(contributor);
        }
    }
    public void CLIRegular(Regular regular) throws Exception {
        System.out.println("Username: " + regular.getUsername());
        System.out.println("User experience: " + regular.getExperience());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action :");
        System.out.println("1) View productions details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifactions");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Add/Delete actor/movie/series to/from favorites");
        System.out.println("6) Add/Delete request");
        System.out.println("7) Add/Delete rating");
        System.out.println("8) Logout");
        System.out.print("Optiunea aleasa este : ");
        String optiune = scanner.nextLine();

        switch (optiune){
            case "1":
                afisare_prod();
                CLIRegular(regular);
                break;
            case "2":
                afisare_actors();
                CLIRegular(regular);
                break;
            case "3":
                vizualizareNotificari(regular);
                CLIRegular(regular);
                break;
            case "4":
                searchActorOrprod();
                CLIRegular(regular);
                break;
            case "5":
                add_or_del_fav(regular);
                CLIRegular(regular);
                break;
            case "6":
                add_del_request(regular);
                CLIRegular(regular);
                break;
            case "7":
                add_del_rating(regular);
                CLIRegular(regular);
                break;
            case "8":
                logout();
                break;
            default:
                System.out.println("Nu ai introdus o optiune valida");
                CLIRegular(regular);
        }
    }

    public void rulareInCLI() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti email: ");
        String email = scanner.nextLine();
        System.out.print("Introduceti passwaord: ");
        String password = scanner.nextLine();

        User loginUser = findUserbyEmail(email);
        while(loginUser == null){
            System.out.print("Introduceti email inca odata: ");
            email = scanner.nextLine();
            System.out.print("Introduceti passwaord inca odata: ");
            password = scanner.nextLine();
            loginUser = findUserbyEmail(email);
        }
        ///System.out.println(loginUser.getInformation().getCredentials().getPassword());
        if(Objects.equals(loginUser.getInformation().getCredentials().getPassword(), password)){
            ///System.out.println("M-am logat");
            System.out.println("Welcome back user " + loginUser.getUsername());
            rulareSpeficAccount(loginUser);
            return;
        }
        System.out.print("\n Parola incorecte\n");
        rulareInCLI();

    }

    public void run() throws Exception {
        System.out.println("In ce mod vei folosi aplicati");
        System.out.println("1 - CLI");
        System.out.println("2 - GUI");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceti optiunea ");
        String optiunederulare = scanner.nextLine();
        System.out.println();
        while(!Objects.equals(optiunederulare, "1") && !Objects.equals(optiunederulare, "2")){
            System.out.print("Introduceti  optiunea inca odata ");
            optiunederulare = scanner.nextLine();
        }
        if(optiunederulare.equals("1")){
            rulareInCLI();
        }
    }
}
