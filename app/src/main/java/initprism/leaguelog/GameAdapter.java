package initprism.leaguelog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerTextView;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;

import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.ParticipantIdentity;
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.constant.Platform;

import org.json.JSONObject;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import db.BookmarkSummonerDTO;
import db.HistorySummonerDTO;
import db.SummonerDAO;
import de.hdodenhof.circleimageview.CircleImageView;
import misc.MatchBeen;
import misc.OnSingleClickListener;
import riot.LeagueIngame;
import riot.Summoner;
import riot.Util;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class GameHolder extends RecyclerView.ViewHolder {
        CircleImageView champIcon;
        TextView level;
        ImageView summonerRune1;
        ImageView summonerRune2;
        ImageView summonerSpell1;
        ImageView summonerSpell2;
        ImageView summonerTierIcon;
        TextView summonerId;
        TextView record;
        TextView kda;
        ImageView item0, item1, item2, item3, item4, item5;
        CircleImageView item6;
        TextView totalCS;
        TextRoundCornerProgressBar textRoundCornerProgressBar;

        GameHolder(View view) {
            super(view);

            champIcon = view.findViewById(R.id.mChampIcon);
            level = view.findViewById(R.id.mLevel);
            summonerRune1 = view.findViewById(R.id.mSummonerRune1);
            summonerRune2 = view.findViewById(R.id.mSummonerRune2);
            summonerSpell1 = view.findViewById(R.id.mSummonerSpell1);
            summonerSpell2 = view.findViewById(R.id.mSummonerSpell2);
            summonerTierIcon = view.findViewById(R.id.mSummonerTierIcon);
            summonerId = view.findViewById(R.id.mSummonerId);
            record = view.findViewById(R.id.mRecord);
            kda = view.findViewById(R.id.mKDA);
            item0 = view.findViewById(R.id.mItem0);
            item1 = view.findViewById(R.id.mItem1);
            item2 = view.findViewById(R.id.mItem2);
            item3 = view.findViewById(R.id.mItem3);
            item4 = view.findViewById(R.id.mItem4);
            item5 = view.findViewById(R.id.mItem5);
            item6 = view.findViewById(R.id.mItem6);
            totalCS = view.findViewById(R.id.mTotalCS);
            textRoundCornerProgressBar = view.findViewById(R.id.mDamProgress);

        }
    }

    static Util util = new Util();

    private ArrayList<MatchBeen> ArrayList;
    Participant participant;
    ParticipantIdentity participantIdentity;
    ParticipantStats participantStats;
    Platform platform;
    long duration;
    GameAdapter(ArrayList<MatchBeen> l) {
        this.ArrayList = l;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);

        return new GameHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GameHolder gameHolder = (GameHolder) holder;
        participant = ArrayList.get(position).getParticipant();
        participantIdentity = ArrayList.get(position).getParticipantIdentity();
        participantStats = participant.getStats();
        duration =ArrayList.get(position).getDuration();
        platform = ArrayList.get(position).getPlatform();

        gameHolder.champIcon.setImageBitmap(getChampionIcon());
        gameHolder.level.setText(getChampLevel());
        gameHolder.summonerRune1.setImageBitmap(getPerksIcon1());
        gameHolder.summonerRune2.setImageBitmap(getPerksIcon2());
        gameHolder.summonerSpell1.setImageBitmap(getSpellIcon1());
        gameHolder.summonerSpell2.setImageBitmap(getSpellIcon2());
        gameHolder.summonerTierIcon.setImageResource(getTierIcon());
        gameHolder.summonerId.setText(participantIdentity.getPlayer().getSummonerName());
        gameHolder.record.setText(getRecord());
        gameHolder.kda.setText(getKDA());
        gameHolder.totalCS.setText(getTotalCS());
        gameHolder.item0.setImageBitmap(getItem(participantStats.getItem0()));
        gameHolder.item1.setImageBitmap(getItem(participantStats.getItem1()));
        gameHolder.item2.setImageBitmap(getItem(participantStats.getItem2()));
        gameHolder.item3.setImageBitmap(getItem(participantStats.getItem3()));
        gameHolder.item4.setImageBitmap(getItem(participantStats.getItem4()));
        gameHolder.item5.setImageBitmap(getItem(participantStats.getItem5()));
        gameHolder.item6.setImageBitmap(getItem(participantStats.getItem6()));
        gameHolder.textRoundCornerProgressBar.setMax(ArrayList.get(position).getTopDamage());
        gameHolder.textRoundCornerProgressBar.setProgress(getDamage());
        gameHolder.textRoundCornerProgressBar.setProgressText(String.valueOf(getDamage()));

    }

    @Override
    public int getItemCount() {
        return ArrayList.size();
    }

    Bitmap getChampionIcon() {

        Iterator<String> championKeys = util.getChampionData().keys();
        while (championKeys.hasNext()) {
            try {
                JSONObject temp = util.getChampionData().getJSONObject(championKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(participant.getChampionId()))) {
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

    String getChampLevel() {
        return String.valueOf(participantStats.getChampLevel());
    }

    Bitmap getSpellIcon1() {
        Iterator<String> spellKeys = util.getSpellData().keys();
        while (spellKeys.hasNext()) {
            try {
                JSONObject temp = util.getSpellData().getJSONObject(spellKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(participant.getSpell1Id()))) {
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

    Bitmap getSpellIcon2() {
        Iterator<String> spellKeys = util.getSpellData().keys();

        try {
            while (spellKeys.hasNext()) {
                JSONObject temp = util.getSpellData().getJSONObject(spellKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(participant.getSpell2Id()))) {
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

    Bitmap getPerksIcon1() {
        try {
            for (int j = 0; j < util.getPerksData().length(); j++) {
                JSONObject temp1 = util.getPerksData().getJSONObject(j);

                if (String.valueOf(participantStats.getPerkPrimaryStyle()).equals(String.valueOf(temp1.get("id")))) {
                    URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/" + temp1.get("icon"));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    return bitmap;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    Bitmap getPerksIcon2() {
        try {
            for (int j = 0; j < util.getPerksData().length(); j++) {
                JSONObject temp1 = util.getPerksData().getJSONObject(j);

                if (String.valueOf(participantStats.getPerkSubStyle()).equals(String.valueOf(temp1.get("id")))) {
                    URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/" + temp1.get("icon"));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    return bitmap;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    int getTierIcon() {
        LeagueIngame leagueIngame = new LeagueIngame(platform, participantIdentity.getPlayer().getSummonerId());
        switch (leagueIngame.getSoloRank().toLowerCase()) {
            case "iron": return R.drawable.iron;
            case "bronze": return R.drawable.bronze;
            case "silver": return R.drawable.silver;
            case "gold": return R.drawable.gold;
            case "platinum": return R.drawable.platinum;
            case "diamond": return R.drawable.diamond;
            case "master": return R.drawable.master;
            case "grandmaster": return R.drawable.grandmaster;
            case "challenger": return R.drawable.challenger;
            default: return R.drawable.provisional;
        }
    }

    String getRecord() {
        return String.valueOf(participantStats.getKills() +
                " / " + participantStats.getDeaths() +
                " / " + participantStats.getAssists());
    }

    String getKDA() {
        DecimalFormat df = new DecimalFormat("###.00");
        double kda = (double) (participantStats.getKills() + participantStats.getAssists()) / participantStats.getDeaths();

        return String.valueOf(df.format(kda) + " KDA");
    }

    String getTotalCS() {

        DecimalFormat df = new DecimalFormat("###.00");

        int km = participantStats.getTotalMinionsKilled() + participantStats.getNeutralMinionsKilledEnemyJungle() +
                participantStats.getNeutralMinionsKilledTeamJungle() + participantStats.getWardsKilled();
        double kmavr = (double) km / (duration / 60);

        return String.valueOf(km + " (" +
                df.format(kmavr) + ")" + " CS");
    }

    Bitmap getItem(int num) {
        try {
            URL url = new URL(util.getItemIconURL() + num + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            return bitmap;
        } catch (Exception e) {
            URL url = null;
            try {
                url = new URL(util.getItemIconURL() + "3637" + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                return bitmap;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    long getDamage(){
        return participantStats.getTotalDamageDealtToChampions();
    }

}
