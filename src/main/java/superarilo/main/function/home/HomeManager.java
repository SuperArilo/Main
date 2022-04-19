package superarilo.main.function.home;

import org.bukkit.Location;
import org.bukkit.Material;

public interface HomeManager extends HomeOnRedis {
    void createNewHome(String homeId);
    void deleteHome(String homeId);
    boolean modifyHomeName(String newName);
    boolean modifyHomeIcon(Material newMaterial);
    boolean modifyLocation(Location newLocation);
}
