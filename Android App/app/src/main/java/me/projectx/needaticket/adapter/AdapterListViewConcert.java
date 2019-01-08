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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import me.projectx.needaticket.ConcertActivity;
import me.projectx.needaticket.R;
import me.projectx.needaticket.listener.ListenerDoubleTap;
import me.projectx.needaticket.pojo.Artist;
import me.projectx.needaticket.pojo.Concert;
import me.projectx.needaticket.pojo.TicketType;

public class AdapterListViewConcert extends ArrayAdapter<Concert> {

    private AppCompatActivity appCompatActivityResource;
    private ArrayList<Concert> data;
    private String uID;

    public AdapterListViewConcert(AppCompatActivity res, String uID, @LayoutRes int resource, ArrayList<Concert> data){
        super(res, resource, data);
        this.uID = uID;
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
        View rowView = inflater.inflate(R.layout.listview_item_concert, parent, false);
        TextView header = (TextView) rowView.findViewById(R.id.list_item_concert_name);
        TextView genre = (TextView) rowView.findViewById(R.id.list_item_concert_genre);
        TextView artist = (TextView) rowView.findViewById(R.id.list_item_concert_artist);
        TextView location = (TextView) rowView.findViewById(R.id.list_item_concert_location);
        TextView date = (TextView) rowView.findViewById(R.id.list_item_concert_date);
        String artists = "";
        for(Artist a : concert.getArtists()){
            artists += a.getName() + ", ";
        }
        artists = artists.substring(0, artists.length()-2);
        artist.setText(artists);
        genre.setText(concert.getGenre().toString());
        location.setText(concert.getAddress());
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yy");
        date.setText(dateFormat.format(concert.getDate()));
        setUpIconCategory(rowView,concert.getTickets().get(0).getType());
        header.setText(concert.getTitle());
        this.setUpRowViewListener(rowView, concert);
        this.setUpTitleListener(rowView, concert);
        return rowView;
    }

    private void setUpIconCategory(View rowView, TicketType ticketType){
        ImageView imageview_header_image_category = (ImageView) rowView.findViewById(R.id.category_image_concert_list_item);
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
                        concert_activity.putExtra("uID", uID);
                        concert_activity.putExtra("cID", concert.getId());
                        getAppCompatActivityResource().startActivity(concert_activity);
                    }
                }
        );
    }
}