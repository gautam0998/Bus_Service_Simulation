package pack;

import java.util.LinkedList;
import java.util.Queue;

public class stop {
    public int name = 0;
    public int people = 0;
    public int bus_at_stop = 0;         //If there is a bus at the stop
    public int minNum = 0;              //minimum number of people in queue
    public int maxNum = 0;              //Maximum number of people in queue
    public int totalPeople = 0;         //Total people enetering the queue in an hour
    public int totalBuses = 0;          //Total buses entering the queue in an hour
    public Queue<bus> busesWaiting= new LinkedList<bus>();          //Queue of buses waiting at stop


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