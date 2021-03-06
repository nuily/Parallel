package com.rooksoto.parallel.activityhub.enterid;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rooksoto.parallel.BaseView;
import com.rooksoto.parallel.R;
import com.rooksoto.parallel.activityhub.ActivityHubPresenter;
import com.rooksoto.parallel.utility.AppContext;
import com.rooksoto.parallel.utility.OnClickEffect;
import com.rooksoto.parallel.utility.geolocation.ParallelLocation;

@SuppressLint("ValidFragment")
public class FragmentHubEnterID extends Fragment implements BaseView {
    private FragmentHubEnterIDPresenter fragmentHubEnterIDPresenter;
    private ActivityHubPresenter.Listener listener;

    private View view;
    private EditText textViewEventID;
    private Button buttonEnter;

    private int containerID = R.id.content_frame;

    ParallelLocation location;
    FirebaseDatabase database;
    DatabaseReference reference;

    private static final String TAG = "FragmentHubEnterID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = ParallelLocation.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("ValidFragment")
    public FragmentHubEnterID(ActivityHubPresenter.Listener listener){
        fragmentHubEnterIDPresenter = new FragmentHubEnterIDPresenter(listener);
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hub_enterid, container, false);
        initialize();
        return view;
    }

    public void initialize () {
        setViews();
    }

    @Override
    public void setViews () {
        textViewEventID = (EditText) view.findViewById(R.id.fragment_start_enterid_eventid);

        buttonEnter = (Button) view.findViewById(R.id.enter_button);
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                OnClickEffect.setButton(buttonEnter);
                checkEventID();
            }
        });
        textViewEventID.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    checkEventID();
                    handled = false;
                }
                return handled;
            }
        });
    }

    private void checkEventID(){
        if (textViewEventID.getText().toString().equals("")) {
            ParallelLocation.eventID = "empty";
            shakeAnimation();
        } else {
            ParallelLocation.eventID = textViewEventID.getText().toString();
        }
        Log.d(TAG, "onClick: eventID Current Value is: " + ParallelLocation.eventID);
        reference = database.getReference();
        fragmentHubEnterIDPresenter.checkEventID(ParallelLocation.eventID, reference);
    }

    private void shakeAnimation () {
        Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_side_to_side);
        textViewEventID.startAnimation(animShake);
    }
}
