package violantic.tron.game;

/**
 * Created by Ethan on 11/13/2016.
 */
public class GameState {

    private boolean canMove;
    private boolean canBreak;
    private boolean canPlace;
    private boolean canTalk;
    private boolean canDrop;

    private String name;

    public GameState(String name) {
        this.name = name;
        canMove = true;
        canBreak = false;
        canPlace = false;
        canTalk = true;
        canDrop = true;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isCanBreak() {
        return canBreak;
    }

    public void setCanBreak(boolean canBreak) {
        this.canBreak = canBreak;
    }

    public boolean isCanPlace() {
        return canPlace;
    }

    public void setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
    }

    public boolean isCanTalk() {
        return canTalk;
    }

    public void setCanTalk(boolean canTalk) {
        this.canTalk = canTalk;
    }

    public boolean isCanDrop() {
        return canDrop;
    }

    public void setCanDrop(boolean canDrop) {
        this.canDrop = canDrop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
