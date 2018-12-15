package initprism.leaguelog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.rithms.riot.constant.Platform;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import db.BookmarkSummonerDTO;
import db.MySummonerDTO;
import db.SummonerDAO;
import misc.OnSingleClickListener;


public class MainActivity extends AppCompatActivity {

    /*--------CALL BACK METHOD--------*/
    public interface MyCallBack {
        void refreshMainActivity();

        void refreshBookmark();
    }

    public static MyCallBack mCallback;
    /*--------CALL BACK METHOD--------*/

    EditText summonerSearch;
    TextView platformTextView;

    AtomicReference<Platform> platform = new AtomicReference<Platform>(Platform.KR);
    Fragment_register fragment_register;
    Fragment_mySummoner fragment_mySummoner;

    PlatformSheetDialog dialog;
    Bundle bundle;

    SummonerDAO summonerDAO;

    RecyclerView bookmarkRecycler;
    RecyclerView.LayoutManager mLayoutManager;
    BookmarkAdapter bookmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        summonerSearch = (EditText) findViewById(R.id.mSummonerSearch);
        platformTextView = (TextView) findViewById(R.id.mPlatformTextView);
        dialog = new PlatformSheetDialog(platformTextView, platform);

        summonerDAO = new SummonerDAO(this);
        MySummonerDTO mySummonerDTO = summonerDAO.getMySummoner(platform.get().getName());

        if (mySummonerDTO == null) {
            fragment_register = new Fragment_register();
            bundle = new Bundle(1);
            bundle.putString("platform", platform.get().getName());
            fragment_register.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.mUserInfo, fragment_register).commitNow();
        } else {
            fragment_mySummoner = new Fragment_mySummoner();
            bundle = new Bundle(1);
            bundle.putSerializable("mySummonerDTO", mySummonerDTO);
            fragment_mySummoner.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.mUserInfo, fragment_mySummoner).commitNow();
            Toast.makeText(getApplicationContext(), mySummonerDTO.getName(), Toast.LENGTH_SHORT).show();
        }


        bookmarkRecycler = findViewById(R.id.mBookmarkRecycler);
        bookmarkRecycler.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        bookmarkRecycler.setLayoutManager(mLayoutManager);

        ArrayList<BookmarkSummonerDTO> bookmarkArrayList = summonerDAO.getAllBookmarkSummoners();
        bookmarkAdapter = new BookmarkAdapter(bookmarkArrayList);
        bookmarkRecycler.setAdapter(bookmarkAdapter);


        summonerSearch.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("platform", platform.get());
                startActivity(intent);
            }
        });


        platformTextView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dialog.showNow(getSupportFragmentManager(), "bottomSheet");
                dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mCallback.refreshMainActivity();
                    }
                });
            }
        });


        /*--------CALL BACK METHOD--------*/
        mCallback = new MyCallBack() {
            @Override
            public void refreshMainActivity() {
                MySummonerDTO mySummonerDTO = summonerDAO.getMySummoner(platform.get().getName());
                if (mySummonerDTO == null) {
                    fragment_register = new Fragment_register();
                    bundle = new Bundle(1);
                    bundle.putString("platform", platform.get().getName());
                    fragment_register.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mUserInfo, fragment_register).commitNow();
                } else {
                    fragment_mySummoner = new Fragment_mySummoner();
                    bundle = new Bundle(1);
                    bundle.putSerializable("mySummonerDTO", mySummonerDTO);
                    fragment_mySummoner.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mUserInfo, fragment_mySummoner).commitNow();
                }
            }

            @Override
            public void refreshBookmark() {
                ArrayList<BookmarkSummonerDTO> bookmarkArrayList = summonerDAO.getAllBookmarkSummoners();
                bookmarkAdapter = new BookmarkAdapter(bookmarkArrayList);
                bookmarkRecycler.setAdapter(bookmarkAdapter);
            }
        };
        /*--------CALL BACK METHOD--------*/
    }
}
