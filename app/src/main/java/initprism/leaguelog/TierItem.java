package initprism.leaguelog;

public class TierItem {
    String summonerTierIcon;
    String rankType;
    String summonerTierInfo;
    String summonerLp;
    String wins;
    String losses;
    String summonerAvr;

    TierItem(String summonerTierIcon, String rankType, String summonerTierInfo, String summonerLp, String wins, String losses, String summonerAvr){
        this.summonerTierIcon = summonerTierIcon;
        this.rankType = rankType;
        this.summonerTierInfo = summonerTierInfo;
        this.summonerLp = summonerLp;
        this.wins = wins;
        this.losses = losses;
        this.summonerAvr = summonerAvr;
    }

}
