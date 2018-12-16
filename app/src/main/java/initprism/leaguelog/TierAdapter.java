package initprism.leaguelog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import db.BookmarkSummonerDTO;
import db.HistorySummonerDTO;
import db.SummonerDAO;
import misc.OnSingleClickListener;
import riot.Util;

public class TierAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class TierHolder extends RecyclerView.ViewHolder {
        ImageView summonerTierIcon;
        TextView rankType;
        TextView summonerTier;
        TextView summonerLp;
        TextView summonerRecord;

        TierHolder(View view) {
            super(view);
            summonerTierIcon = view.findViewById(R.id.mSummonerTierIcon);
            rankType = view.findViewById(R.id.mRankType);
            summonerTier = view.findViewById(R.id.mSummonerTier);
            summonerLp = view.findViewById(R.id.mSummonerLp);
            summonerRecord = view.findViewById(R.id.mSummonerRecord);
        }
    }

    static Util util = new Util();

    private ArrayList<TierItem> TierArrayList;

    TierAdapter(ArrayList<TierItem> tierItems) {
        this.TierArrayList = tierItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tier, parent, false);

        return new TierHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TierHolder tierHolder = (TierHolder) holder;


        tierHolder.rankType.setText(TierArrayList.get(position).rankType);
        tierHolder.summonerTier.setText(TierArrayList.get(position).summonerTierInfo);

        switch (TierArrayList.get(position).summonerTierIcon.toLowerCase()) {
            case "iron":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.iron);
                break;
            case "bronze":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.bronze);
                break;
            case "silver":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.silver);
                break;
            case "gold":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.gold);
                break;
            case "platinum":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.platinum);
                break;
            case "diamond":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.diamond);
                break;
            case "master":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.master);
                break;
            case "grandmaster":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.grandmaster);
                break;
            case "challenger":
                tierHolder.summonerTierIcon.setImageResource(R.drawable.challenger);
                break;
            default:
                tierHolder.summonerTierIcon.setImageResource(R.drawable.provisional);
                break;
        }


        if (TierArrayList.get(position).summonerLp.isEmpty())
            tierHolder.summonerLp.setText("");
        else
            tierHolder.summonerLp.setText(TierArrayList.get(position).summonerLp + " Lp");

        if (TierArrayList.get(position).summonerAvr.isEmpty())
            tierHolder.summonerRecord.setText("");
        else
            tierHolder.summonerRecord.setText(
                    TierArrayList.get(position).wins + "승 " + TierArrayList.get(position).losses + "패 "
                            + "(" + TierArrayList.get(position).summonerAvr + "%)");


    }

    @Override
    public int getItemCount() {
        return TierArrayList.size();
    }
}
