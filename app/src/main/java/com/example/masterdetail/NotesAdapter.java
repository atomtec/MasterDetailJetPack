package com.example.masterdetail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public  class NotesAdapter extends ListAdapter<Note,NotesAdapter.NotesViewHolder> {

    private ViewClickListener mViewClickListener;

    protected NotesAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback) {
        super(diffCallback);

    }

    public interface ViewClickListener {
        void onViewClicked(int viewId, Note note);
    }

    public NotesAdapter(ViewClickListener listener){
        this(new DiffCallBack());
        this.mViewClickListener = listener;
    }



    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row_item, parent, false);
        //Log.d(TAG, "onCreateViewHolder (" + ++counterOnCreateViewHolder + ")");
        NotesViewHolder nvh = new NotesViewHolder(v);
        nvh.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = getItem(nvh.getAdapterPosition());
                mViewClickListener.onViewClicked(nvh.getEditButton().getId(),note);

            }
        });
        nvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = getItem(nvh.getAdapterPosition());
                mViewClickListener.onViewClicked(nvh.getDeleteButton().getId(),note);

            }
        });
        nvh.getTitleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = getItem(nvh.getAdapterPosition());
                mViewClickListener.onViewClicked(nvh.getTitleView().getId(),note);

            }
        });

        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
            holder.getTitleView().setText(getItem(position).getTitle());
    }

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final ImageButton deletButton;
        private ImageButton editButton;

        private NotesViewHolder(View itemView){
            super(itemView);
            titleView = itemView.findViewById(R.id.tileTextView);
            deletButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }

        public TextView getTitleView() {
            return titleView;
        }

        public ImageButton  getDeleteButton() { return deletButton;}

        public ImageButton getEditButton() {
            return editButton;
        }
    }

    private static class DiffCallBack extends DiffUtil.ItemCallback<Note>{

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            Log.d("EmployeeListAdapter", "oldItem " + oldItem.toString() + " new item " + newItem.toString());
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            Log.d("EmployeeListAdapter", "oldItem Equals" + oldItem.toString() + " new item " + newItem.toString());
            return oldItem.equals(newItem);
        }
    }
}