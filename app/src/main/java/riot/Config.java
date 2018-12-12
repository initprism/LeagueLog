package riot;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;

public class Config {

    private ApiConfig config;
    private RiotApi api;
    private String key = "RGAPI-f59fd6f4-a246-4825-af20-c4b0288d23f9";

    public Config() {
        config = new ApiConfig().setKey(key);
        api = new RiotApi(config);
    }

    public RiotApi getApi(){
        return api;
    }
}
