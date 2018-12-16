package initprism.leaguelog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.rithms.riot.constant.Platform;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import db.BookmarkSummonerDTO;
import db.HistorySummonerDTO;
import db.SummonerDAO;
import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;
import misc.OnSingleClickListener;
import riot.League;
import riot.Spectator;
import riot.Summoner;
import riot.Util;


public class SummonerActivity extends AppCompatActivity {

    ImageView buttonBack;
    ImageView bookmark;
    CircleImageView summonerIcon;
    TextView summonerLevel;
    TextView summonerId;
    FancyButton buttonIngame;

    RecyclerView summonerRecycler;
    TierAdapter tierAdapter;

    static Util util = new Util();
    Platform platform;

    SummonerDAO summonerDAO;
    Summoner summoner;
    League league;
    Spectator spectator;

    SearchActivity.MyCallBack callBackHistory;
    MainActivity.MyCallBack callBackBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);

        // get data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String platformName = (String) bundle.getSerializable("platform");
        final String name = (String) bundle.getSerializable("name");

        switch (platformName) {
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
        }

        summonerDAO = new SummonerDAO(this);

        buttonBack = (ImageView) findViewById(R.id.mButtonBack);
        bookmark = (ImageView) findViewById(R.id.mBookmark);
        summonerIcon = (CircleImageView) findViewById(R.id.mSummonerIcon);
        summonerLevel = (TextView) findViewById(R.id.mSummonerLevel);
        summonerId = (TextView) findViewById(R.id.mSummonerId);
        buttonIngame = (FancyButton) findViewById(R.id.mButtonIngame);

        summonerRecycler = (RecyclerView) findViewById(R.id.mSummonerTier_recycler);
        summonerRecycler.setHasFixedSize(true);
        summonerRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));





        // task
        AsyncTaskSummoner taskSummoner = new AsyncTaskSummoner();
        taskSummoner.execute(name);

        // bookmark button

        if (summonerDAO.getBookmarkSummoner(name, platformName) != null)
            bookmark.setImageResource(R.drawable.favorite_black);
        else
            bookmark.setImageResource(R.drawable.favorite_border_black);

        bookmark.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                BookmarkSummonerDTO bookmarkSummoner = summonerDAO.getBookmarkSummoner(name, platformName);

                if (bookmarkSummoner != null) {
                    bookmark.setImageResource(R.drawable.favorite_border_black);

                    summonerDAO.updateHistorySummoner(new HistorySummonerDTO(platformName, name,
                            bookmarkSummoner.getTier(), bookmarkSummoner.getTierInfo(), bookmarkSummoner.getLevel(),
                            bookmarkSummoner.getProfileIcon(), "n"));

                    summonerDAO.deleteBookmarkSummoner(new BookmarkSummonerDTO(platformName, name, "", "", "", ""));

                    callBackBookmark = MainActivity.mCallback;
                    callBackBookmark.refreshBookmark();
                    callBackHistory = SearchActivity.mCallback;
                    callBackHistory.refreshSearchActivity();
                } else {
                    bookmark.setImageResource(R.drawable.favorite_black);
                    summonerDAO.updateHistorySummoner(new HistorySummonerDTO(platformName, name,
                            league.getSoloRank(), league.getSoloRankInfo(), String.valueOf(summoner.getSummonerLevel()),
                            String.valueOf(summoner.getProfileIconId()), "y"));

                    summonerDAO.addBookmarkSummoner(new BookmarkSummonerDTO(platformName, name,
                            league.getSoloRank(), league.getSoloRankInfo(), String.valueOf(summoner.getSummonerLevel()),
                            String.valueOf(summoner.getProfileIconId())));

                    callBackBookmark = MainActivity.mCallback;
                    callBackBookmark.refreshBookmark();
                    callBackHistory = SearchActivity.mCallback;
                    callBackHistory.refreshSearchActivity();
                }

            }
        });

        buttonIngame.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(spectator.isInGame() != -1){
                    Intent intent = new Intent(SummonerActivity.this, IngameActivity.class);
                    intent.putExtra("spectator", spectator);
                    intent.putExtra("platform", platform);
                    startActivity(intent);
                }else{
                    showIngameAlert();
                }

            }
        });

        // finish button
        buttonBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
    }

    public class AsyncTaskSummoner extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0].replaceAll("\\s+", "").toLowerCase();
            return name;
        }

        @Override
        protected void onPostExecute(String name) {
            super.onPostExecute(name);

            summoner = new Summoner(platform, name);
            league = new League(summoner);
            spectator = new Spectator(summoner);

            summonerLevel.setText("Lv." + String.valueOf(summoner.getSummonerLevel()));
            summonerId.setText(summoner.getName());
            try {
                URL url = new URL(util.getProfileIconURL() + summoner.getProfileIconId() + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                summonerIcon.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                summonerIcon.setImageResource(R.drawable.testicon);
            }
            if(spectator.isInGame() != -1){
                buttonIngame.setBackgroundColor(Color.parseColor("#008b8b"));
            }

            ArrayList<TierItem> tierItems = new ArrayList<>();
            tierItems.add(new TierItem(league.getSoloRank(), "솔랭",
                    league.getSoloRankInfo(), String.valueOf(league.getLeagueSoloPoints()),
                    league.getSoloWins(), league.getSoloLosses(), league.getSoloWinningAverage()));

            tierItems.add(new TierItem(league.getTeamRank(), "자유 5:5 랭크",
                    league.getTeamRankInfo(), String.valueOf(league.getLeagueTeamPoints()),
                    league.getTeamWins(), league.getTeamLosses(), league.getTeamWinningAverage()));

            tierAdapter = new TierAdapter(tierItems);
            summonerRecycler.setAdapter(tierAdapter);



        }
    }

    void showIngameAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("'"+ summoner.getName() + "'님은 현재 게임중이 아닙니다");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        builder.show();
    }

}
