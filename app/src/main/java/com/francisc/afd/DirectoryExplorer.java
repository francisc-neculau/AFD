package com.francisc.afd;

import android.view.View;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by franc on 1/7/2017.
 */

public class DirectoryExplorer
{
    public static final String INTERNAL_STORAGE_ROOT_PATH = "/storage/emulated/0";
    public static final String EXTERNAL_STORAGE_ROOT_PATH = "/storage/sdcard1"; // -> Make sure you can access it !!! also it should be possible to implement some sort of listener to know when it's available
    // make a memory stack to know how the user moves through the filesistem
    // when you go back to a file that has been moved it should kill the rest of the stack
    private  ArrayList<FileDescriptor> list;
    private HashMap<Integer, FileDescriptor> checked;

    private ArrayDeque<String> stack;

    public DirectoryExplorer()
    {
        stack = new ArrayDeque<String>();
        list  = new ArrayList<FileDescriptor>();
        goTo(INTERNAL_STORAGE_ROOT_PATH);
    }

    public ArrayList<FileDescriptor> getData()
    {
        return list;
    }

    public int size()
    {
        return this.list.size();
    }

    public FileDescriptor get(int position)
    {
        return this.list.get(position);
    }

    public void updateChecked()
    {}

    // first you should check if we can go in there !
    // and throw an exception
    public void goTo(String path)
    {
        File root = new File(path);//Environment.getDataDirectory();
        list.clear();
        for (File f : root.listFiles())
        {
            if(f.isDirectory())
                list.add(new FileDescriptor(f.getAbsolutePath(), f.getName(), "", FileDescriptor.Type.Directory));
            else
                list.add(new FileDescriptor(f.getAbsolutePath(), f.getName(), "", FileDescriptor.Type.File));
        }
        stack.push(path);
    }

    public void goBack()
    {
        stack.poll(); // eliminate the current location
        String path = stack.poll(); // eliminate also the parent location, because it will be put back in the goTo method
        goTo(path);
    }

    public void canGo(){}
}
