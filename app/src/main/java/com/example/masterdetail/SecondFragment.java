package com.example.masterdetail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    private EditText mTitleText;
    private EditText mDetailText;
    private Button mSaveButton;

    private long mNoteId = 0L;
    private boolean mIsInEditMode = false;

    public static String UPDATE_VIEWS = "UPDATE_VIEWS";
    public static String NOTEID_KEY = "NOTEID_KEY";
    public static String EDITMODE_KEY = "EDITMODE_KEY";

    public SecondFragment(long noteId, boolean isInEditMode){
        this.mNoteId = noteId;
        this.mIsInEditMode = isInEditMode;
    }

    public SecondFragment(){

    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleText = view.findViewById(R.id.titleEditText);
        mDetailText = view.findViewById(R.id.editTextTextMultiLine);
        mSaveButton = view.findViewById(R.id.saveButton);
        if(MainActivity.class.cast(getActivity()).isTablet()){
            getParentFragmentManager().setFragmentResultListener(UPDATE_VIEWS, this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                    mNoteId = bundle.getLong(NOTEID_KEY,0l);
                    mIsInEditMode = bundle.getBoolean(EDITMODE_KEY,false);
                    if(mNoteId != 0l) {
                        upDateViews();
                    }
                    if(mNoteId == 0l && mIsInEditMode){
                        newMode();
                    }
                }
            });

        }
        else {
            mNoteId = SecondFragmentArgs.fromBundle(getArguments()).getNoteId();
            mIsInEditMode = SecondFragmentArgs.fromBundle(getArguments()).getIsEditable();
        }
        upDateViews();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note();
                note.setId(mNoteId);
                note.setTitle(mTitleText.getText().toString());
                note.setDetail(mDetailText.getText().toString());
                DBHelper.getsInstance(getActivity().getApplicationContext()).insertorUpdate(note);
                if(MainActivity.class.cast(getActivity()).isTablet()){
                    readOnlyMode();
                    Bundle result = new Bundle();
                    result.putString("bundleKey", "result");
                    getParentFragmentManager().setFragmentResult(FirstFragment.UPDATE_LIST, result);

                }
                else{
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
            }
        });



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

   private void readOnlyMode(){
       mTitleText.setEnabled(false);
       mDetailText.setEnabled(false);

       //To make desiabled text appear normal black
       int start = 0;
       int end = mTitleText.getText().toString().length();
       int flag = Spannable.SPAN_INCLUSIVE_INCLUSIVE;
       mTitleText.getText().setSpan(new ForegroundColorSpan(Color.BLACK), start, end, flag);
       mDetailText.getText().setSpan(new ForegroundColorSpan(Color.BLACK),
              0, mDetailText.getText().toString().length(), flag);
       mSaveButton.setVisibility(View.INVISIBLE);
   }

   private void newMode(){
        mTitleText.setEnabled(true);
        mDetailText.setEnabled(true);
        mTitleText.setText("");
        mDetailText.setText("");
        mSaveButton.setVisibility(View.VISIBLE);
   }

    private void editMode(){
        mTitleText.setEnabled(true);
        mDetailText.setEnabled(true);
        mSaveButton.setVisibility(View.VISIBLE);
    }


    private void setData(Note note){
       mTitleText.setText(note.getTitle());
       mDetailText.setText(note.getDetail());
   }

   private void upDateViews(){
       if (mNoteId != 0) {
           Note note = DBHelper.getsInstance(getActivity().getApplicationContext()).getNoteByid(mNoteId);
           setData(note);
           if (!mIsInEditMode) {
               readOnlyMode();
           }
           else{
               if(MainActivity.class.cast(getActivity()).isTablet())
                editMode();
           }
       }
   }
}