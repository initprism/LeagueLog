package initprism.leaguelog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.rithms.riot.api.endpoints.spectator.dto.BannedChampion;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.constant.Platform;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import misc.OnSingleClickListener;
import riot.League;
import riot.Spectator;
import riot.Util;

public class IngameActivity extends AppCompatActivity {

    /*--------CALL BACK METHOD--------*/
    public interface MyCallBack {
        Platform getPlatform();
    }

    public static MyCallBack mCallback;
    /*--------CALL BACK METHOD--------*/


    ImageView buttonClose;
    TextView gameType;
    TextView map;
    TextView gameTime;
    TextView bannedText;
    CircleImageView banned1, banned2, banned3, banned4, banned5, banned6, banned7, banned8, banned9, banned10;
    List<BannedChampion> bannedChampionList;

    RecyclerView inGameRecycler;
    IngameAdapter ingameAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    static Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);
        Intent intent = getIntent();
        Spectator spectator = (Spectator) intent.getSerializableExtra("spectator");
        final Platform platform = (Platform) intent.getSerializableExtra("platform");

        buttonClose = (ImageView) findViewById(R.id.mButtonClose);
        gameType = (TextView) findViewById(R.id.mGameType);
        map = (TextView) findViewById(R.id.mMap);
        gameTime = (TextView) findViewById(R.id.mGameTime);
        bannedText = (TextView) findViewById(R.id.mBannedText);

        banned1 = (CircleImageView) findViewById(R.id.mbanned1);
        banned2 = (CircleImageView) findViewById(R.id.mbanned2);
        banned3 = (CircleImageView) findViewById(R.id.mbanned3);
        banned4 = (CircleImageView) findViewById(R.id.mbanned4);
        banned5 = (CircleImageView) findViewById(R.id.mbanned5);
        banned6 = (CircleImageView) findViewById(R.id.mbanned6);
        banned7 = (CircleImageView) findViewById(R.id.mbanned7);
        banned8 = (CircleImageView) findViewById(R.id.mbanned8);
        banned9 = (CircleImageView) findViewById(R.id.mbanned9);
        banned10 = (CircleImageView) findViewById(R.id.mbanned10);

        bannedChampionList = spectator.getBannedChampions();
        banned1.setImageBitmap(setBannedChampionIcon(0));
        banned2.setImageBitmap(setBannedChampionIcon(1));
        banned3.setImageBitmap(setBannedChampionIcon(2));
        banned4.setImageBitmap(setBannedChampionIcon(3));
        banned5.setImageBitmap(setBannedChampionIcon(4));
        banned6.setImageBitmap(setBannedChampionIcon(5));
        banned7.setImageBitmap(setBannedChampionIcon(6));
        banned8.setImageBitmap(setBannedChampionIcon(7));
        banned9.setImageBitmap(setBannedChampionIcon(8));
        banned10.setImageBitmap(setBannedChampionIcon(9));


        switch (spectator.getCurrentGameQueueConfigId()) {
            case 420:
                gameType.setText("솔로랭크");
                map.setText("소환사의 협곡");
                bannedText.setText("금지한 챔피언");
                break;
            case 430:
                gameType.setText("일반");
                map.setText("소환사의 협곡");
                bannedText.setText("");
                break;
            case 440:
                gameType.setText("자유 5:5 랭크");
                map.setText("소환사의 협곡");
                bannedText.setText("금지한 챔피언");
                break;
            case 450:
                gameType.setText("무작위 총력전");
                map.setText("칼바람 나락");
                bannedText.setText("");
                break;
            default:
                gameType.setText("커스텀 게임");
                map.setText("");
                bannedText.setText("금지한 챔피언");
                break;
        }

        updateGameTime(spectator);


        inGameRecycler = findViewById(R.id.mIngameRecycler);
        inGameRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        inGameRecycler.setLayoutManager(mLayoutManager);

        ArrayList<ArrayList<CurrentGameParticipant>> currentGmaeparticipants = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<CurrentGameParticipant>(Arrays.asList(spectator.getCurrentGameParticipants().get(0), spectator.getCurrentGameParticipants().get(5))),
                        new ArrayList<CurrentGameParticipant>(Arrays.asList(spectator.getCurrentGameParticipants().get(1), spectator.getCurrentGameParticipants().get(6))),
                        new ArrayList<CurrentGameParticipant>(Arrays.asList(spectator.getCurrentGameParticipants().get(2), spectator.getCurrentGameParticipants().get(7))),
                        new ArrayList<CurrentGameParticipant>(Arrays.asList(spectator.getCurrentGameParticipants().get(3), spectator.getCurrentGameParticipants().get(8))),
                        new ArrayList<CurrentGameParticipant>(Arrays.asList(spectator.getCurrentGameParticipants().get(4), spectator.getCurrentGameParticipants().get(9)))));
        ingameAdapter = new IngameAdapter(currentGmaeparticipants);
        inGameRecycler.setAdapter(ingameAdapter);

        buttonClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
        /*--------CALL BACK METHOD--------*/
        mCallback = new MyCallBack() {
            @Override
            public Platform getPlatform() {
                return platform;
            }
        };
    }

    public void updateGameTime(final Spectator spectator) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long curTime = System.currentTimeMillis();
                                long gameStartTime = spectator.getCurrentGameStartTime();
                                long min = (long) ((curTime - gameStartTime) / 60000);
                                long sec = ((curTime - gameStartTime) % 60000) / 1000;
                                gameTime.setText(String.valueOf((min == 0 ? "00:" : (min < 10 ? ("0" + min + ":") : min + ":")) +
                                        (sec == 0 ? "00" : (sec < 10 ? ("0" + sec) : sec))));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    Bitmap setBannedChampionIcon(int num) {
        Iterator<String> championKeys = util.getChampionData().keys();
        while (championKeys.hasNext()) {
            try {
                JSONObject temp = util.getChampionData().getJSONObject(championKeys.next());
                if (temp.get("key").toString().equals(String.valueOf(bannedChampionList.get(num).getChampionId()))) {
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

}
