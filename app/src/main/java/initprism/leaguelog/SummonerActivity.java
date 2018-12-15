package initprism.leaguelog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.rithms.riot.constant.Platform;

import java.net.URL;

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
    ImageView actvieGameFlag;
    TextView summonerLevel;
    TextView summonerId;
    FancyButton buttonIngame;

    static Util util = new Util();
    Platform platform;

    SummonerDAO summonerDAO;
    Summoner summoner;
    League league;

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

        buttonBack = (ImageView) findViewById(R.id.mButtonBack);
        bookmark = (ImageView) findViewById(R.id.mBookmark);
        summonerIcon = (CircleImageView) findViewById(R.id.mSummonerIcon);
        actvieGameFlag = (ImageView) findViewById(R.id.activeGameFlag);
        summonerLevel = (TextView) findViewById(R.id.mSummonerLevel);
        summonerId = (TextView) findViewById(R.id.mSummonerId);
        buttonIngame = (FancyButton) findViewById(R.id.mButtonIngame);

        // task
        AsyncTaskSummoner taskSummoner = new AsyncTaskSummoner();
        taskSummoner.execute(name);

        // bookmark button
        summonerDAO = new SummonerDAO(this);

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
            Spectator spectator = new Spectator(summoner);

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


        }
    }
}
