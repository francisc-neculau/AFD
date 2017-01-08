package com.francisc.afd;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by franc on 1/7/2017.
 */

public class FileDescriptorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private FileDescriptorViewHolder.OnClickListener listener;

    private ImageView type;
    private TextView name;
    private TextView details;
    private CheckBox isSelected;

    private FileDescriptor current;
    private int currentPosition; // maybe this should not exist at all

    public FileDescriptorViewHolder(View view, FileDescriptorViewHolder.OnClickListener listener)
    {
        super(view);
        type       = (ImageView) view.findViewById(R.id.FileDescriptor_ImageView_Type);
        name       = (TextView)  view.findViewById(R.id.FileDescriptor_TextView_Name);
        details    = (TextView)  view.findViewById(R.id.FileDescriptor_TextView_Details);
        isSelected = (CheckBox)  view.findViewById(R.id.FileDescriptor_CheckBox_IsSelected);

        this.listener = listener;

        type.setOnClickListener(this);
    }

    public void setData(FileDescriptor current, int position)
    {
        if(current.isDirectory())
            this.type.setImageResource(R.drawable.folder_103);
        else
            this.type.setImageResource(R.drawable.txt_103);
        this.name.setText(current.getName());
        this.details.setText(current.getDetails());
        this.isSelected.setActivated(current.isSelected());
        this.currentPosition = position;
        this.current = current;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(this);
    }

    interface OnClickListener
    {
        public void onClick(FileDescriptorViewHolder view);
    }

    public int getCurrentPosition()
    {
        return this.currentPosition;
    }

    public FileDescriptor getCurrent() {
        return current;
    }

    public void setCurrent(FileDescriptor current) {
        this.current = current;
    }

}

