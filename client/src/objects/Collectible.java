package objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import components.*;
import entities.Player;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OBJ_Heart.class, name = "OBJ_Heart"),
        @JsonSubTypes.Type(value = OBJ_Mana.class, name = "OBJ_Mana")
})

public class Collectible {
    public String name;
    public RenderComponent render;
    public PositionComponent position;
    public HitboxComponent hitbox;
    public ItemComponent item;
    public AnimationComponent animation;

    public Collectible() {
        this.name = "object";
    }

    public void update() {}

    public void interact(Player player) {

    }

}
