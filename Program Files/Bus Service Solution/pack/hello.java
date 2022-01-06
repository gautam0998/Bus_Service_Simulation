package pack;
import java.util.Random;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class hello {

    public static LinkedList<event> elq = new LinkedList<event>();          //Event Queue
    public static bus[] bus_list = new bus[initial_values.buses];           //The list of buses
    public static stop[] stop_list = new stop[initial_values.bus_stops];    //The list of Stops
    public static double systemClock = 0;                                   //System Clock
    public static int[] flag = new int[initial_values.bus_stops];

    public static File log = new File("log.txt");                           //Log File object
    public static File st = new File("snapshot.txt");                       //Snapshot File Object



    public static void log_creator() {                                       //Creating log file
        try {
            if (log.exists()==false) {
                log.createNewFile();
            }

            else {                                              //To overwrite any existing file of same name
                try {
                    PrintWriter logger = new PrintWriter(new FileWriter(log, false));       
                    logger.write("");
                    logger.close();
                }
        
                catch (IOException e) {
                    System.out.println("System Exception: " + e);
                }
            }

        } catch (Exception e) {
            System.out.println("Exception: "+e);
        }
    }

    public static void st_creator() {                                       //Creating log file
        try {
            if (st.exists()==false) {
                st.createNewFile();
            }

            else {                                                      //To overwrite any existing file of same name
                try {
                    PrintWriter snaping = new PrintWriter(new FileWriter(st, false));
                    snaping.write("");
                    snaping.close();
                }
        
                catch (IOException e) {
                    System.out.println("System Exception: " + e);
                }
            }

        } 
        
        catch (Exception e) {
            System.out.println("Exception: "+e);
        }
    }

    public static void logging(CharSequence s) {                             //Log Writer
        try {
            PrintWriter logger = new PrintWriter(new FileWriter(log, true));
            logger.append(s);
            logger.close();
        } 
        catch (IOException e) {
            System.out.println("System Exception: " + e);
        }
    }
    
    public static void snapshot() {                                             //Create Snapshots at every hour
        System.out.println("\nSnapshot");
        try {
            PrintWriter snaping = new PrintWriter(new FileWriter(st, true));
            snaping.append("\n\n\nSnapshot at System Clock: "+systemClock);

            for (int i = 0; i < bus_list.length; i++) {
                snaping.append("\n\n * * * * * * * * * * * * * * * * * * * * Bus: "+(i+1)+" * * * * * * * * * * * * * * * * * * * * ");
                if (bus_list[i].pos == 1) {
                    snaping.append("\nBus is at the stop: "+bus_list[i].current_stop);
                }

                if (bus_list[i].pos == 2) {
                    snaping.append("\nBus is on the way to the stop: "+(bus_list[i].current_stop+1));
                }

                if (bus_list[i].pos == 3) {
                    snaping.append("\nBus is in waiting queue at stop: "+(bus_list[i].current_stop+1));
                }

            }
            snaping.close();
        } 
        catch (IOException e) {
            System.out.println("System Exception: " + e);
        }
    }

    public static void initialize() {                                       //Initializing

        for(int i=0;i<initial_values.buses;i++) {                       //Initializing Buses
            bus_list[i] = new bus(i+1, 0, 0, 0); 
        }

        for (int i = 0; i < initial_values.bus_stops; i++) {               //Initializing Stops
            stop_list[i] = new stop(i+1, 0);
        }

        for (int i = 0; i < initial_values.bus_stops; i++) {
            event e1 = new event(0, 1, 0, i+1);                         //Adding events for initializing 1 person in starting at each stop
            addEvent(e1);
        }

        for (int i = 0; i < initial_values.buses; i++) {
            event e1 = new event(0, 2, i+1, (i+1)*(initial_values.bus_stops/initial_values.buses)); //Adding events for Bus Initialization
            addEvent(e1);
        }

        for (int i = 0; i < initial_values.simulation_time/3600; i++) {                             //Event generation for snapshots
            event e1 = new event(3600*(i+1), 4, 0, 0);                      //Adding event for Snapshot
            addEvent(e1);
        }

        for (int i = 0; i < initial_values.simulation_time/initial_values.busDepart; i++) {            //Event Generation for bus departing
            event e1 = new event(initial_values.busDepart*(i+1), 5, 0, 0);
            addEvent(e1);
        }

        for (int i = 0; i < flag.length; i++) {
            flag[i] =0;
        }
    }

    public static void addEvent(event e1) {                 //To add event to queue based on its time
        double t = e1.getTime();
        if (elq.size()==0) {                                //When the queue is empty
            //System.out.println("Size 0");
            elq.add(e1);
            return;
        }
        for(int i = 0; i < elq.size(); i++) {               
            if(t<elq.get(i).time && i==0) {                 //When the event to be added has the earliest time
                elq.add(0, e1);
                break;
            }
            if(t<elq.get(i).time) {                            //When the event needs to be added somewhere between the queue
                //System.out.println("adding element before "+elq.get(i).time);
                elq.add(i, e1);
                break;
            }

            if(i== elq.size()-1) {                          //When the event needs to be added at last
                elq.addLast(e1);
                break;
            }
        }
    }

    public static void event_handler(event e) {
        if(e.type == 1) {                                                          //Arrival of Person Handler

            //System.out.println("Arrival of Person at Stop: "+e.stop);

            //logging("\nSystem Clock: "+systemClock+" : Person Arrived at stop: "+e.stop);       //Log: Person arrived at stop

            /*if (stop_list[e.stop-1].people == 0 && stop_list[e.stop-1].bus_at_stop == 1 && flag[e.stop-1]==0) {
                event e1 = new event(systemClock + initial_values.boarding_time, 3, 0, e.stop);
                addEvent(e1);
                logging("\nSystem Clock: "+systemClock+" : Boarder event generated for above person: "+e.stop);     //Log: Boarder event generated
                //flag[e.stop-1] = 1;
            }*/

            stop_list[e.stop-1].people = stop_list[e.stop-1].people + 1;           //Adding a person to the stop

            stop_list[e.stop-1].totalPeople = stop_list[e.stop-1].totalPeople + 1;

            if (stop_list[e.stop-1].minNum == -1) {
                stop_list[e.stop-1].minNum = stop_list[e.stop-1].people;
            }

            if (stop_list[e.stop-1].maxNum == -1) {
                stop_list[e.stop-1].maxNum = stop_list[e.stop-1].people;
            }

            if (stop_list[e.stop-1].people < stop_list[e.stop-1].minNum) {
                stop_list[e.stop-1].minNum = stop_list[e.stop-1].people;
            }

            if (stop_list[e.stop-1].people > stop_list[e.stop-1].maxNum) {
                stop_list[e.stop-1].maxNum = stop_list[e.stop-1].people;
            }

            double t = getNextPersonTime();
            event e1 = new event((systemClock + t), 1, 0, e.stop);              //Generate event for next persons arrival
            addEvent(e1);
            //logging("\nSystem Clock: "+systemClock+" : New Person requested at: "+e.stop+" :Will arrive at: "+(systemClock+t));


            //logging("\nSystem Clock: "+systemClock+" : New Person requested at: "+e.stop);          //Log: Person Requested
        }

        if(e.type == 2) {                                                           //Arrival of Bus handler
            if(stop_list[e.stop-1].bus_at_stop == 0) {                              //No bus at the stop so enter
                //System.out.println("Arrival of Bus at Stop: "+e.stop);

                logging("\nSystem Clock: "+systemClock+" : Bus arrived at stop: "+e.stop);          //Log: Bus arriving at stop

                bus_list[e.bus-1].current_stop = e.stop;                                            //updating current stop of bus

                bus_list[e.bus-1].pos = 1;                                                          //pos = 1 means bus at stop

                stop_list[e.stop-1].totalBuses = stop_list[e.stop-1].totalBuses + 1;

                stop_list[e.stop-1].bus_at_stop = 1;

                //logging("\nSystem Clock: "+systemClock+" : Number of people at bus stop: "+stop_list[e.stop-1].people);          //Log: Number of people in queue 

                if (stop_list[e.stop-1].people == 0) {                                                  //If the queue at stop is empty
                    //logging("\nSystem Clock: "+systemClock+" : Number of people at bus stop: "+stop_list[e.stop-1].people);        //Log: Number of people in queue
                    //logging("\nSystem Clock: "+systemClock+" : No people in queue at stop: "+e.stop);                           //Log: No one in queue at stop

                    //flag[e.stop-1]=0;


                    /*int tempStop = e.stop;                          //Temporary variable to save original event stop
                    if (e.stop+1>15) {
                        e.stop = 0;                                                                     //To accomodate going from stop 15 to 1
                    }
                    event e1 = new event(systemClock+initial_values.drive_time, 2, e.bus, (e.stop+1));      //generating event for bus departing
                    addEvent(e1);
                    logging("\n\nSystem Clock: "+systemClock+" : Bus departing stop for next stop: "+(e.stop+1));     //Log: Bus Departing
                    stop_list[tempStop-1].bus_at_stop=0;

                    bus_list[e.bus - 1].pos = 2;

                    if (stop_list[tempStop-1].busesWaiting.size()>0){                                             //Check if buses are waiting in queue at stop
                        int nextBus = stop_list[tempStop-1].busesWaiting.poll().name;                             //Find the name of bus next in line
                        event e2 = new event(systemClock, 2, nextBus, tempStop);                                  //Generate event for bus to arrive at stop
                        addEvent(e2);
                        logging("\nSystem Clock: "+systemClock+" : Next bus in Queue is arriving at stop: "+tempStop);    //Log: bus exits queue and arrives at stop
                    }
                    else {
                        stop_list[tempStop-1].bus_at_stop=0;                          //If queue empty then set the value to 0
                    }*/
                }

                else {
                    //logging("\nSystem Clock: "+systemClock+" : Number of people at bus stop: "+stop_list[e.stop-1].people);    //Log: People at bus stop
                    event e1 = new event(systemClock+initial_values.boarding_time, 3, e.bus, e.stop);
                    addEvent(e1);
                    //flag[e.stop-1]=1;
                }
            }

            else {                                                                              //Already a bus at the stop
                stop_list[e.stop-1].busesWaiting.add(bus_list[e.bus-1]);                        //Add bus to the Waiting at queue
                logging("\nSystem Clock: "+systemClock+" : Bus in waiting queue at stop: "+e.stop);
                bus_list[e.bus - 1].pos = 3;
            }
        }

        if (e.type == 3) {                                                          //Boarder Event Handler

            //System.out.println("Person Boarded at stop: "+e.stop);       
            
            if (stop_list[e.stop-1].bus_at_stop == 1) {
                //logging("\nSystem Clock: "+systemClock+" : Person boarding at stop: "+e.stop);      //Log: Person Boarding
                stop_list[e.stop-1].people = stop_list[e.stop-1].people - 1;
                
            }

            /*else {
                event e1 = new event(systemClock+initial_values.drive_time, 1, 0, e.stop);              //No bus at stop so will eneter queue again
                addEvent(e1);
            }*/


            //stop_list[e.stop-1].people = stop_list[e.stop-1].people - 1;                        //Reducing people in queue after boarding

            if (stop_list[e.stop-1].people == 0) {                                              //If queue becomes empty
                //logging("\nSystem Clock: "+systemClock+" : No people in queue at stop: "+e.stop);       //Log: Queue empty

                //flag[e.stop-1] = 0;

                /*int tempStop = e.stop;                                                          //Temporary variable to store original event stop
                if (e.stop+1>15) { 
                    e.stop = 0;
                }
                event e1 = new event(systemClock+initial_values.drive_time, 2, e.bus, (e.stop+1));          //Depart bus to next stop
                addEvent(e1);
                logging("\n\nSystem Clock: "+systemClock+" : Bus departing stop for next stop: "+(e.stop+1));

                bus_list[e.bus-1].pos = 2;

                stop_list[tempStop-1].bus_at_stop=0;
                if (stop_list[tempStop-1].busesWaiting.size()>0){                                             //Check if buses are waiting in queue at stop
                    int nextBus = stop_list[tempStop-1].busesWaiting.poll().name;                             //Find the name of bus next in line
                    event e2 = new event(systemClock, 2, nextBus, tempStop);                                  //Generate event for bus to arrive at stop
                    addEvent(e2);
                    logging("\nSystem Clock: "+systemClock+" : Next bus in Queue is arriving at stop: "+tempStop);    //Log: bus exits queue and arrives at stop
                }
                else {
                    stop_list[tempStop-1].bus_at_stop=0;                          //If queue empty then set the value to 0
                }*/
            }

            else {
                //logging("\nSystem Clock: "+systemClock+" : Number of people at bus stop: "+stop_list[e.stop-1].people);    //Log: People at bus stop
                event e1 = new event(systemClock + initial_values.boarding_time, 3, e.bus, e.stop);             //Board the next person in queue
                addEvent(e1);
                //flag[e.stop-1]=1;
            }
        }
        
        if (e.type == 4) {
            snapshot();                                                             //Take a snapshot
        }

        if (e.type == 5) {
            for (int i = 0; i < bus_list.length; i++) {
                event e1 = new event(systemClock+initial_values.drive_time, 2, bus_list[i].name, bus_list[i].current_stop+1);
                if (e1.stop==16) {
                    e1.stop=1;
                }
                bus_list[i].pos = 2;
                addEvent(e1);
                logging("\nSystem Clock: "+systemClock+" :Bus departing stop: "+bus_list[i].current_stop);
                stop_list[bus_list[i].current_stop-1].bus_at_stop = 0;
            }
        }
    }

    public static double getNextPersonTime() {                      //To get arrival time of next person
        Random rand = new Random();
        return -1*(Math.log(1 - rand.nextDouble())/(initial_values.lambda));        //Generating inter arrival time
    }

    public static void printList() {                            //To print the time of all the events in the event queue
        System.out.println("Printing event times:");
        for (int i = 0; i < elq.size(); i++) {
            System.out.println(elq.get(i).type);
        }
    }

    /*public static void minMax() {
        for (int i = 0; i < stop_list.length; i++) {
            int t = stop_list[i].people;                    //Getting No. of people in queue
            if(stop_list[i].minNum == -1) {                    //When minNum = -1
                stop_list[i].minNum = t;
            }

            if(stop_list[i].maxNum == -1) {                     //When maxNum = -1
                stop_list[i].maxNum = t;
            }

            if(t<stop_list[i].minNum) {                         //Updating minNum
                stop_list[i].minNum = t;
                logging("\nminNum updated for stop : "+(i+1));
            }

            if(t>stop_list[i].maxNum) {                         //Updating maxNum
                stop_list[i].maxNum = t;
                logging("\nmaxNum updated for stop : "+(i+1));
            }
        }
    }*/


    public static void main(String args[]) {
        //double x = 0;
        /*(for (int i = 0; i < 10000; i++) {
            x = x+getNextPersonTime();
        }*/

        /*event e1 = new event(0,0,0,0);
        addEvent(e1);
        printList();

        //double y = x/10000;
        //System.out.println("The Mean is: "+y);

        event e2 = new event(1,1,1,1);
        addEvent(e2);
        printList();

        event e3 = new event(0.5,1,1,1);
        addEvent(e3);
        printList();

        event e4 = new event(0.7,1,1,1);
        addEvent(e4);
        printList();

        event e5 = new event(0.6,1,1,1);
        addEvent(e5);
        printList();

        event e6 = new event(-2,1,1,1);
        addEvent(e6);
        printList();

        System.out.println(elq.get(3).time);
        elq.remove(3);
        printList();*/

        initialize();

        log_creator();

        st_creator();

        logging("Logs \n");
        
        {                                           //Snapshot Initialization
            try {
                PrintWriter snaping = new PrintWriter(new FileWriter(st, true));
                snaping.append("Snapshots \n");
                snaping.append("Average waiting people = (Total people)/(Total buses arrived at the stop)");
                snaping.close();
            }

            catch (IOException e) {
                System.out.println("System Exception: " + e);
            }
        }


        while(systemClock<initial_values.simulation_time) {                         //Event Handlings
            event e1 = elq.poll();
            systemClock = e1.time;


            if (e1.type == 1 || e1.type == 3) {
                //System.out.println("No. of people at stop " + e1.stop + " Before event: " +stop_list[e1.stop-1].people);
                event_handler(e1);
                //System.out.println("No. of people at stop " + e1.stop + " After event: " +stop_list[e1.stop-1].people);
            }

            if(e1.type == 2){
                //System.out.println("The Bus: "+e1.bus);
                event_handler(e1);
                //System.out.println("The Current Stop = " + bus_list[e1.bus - 1].current_stop);
            }

            if (e1.type == 4) {
                event_handler(e1);
            }

            if (e1.type == 5) {
                event_handler(e1);
            }
            
            if(elq.size() == 0) {
                System.out.println("Event Queue Empty");            //Queue empty so exit simulation
                break;
            }

            //minMax();                                                   //To update min and max

            //System.out.println("The System Clock: " + systemClock);
        }

        System.out.println("Simulation Ended");
        logging("\n\n Simulation Ended");

    }

}
