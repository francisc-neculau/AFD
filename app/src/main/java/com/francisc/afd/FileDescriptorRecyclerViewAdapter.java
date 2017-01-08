package com.francisc.afd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by franc on 1/7/2017.
 */

public class FileDescriptorRecyclerViewAdapter extends RecyclerView.Adapter<FileDescriptorViewHolder>
{

    private static final String TAG = FileDescriptorRecyclerViewAdapter.class.getSimpleName();

    private DirectoryExplorer dataList;
    private LayoutInflater inflater;
    private FileDescriptorViewHolder.OnClickListener listener;
    public FileDescriptorRecyclerViewAdapter(Context context, DirectoryExplorer dataList, FileDescriptorViewHolder.OnClickListener listener)
    {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public FileDescriptorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.card_view_file_descriptor, parent, false);
        FileDescriptorViewHolder holder = new FileDescriptorViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(FileDescriptorViewHolder holder, int position)
    {
        FileDescriptor current = dataList.get(position);
        holder.setData(current, position);
        //holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

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
