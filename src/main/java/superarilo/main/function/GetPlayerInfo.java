package superarilo.main.function;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import superarilo.main.Main;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class GetPlayerInfo {

    private final Player player;

    public GetPlayerInfo (Player player){
        this.player = player;
    }

    public CompletableFuture<Boolean> isPlayerInGroupAsync (String groupName){
        return Main.luckPerms.getUserManager().loadUser(this.player.getUniqueId()).thenApplyAsync(user -> {
            Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
            return inheritedGroups.stream().anyMatch(g -> g.getName().equals("admin"));
        });
    }

    public boolean isPlayerInGroup (String groupName){
        User user = Main.luckPerms.getUserManager().getUser(this.player.getUniqueId());
        return user != null && (user.getPrimaryGroup().equals(groupName));
    }

    public String getPlayerGroupName (){
        User user = Main.luckPerms.getUserManager().getUser(this.player.getUniqueId());
        return user == null ? "null" : user.getPrimaryGroup();
    }
}
