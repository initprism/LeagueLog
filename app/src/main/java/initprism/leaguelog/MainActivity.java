package initprism.leaguelog;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.rithms.riot.constant.Platform;

import java.util.concurrent.atomic.AtomicReference;

import db.MySummoner;
import db.MySummonerDAO;
import misc.OnSingleClickListener;


public class MainActivity extends AppCompatActivity {

    TextView platformTextView;

    AtomicReference<Platform> platform = new AtomicReference<Platform>(Platform.KR);
    Fragment_register fragment_register;
    Fragment_mySummoner fragment_mySummoner;

    PlatformSheetDialog dialog;
    Bundle bundle;

    MySummonerDAO mySummonerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        platformTextView = (TextView) findViewById(R.id.mPlatformTextView);
        dialog = new PlatformSheetDialog(platformTextView, platform);




        mySummonerDAO = new MySummonerDAO(this);
        MySummoner mySummoner = mySummonerDAO.getSummoner(platform.get().getName());
        if (mySummoner == null) {
            fragment_register = new Fragment_register();
            bundle = new Bundle(1);
            bundle.putString("platform", platform.get().getName());
            fragment_register.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.mUserInfo, fragment_register).commitNow();
        } else {
            fragment_mySummoner = new Fragment_mySummoner();
            bundle = new Bundle(1);
            bundle.putSerializable("mySummoner", mySummoner);
            fragment_mySummoner.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.mUserInfo, fragment_mySummoner).commitNow();
            Toast.makeText(getApplicationContext(), mySummoner.getName(), Toast.LENGTH_SHORT).show();
        }


        platformTextView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                switch (v.getId()) {
                    case R.id.mPlatformTextView:
                        dialog.showNow(getSupportFragmentManager(), "bottomSheet");
                        dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                fragment_register = new Fragment_register();
                                bundle.putString("platform", platform.get().getName());
                                fragment_register.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().replace(R.id.mUserInfo, fragment_register).commitNow();
                            }
                        });
                        break;
                }
            }
        });

    }


}
