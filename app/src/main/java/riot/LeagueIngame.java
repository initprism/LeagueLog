package riot;

import android.util.Log;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiAsync;
import net.rithms.riot.api.endpoints.league.constant.LeagueQueue;
import net.rithms.riot.api.endpoints.league.dto.LeaguePosition;
import net.rithms.riot.api.request.AsyncRequest;
import net.rithms.riot.api.request.RequestAdapter;
import net.rithms.riot.constant.Platform;

import java.util.Set;
import java.util.logging.Level;

public class LeagueIngame {
    private LeaguePosition leagueSolo;
    private LeaguePosition leagueFlexSR;

    private Platform platform;
    private RiotApi api;
    private RiotApiAsync apiAsync;
    AsyncRequest requestLeague;

    public LeagueIngame(Platform platform, long id) {
        try {
            RiotApi api = new Config().getApi();
            apiAsync = api.getAsyncApi();
            requestLeague = apiAsync.getLeaguePositionsBySummonerId(platform, id);
            requestLeague.addListeners(new RequestAdapter() {
                @Override
                public void onRequestSucceeded(AsyncRequest request) {
                    Set<LeaguePosition> leaguePositions = request.getDto();
                    leagueSolo = null;
                    leagueFlexSR = null;
                    if (leaguePositions == null || leaguePositions.isEmpty()) {
                        return;
                    }
                    for (LeaguePosition leaguePosition : leaguePositions) {
                        if (leaguePosition.getQueueType().equals(LeagueQueue.RANKED_SOLO_5x5.name())) {
                            leagueSolo = leaguePosition;
                        } else if (leaguePosition.getQueueType().equals(LeagueQueue.RANKED_FLEX_SR.name())) {
                            leagueFlexSR = leaguePosition;
                        }
                    }
                }
            });

            try {
                // Wait for all asynchronous requests to finish
               apiAsync.awaitAll();
            } catch (InterruptedException e) {
                // We can use the Api's logger
                RiotApi.log.log(Level.SEVERE, "Waiting Interrupted", e);
            }
        } catch (Exception e) {
            leagueSolo = null;
            leagueFlexSR = null;
            Log.d("ERROR :: ", "Can't find summoner");
            return;
        }
    }

    public String getSoloRank() {
        if (leagueSolo == null) {
            return "unranked";
        } else {
            return leagueSolo.getTier();
        }
    }

    public String getSoloRankInfo() {
        if (leagueSolo == null) {
            return "unranked";
        } else {
            return leagueSolo.getTier() + " " + leagueSolo.getRank();
        }
    }


    public String getSoloWins() {
        if (leagueSolo == null) {
            return "";
        } else {
            return Integer.toString(leagueSolo.getWins());
        }
    }

    public String getSoloLosses() {
        if (leagueSolo == null) {
            return "";
        } else {
            return Integer.toString(leagueSolo.getLosses());
        }
    }

    public String getLeagueSoloPoints() {

        if (leagueSolo == null) {
            return "";
        } else {
            return Integer.toString(leagueSolo.getLeaguePoints());
        }
    }

    public String getSoloWinningAverage() {
        if (leagueSolo == null) {
            return "";
        } else {
            double d = ((double) leagueSolo.getWins() / (double) (leagueSolo.getWins() + leagueSolo.getLosses())) * 100;
            return Double.toString(Double.parseDouble(String.format("%.2f", d)));
        }
    }

    public String getTeamRank() {
        if (leagueFlexSR == null) {
            return "unranked";
        } else {
            return leagueFlexSR.getTier();
        }
    }

    public String getTeamRankInfo() {
        if (leagueFlexSR == null) {
            return "unranked";
        } else {
            return leagueFlexSR.getTier() + " " + leagueFlexSR.getRank();
        }
    }

    public String getTeamWins() {
        if (leagueFlexSR == null) {
            return "";
        } else {
            return Integer.toString(leagueFlexSR.getWins());
        }
    }

    public String getTeamLosses() {
        if (leagueFlexSR == null) {
            return "";
        } else {
            return Integer.toString(leagueFlexSR.getLosses());
        }
    }

    public String getLeagueTeamPoints() {

        if (leagueFlexSR == null) {
            return "";
        } else {
            return Integer.toString(leagueFlexSR.getLeaguePoints());
        }
    }

    public String getTeamWinningAverage() {
        if (leagueFlexSR == null) {
            return "";
        } else {
            double d = ((double) leagueFlexSR.getWins() / (double) (leagueFlexSR.getWins() + leagueFlexSR.getLosses())) * 100;
            return Double.toString(Double.parseDouble(String.format("%.2f", d)));
        }
    }

}
