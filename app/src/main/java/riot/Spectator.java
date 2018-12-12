package riot;

import android.util.Log;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameInfo;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.api.request.AsyncRequest;
import net.rithms.riot.api.request.RequestAdapter;

import java.util.List;
import java.util.logging.Level;

public class Spectator {

    public CurrentGameInfo currentGameInfo;
    AsyncRequest requestSpecator;

    public Spectator(Summoner summoner) {
        try {
            requestSpecator = summoner.getApiAsync().getActiveGameBySummoner(summoner.getPlatform(), summoner.getSummonerId());
            requestSpecator.addListeners(new RequestAdapter() {
                @Override
                public void onRequestSucceeded(AsyncRequest request) {
                    currentGameInfo = null;
                    currentGameInfo = request.getDto();
                }
            });
             try {
                // Wait for all asynchronous requests to finish
                summoner.getApiAsync().awaitAll();
            } catch (InterruptedException e) {
                // We can use the Api's logger
                RiotApi.log.log(Level.SEVERE, "Waiting Interrupted", e);
            }
        }catch (Exception e){
            Log.d("ERROR :: ", "Can't find summoner");
        }
    }

    public long isInGame() {
        return currentGameInfo.getGameId();
    }

    public long getCurrentGameLength() {
        return currentGameInfo.getGameLength();
    }

    public String getCurrentGameMode() {
        return currentGameInfo.getGameMode();
    }

    public long getCurrentGameStartTime() {
        try {
            long result = currentGameInfo.getGameStartTime();
            return result;
        } catch (Exception e) {
            return -1;
        }
    }

    public String getCurrentGameType() {
        return currentGameInfo.getGameType();
    }

    public int getCurrentGameQueueConfigId() {
        return currentGameInfo.getGameQueueConfigId();
    }

    public List<CurrentGameParticipant> getCurrentGameParticipants() {
        return currentGameInfo.getParticipants();
    }
}
