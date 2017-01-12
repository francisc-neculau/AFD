package com.francisc.afd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by franc on 1/7/2017.
 */

public class FileDescriptorRecyclerViewAdapter extends RecyclerView.Adapter<FileDescriptorViewHolder> /*implements  View.OnClickListener, CompoundButton.OnCheckedChangeListener*/
{
    private static final String TAG = FileDescriptorRecyclerViewAdapter.class.getSimpleName();

    private ArrayList<FileDescriptor> fileDescriptorsList;
    private LayoutInflater inflater;
    private FileDescriptorViewHolder.FileDescriptorListener listener;

    public FileDescriptorRecyclerViewAdapter(Context context, FileDescriptorViewHolder.FileDescriptorListener listener)
    {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public FileDescriptorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.card_view_file_descriptor, parent, false);
        FileDescriptorViewHolder holder = new FileDescriptorViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FileDescriptorViewHolder holder, int position)
    {
        FileDescriptor fileDescriptor = fileDescriptorsList.get(position);
        holder.setData(fileDescriptor, position);
        holder.setMainListener(listener);
        //holder.selectedCheckBox.setChecked();
    }

    @Override
    public void onViewRecycled(FileDescriptorViewHolder holder) {
        super.onViewRecycled(holder);
        holder.selectedCheckBox.setOnCheckedChangeListener(null);
    }

    @Override
    public int getItemCount() {
        return fileDescriptorsList.size();
    }

    public void setData(ArrayList<FileDescriptor> fileDescriptorsList) { this.fileDescriptorsList = fileDescriptorsList; }

//    /*
//     View.OnClickListener
//     */
//    @Override
//    public void onClick(View v)
//    {
//        listener.onClicked((FileDescriptorViewHolder)v);
//    }
//    /*
//    CompoundButton.OnCheckedChangeListener
//     */
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        listener.onCheckedChanged(FileDescriptorViewHolder.this);
//    }

//    public void removeItem(int position) {
//        mDataList.remove(position);
//        notifyItemRemoved(position);
////		notifyItemRangeChanged(position, mDataList.size());
////		notifyDataSetChanged();
//    }
//
//    public void addItem(int position, Landscape landscape) {
//        mDataList.add(position, landscape);
//        notifyItemInserted(position);
////		notifyItemRangeChanged(position, mDataList.size());
////		notifyDataSetChanged();
//    }
}
