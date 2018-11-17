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

import me.projectx.needaticket.BuyActivity;
import me.projectx.needaticket.listener.ListenerDoubleTap;
import me.projectx.needaticket.pojo.Ticket;
import me.projectx.needaticket.pojo.TicketType;

public class AdapterListViewTicket extends ArrayAdapter<Ticket> {

    AppCompatActivity appCompatActivityResource;
    ArrayList<Ticket> data;

    public AdapterListViewTicket(AppCompatActivity res, @LayoutRes int resource, ArrayList<Ticket> data){
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
        Ticket ticket = this.data.get(position);
        System.out.println(ticket);
        LayoutInflater inflater = (LayoutInflater) this.getAppCompatActivityResource().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item_event, parent, false);
        TextView header = (TextView) rowView.findViewById(R.id.list_item_ticket_title);
        TextView seats = (TextView) rowView.findViewById(R.id.list_item_ticket_seats);
        TextView price = (TextView) rowView.findViewById(R.id.list_item_ticket_price);
        seats.setText(ticket.getSeats());
        price.setText(ticket.getPrice() + "€");
        setUpIconCategory(rowView,ticket.getType());
        header.setText(ticket.getTitle());
        this.setUpRowViewListener(rowView, ticket);
        this.setUpTitleListener(rowView, ticket);
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

    private void setUpRowViewListener(final View rowView, final Ticket ticket){

        rowView.setOnClickListener(new ListenerDoubleTap() {

            @Override
            public void onSingleClick(View v) {
                changeActivity(rowView, ticket);
            }

            @Override
            public void onDoubleClick(View v) {
                changeActivity(rowView, ticket);
            }
        });
    }

    private void setUpTitleListener(View rowView, final Ticket ticket){
        changeActivity(rowView, ticket);
    }

    private void changeActivity(View rowView, final Ticket ticket){
        final LinearLayout content_title = (LinearLayout) rowView.findViewById(R.id.list_item_header_title);
        content_title.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent buy_activity = new Intent(getAppCompatActivityResource(), BuyActivity.class);
                        buy_activity.putExtra("tID", ticket.getId());
                        getAppCompatActivityResource().startActivity(buy_activity);
                    }
                }
        );
    }
}
