package superarilo.main.function.warp;

import org.bukkit.entity.Player;
import superarilo.main.gui.warp.WarpMenu;

public class WarpListFunction {

    private final Player player;
    private final int slot;
    private final WarpListFunctionType type;

    public WarpListFunction(Player player, int slot, WarpListFunctionType type){
        this.player = player;
        this.slot = slot;
        this.type = type;
    }

    public void doThis(){
        switch (this.type){
            case BACK: {
                this.player.closeInventory();
                new WarpMenu(this.player).open();
                break;
            }
            case NEXT: {
                System.out.println("下一页");
                break;
            }
            case WARP: {
                System.out.println("传送到此处的地标 " + this.slot);
                break;
            }
            case PREVIOUS: {
                System.out.println("上一页");
                break;
            }
        }
    }

    public enum WarpListFunctionType{
        BACK,
        WARP,
        PREVIOUS,
        NEXT
    }
}
