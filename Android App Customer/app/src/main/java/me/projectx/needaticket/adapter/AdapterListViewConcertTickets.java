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
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.projectx.needaticket.R;
import me.projectx.needaticket.activities.BuyActivity;
import me.projectx.needaticket.customgui.NumberPickerDialog;
import me.projectx.needaticket.listener.ListenerDoubleTap;
import me.projectx.needaticket.listener.ListenerNumberPicked;
import me.projectx.needaticket.pojo.Ticket;
import me.projectx.needaticket.pojo.TicketType;
public class AdapterListViewConcertTickets extends ArrayAdapter<Ticket> implements ListenerNumberPicked {
    private AppCompatActivity appCompatActivityResource;
    private ArrayList<Ticket> data;
    private String uID;
    private String cID;
    private ListenerNumberPicked listener = this;
    public AdapterListViewConcertTickets (AppCompatActivity res, String uID, String cID,
                                          @LayoutRes int resource, List<Ticket> data) {
        super(res, resource, data);
        this.uID = uID;
        this.cID = cID;
        this.appCompatActivityResource = res;
        this.data = (ArrayList<Ticket>) data;
    }
    @NonNull @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ticket ticket = this.data.get(position);
        LayoutInflater inflater = (LayoutInflater) this.getAppCompatActivityResource().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item_concert_ticket, parent, false);
        TextView header = rowView.findViewById(R.id.list_item_ticket_title);
        TextView seller = rowView.findViewById(R.id.list_item_ticket_seller);
        TextView price = rowView.findViewById(R.id.list_item_ticket_price);
        TextView amount = rowView.findViewById(R.id.list_item_ticket_count);
        seller.setText(ticket.getSeller().getUsername());
        price.setText(ticket.getPrice() + " €");
        amount.setText("" + ticket.getAvailable());
        //ToDo: remove after data fix
        if(ticket.getType() != null) setUpIconCategory(rowView, ticket.getType());
        header.setText(ticket.getConcert().getTitle());
        this.setUpRowViewListener(rowView, ticket);
        this.setUpTitleListener(rowView, ticket);
        return rowView;
    }
    public AppCompatActivity getAppCompatActivityResource () {
        return appCompatActivityResource;
    }
    public void setAppCompatActivityResource (AppCompatActivity appCompatActivityResource) {
        this.appCompatActivityResource = appCompatActivityResource;
    }
    private void setUpIconCategory (View rowView, TicketType ticketType) {
        ImageView imageviewHeaderImageCategory = rowView.findViewById(R.id.category_image_ticket_list_item);
        switch (ticketType) {
            case TICKET_CONCERT:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_ticket_concert);
                break;
            case TICKET_FESTIVAL:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_ticket_festival);
                break;
            default:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
        }
    }
    private void setUpRowViewListener (final View rowView, final Ticket ticket) {
        rowView.setOnClickListener(new ListenerDoubleTap() {
            @Override public void onSingleClick (View v) {
                changeActivity(v, ticket);
            }
            @Override public void onDoubleClick (View v) {
                changeActivity(v, ticket);
            }
        });
    }
    private void setUpTitleListener (View rowView, final Ticket ticket) {
        changeActivity(rowView, ticket);
    }
    private void changeActivity (View rowView, final Ticket ticket) {
        final LinearLayout contentTitle = rowView.findViewById(R.id.list_item_header_title);
        contentTitle.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick (View v) {
                NumberPickerDialog npd = new NumberPickerDialog(ticket, ticket.getAvailable());
                npd.setValueChangeListener(listener);
                npd.show(appCompatActivityResource.getSupportFragmentManager(), "time picker");
            }
        });
    }
    @Override public void onValueChange (NumberPicker picker, int oldVal, int newVal, Ticket t) {
        Intent buyActivity = new Intent(getAppCompatActivityResource(), BuyActivity.class);
        buyActivity.putExtra("uID", uID);
        buyActivity.putExtra("sellerName", t.getSeller().getUsername());
        buyActivity.putExtra("sID", t.getSeller().get_id());
        buyActivity.putExtra("ticketTitle", t.getConcert().getTitle());
        buyActivity.putExtra("price", "" + t.getPrice());
        //buyActivity.putExtra("ticketType", t.getType().toString());
        buyActivity.putExtra("amount", "" + t.getAvailable());
        buyActivity.putExtra("amountSelected", "" + newVal);
        buyActivity.putExtra("cID", cID);
        getAppCompatActivityResource().startActivity(buyActivity);
    }
}