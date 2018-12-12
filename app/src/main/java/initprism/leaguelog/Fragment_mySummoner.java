package initprism.leaguelog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import db.MySummoner;
import de.hdodenhof.circleimageview.CircleImageView;
import misc.OnSingleClickListener;
import riot.Util;

public class Fragment_mySummoner extends Fragment{


    public Fragment_mySummoner(){
    }


    CircleImageView mSummonerIcon;
    TextView mSummonerLevel;
    TextView mSummonerId;
    ImageView mRemoveMySummoner;
    ImageView mSummonerTierIcon;
    TextView mSummonerTier;
    TextView mSummonerLp;
    TextView mSummonerRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mysummoner, container, false);

        Util util = new Util();

        mSummonerIcon = (CircleImageView) view.findViewById(R.id.mSummonerIcon);
        mSummonerLevel = (TextView) view.findViewById(R.id.mSummonerLevel);
        mSummonerId = (TextView) view.findViewById(R.id.mSummonerId);
        mRemoveMySummoner = (ImageView) view.findViewById(R.id.mRemoveMySummmoner);
        mSummonerTierIcon = (ImageView) view.findViewById(R.id.mSummonerTierIcon);
        mSummonerTier = (TextView) view.findViewById(R.id.mSummonerTier);
        mSummonerLp = (TextView) view.findViewById(R.id.mSummonerLp);
        mSummonerRecord = (TextView) view.findViewById(R.id.mSummonerRecord);

        MySummoner mySummoner = (MySummoner) getArguments().getSerializable("mySummoner");
        URL url = null;
        try {
            url = new URL(util.getProfileIconURL() + mySummoner.getProfileIcon() + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            mSummonerIcon.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            mSummonerIcon.setImageResource(R.drawable.testicon);
        }

        mSummonerLevel.setText(mySummoner.getLevel());
        mSummonerId.setText(mySummoner.getName());

        switch (mySummoner.getTier().toLowerCase()) {
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

        mSummonerTier.setText(" " + mySummoner.getTierInfo());
        mSummonerLp.setText(" (" + mySummoner.getLp() + "LP)");
        mSummonerRecord.setText(mySummoner.getWins() + "승 " + mySummoner.getLosses() + "패 / " + "(" + mySummoner.getAvr() +"%)");


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
