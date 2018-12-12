package riot;

import android.util.Log;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiAsync;
import net.rithms.riot.api.request.AsyncRequest;
import net.rithms.riot.api.request.RequestAdapter;
import net.rithms.riot.constant.Platform;

import java.util.logging.Level;


public class Summoner {

    private net.rithms.riot.api.endpoints.summoner.dto.Summoner summoner;
    private Platform platform;
    private RiotApi api;
    private RiotApiAsync apiAsync;
    private AsyncRequest requestSummoner;

    public Summoner(Platform platform, String name) {

        RiotApi api = new Config().getApi();
        apiAsync = api.getAsyncApi();
        this.platform = platform;

        try {
            summoner = api.getSummonerByName(platform, name);

            requestSummoner = apiAsync.getSummoner(platform, getSummoner().getId());
            requestSummoner.addListeners(new RequestAdapter() {
                @Override
                public void onRequestSucceeded(AsyncRequest request) {
                    summoner = null;
                    summoner = request.getDto();
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
            Log.d("ERROR :: ", "Can't find summoner");
            return;
        }
    }

    public RiotApiAsync getApiAsync() {
        return apiAsync;
    }

    public Platform getPlatform(){
        return platform;
    }

    public net.rithms.riot.api.endpoints.summoner.dto.Summoner getSummoner() {
        return summoner;
    }

    public long getSummonerId() {
        return summoner.getId();
    }

    public String getName() {
        return summoner.getName();
    }

    public long getSummonerLevel() {
        return summoner.getSummonerLevel();
    }

    public long getProfileIconId() {
        return summoner.getProfileIconId();
    }
}
