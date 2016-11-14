package violantic.tron.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 11/13/2016.
 */
public class TrailManager {

    private Map<UUID, String> userMap;

    public TrailManager() {
        this.userMap = new HashMap<UUID, String>();
    }

    public Map<UUID, String> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<UUID, String> userMap) {
        this.userMap = userMap;
    }

    public void register(UUID uuid, String color) {
        getUserMap().put(uuid, color);
    }

}
