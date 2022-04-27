package superarilo.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("mc_warp_list")
public class Warp {
    @TableId(value = "id")
    private int id;
    private String warpId;
    private String warpName;
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
    private String warpClass;
    private String describe;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWarpId() {
        return warpId;
    }

    public void setWarpId(String warpId) {
        this.warpId = warpId;
    }

    public String getWarpName() {
        return warpName;
    }

    public void setWarpName(String warpName) {
        this.warpName = warpName;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
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

    public String getWarpClass() {
        return warpClass;
    }

    public void setWarpClass(String warpClass) {
        this.warpClass = warpClass;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
