package com.rooksoto.parallel.activityhub.eventmap;

import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rooksoto.parallel.R;
import com.rooksoto.parallel.utility.CustomAlertDialog;
import com.rooksoto.parallel.utility.PinView;
import com.rooksoto.parallel.utility.widgets.recyclerview.AttendeesAdapter;

public class FragmentEventMap extends Fragment implements FragmentEventMapPresenter.Listener {
    private View rootView;
    private PinView imageView;
    private ImageButton imageButtonExit;
    private RecyclerView recyclerViewAttendees;
    private BottomSheetLayout bottomSheet;
    private View sheetView;
    private FragmentEventMapPresenter fragEventMapPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragEventMapPresenter = new FragmentEventMapPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hub_event_map, container, false);
        imageView = (PinView) rootView.findViewById(R.id.imageView);
        bottomSheet = (BottomSheetLayout) rootView.findViewById(R.id.bottomsheet);
        sheetView = inflater.inflate(R.layout.attendee_bottom_sheet, bottomSheet, false);
        imageButtonExit = (ImageButton) rootView.findViewById(R.id.activity_hub_action_bar_button);
        setOnClickListeners();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragEventMapPresenter.onViewCreated();
        imageView.setImage(ImageSource.resource(R.drawable.floorplan));
        // TODO grab list of coordinates from firebase and display all
        imageView.addPin(new PointF(293.5547f, 1392.5f));
        imageView.addPin(new PointF(890.15625f, 1356.875f));
        imageView.addPin(new PointF(1221.3281f, 1079.375f));
    }

    @Override
    public void onResume() {
        super.onResume();
        final GestureDetector gestureDetector = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                        if (imageView.isReady()) {
                            PointF coordinate = imageView.viewToSourceCoord(e.getX(), e.getY());
                            imageView.setPin(coordinate);
                            bottomSheet.showWithSheetView(sheetView);
                            // TODO push coordinates to firebase & refresh view
                        }
                    }
                });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

    }

    private void setOnClickListeners() {
        imageButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog();
                customAlertDialog.exit(getActivity());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setViews(AttendeesAdapter attendeesAdapter) {
        // get bottom sheet view
        recyclerViewAttendees = (RecyclerView) sheetView.findViewById(R.id.fragment_hub_attendees_recyclerview);
        recyclerViewAttendees.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAttendees.setAdapter(attendeesAdapter);

        imageButtonExit = (ImageButton) rootView.findViewById(R.id.activity_hub_action_bar_button);
    }
}
