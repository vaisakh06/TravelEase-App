package com.example.traveleaseapp.ADAPTOR;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.example.traveleaseapp.USER_MODULE.BookVehicles;

import java.util.List;

public class UserVehiclesAdaptor extends ArrayAdapter<TravelEaseModel> {

    private final Activity context;
    private final List<TravelEaseModel> vehicleList;

    public UserVehiclesAdaptor(Activity context, List<TravelEaseModel> vehicleList) {
        super(context, R.layout.activity_user_vehicles_adaptor, vehicleList);
        this.context = context;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_user_vehicles_adaptor, parent, false);

            holder = new ViewHolder();
            holder.vehicleName = convertView.findViewById(R.id.vehiclename);
            holder.vehicleType = convertView.findViewById(R.id.vtype);
            holder.vehicleBrand = convertView.findViewById(R.id.vbrand);
            holder.registrationNo = convertView.findViewById(R.id.registrationno);
            holder.seatingCapacity = convertView.findViewById(R.id.seatingcapacity);
            holder.luggageCapacity = convertView.findViewById(R.id.luggagecapacity);
            holder.fuelType = convertView.findViewById(R.id.fueltype);
            holder.vehicleColor = convertView.findViewById(R.id.vcolor);
            holder.vehicleStatus = convertView.findViewById(R.id.vstatus);
            holder.rentalPrice = convertView.findViewById(R.id.rentalprice);
            holder.vehicleImage = convertView.findViewById(R.id.vimage);
            holder.bookButton = convertView.findViewById(R.id.view_book); // Initialize book button

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TravelEaseModel vehicle = vehicleList.get(position);

        holder.vehicleName.setText(vehicle.getVehiclename());
        holder.vehicleType.setText("Type: " + vehicle.getVtype());
        holder.vehicleBrand.setText("Brand: " + vehicle.getVbrand());
        holder.registrationNo.setText("Reg No: " + vehicle.getRegistrationno());
        holder.seatingCapacity.setText("Seating: " + vehicle.getSeatingcapacity());
        holder.luggageCapacity.setText("Luggage: " + vehicle.getLuggagecapacity());
        holder.fuelType.setText("Fuel: " + vehicle.getFueltype());
        holder.vehicleColor.setText("Color: " + vehicle.getVcolor());
//        holder.vehicleStatus.setText("Status: " + vehicle.getVstatus());
        holder.rentalPrice.setText("Price: ₹" + vehicle.getRentalprice() + "/day");

        // Decode and set image
        String imgData = vehicle.getVimage();
        if (imgData != null && !imgData.isEmpty()) {
            try {
                byte[] imageAsBytes = Base64.decode(imgData, Base64.DEFAULT);
                holder.vehicleImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Set click listener for the book button
        holder.bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookVehicles.class);
            intent.putExtra("proid", vehicle.getProid()); // Ensure 'getProid()' exists in TravelEaseModel
            intent.putExtra("vid", vehicle.getVid()); // Ensure 'getVid()' exists in TravelEaseModel
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        TextView vehicleName, vehicleType, vehicleBrand, registrationNo, seatingCapacity, luggageCapacity, fuelType, vehicleColor, vehicleStatus, rentalPrice;
        ImageView vehicleImage;
        Button bookButton;
    }
}
