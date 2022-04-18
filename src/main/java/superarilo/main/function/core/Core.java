package superarilo.main.function.core;

import org.bukkit.entity.Player;
import superarilo.main.function.home.HomeManager;
import superarilo.main.function.warp.WarpManager;

public interface Core {
    HomeManager getHomeManager(Player player);
    WarpManager getWarpManager(Player player);
}
