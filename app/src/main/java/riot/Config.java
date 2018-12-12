package riot;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;

public class Config {

    private ApiConfig config;
    private RiotApi api;
    private String key = "RGAPI-ce46922f-2084-4697-a686-bb814f83e0a2";

    public Config() {
        config = new ApiConfig().setKey(key);
        api = new RiotApi(config);
    }

    public RiotApi getApi(){
        return api;
    }
}
