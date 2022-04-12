package superarilo.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("mc_home_list")
public class PlayerHome {
    private String homeId;
    private String homeName;
    @TableField(value = "player_uuid")
    private String playerUUID;
    private double locationX;
    private double locationY;
    private double locationZ;
    private String world;
    private String worldAlias;
    private String material;
    private double vectorX;
    private double vectorY;
    private double vectorZ;

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

    public void setLocationZ(int locationZ) {
        this.locationZ = locationZ;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public double getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(double locationZ) {
        this.locationZ = locationZ;
    }

    public String getWorldAlias() {
        return worldAlias;
    }

    public void setWorldAlias(String worldAlias) {
        this.worldAlias = worldAlias;
    }

    public double getVectorX() {
        return vectorX;
    }

    public void setVectorX(double vectorX) {
        this.vectorX = vectorX;
    }

    public double getVectorY() {
        return vectorY;
    }

    public void setVectorY(double vectorY) {
        this.vectorY = vectorY;
    }

    public double getVectorZ() {
        return vectorZ;
    }

    public void setVectorZ(double vectorZ) {
        this.vectorZ = vectorZ;
    }
}
