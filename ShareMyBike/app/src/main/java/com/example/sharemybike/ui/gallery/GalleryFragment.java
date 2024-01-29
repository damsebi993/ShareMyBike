package com.example.sharemybike.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.sharemybike.BikeActivity;
import com.example.sharemybike.MainActivity;
import com.example.sharemybike.R;
import com.example.sharemybike.databinding.FragmentGalleryBinding;
import com.example.sharemybike.ui.home.BikeFragment;

public class GalleryFragment extends Fragment {
    private CalendarView calendarioView;
    private TextView dateTextView;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarioView = root.findViewById(R.id.calendarView2);
        dateTextView = root.findViewById(R.id.textView2);
        Button btncontinuar= root.findViewById(R.id.button);

        calendarioView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarioView, int year, int month, int dayOfMonth) {
                //actualizar el TextView
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                dateTextView.setText("Date: " + selectedDate);
            }
        });
        btncontinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_home);

                //Navega al HomeFragment que contiene el RecyclerView (BikeFragment)
                navController.navigate(R.id.nav_home);
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