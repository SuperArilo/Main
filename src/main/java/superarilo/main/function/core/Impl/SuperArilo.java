package superarilo.main.function.core.Impl;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import superarilo.main.function.core.Core;
import superarilo.main.function.home.HomeManager;
import superarilo.main.function.home.Impl.HomeManagerImpl;
import superarilo.main.function.warp.Impl.WarpManagerImpl;
import superarilo.main.function.warp.WarpManager;

public class SuperArilo extends JavaPlugin implements Core {

    @Override
    public HomeManager getHomeManager(Player player) {
        return new HomeManagerImpl(player);
    }

    @Override
    public WarpManager getWarpManager(Player player) {
        return new WarpManagerImpl(player);
    }
}
