package me.chazzagram.showdown2.listeners;

public class CrumbleBlockRecord {

    public String attacker;
    public long time;
    public int blockX;
    public int blockY;
    public int blockZ;

    public CrumbleBlockRecord(String attacker, long time, int blockX, int blockY, int blockZ) {
        this.attacker = attacker;
        this.time = time;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

}