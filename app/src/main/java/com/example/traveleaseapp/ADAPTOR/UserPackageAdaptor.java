package com.example.traveleaseapp.ADAPTOR;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.R;
import com.example.traveleaseapp.USER_MODULE.BookingPackages;

import java.util.List;

public class UserPackageAdaptor extends ArrayAdapter<TravelEaseModel> {

    private final Activity context;
    private final List<TravelEaseModel> list;

    public UserPackageAdaptor(Activity context, List<TravelEaseModel> list) {
        super(context, R.layout.activity_user_package_adaptor, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_user_package_adaptor, parent, false);

            holder = new ViewHolder();
            holder.packName = convertView.findViewById(R.id.packName);
            holder.destName = convertView.findViewById(R.id.destiName);
            holder.packPrice = convertView.findViewById(R.id.packPrice);
            holder.packDuration = convertView.findViewById(R.id.packDuration);
            holder.packDesc = convertView.findViewById(R.id.packDescription);
            holder.packInclusions = convertView.findViewById(R.id.packInclusions);
            holder.packExclusions = convertView.findViewById(R.id.packExclusions);
            holder.departureDate = convertView.findViewById(R.id.departureDate);
            holder.returnDate = convertView.findViewById(R.id.returnDate);
            holder.packImage = convertView.findViewById(R.id.packImage);
            holder.bookBtn = convertView.findViewById(R.id.bookNowButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TravelEaseModel packageItem = list.get(position);

        holder.packName.setText(packageItem.getPackageName());
        holder.destName.setText(packageItem.getDestinationName());
        holder.packPrice.setText("₹" + packageItem.getPrice());
        holder.packDuration.setText("Duration: " + packageItem.getDuration() + " days");
        holder.packDesc.setText(packageItem.getDescription());
        holder.packInclusions.setText("Includes: " + packageItem.getInclusions());
        holder.packExclusions.setText("Excludes: " + packageItem.getExclusions());
        holder.departureDate.setText("Departure: " + packageItem.getDepartureDate());
        holder.returnDate.setText("Return: " + packageItem.getReturnDate());

        // Decode and set image
        String imgg = packageItem.getPackageImage();
        if (imgg != null && !imgg.isEmpty()) {
            try {
                byte[] imageAsBytes = Base64.decode(imgg, Base64.DEFAULT);
                holder.packImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.bookBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingPackages.class);
            intent.putExtra("package_id", packageItem.getPackageId());
            intent.putExtra("package_name", packageItem.getPackageName());
            intent.putExtra("destination_name", packageItem.getDestinationName());
            intent.putExtra("price", packageItem.getPrice());
            intent.putExtra("duration", packageItem.getDuration());
            intent.putExtra("description", packageItem.getDescription());
            intent.putExtra("inclusions", packageItem.getInclusions());
            intent.putExtra("exclusions", packageItem.getExclusions());
            intent.putExtra("departure_date", packageItem.getDepartureDate());
            intent.putExtra("return_date", packageItem.getReturnDate());
            intent.putExtra("package_image", packageItem.getPackageImage());
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        TextView packName, destName, packPrice, packDuration, packDesc, packInclusions, packExclusions, departureDate, returnDate;
        ImageView packImage;
        Button bookBtn;
    }
}
