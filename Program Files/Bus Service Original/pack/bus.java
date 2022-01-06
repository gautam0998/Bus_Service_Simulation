package pack;

public class bus {
    public int name = 0;
    public int current_stop = 0;
    public int pos = 0;

    //pos = 1: Bus at stop
    //pos = 2: Bus on the way
    //pos = 3: Bus in waiting queue


    public bus(int name, int current_stop, int pos) {
        this.name = name;
        this.current_stop = current_stop;
        this.pos = pos;
    }

    public int getName() {
        return this.name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getCurrent_stop() {
        return this.current_stop;
    }

    public void setCurrent_stop(int current_stop) {
        this.current_stop = current_stop;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
    
}