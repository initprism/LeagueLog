package db;

import java.io.Serializable;

public class BookmarkSummonerDTO implements Serializable {

    String platform;
    String name;
    String tier;
    String tierInfo;
    String level;
    String profileIcon;

    public BookmarkSummonerDTO(){}

    public BookmarkSummonerDTO(String platform, String name, String tier, String tierInfo, String level, String profileIcon) {
        this.platform = platform;
        this.name = name;
        this.tier = tier;
        this.tierInfo = tierInfo;
        this.level = level;
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

    public String getLevel(){ return level;};

    public void setLevel(String level) { this.level = level; }

    public String getProfileIcon() { return profileIcon; }

    public void setProfileIcon(String profileIcon) {this.profileIcon = profileIcon; }

}


