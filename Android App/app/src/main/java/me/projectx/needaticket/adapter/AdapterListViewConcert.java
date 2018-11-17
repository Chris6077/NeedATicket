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

import me.projectx.needaticket.ConcertActivity;
import me.projectx.needaticket.listener.ListenerDoubleTap;
import me.projectx.needaticket.pojo.Artist;
import me.projectx.needaticket.pojo.Concert;
import me.projectx.needaticket.pojo.TicketType;

public class AdapterListViewConcert extends ArrayAdapter<Concert> {

    AppCompatActivity appCompatActivityResource;
    ArrayList<Concert> data;

    public AdapterListViewConcert(AppCompatActivity res, @LayoutRes int resource, ArrayList<Concert> data){
        super(res, resource, data);
        this.appCompatActivityResource = res;
        this.data = data;
    }

    public AppCompatActivity getAppCompatActivityResource() {
        return appCompatActivityResource;
    }

    public void setAppCompatActivityResource(AppCompatActivity appCompatActivityResource){
        this.appCompatActivityResource = appCompatActivityResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Concert concert = this.data.get(position);
        System.out.println(concert);
        LayoutInflater inflater = (LayoutInflater) this.getAppCompatActivityResource().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item_event, parent, false);
        TextView header = (TextView) rowView.findViewById(R.id.list_item_concert_name);
        TextView genre = (TextView) rowView.findViewById(R.id.list_item_genre);
        TextView artist = (TextView) rowView.findViewById(R.id.list_item_concert_artist);
        String artists = "";
        for(Artist a : concert.getArtists()){
            artists += a.getName() + ", ";
        }
        artists = artists.substring(0, artists.length()-2);
        artist.setText(artists);
        genre.setText(concert.getGenre().toString());
        setUpIconCategory(rowView,concert.getTickets().get(0).getType());
        header.setText(concert.getTitle());
        this.setUpRowViewListener(rowView, concert);
        this.setUpTitleListener(rowView, concert);
        return rowView;
    }

    private void setUpIconCategory(View rowView, TicketType ticketType){
        ImageView imageview_header_image_category = (ImageView) rowView.findViewById(R.id.category_image_event_list_item);
        switch (ticketType){
            case CONCERT:
                imageview_header_image_category.setImageResource(R.drawable.category_concert);
                break;
            case FESTIVAL:
                imageview_header_image_category.setImageResource(R.drawable.category_festival);
                break;
        }
    }

    private void setUpRowViewListener(final View rowView, final Concert concert){

        rowView.setOnClickListener(new ListenerDoubleTap() {

            @Override
            public void onSingleClick(View v) {
                changeActivity(rowView, concert);
            }

            @Override
            public void onDoubleClick(View v) {
                changeActivity(rowView, concert);
            }
        });
    }

    private void setUpTitleListener(View rowView, final Concert concert){
        changeActivity(rowView, concert);
    }

    private void changeActivity(View rowView, final Concert concert){
        final LinearLayout content_title = (LinearLayout) rowView.findViewById(R.id.list_item_header_title);
        content_title.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent concert_activity = new Intent(getAppCompatActivityResource(),ConcertActivity.class);
                        concert_activity.putExtra("cID", concert.getId());
                        getAppCompatActivityResource().startActivity(concert_activity);
                    }
                }
        );
    }
}