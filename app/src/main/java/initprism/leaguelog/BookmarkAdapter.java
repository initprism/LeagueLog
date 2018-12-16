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

public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class BookmarkHolder extends RecyclerView.ViewHolder {
        ImageView summonerIcon;
        TextView summonerLevel;
        TextView summonerId;
        ImageView summonerTierIcon;
        TextView summonerTier;
        ImageView bookmark;

        LinearLayout bookmarkLayout;

        BookmarkHolder(View view) {
            super(view);
            summonerIcon = view.findViewById(R.id.mSummonerIcon);
            summonerLevel = view.findViewById(R.id.mSummonerLevel);
            summonerId = view.findViewById(R.id.mSummonerId);
            summonerTierIcon = view.findViewById(R.id.mSummonerTierIcon);
            summonerTier = view.findViewById(R.id.mSummonerTier);
            bookmark = view.findViewById(R.id.mBookmark);
            bookmarkLayout = view.findViewById(R.id.mBookmarkLayout);
        }
    }

    static Util util = new Util();
    SearchActivity.MyCallBack callBackHistory;
    MainActivity.MyCallBack callBackBookmark;

    private ArrayList<BookmarkSummonerDTO> BookmarkArrayList;

    BookmarkAdapter(ArrayList<BookmarkSummonerDTO> bookmarkSummonerDTO) {
        this.BookmarkArrayList = bookmarkSummonerDTO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);

        return new BookmarkHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BookmarkHolder bookmarkholder = (BookmarkHolder) holder;
        bookmarkholder.summonerId.setText(BookmarkArrayList.get(position).getName());
        bookmarkholder.summonerTier.setText(" " + BookmarkArrayList.get(position).getTierInfo());
        bookmarkholder.summonerLevel.setText(" " + BookmarkArrayList.get(position).getLevel());

        try {
            URL url = new URL(util.getProfileIconURL() + BookmarkArrayList.get(position).getProfileIcon() + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            bookmarkholder.summonerIcon.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            bookmarkholder.summonerIcon.setImageResource(R.drawable.testicon);
        }

        switch (BookmarkArrayList.get(position).getTier().toLowerCase()) {
            case "iron":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.iron);
                break;
            case "bronze":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.bronze);
                break;
            case "silver":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.silver);
                break;
            case "gold":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.gold);
                break;
            case "platinum":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.platinum);
                break;
            case "diamond":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.diamond);
                break;
            case "master":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.master);
                break;
            case "grandmaster":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.grandmaster);
                break;
            case "challenger":
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.challenger);
                break;
            default:
                bookmarkholder.summonerTierIcon.setImageResource(R.drawable.provisional);
                break;
        }

        bookmarkholder.bookmark.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                BookmarkSummonerDTO bookmarkSummoner = BookmarkArrayList.get(position);
                SummonerDAO summonerDAO = new SummonerDAO(v.getContext());
                summonerDAO.deleteBookmarkSummoner(bookmarkSummoner);

                summonerDAO.updateHistorySummoner(new HistorySummonerDTO(
                        bookmarkSummoner.getPlatform(),
                        bookmarkSummoner.getName(),
                        bookmarkSummoner.getTier(),
                        bookmarkSummoner.getTierInfo(),
                        bookmarkSummoner.getLevel(),
                        bookmarkSummoner.getProfileIcon(),
                        "n"));

                callBackBookmark = MainActivity.mCallback;
                callBackBookmark.refreshBookmark();

            }
        });

        bookmarkholder.bookmarkLayout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                SummonerDAO summonerDAO = new SummonerDAO(v.getContext());
                summonerDAO.replaceHistorySummoner(new HistorySummonerDTO(
                        BookmarkArrayList.get(position).getPlatform(),
                        BookmarkArrayList.get(position).getName(),
                        BookmarkArrayList.get(position).getTier(),
                        BookmarkArrayList.get(position).getTierInfo(),
                        BookmarkArrayList.get(position).getLevel(),
                        BookmarkArrayList.get(position).getProfileIcon(),
                        "y"
                ));
                callBackHistory = SearchActivity.mCallback;
                callBackHistory.refreshSearchActivity();

                Intent intent = new Intent(v.getContext(), SummonerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("platform", BookmarkArrayList.get(position).getPlatform());
                bundle.putSerializable("name", BookmarkArrayList.get(position).getName());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return BookmarkArrayList.size();
    }
}
