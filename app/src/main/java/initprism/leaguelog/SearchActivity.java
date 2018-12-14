package initprism.leaguelog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import net.rithms.riot.constant.Platform;

import java.util.ArrayList;
import java.util.Collections;

import db.HistorySummonerDTO;
import db.SummonerDAO;
import misc.OnSingleClickListener;
import riot.League;
import riot.Summoner;

public class SearchActivity extends AppCompatActivity {
    /*--------CALL BACK METHOD--------*/
    public interface MyCallBack {
        void refreshSearchActivity();
    }

    public static MyCallBack mCallback;
    /*--------CALL BACK METHOD--------*/

    Platform platform;

    ImageView buttonClose;
    EditText summonerSearch;

    SummonerDAO summonerDAO;

    OmegaRecyclerView omegaRecyclerView;
    OmegaRecyclerView.LayoutManager mLayoutManager;

    HistoryAdapter myAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        summonerDAO = new SummonerDAO(this);

        Intent intent = getIntent();
        platform = (Platform) intent.getSerializableExtra("platform");

        buttonClose = (ImageView) findViewById(R.id.mButtonClose);
        summonerSearch = (EditText) findViewById(R.id.mSummonerSearch);

        omegaRecyclerView = findViewById(R.id.mHistoryRecycler);
        mLayoutManager = new LinearLayoutManager(this);
        omegaRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<HistorySummonerDTO> HistorySummoners = summonerDAO.getAllHistorySummoners();
        Collections.reverse(HistorySummoners);
        myAdapter = new HistoryAdapter(HistorySummoners);
        omegaRecyclerView.setAdapter(myAdapter);

        //touch icon
        summonerSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (summonerSearch.getRight() - summonerSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (summonerSearch.getText().toString().length() == 0) {
                            Toast.makeText(getApplicationContext(), "소환사 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            summonerSearch.clearFocus();
                            AsyncTaskSearch searchTask = new AsyncTaskSearch();
                            searchTask.execute(summonerSearch.getText().toString());
                            return true;
                        }

                    }
                }
                return false;
            }
        });

        //Enter key Action
        summonerSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (summonerSearch.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "소환사 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        summonerSearch.clearFocus();
                        AsyncTaskSearch searchTask = new AsyncTaskSearch();
                        searchTask.execute(summonerSearch.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });

        buttonClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });

        /*--------CALL BACK METHOD--------*/
        mCallback = new MyCallBack() {
            @Override
            public void refreshSearchActivity() {
                ArrayList<HistorySummonerDTO> HistorySummoners = summonerDAO.getAllHistorySummoners();
                Collections.reverse(HistorySummoners);
                myAdapter = new HistoryAdapter(HistorySummoners);
                omegaRecyclerView.setAdapter(myAdapter);
            }
        };
        /*--------CALL BACK METHOD--------*/
    }

    public class AsyncTaskSearch extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0].replaceAll("\\s+", "").toLowerCase();
            return name;
        }

        @Override
        protected void onPostExecute(String name) {
            super.onPostExecute(name);
            Summoner summoner = new Summoner(platform, name);

            if (summoner.getSummoner() == null) {
                Toast.makeText(getApplicationContext(), "소환사 이름을 확인해 주세요", Toast.LENGTH_SHORT).show();
                return;
            } else {
                League league = new League(summoner);

                //HISTORY DB INSERT
                if (summonerDAO.getHistorySummoner(summoner.getName(), platform.getName()) != null) {

                    if(summonerDAO.getBookmarkSummoner(summoner.getName(), platform.getName()) != null)
                        summonerDAO.replaceHistorySummoner(new HistorySummonerDTO(
                                summoner.getPlatform().getName(),
                                summoner.getName(),
                                league.getSoloRank(),
                                league.getSoloRankInfo(),
                                String.valueOf(summoner.getProfileIconId()),
                                "y"
                        ));
                    else
                        summonerDAO.replaceHistorySummoner(new HistorySummonerDTO(
                                summoner.getPlatform().getName(),
                                summoner.getName(),
                                league.getSoloRank(),
                                league.getSoloRankInfo(),
                                String.valueOf(summoner.getProfileIconId()),
                                "n"
                        ));


                    mCallback.refreshSearchActivity();
                } else {

                    if(summonerDAO.getBookmarkSummoner(summoner.getName(), platform.getName()) != null)
                        summonerDAO.addHistorySummoner(new HistorySummonerDTO(
                                summoner.getPlatform().getName(),
                                summoner.getName(),
                                league.getSoloRank(),
                                league.getSoloRankInfo(),
                                String.valueOf(summoner.getProfileIconId()),
                                "y"
                        ));
                    else
                        summonerDAO.addHistorySummoner(new HistorySummonerDTO(
                                summoner.getPlatform().getName(),
                                summoner.getName(),
                                league.getSoloRank(),
                                league.getSoloRankInfo(),
                                String.valueOf(summoner.getProfileIconId()),
                                "n"
                        ));

                    mCallback.refreshSearchActivity();
                }

            }
        }
    }
}
