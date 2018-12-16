package initprism.leaguelog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URL;

import db.MySummonerDTO;
import db.SummonerDAO;
import de.hdodenhof.circleimageview.CircleImageView;
import misc.OnSingleClickListener;
import riot.Util;

public class Fragment_mySummoner extends Fragment {


    public Fragment_mySummoner() {
    }

    CircleImageView mSummonerIcon;
    TextView mSummonerLevel;
    TextView mSummonerId;
    ImageView mRemoveMySummoner;
    ImageView mSummonerTierIcon;
    TextView mSummonerTier;
    TextView mSummonerLp;
    TextView mSummonerRecord;

    MySummonerDTO mySummonerDTO;
    SummonerDAO summonerDAO;

    LinearLayout mySummonerLayout;

    MainActivity.MyCallBack callBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mysummoner, container, false);

        Util util = new Util();
        summonerDAO = new SummonerDAO(view.getContext());

        mSummonerIcon = (CircleImageView) view.findViewById(R.id.mSummonerIcon);
        mSummonerLevel = (TextView) view.findViewById(R.id.mSummonerLevel);
        mSummonerId = (TextView) view.findViewById(R.id.mSummonerId);
        mRemoveMySummoner = (ImageView) view.findViewById(R.id.mRemoveMySummmoner);
        mSummonerTierIcon = (ImageView) view.findViewById(R.id.mSummonerTierIcon);
        mSummonerTier = (TextView) view.findViewById(R.id.mSummonerTier);
        mSummonerLp = (TextView) view.findViewById(R.id.mSummonerLp);
        mSummonerRecord = (TextView) view.findViewById(R.id.mSummonerRecord);
        mySummonerLayout = (LinearLayout) view.findViewById(R.id.mMySummonerLayout);

        mySummonerDTO = (MySummonerDTO) getArguments().getSerializable("mySummonerDTO");

        try {
            URL url = new URL(util.getProfileIconURL() + mySummonerDTO.getProfileIcon() + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            mSummonerIcon.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            mSummonerIcon.setImageResource(R.drawable.testicon);
        }


        switch (mySummonerDTO.getTier().toLowerCase()) {
            case "iron":
                mSummonerTierIcon.setImageResource(R.drawable.iron);
                break;
            case "bronze":
                mSummonerTierIcon.setImageResource(R.drawable.bronze);
                break;
            case "silver":
                mSummonerTierIcon.setImageResource(R.drawable.silver);
                break;
            case "gold":
                mSummonerTierIcon.setImageResource(R.drawable.gold);
                break;
            case "platinum":
                mSummonerTierIcon.setImageResource(R.drawable.platinum);
                break;
            case "diamond":
                mSummonerTierIcon.setImageResource(R.drawable.diamond);
                break;
            case "master":
                mSummonerTierIcon.setImageResource(R.drawable.master);
                break;
            case "grandmaster":
                mSummonerTierIcon.setImageResource(R.drawable.grandmaster);
                break;
            case "challenger":
                mSummonerTierIcon.setImageResource(R.drawable.challenger);
                break;
            default:
                mSummonerTierIcon.setImageResource(R.drawable.provisional);
                break;
        }


        mSummonerLevel.setText(mySummonerDTO.getLevel());
        mSummonerId.setText(mySummonerDTO.getName());
        mSummonerTier.setText(" " + mySummonerDTO.getTierInfo());
        if (mySummonerDTO.getTier().equals("unranked")) {
            mSummonerLp.setText("");
            mSummonerRecord.setText("");
        } else {
            mSummonerLp.setText(" (" + mySummonerDTO.getLp() + "LP)");
            mSummonerRecord.setText(mySummonerDTO.getWins() + "승 " + mySummonerDTO.getLosses() + "패 / " + "(" + mySummonerDTO.getAvr() + "%)");
        }

        mySummonerLayout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(v.getContext(), SummonerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("platform", mySummonerDTO.getPlatform());
                bundle.putSerializable("name", mySummonerDTO.getName());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        mRemoveMySummoner.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                summonerDAO.deleteMySummoner(mySummonerDTO);
                callBack = MainActivity.mCallback;
                callBack.refreshMainActivity();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
