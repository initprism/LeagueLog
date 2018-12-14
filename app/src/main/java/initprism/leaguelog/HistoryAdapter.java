package initprism.leaguelog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.net.URL;
import java.util.ArrayList;

import db.BookmarkSummonerDTO;
import db.HistorySummonerDTO;
import db.SummonerDAO;
import misc.OnSingleClickListener;
import riot.Util;

public class HistoryAdapter extends OmegaRecyclerView.Adapter<HistoryAdapter.ViewHolder>
        implements com.omega_r.libs.omegarecyclerview.sticky_header.StickyHeaderAdapter<HistoryAdapter.HeaderHolder> {


    static Util util = new Util();
    SearchActivity.MyCallBack callBack;

    private ArrayList<HistorySummonerDTO> HistoryArrayList;

    HistoryAdapter(ArrayList<HistorySummonerDTO> history) {
        this.HistoryArrayList = history;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HistoryAdapter.ViewHolder holder, final int position) {

        holder.summonerId.setText(HistoryArrayList.get(position).getName());
        holder.summonerTier.setText(" " + HistoryArrayList.get(position).getTierInfo());

        try {
            URL url = new URL(util.getProfileIconURL() + HistoryArrayList.get(position).getProfileIcon() + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            holder.summonerIcon.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            holder.summonerIcon.setImageResource(R.drawable.testicon);
        }

        switch (HistoryArrayList.get(position).getTier().toLowerCase()) {
            case "iron":
                holder.summonerTierIcon.setImageResource(R.drawable.iron);
                break;
            case "bronze":
                holder.summonerTierIcon.setImageResource(R.drawable.bronze);
                break;
            case "silver":
                holder.summonerTierIcon.setImageResource(R.drawable.silver);
                break;
            case "gold":
                holder.summonerTierIcon.setImageResource(R.drawable.gold);
                break;
            case "platinum":
                holder.summonerTierIcon.setImageResource(R.drawable.platinum);
                break;
            case "diamond":
                holder.summonerTierIcon.setImageResource(R.drawable.diamond);
                break;
            case "master":
                holder.summonerTierIcon.setImageResource(R.drawable.master);
                break;
            case "grandmaster":
                holder.summonerTierIcon.setImageResource(R.drawable.grandmaster);
                break;
            case "challenger":
                holder.summonerTierIcon.setImageResource(R.drawable.challenger);
                break;
            default:
                holder.summonerTierIcon.setImageResource(R.drawable.provisional);
                break;
        }

        if (HistoryArrayList.get(position).getBookmark().equals("y"))
            holder.buttonBookmark.setImageResource(R.drawable.favorite_black);
        else
            holder.buttonBookmark.setImageResource(R.drawable.favorite_border_black);

        holder.buttonBookmark.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                SummonerDAO summonerDAO = new SummonerDAO(v.getContext());
                HistorySummonerDTO h = HistoryArrayList.get(position);

                if (h.getBookmark().equals("y")) {
                    summonerDAO.updateHistorySummoner(new HistorySummonerDTO(
                            h.getPlatform(),
                            h.getName(),
                            h.getTier(),
                            h.getTierInfo(),
                            h.getProfileIcon(),
                            "n"
                    ));

                    summonerDAO.deleteBookmarkSummoner(new BookmarkSummonerDTO(
                            h.getPlatform(),
                            h.getName(),
                            h.getTier(),
                            h.getTierInfo(),
                            h.getProfileIcon()
                    ));

                    h.setBookmark("n");
                    holder.buttonBookmark.setImageResource(R.drawable.favorite_border_black);

                } else {
                    summonerDAO.updateHistorySummoner(new HistorySummonerDTO(
                            h.getPlatform(),
                            h.getName(),
                            h.getTier(),
                            h.getTierInfo(),
                            h.getProfileIcon(),
                            "y"
                    ));

                    summonerDAO.addBookmarkSummoner(new BookmarkSummonerDTO(
                            h.getPlatform(),
                            h.getName(),
                            h.getTier(),
                            h.getTierInfo(),
                            h.getProfileIcon()
                    ));

                    h.setBookmark("y");
                    holder.buttonBookmark.setImageResource(R.drawable.favorite_black);
                }


            }
        });

        holder.buttonRemove.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                SummonerDAO summonerDAO = new SummonerDAO(v.getContext());
                summonerDAO.deleteHistorySummoner(HistoryArrayList.get(position));

                callBack = SearchActivity.mCallback;
                callBack.refreshSearchActivity();
            }
        });


    }

    @Override
    public int getItemCount() {
        return HistoryArrayList.size();
    }

    @Override
    public long getHeaderId(int position) {
        return (long) 1;
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_header, parent, false);
        return new HeaderHolder(v);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewHolder, int position) {
        HeaderHolder myHeaderHolder = (HeaderHolder) viewHolder;
        myHeaderHolder.header.setText("소환사");
    }

    static class ViewHolder extends OmegaRecyclerView.ViewHolder {
        ImageView summonerIcon;
        TextView summonerId;
        ImageView summonerTierIcon;
        TextView summonerTier;

        ImageView buttonRemove;
        ImageView buttonBookmark;

        public ViewHolder(View itemView) {
            super(itemView);
            summonerIcon = itemView.findViewById(R.id.mSummonerIcon);
            summonerId = itemView.findViewById(R.id.mSummonerId);
            summonerTierIcon = itemView.findViewById(R.id.mSummonerTierIcon);
            summonerTier = itemView.findViewById(R.id.mSummonerTier);

            buttonRemove = itemView.findViewById(R.id.mButtonRemove);
            buttonBookmark = itemView.findViewById(R.id.mBookmark);
        }
    }

    static class HeaderHolder extends OmegaRecyclerView.ViewHolder {
        TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
        }
    }
}
