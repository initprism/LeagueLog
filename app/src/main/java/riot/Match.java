package riot;

import android.util.Log;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.ParticipantIdentity;
import net.rithms.riot.api.endpoints.match.dto.TeamStats;
import net.rithms.riot.api.request.AsyncRequest;
import net.rithms.riot.api.request.RequestAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

public class Match implements Serializable {


    net.rithms.riot.api.endpoints.match.dto.Match match;
    MatchReference matchReference;
    String name;
    public Match(Summoner summoner, final long matchId, MatchReference matchReference) {
        try {
            this.matchReference = matchReference;
            name = summoner.getName();

            AsyncRequest requestMatch = summoner.getApiAsync().getMatch(summoner.getPlatform(), matchId);

            requestMatch.addListeners(new RequestAdapter() {
                @Override
                public void onRequestSucceeded(AsyncRequest request) {
                    match = request.getDto();
                }
            });

            try {
                // Wait for all asynchronous requests to finish
                summoner.getApiAsync().awaitAll();
            } catch (InterruptedException e) {
                // We can use the Api's logger
                RiotApi.log.log(Level.SEVERE, "Waiting Interrupted", e);
            }
        } catch (Exception e) {
            Log.d("ERROR :: ", "Can't find summoner");
            return;
        }
    }

    public List<ParticipantIdentity> getParticipantIdentities() {
        return match.getParticipantIdentities();
    }


    public List<Participant> getParticipants() {
        return match.getParticipants();
    }

    public List<TeamStats> getTeamStats() {
        return match.getTeams();
    }

    public MatchReference getMatchReference() {
        return matchReference;
    }

    public long getGameDuration(){
        return match.getGameDuration();
    }

    public long getGameCreation(){
        return match.getGameCreation();
    }

    public String getGameMode(){
        return match.getGameMode();
    }

    public String getGameType(){
        return match.getGameType();
    }

    public String getName(){
        return name;
    }

}
