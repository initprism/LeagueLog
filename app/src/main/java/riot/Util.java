package riot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import misc.JsonReader;

public class Util {

    private String version;
    private JSONObject championData;
    private JSONObject spellData;
    private JSONArray perksData;

    private String profileIconURL;


    public Util(){
        try {
            JSONArray versionData = new JSONArray(JsonReader.readJsonArrayFromUrl("https://ddragon.leagueoflegends.com/api/versions.json"));

            version = versionData.get(0).toString();
            championData = JsonReader.readJsonFromUrl("http://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/champion.json".trim()).getJSONObject("data");
            spellData = JsonReader.readJsonFromUrl("http://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/summoner.json".trim()).getJSONObject("data");
            perksData = new JSONArray(JsonReader.readJsonArrayFromUrl("http://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/runesReforged.json"));
            profileIconURL = "http://ddragon.leagueoflegends.com/cdn/" + version + "/img/profileicon/";
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getVersion(){
        return  version;
    }

    public JSONObject getChampionData() {
        return championData;
    }

    public JSONObject getSpellData() {
        return spellData;
    }

    public JSONArray getPerksData() {
        return perksData;
    }

    public String getProfileIconURL(){
        return profileIconURL;
    }
}

