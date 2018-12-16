package riot;

import android.util.Log;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.request.AsyncRequest;
import net.rithms.riot.api.request.RequestAdapter;

import java.io.Serializable;
import java.util.logging.Level;

public class MatchList implements Serializable {


    net.rithms.riot.api.endpoints.match.dto.MatchList matchList;

    public MatchList(Summoner summoner) {
        try {
            final AsyncRequest requestMatch = summoner.getApiAsync().getMatchListByAccountId(summoner.getPlatform(), summoner.getSummoner().getAccountId());

            requestMatch.addListeners(new RequestAdapter() {
                @Override
                public void onRequestSucceeded(AsyncRequest request) {
                    matchList = request.getDto();
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

    public net.rithms.riot.api.endpoints.match.dto.MatchList getMatchList() {
        return matchList;
    }
}
