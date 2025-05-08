package com.example.traveleaseapp.ADAPTOR;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.R;

import java.util.List;

public class UserVehicleBookingAdaptor extends ArrayAdapter<TravelEaseModel> {
    private final Activity context;
    private final List<TravelEaseModel> booklist;

    public UserVehicleBookingAdaptor(Activity context, List<TravelEaseModel> booklist) {
        super(context, R.layout.activity_user_vehicle_booking_adaptor, booklist);
        this.context = context;
        this.booklist = booklist;
    }

    private static class ViewHolder {
        TextView tvVehicleName, tvDepartureDate, tvReturnDate, tvBookingStatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_user_vehicle_booking_adaptor, parent, false);

            holder = new ViewHolder();
            holder.tvVehicleName = convertView.findViewById(R.id.tvVehicleName);
            holder.tvDepartureDate = convertView.findViewById(R.id.tvDepartureDate);
            holder.tvReturnDate = convertView.findViewById(R.id.tvReturnDate);
            holder.tvBookingStatus = convertView.findViewById(R.id.tvBookingStatus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TravelEaseModel item = booklist.get(position);

        // Set text values
        holder.tvVehicleName.setText("Vehicle: " + item.getVehiclename());
        holder.tvDepartureDate.setText("Departure Date: " + item.getBdeparturedate());
        holder.tvReturnDate.setText("Return Date: " + item.getBreturndate());
        holder.tvBookingStatus.setText("Status: " + item.getBvstatus());

        return convertView;
    }
}
