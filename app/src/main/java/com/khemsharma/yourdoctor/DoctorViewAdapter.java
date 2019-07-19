package com.khemsharma.yourdoctor;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.khemsharma.yourdoctor.Models.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewAdapter extends RecyclerView.Adapter<DoctorViewAdapter.ViewHolder> implements Filterable {

    Context context;
    List<Doctor> doctorsList,doctorListFilterd;

    public DoctorViewAdapter(Context context, List<Doctor> doctorsList) {
        this.context = context;
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_list,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final Doctor doctor=doctorsList.get(position);

        viewHolder.name.setText(doctor.getName());
        viewHolder.spcialist.setText(doctor.getSpecialist());




        viewHolder.listCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked:"+position, Toast.LENGTH_SHORT).show();
                Intent doctorsDetails = new Intent(context,DoctorDetails.class);
                doctorsDetails.putExtra("doctorId",doctor.getDoctorID());
                context.startActivity(doctorsDetails);
            }
        });


    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    doctorListFilterd = doctorsList;
                } else {
                    List<Doctor> filteredList = new ArrayList<>();
                    for (Doctor row : doctorsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    doctorListFilterd = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = doctorListFilterd;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                doctorListFilterd = (ArrayList<Doctor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface DoctorAdapterListner {
        void onDoctorSelected(Doctor doctor);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,spcialist;
        CardView listCardView;
        public View view;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;

            name=itemView.findViewById(R.id.docName);
            spcialist=itemView.findViewById(R.id.docSpecialist);
            listCardView=itemView.findViewById(R.id.listCardView);


        }


    }



    }

