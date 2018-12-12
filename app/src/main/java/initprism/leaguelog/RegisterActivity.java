package initprism.leaguelog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cielyang.android.clearableedittext.ClearableEditText;

import net.rithms.riot.constant.Platform;

import db.MySummonerDTO;
import db.SummonerDAO;
import riot.League;
import riot.Summoner;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView platformTextView;
    ImageView closeTab;
    ClearableEditText registerEditText;
    Button completeButton;
    Platform platform;

    SummonerDAO mySummonerDB;
    MainActivity.MyCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mySummonerDB = new SummonerDAO(this);

        Intent intent = getIntent();
        String pf = intent.getStringExtra("platform").toUpperCase();

        if(pf.equals("KR")){
            platform = Platform.KR;
        }else if(pf.equals("JP")){
            platform = Platform.JP;
        }else if(pf.equals("EUNE")) {
            platform = Platform.EUNE;
        }else if(pf.equals("EUW")) {
            platform = Platform.EUW;
        }else if(pf.equals("LAN")) {
            platform = Platform.LAN;
        }else if(pf.equals("NA")) {
            platform = Platform.NA;
        }else if(pf.equals("RU")) {
            platform = Platform.RU;
        }else if(pf.equals("BR")) {
            platform = Platform.BR;
        }else if(pf.equals("TR")) {
            platform = Platform.TR;
        }else if(pf.equals("OCE")) {
            platform = Platform.OCE;
        }



        platformTextView = (TextView) findViewById(R.id.mPlatformTextView);
        platformTextView.setText(pf);

        closeTab = (ImageView) findViewById(R.id.mButtonClose);
        registerEditText = (ClearableEditText) findViewById(R.id.mEditTextRegister);
        completeButton = (Button) findViewById(R.id.mButtonComplete);


        closeTab.setOnClickListener(this);
        completeButton.setOnClickListener(this);
    }

    public class AsyncTaskRegister extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0].replaceAll("\\s+", "").toLowerCase();
            return name;
        }

        @Override
        protected void onPostExecute(String name) {
            super.onPostExecute(name);
            Summoner summoner = new Summoner(platform, name);

            if(summoner.getSummoner() == null){
                Toast.makeText(getApplicationContext(), "소환사 이름을 확인해 주세요", Toast.LENGTH_SHORT).show();
                return;
            }else{
                League league = new League(summoner);
                mySummonerDB.addMySummoner(new MySummonerDTO(
                        summoner.getPlatform().getName(),
                        summoner.getName(),
                        String.valueOf(summoner.getSummonerLevel()),
                        league.getSoloRank(),
                        league.getSoloRankInfo(),
                        league.getLeagueSoloPoints(),
                        league.getSoloWins(),
                        league.getSoloLosses(),
                        league.getSoloWinningAverage(),
                        String.valueOf(summoner.getProfileIconId())
                ));
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mButtonClose:
                finish();
                break;
            case R.id.mButtonComplete:
                if (registerEditText.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "소환사 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if(registerEditText.getText().toString().length() == 1) {
                    Toast.makeText(getApplicationContext(), "소환사 이름을 확인해 주세요", Toast.LENGTH_SHORT).show();
                }else {
                    registerEditText.clearFocus();
                    AsyncTaskRegister registerTask = new AsyncTaskRegister();
                    registerTask.execute(registerEditText.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onDestroy(){
            super.onDestroy();
        callBack = MainActivity.mCallback;
        callBack.refreshMainActivity();
    }
}
