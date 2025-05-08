package com.example.traveleaseapp.ADAPTOR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.R;
import com.example.traveleaseapp.USER_MODULE.PaymentActivity;

import java.util.List;

public class UserBookingAdaptor extends android.widget.ArrayAdapter<TravelEaseModel> {
    private final Activity context;
    private final List<TravelEaseModel> booklist;

    public UserBookingAdaptor(Activity context, List<TravelEaseModel> booklist) {
        super(context, R.layout.activity_user_booking_adaptor, booklist);
        this.context = context;
        this.booklist = booklist;
    }

    private static class ViewHolder {
        TextView tvPackageName, tvDestination, tvDates, tvPrice, tvBookingStatus;
        Button btnPayNow;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_user_booking_adaptor, parent, false);

            holder = new ViewHolder();
            holder.tvPackageName = convertView.findViewById(R.id.tvPackageName);
            holder.tvDestination = convertView.findViewById(R.id.tvDestination);
            holder.tvDates = convertView.findViewById(R.id.tvDates);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.tvBookingStatus = convertView.findViewById(R.id.tvBookingStatus);
            holder.btnPayNow = convertView.findViewById(R.id.btnPayNow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TravelEaseModel item = booklist.get(position);
        String status = item.getBookstatus();

        // Set text values
        holder.tvPackageName.setText("Package: " + item.getBook_packname());
        holder.tvDestination.setText("Destination: " + item.getBook_destname());
        holder.tvDates.setText("Dates: " + item.getBook_departuredate()+" - "+item.getBook_returndate());
        holder.tvPrice.setText("Price: ₹" + item.getBookprice());

        // Set booking status
        if ("Paid".equalsIgnoreCase(status)) {
            holder.tvBookingStatus.setText("Status: Paid");
            holder.tvBookingStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.btnPayNow.setVisibility(View.GONE);
        } else {
            holder.tvBookingStatus.setText("Status: Booked");
            holder.tvBookingStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.btnPayNow.setVisibility(View.VISIBLE);
        }


        // Handle Pay Now button
        holder.btnPayNow.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), PaymentActivity.class);
            i.putExtra("bid", item.getBookid());
            i.putExtra("uid", item.getBookuid());
            i.putExtra("packid", item.getBookpackid());
            i.putExtra("pack_na", item.getBook_packname());
            i.putExtra("price", item.getBookprice());
            getContext().startActivity(i);
        });

        return convertView;
    }
}
