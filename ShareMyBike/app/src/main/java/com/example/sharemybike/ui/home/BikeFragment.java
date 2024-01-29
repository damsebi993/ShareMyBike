package com.example.sharemybike.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemybike.Bike;
import com.example.sharemybike.MyItemRecyclerViewAdapter;
import com.example.sharemybike.R;
import com.example.sharemybike.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BikeFragment extends Fragment {
    private MyItemRecyclerViewAdapter mAdapter;
    private List<Bike> bikeList;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bikeList = new ArrayList<>();
        mAdapter = new MyItemRecyclerViewAdapter(bikeList);
        recyclerView.setAdapter(mAdapter);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child("bikes_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bikeList.clear();


                for (DataSnapshot bikeSnapshot : snapshot.getChildren()) {
                    Bike bike = bikeSnapshot.getValue(Bike.class);
                    Log.d(TAG, "Image URL: " + bike.getImage());
                    bikeList.add(bike);
                }


                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}