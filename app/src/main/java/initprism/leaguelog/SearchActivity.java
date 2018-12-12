package initprism.leaguelog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.rithms.riot.constant.Platform;

import db.HistorySummonerDTO;
import db.SummonerDAO;
import misc.OnSingleClickListener;
import riot.League;
import riot.Summoner;

public class SearchActivity extends AppCompatActivity {
    Platform platform;

    ImageView buttonClose;
    EditText summonerSearch;

    SummonerDAO summonerDAO;

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
                summonerDAO.addHistorySummoner(new HistorySummonerDTO(
                        summoner.getPlatform().getName(),
                        summoner.getName(),
                        league.getSoloRank(),
                        league.getSoloRankInfo(),
                        String.valueOf(summoner.getProfileIconId())
                ));
                Toast.makeText(getApplicationContext(), summoner.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
