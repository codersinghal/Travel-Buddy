package com.example.travelbuddy.trips;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.Adapters.PastTripsAdapter;
import com.example.travelbuddy.Adapters.UpcomingTripsAdapter;

public class SwipeToDelete extends ItemTouchHelper.SimpleCallback {
    private UpcomingTripsAdapter mAdapter;

    public SwipeToDelete(UpcomingTripsAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }
}
