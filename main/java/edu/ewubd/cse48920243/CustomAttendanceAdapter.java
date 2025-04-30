package edu.ewubd.cse48920243;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class CustomAttendanceAdapter extends ArrayAdapter<Attendance> {
    private final Context context;
    private final ArrayList<Attendance> records;

    public CustomAttendanceAdapter(@NonNull Context context, @NonNull ArrayList<Attendance> records) {
        super(context, -1, records);
        this.context = context;
        this.records = records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_attendance, parent, false);

        TextView tvSN = rowView.findViewById(R.id.tvSN);
        TextView tvID = rowView.findViewById(R.id.tvID);
        TextView tvName = rowView.findViewById(R.id.tvName);
        RadioButton rdPresent = rowView.findViewById(R.id.rdPresent);
        RadioButton rdAbsent = rowView.findViewById(R.id.rdAbsent);

        tvSN.setText(String.valueOf(position+1));
        tvID.setText(records.get(position).id);
        tvName.setText(records.get(position).name);

        //System.out.println(records.get(position).status);

        rdPresent.setChecked(records.get(position).status==1);
        rdAbsent.setChecked(records.get(position).status==0);

        rdPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                records.get(position).status = 1;
            }
        });
        rdAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                records.get(position).status = 0;
            }
        });
        return rowView;
    }
}
