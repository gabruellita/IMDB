package org.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    private List<Request> assignedRequests;
    private ArrayList<String> productionsContribution;
    private ArrayList<String> actorsContribution;
    private SortedSet<T> contributions = new TreeSet<>(super.getNameComparator());

    public Staff(){
        super();
        this.assignedRequests = new ArrayList<>();
        this.productionsContribution = null;
        this.actorsContribution = null;
    }
    public Staff(AccountType accountType, String username, Information information){
        super(accountType, username, information);
        this.contributions = new TreeSet<>();
        this.assignedRequests = new ArrayList<Request>();
    }

    public List<Request> getAssignedRequests() {
        return assignedRequests;
    }

    public void setAssignedRequests(List<Request> assignedRequests) {
        this.assignedRequests = assignedRequests;
    }

    public ArrayList<String> getProductionsContribution() {
        return productionsContribution;
    }

    public void setProductionsContribution(ArrayList<String> productionsContribution) {
        this.productionsContribution = productionsContribution;
    }

    public ArrayList<String> getActorsContribution() {
        return actorsContribution;
    }

    public void setActorsContribution(ArrayList<String> actorsContribution) {
        this.actorsContribution = actorsContribution;
    }

    public SortedSet<T> getContributions() {
        return contributions;
    }

    public void setContributions(SortedSet<T> contributions) {
        this.contributions = contributions;
    }

    @Override
    public void addProductionSystem(Production p) throws IOException {
        IMDB.getInstance().getProductions().add(p);
    }

    @Override
    public void addActorSystem(Actor a) throws IOException {
        IMDB.getInstance().getActors().add(a);
    }

    @Override
    public void removeProductionSystem(String name) throws IOException {
        System.out.print("Introduceti numele productiei ");
        Scanner scanner = new Scanner(System.in);
        String prodname = scanner.nextLine();
        Production prod = IMDB.getInstance().findProduction(prodname);
        if(prod == null){
            System.out.println("Productia nu se afla in sistem");
            System.out.print("Doresti reincercarea stergeri?[yes/no] ");
            String optiune3 = scanner.nextLine();
            if(Objects.equals(optiune3, "yes")){
                IMDB.getInstance().add_del_sistem(this);
            }
            return;
        }
        IMDB.getInstance().getProductions().remove(prod);
        System.out.println("Productia a fost scoasa din sistem");
        System.out.print("Doresti inca o stergeri?[yes/no] ");
        String optiune3 = scanner.nextLine();
        if(Objects.equals(optiune3, "yes")){
            IMDB.getInstance().add_del_sistem(this);
        }
        return;

    }

    @Override
    public void removeActorSystem(String name) throws IOException {
        System.out.print("Introduceti numele actorului ");
        Scanner scanner = new Scanner(System.in);
        String actname = scanner.nextLine();
        Actor actor = IMDB.getInstance().findActor(actname);
        if(actor == null){
            System.out.println("Actorul nu se afla in sitem");
            System.out.print("Doresti reincercarea stergeri?[yes/no] ");
            String optiune3 = scanner.nextLine();
            if(Objects.equals(optiune3, "yes")){
                IMDB.getInstance().add_del_sistem(this);
            }
            return;
        }
        IMDB.getInstance().getActors().remove(actor);
        System.out.println("Actorul a fost scos din sistem");
        System.out.print("Doresti inca o stergeri?[yes/no] ");
        String optiune3 = scanner.nextLine();
        if(Objects.equals(optiune3, "yes")){
            IMDB.getInstance().add_del_sistem(this);
        }
        return;
    }


    @Override
    public void updateProduction(Production p) throws IOException {
        Production prod = IMDB.getInstance().findProduction(p.getTitle());
        prod = p;
    }

    @Override
    public void updateActor(Actor a) throws IOException {
        Actor actor = IMDB.getInstance().findActor(a.getName());
        actor = a;
    }
    @Override
    public void displayInfo(){
        super.displayInfo();
        System.out.println("Contribution{");
        for(Object obj : getContributions()){
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

    public Request find_request_by_date(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String datestr = date.format(formatter);
        for(Request request1 : this.assignedRequests){
            String createddatestr = request1.getCreatedDate().format(formatter);
            if(createddatestr.equals(datestr)){
                return request1;
            }
        }
        return null;
    }
}
