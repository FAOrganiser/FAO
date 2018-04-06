package com.cristina.fao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.AdapterViewHolder> {

    OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onClick();
    }

    public ListAdapter(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public ListAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.list_element;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.AdapterViewHolder holder, int position) {
        // TODO: de adaugat numele, configurat containere
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mListName;
        public AdapterViewHolder(View itemView) {
            super(itemView);

            /* o sa contina numele listei */
            mListName = (TextView) itemView.findViewById(R.id.list_name);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick();
        }
    }
}
