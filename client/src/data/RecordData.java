package data;

public class RecordData {
    public String username;
    public long time;

    public int health;

    public int mana;

    public RecordData() {}

    public RecordData(String username, long time, int health, int mana) {
        this.username = username;
        this.time = time;
        this.health = health;
        this.mana = mana;
    }

    public long getTime() { return time; }
}
