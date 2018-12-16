package riot;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;

public class Config {

    private ApiConfig config;
    private RiotApi api;
    private String key = "RGAPI-479d6e06-3f3e-4fbc-9d1d-541298886d5c";

    public Config() {
        config = new ApiConfig().setKey(key);
        api = new RiotApi(config);
    }

    public RiotApi getApi(){
        return api;
    }
}
