package initprism.leaguelog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.api.endpoints.match.dto.TeamStats;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import riot.Match;
import riot.Util;

public class MatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static class MatchHolder extends RecyclerView.ViewHolder {
        LinearLayout winFlag;
        TextView winText;
        TextView time;
        CircleImageView champIcon;
        TextView level;
        ImageView summonerRune1;
        ImageView summonerRune2;
        ImageView summonerSpell1;
        ImageView summonerSpell2;
        TextView record;
        TextView kda;
        TextView totalCS;
        TextView kp;
        TextView gameType;
        TextView gameDuration;
        ImageView item0, item1, item2, item3, item4, item5, item6;

        MatchHolder(View view) {
            super(view);
            winFlag = view.findViewById(R.id.mWinFlag);
            winText = view.findViewById(R.id.mWinText);
            time = view.findViewById(R.id.mTime);
            champIcon = view.findViewById(R.id.mChampIcon);
            level = view.findViewById(R.id.mLevel);
            summonerRune1 = view.findViewById(R.id.mSummonerRune1);
            summonerRune2 = view.findViewById(R.id.mSummonerRune2);
            summonerSpell1 = view.findViewById(R.id.mSummonerSpell1);
            summonerSpell2 = view.findViewById(R.id.mSummonerSpell2);
            record = view.findViewById(R.id.mRecord);
            kda = view.findViewById(R.id.mKDA);
            totalCS = view.findViewById(R.id.mTotalCS);
            kp = view.findViewById(R.id.mKP);
            gameType = view.findViewById(R.id.mGameType);
            gameDuration = view.findViewById(R.id.mGameDuration);
            item0 = view.findViewById(R.id.mItem0);
            item1 = view.findViewById(R.id.mItem1);
            item2 = view.findViewById(R.id.mItem2);
            item3 = view.findViewById(R.id.mItem3);
            item4 = view.findViewById(R.id.mItem4);
            item5 = view.findViewById(R.id.mItem5);
            item6 = view.findViewById(R.id.mItem6);

        }
    }

    static Util util = new Util();
    private ArrayList<Match> ArrayList;
    Match match;
    int participantId;
    int teamId;
    ParticipantStats participantStats;
    TeamStats teamStats;

    MatchAdapter(ArrayList<Match> l) {
        this.ArrayList = l;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);

        return new MatchHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MatchHolder matchHolder = (MatchHolder) holder;
        match = ArrayList.get(position);

        for (int i = 0; i < match.getParticipantIdentities().size(); i++) {
            if (match.getParticipantIdentities().get(i).getPlayer().getSummonerName().equals(match.getName())) {
                participantId = match.getParticipantIdentities().get(i).getParticipantId();
                break;
            }
        }
        teamId = match.getParticipants().get(participantId - 1).getTeamId();

        participantStats = match.getParticipants().get(participantId - 1).getStats();
        if (teamId == 100) {
            teamStats = match.getTeamStats().get(0);
        } else if (teamId == 200) {
            teamStats = match.getTeamStats().get(1);
        }


        matchHolder.champIcon.setImageBitmap(getChampionIcon(position));
        matchHolder.level.setText(getChampLevel());
        matchHolder.time.setText(getDateAgo());
        matchHolder.gameDuration.setText(getDuration());
        matchHolder.gameType.setText(getGameType());
        matchHolder.summonerSpell1.setImageBitmap(getSpellIcon1());
        matchHolder.summonerSpell2.setImageBitmap(getSpellIcon2());
        matchHolder.summonerRune1.setImageBitmap(getPerksIcon1());
        matchHolder.summonerRune2.setImageBitmap(getPerksIcon2());
        matchHolder.winFlag.setBackgroundColor(Color.parseColor(getWinColor()));
        matchHolder.winText.setText(getWinText());
        matchHolder.winText.setTextColor(Color.parseColor(getWinColor()));
        matchHolder.record.setText(getRecord());
        matchHolder.kda.setText(getKDA());
        matchHolder.totalCS.setText(getTotalCS());
        matchHolder.kp.setText(getKP());

        matchHolder.item0.setImageBitmap(getItem(participantStats.getItem0()));
        matchHolder.item1.setImageBitmap(getItem(participantStats.getItem1()));
        matchHolder.item2.setImageBitmap(getItem(participantStats.getItem2()));
        matchHolder.item3.setImageBitmap(getItem(participantStats.getItem3()));
        matchHolder.item4.setImageBitmap(getItem(participantStats.getItem4()));
        matchHolder.item5.setImageBitmap(getItem(participantStats.getItem5()));
        matchHolder.item6.setImageBitmap(getItem(participantStats.getItem6()));

    }

    @Override
    public int getItemCount() {
        return ArrayList.size();
    }

    Bitmap getChampionIcon(int pos) {
        Iterator<String> championKeys = util.getChampionData().keys();
        while (championKeys.hasNext()) {
            try {
                JSONObject temp = util.getChampionData().getJSONObject(championKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(ArrayList.get(pos).getMatchReference().getChampion()))) {
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

    String getDateAgo() {
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp2 = new Timestamp(match.getGameCreation() + match.getGameDuration() * 1000);
        long differ = (timestamp1.getTime() - timestamp2.getTime());

        if (differ / (1000 * 60 * 60 * 24) != 0) {
            return String.valueOf(differ / (1000 * 60 * 60 * 24)) + "일 전";
        } else if (differ / 3600000 != 0) {
            return String.valueOf(differ / 3600000) + "시간 전";
        } else {
            return String.valueOf(differ / 60000) + "분 전";
        }
    }

    String getDuration() {

        long time = match.getGameDuration();

        String min = String.valueOf(time / 60);
        if (min.length() == 1) {
            min = "0" + min;
        }
        String sec = String.valueOf(time - Integer.parseInt(min) * 60);
        if (sec.length() == 1) {
            sec = "0" + sec;
        }
        return min + ":" + sec;
    }

    String getGameType() {
        switch (match.getMatchReference().getQueue()) {
            case 420:
                return "솔로랭크";
            case 430:
                return "일반";
            case 440:
                return "자유 5:5 랭크";
            case 450:
                return "무작위 총력전";
            case 1200:
                return "넥서스 공성전";
            default:
                return "커스텀 게임";
        }
    }

    String getWinText() {
        if (participantStats.isWin()) {
            return "승리";
        } else {
            return "패배";
        }
    }

    String getWinColor() {
        if (participantStats.isWin()) {
            return "#3d95e5";
        } else {
            return "#ee5a52";
        }
    }

    Bitmap getSpellIcon1() {
        Iterator<String> spellKeys = util.getSpellData().keys();
        while (spellKeys.hasNext()) {
            try {
                JSONObject temp = util.getSpellData().getJSONObject(spellKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(match.getParticipants().get(participantId - 1).getSpell1Id()))) {
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
                if (temp.get("key").toString().equals(String.valueOf(match.getParticipants().get(participantId - 1).getSpell2Id()))) {
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

    String getChampLevel() {
        return String.valueOf(participantStats.getChampLevel());
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
        double kmavr = (double) km / (match.getGameDuration() / 60);

        return String.valueOf(km + " (" +
                df.format(kmavr) + ")" + " CS");
    }

    String getKP() {

        DecimalFormat df = new DecimalFormat("###.00");

        int kill = 0;

        for (int i = 0; i < 5; i++) {
            if (teamId == 100) {
                kill += match.getParticipants().get(i).getStats().getKills();
            } else if (teamId == 200) {
                kill += match.getParticipants().get(i + 5).getStats().getKills();
            }
        }
        double kp = (double) (participantStats.getKills() + participantStats.getAssists()) / kill * 100;

        return String.valueOf(df.format(kp) + " KP");
    }

    Bitmap getItem(int num)  {
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
}
