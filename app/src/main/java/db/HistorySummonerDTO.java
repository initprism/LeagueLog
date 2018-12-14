package db;

import java.io.Serializable;
import java.lang.annotation.Retention;

public class HistorySummonerDTO implements Serializable {

    String platform;
    String name;
    String tier;
    String tierInfo;
    String profileIcon;
    String bookmark;

    public HistorySummonerDTO(){}

    public HistorySummonerDTO(String platform, String name, String tier, String tierInfo, String profileIcon, String bookmark) {
        this.platform = platform;
        this.name = name;
        this.tier = tier;
        this.tierInfo = tierInfo;
        this.profileIcon = profileIcon;
        this.bookmark = bookmark;
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

    public String getProfileIcon() { return profileIcon; }

    public void setProfileIcon(String profileIcon) {this.profileIcon = profileIcon; }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {this.bookmark = bookmark;}
}


