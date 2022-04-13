package superarilo.main.function;

import com.destroystokyo.paper.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CreatePlayerTitle {

    private final Player player;
    private final String title;
    private final String subTitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public CreatePlayerTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut){
        this.player = player;
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void sendToPlayer(){
        this.player.sendTitle(ChatColor.translateAlternateColorCodes('&', this.title), ChatColor.translateAlternateColorCodes('&', this.subTitle),this.fadeIn,this.stay,this.fadeOut);
    }
}
