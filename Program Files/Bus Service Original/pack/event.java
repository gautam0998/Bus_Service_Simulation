package pack;

public class event {
    public double time = 0;     //Time of event occuring
    public int type = 0;        //Type of event: Person, Arrival, Boarder 
    public int bus = 0;         //Bus number associated with the event
    public int stop = 0;        //Stop name associated with the event

    //type 1 = Arrival of person event
    //type 2 = Arrival of Bus event
    //type 3 = Boarding of person event
    //type 4 = Snapshot event

    public event (double time, int type, int bus, int stop) {
        this.time = time;
        this.type = type;
        this.bus = bus;
        this.stop = stop;
    }


    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBus() {
        return this.bus;
    }

    public void setBus(int bus) {
        this.bus = bus;
    }

    public int getStop() {
        return this.stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

}