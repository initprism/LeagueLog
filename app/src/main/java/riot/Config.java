package riot;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;

public class Config {

    private ApiConfig config;
    private RiotApi api;
    private String key = "RGAPI-241b7347-41af-4cbb-ba28-aa2ba7ff8a76";

    public Config() {
        config = new ApiConfig().setKey(key);
        api = new RiotApi(config);
    }

    public RiotApi getApi(){
        return api;
    }
}
