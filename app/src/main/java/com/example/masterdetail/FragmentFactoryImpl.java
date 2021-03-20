package com.example.masterdetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

public class FragmentFactoryImpl extends FragmentFactory {
    
    private boolean mIsInEditMode = false;
    private long mNoteId = 0l;
    
    public FragmentFactoryImpl(long noteId, boolean isInEditMode){
        this.mNoteId = noteId;
        this.mIsInEditMode = isInEditMode;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class clazz = loadFragmentClass(classLoader, className);
        if(clazz == SecondFragment.class) return new SecondFragment(mNoteId, mIsInEditMode);
        return super.instantiate(classLoader, className);
    }
}

