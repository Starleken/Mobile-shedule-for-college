package com.example.myapplication.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.GroupAdapter;
import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.databinding.FragmentGalleryBinding;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private RecyclerView groupRecyclerView;

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        groupRecyclerView = binding.GroupRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        groupRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group());
        groups.add(new Group());
        groups.add(new Group());

        GroupAdapter groupAdapter = new GroupAdapter(groups, getContext());
        groupRecyclerView.setAdapter(groupAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}