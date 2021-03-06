package me.projectx.needaticket.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.projectx.needaticket.R;
import me.projectx.needaticket.activities.ConcertActivity;
import me.projectx.needaticket.listener.ListenerDoubleTap;
import me.projectx.needaticket.pojo.Concert;
import me.projectx.needaticket.pojo.ConcertType;
import moer.intervalclick.api.IntervalClick;
public class AdapterListViewConcert extends ArrayAdapter<Concert> {
    private AppCompatActivity appCompatActivityResource;
    private ArrayList<Concert> data;
    private String uID;
    public AdapterListViewConcert (AppCompatActivity res, String uID,
                                   @LayoutRes int resource, List<Concert> data) {
        super(res, resource, data);
        this.uID = uID;
        this.appCompatActivityResource = res;
        this.data = (ArrayList<Concert>) data;
    }
    public AppCompatActivity getAppCompatActivityResource () {
        return appCompatActivityResource;
    }
    @NonNull @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Concert concert = this.data.get(position);
        LayoutInflater inflater = (LayoutInflater) this.getAppCompatActivityResource().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item_concert, parent, false);
        TextView header = rowView.findViewById(R.id.list_item_concert_name);
        TextView genre = rowView.findViewById(R.id.list_item_concert_genre);
        TextView artist = rowView.findViewById(R.id.list_item_concert_artist);
        TextView location = rowView.findViewById(R.id.list_item_concert_location);
        TextView date = rowView.findViewById(R.id.list_item_concert_date);
        artist.setText(concert.getArtist().getName());
        genre.setText(concert.getGenre().toString());
        location.setText(concert.getAddress());
        date.setText(concert.getDate().substring(0,concert.getDate().length()-14));
        setUpIconCategory(rowView, concert.getType());
        header.setText(concert.getTitle());
        this.setUpRowViewListener(rowView, concert);
        this.setUpTitleListener(rowView, concert);
        return rowView;
    }
    private void setUpIconCategory (View rowView, ConcertType concertType) {
        ImageView imageviewHeaderImageCategory = rowView.findViewById(R.id.category_image_concert_list_item);
        switch (concertType) {
            case CONCERT:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
            case FESTIVAL:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_festival);
                break;
            case REHEARSAL:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
            default:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
        }
    }
    private void setUpRowViewListener (final View rowView, final Concert concert) {
        rowView.setOnClickListener(new ListenerDoubleTap() {
            @Override public void onSingleClick (View v) {
                changeActivity(rowView, concert);
            }
            @Override public void onDoubleClick (View v) {
                changeActivity(rowView, concert);
            }
        });
    }
    private void setUpTitleListener (View rowView, final Concert concert) {
        changeActivity(rowView, concert);
    }
    private void changeActivity (View rowView, final Concert concert) {
        final LinearLayout contentTitle = rowView.findViewById(R.id.list_item_header_title);
        contentTitle.setOnClickListener(v -> {
            Intent concertActivity = new Intent(getAppCompatActivityResource(), ConcertActivity.class);
            concertActivity.putExtra("uID", uID);
            concertActivity.putExtra("cID", concert.get_id());
            concertActivity.putExtra("cTitle", concert.getTitle());
            concertActivity.putExtra("cType", concert.getType().toString());
            concertActivity.putExtra("cDate", concert.getDate().substring(0, concert.getDate().length()-14));
            concertActivity.putExtra("cAddress", concert.getAddress());
            concertActivity.putExtra("cArtistName", concert.getArtist().getName());
            concertActivity.putExtra("cGenre", concert.getGenre().toString());
            getAppCompatActivityResource().startActivity(concertActivity);
        });
    }
}