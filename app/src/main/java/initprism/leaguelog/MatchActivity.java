package initprism.leaguelog;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.rithms.riot.constant.Platform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import misc.MatchBeen;
import misc.OnSingleClickListener;
import riot.Match;

public class MatchActivity extends AppCompatActivity {

    LinearLayout layout;
    ImageView buttonClose;
    TextView winText;
    TextView gameType;
    TextView topText;
    TextView bottomText;
    TextView record1;
    TextView record2;



    RecyclerView recycler1;
    RecyclerView recycler2;
    GameAdapter gameAdapter1;
    GameAdapter gameAdapter2;

    Match match;
    int participantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Intent intent = getIntent();
        match = (Match) intent.getSerializableExtra("match");

        layout = (LinearLayout) findViewById(R.id.mLayout);
        buttonClose = (ImageView) findViewById(R.id.mButtonClose);
        winText = (TextView) findViewById(R.id.mWinText);
        gameType = (TextView) findViewById(R.id.mGameType);
        topText = (TextView) findViewById(R.id.mTopText);
        bottomText = (TextView) findViewById(R.id.mBottomText);
        record1 = (TextView) findViewById(R.id.mRecord1);
        record2 = (TextView) findViewById(R.id.mRecord2);

        for (int i = 0; i < match.getParticipantIdentities().size(); i++) {
            if (match.getParticipantIdentities().get(i).getPlayer().getSummonerName().equals(match.getName())) {
                participantId = match.getParticipantIdentities().get(i).getParticipantId();
                break;
            }
        }

        if(match.getParticipants().get(participantId-1).getStats().isWin()){
            layout.setBackgroundColor(Color.parseColor("#1a78ae"));
            winText.setText("승리");
        }else{
            layout.setBackgroundColor(Color.parseColor("#ee5a52"));
            winText.setText("패배");
        }


        gameType.setText(getGameType());
        record1.setText(getRecord1());
        record2.setText(getRecord2());

        if(match.getParticipants().get(0).getStats().isWin()){
            topText.setText("승리");
            topText.setTextColor(Color.parseColor("#1a78ae"));
            bottomText.setText("패배");
            bottomText.setTextColor(Color.parseColor("#ee5a52"));
        }else{
            topText.setText("패배");
            topText.setTextColor(Color.parseColor("#ee5a52"));
            bottomText.setText("승리");
            bottomText.setTextColor(Color.parseColor("#1a78ae"));
        }





        Platform platform;
        switch (match.getMatchReference().getPlatformId().toLowerCase()) {
            case "kr": platform = Platform.KR; break;
            case "jp": platform = Platform.JP; break;
            case "eune": platform = Platform.EUNE; break;
            case "euw": platform = Platform.EUW; break;
            case "lan": platform = Platform.LAN; break;
            case "ru": platform = Platform.RU; break;
            case "na": platform = Platform.NA; break;
            case "br": platform = Platform.BR; break;
            case "tr": platform = Platform.TR; break;
            case "oce": platform = Platform.OCE; break;
            default: platform = Platform.KR; break;
        }

        long topDamage = 0;
        for(int i = 0; i < match.getParticipants().size(); i++){
            if(match.getParticipants().get(i).getStats().getTotalDamageDealtToChampions() > topDamage)
                topDamage = match.getParticipants().get(i).getStats().getTotalDamageDealtToChampions();
        }

        recycler1 = (RecyclerView) findViewById(R.id.mGameRecycler1);
        recycler1.setHasFixedSize(true);
        recycler1.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<MatchBeen> s1 = new ArrayList<>(
                Arrays.asList(new MatchBeen(match.getParticipants().get(0), match.getParticipantIdentities().get(0), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(1), match.getParticipantIdentities().get(1), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(2), match.getParticipantIdentities().get(2), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(3), match.getParticipantIdentities().get(3), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(4), match.getParticipantIdentities().get(4), platform, match.getGameDuration(), topDamage)));
        gameAdapter1 = new GameAdapter(s1);
        recycler1.setAdapter(gameAdapter1);

        recycler2 = (RecyclerView) findViewById(R.id.mGameRecycler2);
        recycler2.setHasFixedSize(true);
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<MatchBeen> s2 = new ArrayList<>(
                Arrays.asList(new MatchBeen(match.getParticipants().get(5), match.getParticipantIdentities().get(5), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(6), match.getParticipantIdentities().get(6), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(7), match.getParticipantIdentities().get(7), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(8), match.getParticipantIdentities().get(8), platform, match.getGameDuration(), topDamage),
                              new MatchBeen(match.getParticipants().get(9), match.getParticipantIdentities().get(9), platform, match.getGameDuration(), topDamage)));
        gameAdapter2 = new GameAdapter(s2);
        recycler2.setAdapter(gameAdapter2);


        buttonClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
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
        String gameType;

        SimpleDateFormat  formatter1 = new SimpleDateFormat("yyyy.MM.dd");
        String createData = formatter1.format(match.getGameCreation());

        switch (match.getMatchReference().getQueue()) {
            case 420:
                gameType = "솔로랭크";
                break;
            case 430:
                gameType = "일반";
                break;
            case 440:
                gameType = "자유 5:5 랭크";
                break;
            case 450:
                gameType = "무작위 총력전";
                break;
            case 1200:
                gameType = "넥서스 공성전";
                break;
            default:
                gameType = "커스텀 게임";
        }
        return gameType + " | " + createData + " | " + getDuration();
    }

    String getRecord1(){
        int kill = 0;
        int death = 0;
        for(int i = 0; i < 5; i++){
            kill += match.getParticipants().get(i).getStats().getKills();
            death += match.getParticipants().get(i).getStats().getDeaths();
        }
        return String.valueOf(kill + " / " + death);
    }

    String getRecord2(){
        int kill = 0;
        int death = 0;
        for(int i = 5; i < 10; i++){
            kill += match.getParticipants().get(i).getStats().getKills();
            death += match.getParticipants().get(i).getStats().getDeaths();
        }
        return String.valueOf(kill + " / " + death);
    }

}
