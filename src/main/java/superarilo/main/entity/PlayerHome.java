package superarilo.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("mc_home_list")
public class PlayerHome {
    private String homeId;
    private String homeName;
    private String playerUUID;
    private int locationX;
    private int locationY;
    private int locationZ;
    private String world;
    private String worldAlias;
    private String material;

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(int locationZ) {
        this.locationZ = locationZ;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getWorldAlias() {
        return worldAlias;
    }

    public void setWorldAlias(String worldAlias) {
        this.worldAlias = worldAlias;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
