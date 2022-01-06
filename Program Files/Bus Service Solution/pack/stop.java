package pack;

import java.util.LinkedList;
import java.util.Queue;

public class stop {
    public int name = 0;
    public int people = 0;
    public int bus_at_stop = 0;
    public int minNum = 0;
    public int maxNum = 0;
    public int totalPeople = 0;
    public int totalBuses = 0;
    public Queue<bus> busesWaiting= new LinkedList<bus>();


    public stop(int name, int people) {
        this.name = name;
        this.people = people;
    }

    public int getName() {
        return this.name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getPeople() {
        return this.people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

}