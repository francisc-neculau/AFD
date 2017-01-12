package com.francisc.afd;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by franc on 1/7/2017.
 */

public class FileDescriptorViewHolder extends RecyclerView.ViewHolder
{
    ImageView typeImageView;
    TextView  nameTextView;
    TextView  detailsTextView;
    CheckBox  selectedCheckBox;

    private FileDescriptor fileDescriptor;
    private int fileDescriptorPosition; // maybe this should not exist at all

    public FileDescriptorViewHolder(View view)
    {
        super(view);
        typeImageView    = (ImageView) view.findViewById(R.id.FileDescriptor_ImageView_Type);
        nameTextView     = (TextView)  view.findViewById(R.id.FileDescriptor_TextView_Name);
        detailsTextView  = (TextView)  view.findViewById(R.id.FileDescriptor_TextView_Details);
        selectedCheckBox = (CheckBox)  view.findViewById(R.id.FileDescriptor_CheckBox_IsSelected);
    }

    public void setData(FileDescriptor fileDescriptor, int fileDescriptorPosition)
    {
        if(fileDescriptor.isDirectory())
            this.typeImageView.setImageResource(R.drawable.folder_103);
        else
            this.typeImageView.setImageResource(R.drawable.txt_103);
        this.nameTextView.setText(fileDescriptor.getName());
        this.detailsTextView.setText(fileDescriptor.getDetails());
        this.selectedCheckBox.setChecked(fileDescriptor.isSelected());

        this.fileDescriptorPosition = fileDescriptorPosition;
        this.fileDescriptor = fileDescriptor;
    }

    public int getCurrentPosition()
    {
        return this.fileDescriptorPosition;
    }

    public FileDescriptor getCurrent() { return fileDescriptor; }

    public void setCurrent(FileDescriptor current) {
        this.fileDescriptor = current;
    }

    public boolean isChecked() { return this.selectedCheckBox.isChecked(); }

    interface FileDescriptorListener
    {
        public void onClicked(FileDescriptorViewHolder view);
        public void onCheckedChanged(FileDescriptorViewHolder view);
    }

    public void setMainListener(final FileDescriptorListener listener)
    {
        typeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onClicked(FileDescriptorViewHolder.this);
            }
        });
        selectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                FileDescriptorViewHolder.this.fileDescriptor.setIsSelected(isChecked);
                listener.onCheckedChanged(FileDescriptorViewHolder.this);
            }
        });
//        selectedCheckBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v)
//            {
//                if(FileDescriptorViewHolder.this.selectedCheckBox.isActivated())
//                    FileDescriptorViewHolder.this.selectedCheckBox.setActivated(false);
//                else
//                    FileDescriptorViewHolder.this.selectedCheckBox.setActivated(true);
//                FileDescriptorViewHolder.this.fileDescriptor.setIsSelected();
//                adapter.notifyItemChanged(FileDescriptorViewHolder.this.fileDescriptorPosition);
//                listener.onCheckedChanged(FileDescriptorViewHolder.this);
//            }
//        });
    }

}
