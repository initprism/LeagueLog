package initprism.leaguelog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import riot.League;
import riot.LeagueIngame;
import riot.Summoner;
import riot.Util;

public class IngameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static class IngameHolder extends RecyclerView.ViewHolder {
        CircleImageView summonerIcon1;
        ImageView summonerRune1_1;
        ImageView summonerRune1_2;
        ImageView summonerSpell1_1;
        ImageView summonerSpell1_2;
        ImageView summonerTierIcon1;
        TextView summonerId1;
        TextView summonerTier1;

        CircleImageView summonerIcon2;
        ImageView summonerRune2_1;
        ImageView summonerRune2_2;
        ImageView summonerSpell2_1;
        ImageView summonerSpell2_2;
        ImageView summonerTierIcon2;
        TextView summonerId2;
        TextView summonerTier2;

        IngameHolder(View view) {
            super(view);

            summonerIcon1 = view.findViewById(R.id.mSummonerIcon1);
            summonerRune1_1 = view.findViewById(R.id.mSummonerRune1_1);
            summonerRune1_2 = view.findViewById(R.id.mSummonerRune1_2);
            summonerSpell1_1 = view.findViewById(R.id.mSummonerSpell1_1);
            summonerSpell1_2 = view.findViewById(R.id.mSummonerSpell1_2);
            summonerTierIcon1 = view.findViewById(R.id.mSummonerTierIcon1);
            summonerId1 = view.findViewById(R.id.mSummonerId1);
            summonerTier1 = view.findViewById(R.id.mSummonerTier1);

            summonerIcon2 = view.findViewById(R.id.mSummonerIcon2);
            summonerRune2_1 = view.findViewById(R.id.mSummonerRune2_1);
            summonerRune2_2 = view.findViewById(R.id.mSummonerRune2_2);
            summonerSpell2_1 = view.findViewById(R.id.mSummonerSpell2_1);
            summonerSpell2_2 = view.findViewById(R.id.mSummonerSpell2_2);
            summonerTierIcon2 = view.findViewById(R.id.mSummonerTierIcon2);
            summonerId2 = view.findViewById(R.id.mSummonerId2);
            summonerTier2 = view.findViewById(R.id.mSummonerTier2);
        }
    }

    static Util util = new Util();
    private ArrayList<ArrayList<CurrentGameParticipant>> ArrayList;
    IngameActivity.MyCallBack callBack;

    IngameAdapter(ArrayList<ArrayList<CurrentGameParticipant>> l) {
        this.ArrayList = l;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingame, parent, false);

        return new IngameHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final IngameHolder ingameHolder = (IngameHolder) holder;
        callBack = IngameActivity.mCallback;
        LeagueIngame user1 = new LeagueIngame(callBack.getPlatform(), ArrayList.get(position).get(0).getSummonerId());
        LeagueIngame user2 = new LeagueIngame(callBack.getPlatform(), ArrayList.get(position).get(1).getSummonerId());


        ingameHolder.summonerTier1.setText(user1.getSoloRankInfo());
        ingameHolder.summonerTier2.setText(user2.getSoloRankInfo());

        switch (user1.getSoloRank().toLowerCase()) {
            case "iron": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.iron); break;
            case "bronze": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.bronze); break;
            case "silver": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.silver); break;
            case "gold": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.gold); break;
            case "platinum": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.platinum); break;
            case "diamond": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.diamond); break;
            case "master": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.master); break;
            case "grandmaster": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.grandmaster); break;
            case "challenger": ingameHolder.summonerTierIcon1.setImageResource(R.drawable.challenger); break;
            default: ingameHolder.summonerTierIcon1.setImageResource(R.drawable.provisional); break;
        }

        switch (user2.getSoloRank().toLowerCase()) {
            case "iron": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.iron); break;
            case "bronze": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.bronze); break;
            case "silver": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.silver); break;
            case "gold": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.gold); break;
            case "platinum": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.platinum); break;
            case "diamond": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.diamond); break;
            case "master": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.master); break;
            case "grandmaster": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.grandmaster); break;
            case "challenger": ingameHolder.summonerTierIcon2.setImageResource(R.drawable.challenger); break;
            default: ingameHolder.summonerTierIcon2.setImageResource(R.drawable.provisional); break;
        }

        ingameHolder.summonerId1.setText(ArrayList.get(position).get(0).getSummonerName());
        ingameHolder.summonerId2.setText(ArrayList.get(position).get(1).getSummonerName());

        ingameHolder.summonerIcon1.setImageBitmap(setChampionIcon(position, 0));
        ingameHolder.summonerIcon2.setImageBitmap(setChampionIcon(position, 1));

        ingameHolder.summonerSpell1_1.setImageBitmap(setSpellIcon1(position, 0));
        ingameHolder.summonerSpell1_2.setImageBitmap(setSpellIcon2(position, 0));
        ingameHolder.summonerSpell2_1.setImageBitmap(setSpellIcon1(position, 1));
        ingameHolder.summonerSpell2_2.setImageBitmap(setSpellIcon2(position, 1));

        ingameHolder.summonerRune1_1.setImageBitmap(setPerksIcon1(position, 0));
        ingameHolder.summonerRune1_2.setImageBitmap(setPerksIcon2(position, 0));
        ingameHolder.summonerRune2_1.setImageBitmap(setPerksIcon1(position, 1));
        ingameHolder.summonerRune2_2.setImageBitmap(setPerksIcon2(position, 1));


    }

    @Override
    public int getItemCount() {
        return ArrayList.size();
    }


    Bitmap setChampionIcon(int pos, int num) {
        Iterator<String> championKeys = util.getChampionData().keys();
        while (championKeys.hasNext()) {
            try {
                JSONObject temp = util.getChampionData().getJSONObject(championKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(ArrayList.get(pos).get(num).getChampionId()))) {
                    URL url = new URL(util.getChampIconURL() + temp.getJSONObject("image").get("full"));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    Bitmap setSpellIcon1(int pos, int num) {
        Iterator<String> spellKeys = util.getSpellData().keys();
        while (spellKeys.hasNext()) {
            try {
                JSONObject temp = util.getSpellData().getJSONObject(spellKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(ArrayList.get(pos).get(num).getSpell1Id()))) {
                    URL url = new URL(util.getSpellIconURL() + temp.getJSONObject("image").get("full"));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    Bitmap setSpellIcon2(int pos, int num) {
        Iterator<String> spellKeys = util.getSpellData().keys();

        try {
            while (spellKeys.hasNext()) {
                JSONObject temp = util.getSpellData().getJSONObject(spellKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(ArrayList.get(pos).get(num).getSpell2Id()))) {
                    URL url = new URL(util.getSpellIconURL() + temp.getJSONObject("image").get("full"));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    return bitmap;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Bitmap setPerksIcon1(int pos, int num) {

        try {
            for (int j = 0; j < util.getPerksData().length(); j++) {
                JSONObject temp1 = util.getPerksData().getJSONObject(j);

                if (String.valueOf(ArrayList.get(pos).get(num).getPerks().getPerkStyle()).equals(String.valueOf(temp1.get("id")))) {
                    JSONArray temp2 = temp1.getJSONArray("slots");
                    for (int k = 0; k < temp2.length(); k++) {
                        JSONObject temp3 = temp2.getJSONObject(k);
                        JSONArray temp4 = temp3.getJSONArray("runes");

                        for (int l = 0; l < temp4.length(); l++) {
                            if (String.valueOf(ArrayList.get(pos).get(num).getPerks().getPerkIds().get(0)).equals(String.valueOf(temp4.getJSONObject(l).get("id")))) {
                                URL url = new URL(util.getPerksIconURL() + temp4.getJSONObject(l).get("icon"));
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                                return bitmap;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Bitmap setPerksIcon2(int pos, int num) {
        try {
            for (int j = 0; j < util.getPerksData().length(); j++) {
                JSONObject temp1 = util.getPerksData().getJSONObject(j);

                if (String.valueOf(ArrayList.get(pos).get(num).getPerks().getPerkSubStyle()).equals(String.valueOf(temp1.get("id")))) {
                    URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/" + temp1.get("icon"));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    return bitmap;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}

