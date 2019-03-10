package me.projectx.needaticket.adapter;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;
import me.itangqi.waveloadingview.WaveLoadingView;
import me.projectx.needaticket.R;
import me.projectx.needaticket.activities.TicketsActivity;
import me.projectx.needaticket.pojo.Ticket;
import me.projectx.needaticket.pojo.TicketType;
public class AdapterListViewTicket extends ArrayAdapter<Ticket> {
    private AppCompatActivity appCompatActivityResource;
    private ArrayList<Ticket> data;
    private String currHash;
    private Context cx;
    public AdapterListViewTicket (AppCompatActivity res,
                                  @LayoutRes int resource, List<Ticket> data) {
        super(res, resource, data);
        this.appCompatActivityResource = res;
        this.data = (ArrayList<Ticket>) data;
    }
    @NonNull @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ticket ticket = this.data.get(position);
        LayoutInflater inflater = (LayoutInflater) this.getAppCompatActivityResource().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item_ticket, parent, false);
        TextView header = rowView.findViewById(R.id.list_item_ticket_title);
        TextView price = rowView.findViewById(R.id.list_item_ticket_price);
        price.setText(ticket.getPrice() + "€");
        setUpIconCategory(rowView, ticket.getType());
        header.setText(ticket.getTitle());
        currHash = "" + ticket.getId() + ":=:" + ticket.getSeller().getId() + ":=:" + ticket.getBuyer().getId() + ":=:" + ticket.getPrice();
        rowView.setOnClickListener(new ShowQRListener());
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
            case CONCERT:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_ticket_concert);
                break;
            case FESTIVAL:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_ticket_festival);
                break;
        }
    }
    private void showDiag (final View tv) {
        final View ctt = getAppCompatActivityResource().findViewById(R.id.content_tickets);
        ctt.setVisibility(View.INVISIBLE);
        final View dialogView = View.inflate(cx, R.layout.dialog_qr_code, null);
        final Dialog dialog = new Dialog(cx, R.style.UserAlertStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        ImageView imageView = dialog.findViewById(R.id.closeDialogQR);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick (View v) {
                revealShow(dialogView, false, dialog, tv);
            }
        });
        ImageView qrView = dialog.findViewById(R.id.image_qr_ticket);
        WaveLoadingView mWaveLoadingView = (WaveLoadingView) getAppCompatActivityResource().findViewById(R.id.qrWaveLoadingView);
        mWaveLoadingView.startAnimation();
        mWaveLoadingView.setVisibility(View.VISIBLE);
        try {
            String hash = BCrypt.with(new SecureRandom()).hashToString(12, (currHash).toCharArray());
            Bitmap bitmap = hashToQR(hash);
            qrView.setImageBitmap(bitmap);
        } catch (Exception ex) {
            qrView.setImageDrawable(getAppCompatActivityResource().getDrawable(R.drawable.error));
        }
        mWaveLoadingView.setVisibility(View.INVISIBLE);
        mWaveLoadingView.cancelAnimation();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow (DialogInterface dialogInterface) {
                revealShow(dialogView, true, null, tv);
                ctt.setVisibility(View.VISIBLE);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey (DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    revealShow(dialogView, false, dialog, tv);
                    ctt.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    private void revealShow (View dialogView, boolean b, final Dialog dialog, View rowView) {
        final View view = dialogView.findViewById(R.id.dialog_qr);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        int cx = (int) (rowView.getX() + ((double) rowView.getWidth() / 2));
        int cy = (int) (rowView.getY()) + rowView.getHeight() + 56;
        if (b) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(700);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd (Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(700);
            anim.start();
        }
    }
    public void addContext (TicketsActivity ticketsActivity) {
        cx = ticketsActivity;
    }
    private class ShowQRListener implements View.OnClickListener {
        @Override public void onClick (View v) {
            showDiag(v);
        }
    }
    private Bitmap hashToQR(String hash) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        Display dpl = getAppCompatActivityResource().getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        dpl.getSize(size);
        int qrSize = size.x-30;
        try {
            bitMatrix = new MultiFormatWriter().encode(hash, BarcodeFormat.DATA_MATRIX.QR_CODE,
                                                       qrSize, qrSize, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, qrSize, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}