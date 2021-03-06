package com.rooksoto.parallel.activityhub.enterid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rooksoto.parallel.BasePresenter;
import com.rooksoto.parallel.R;
import com.rooksoto.parallel.activityhub.ActivityHubPresenter;
import com.rooksoto.parallel.utility.CustomAlertDialog;
import com.rooksoto.parallel.utility.geolocation.ParallelLocation;

public class FragmentHubEnterIDPresenter implements BasePresenter {
    private ActivityHubPresenter.Listener listener;


    public FragmentHubEnterIDPresenter(ActivityHubPresenter.Listener listener){
        this.listener = listener;
    }

    private static final String TAG = "EnterIDPresenter";

    @Override
    public void start () {
    }

    public void onBackPressedOverride (View viewP) {
        CustomAlertDialog customAlertDialog = new CustomAlertDialog();
        customAlertDialog.exit((Activity) viewP.getContext());
    }

    public void setOnClickReplace (Fragment fragmentP, View viewP, int containerID, String id) {
        if (!isOnline(viewP)) {
            Toast.makeText(viewP.getContext(), "Cannot Connect - Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            ((Activity) viewP.getContext()).getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.animator_fade_in, R.animator.animator_fade_out)
                    .replace(containerID, fragmentP, id)
                    .commit();
        }
    }

    private boolean isOnline (View viewP) {
        ConnectivityManager connManager = (ConnectivityManager) viewP.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    void checkEventID (final String enteredEventID, DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(enteredEventID)) {
                    // Command the view to go to next fragment
                    listener.activateParallelEvent(enteredEventID);
                    Log.d(TAG, "onDataChange: Event ID exists, load next fragment");
                    ParallelLocation location = ParallelLocation.getInstance();
                    location.setEventMonitoring();
                } else {
                    // Command the view to show an error - Event ID does not exist
                    listener.showEventIdError(enteredEventID);
                    Log.d(TAG, "onDataChange: This Event ID does not exist - " + ParallelLocation.eventID);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle cancellation
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

    }
}
