package components;

public class ItemComponent {
    public String itemType;
    public int value;
    public boolean isCollected;

    public ItemComponent() {
    }

    public ItemComponent(String itemType, int value) {
        this.itemType = itemType;
        this.value = value;
        this.isCollected = false;
    }
}
