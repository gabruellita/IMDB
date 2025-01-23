package org.example;

import java.io.IOException;

public interface StaffInterface {
    void addProductionSystem(Production p) throws IOException;
    void addActorSystem(Actor a) throws IOException;
    void removeProductionSystem(String name) throws IOException;
    void removeActorSystem(String name) throws IOException;
    void updateProduction(Production p) throws IOException;
    void updateActor(Actor a) throws IOException;
}
