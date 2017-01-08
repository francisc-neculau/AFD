package com.francisc.afd;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by franc on 1/6/2017.
 */

public class MainActivityController implements Toolbar.OnMenuItemClickListener
{
    private Toolbar headerToolbar;

    private NavigationDrawerFragment navigationDrawerFragment;
    private DirectoryExplorer explorer;

    private RecyclerView recyclerView;
    private FileDescriptorRecyclerViewAdapter adapter;

    public MainActivityController()
    {
        //recyclerView.setOn
    }


    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        return false;
    }
}
