package com.example.masterdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;


public class FirstFragment extends Fragment {

    private RecyclerView mNotesView;
    private NotesAdapter mNotesAdapter;
    public static String UPDATE_LIST = "updateList";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNotesView = view.findViewById(R.id.notesView);
        if(MainActivity.class.cast(getActivity()).isTablet()) {
            mNotesAdapter = new NotesAdapter(new NotesAdapter.ViewClickListener() {

                @Override
                public void onViewClicked(int viewId, Note note) {
                    Bundle result = new Bundle();
                    switch (viewId) {
                        case R.id.tileTextView:
                            result.putLong(SecondFragment.NOTEID_KEY,note.getId());
                            break;
                        case R.id.editButton:
                            result.putLong(SecondFragment.NOTEID_KEY,note.getId());
                            result.putBoolean(SecondFragment.EDITMODE_KEY,true);
                            break;
                        case R.id.deleteButton:
                            DBHelper.getsInstance(getActivity().getApplicationContext()).deleteByNode(note);
                            mNotesAdapter.submitList(DBHelper.getsInstance(getActivity().getApplicationContext()).getAllNotes());
                            result.putLong(SecondFragment.NOTEID_KEY,0l);
                            result.putBoolean(SecondFragment.EDITMODE_KEY,true);
                            break;

                    }
                    getParentFragmentManager().setFragmentResult(SecondFragment.UPDATE_VIEWS, result);
                }

            });

            getParentFragmentManager().setFragmentResultListener(UPDATE_LIST, this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                    mNotesAdapter.submitList(DBHelper.getsInstance(getActivity().getApplicationContext()).getAllNotes());
                }
            });


        }
        else {
            mNotesAdapter = new NotesAdapter(new NotesAdapter.ViewClickListener() {
                @Override
                public void onViewClicked(int viewId, Note note) {
                    switch (viewId) {
                        case R.id.tileTextView:
                            FirstFragmentDirections.ActionFirstFragmentToSecondFragment action =
                                    FirstFragmentDirections.actionFirstFragmentToSecondFragment();
                            action.setNoteId(note.getId());
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(action);
                            break;
                        case R.id.editButton:
                            FirstFragmentDirections.ActionFirstFragmentToSecondFragment editaction =
                                    FirstFragmentDirections.actionFirstFragmentToSecondFragment();
                            editaction.setNoteId(note.getId());
                            editaction.setIsEditable(true);
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(editaction);
                            break;
                        case R.id.deleteButton:
                            DBHelper.getsInstance(getActivity().getApplicationContext()).deleteByNode(note);
                            mNotesAdapter.submitList(DBHelper.getsInstance(getActivity().getApplicationContext()).getAllNotes());
                            break;
                    }
                }
            });



        }



        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.class.cast(getActivity()).isTablet()){
                    Bundle result = new Bundle();
                    result.putLong(SecondFragment.NOTEID_KEY,0l);
                    result.putBoolean(SecondFragment.EDITMODE_KEY,true);
                    getParentFragmentManager().setFragmentResult(SecondFragment.UPDATE_VIEWS, result);
                }else {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });



        mNotesView.setAdapter(mNotesAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mNotesAdapter.submitList(DBHelper.getsInstance(getActivity().getApplicationContext()).getAllNotes());
    }
}



