package riot;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;

public class Config {

    private ApiConfig config;
    private RiotApi api;
    private String key = "RGAPI-cad605cb-1e09-4dde-9383-f366658a5b2f";

    public Config() {
        config = new ApiConfig().setKey(key);
        api = new RiotApi(config);
    }

    public RiotApi getApi(){
        return api;
    }
}
