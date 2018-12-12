package db;

import java.io.Serializable;

import initprism.leaguelog.MainActivity;

public class MySummoner implements Serializable {

    String platform;
    String name;
    String level;
    String tier;
    String tierInfo;
    String lp;
    String wins;
    String losses;
    String avr;
    String profileIcon;

    public MySummoner(){}

    public MySummoner(String platform, String name, String level, String tier, String tierInfo, String lp, String wins, String losses, String avr, String profileIcon) {
        this.platform = platform;
        this.name = name;
        this.level = level;
        this.tier = tier;
        this.tierInfo = tierInfo;
        this.lp = lp;
        this.wins = wins;
        this.losses = losses;
        this.avr = avr;
        this.profileIcon = profileIcon;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getTierInfo() {
        return tierInfo;
    }

    public void setTierInfo(String tierInfo) {
        this.tierInfo = tierInfo;
    }

    public String getLp() {
        return lp;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public String getAvr() {
        return avr;
    }

    public void setAvr(String avr) {
        this.avr = avr;
    }

    public String getProfileIcon() { return profileIcon; }

    public void setProfileIcon(String profileIcon) {this.profileIcon = profileIcon; }
}


