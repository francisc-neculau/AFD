package com.francisc.afd;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements FileDescriptorViewHolder.FileDescriptorListener, View.OnClickListener
{
    /*
    TO-DO:
        AFD -  Create a file manager for Android. The user should be able to easily navigate through the filesystem and perform operations such as
    --------
    Optional
    --------
        I. make a bottom toolbar with following buttons :

            1. Copy ( selected FileDescriptors )
                - solves d.1 and d.2
            2. Cut ( selected FileDescriptors )
                - solves e.1 and e.2
            3. Paste ( selected FileDescriptors )
                - works with 1. and 2.
            4. Delete ( selected FileDescriptors )
                - solves b.1 and b.2

        II. Make screen Orientation posibility + different display view

        III. In the Toolbar display current folder + { storage folder + number of files }

        IV. Make the Items Transparent

        V. Sort the items
    ---------
    Mandatory
    ---------
        a.1 create a file
        a.2 edit a text file
        a.3 create a folder

        b.1 deleting a file
        b.2 deleting a folder

        c.1 renaming a file
        c.2 renaming a folder

        d.1 copying a file
        d.2 copying a folder,

        e.1 moving a file
        e.2 moving a folder

     */

    private Toolbar headerToolbar;
    private Toolbar footerToolbar;
    private RecyclerView recyclerView;
    private MainActivityController listener;
    private NavigationDrawerFragment navigationDrawerFragment;
    private DirectoryExplorer explorer;
    private FileDescriptorRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listener = new MainActivityController();

        /*
            Header Toolbar and Footer Toolbar
         */
        headerToolbar = (Toolbar) findViewById(R.id.MainActivity_Toolbar_headerToolbar);
        headerToolbar.setTitle("");
        headerToolbar.inflateMenu(R.menu.menu_activity_main);
        headerToolbar.setOnMenuItemClickListener(listener);
        //headerToolbar.setNavigationIcon(R.drawable.back_normal);

        headerToolbar.setNavigationIcon(R.drawable.abs__ic_ab_back_holo_dark);
        headerToolbar.setNavigationOnClickListener(this);

        footerToolbar = (Toolbar) findViewById(R.id.MainActivity_Toolbar_headerToolbar);
//        headerToolbar.setNavigationIcon();
//        headerToolbar.setNavigationOnClickListener();
        /*
            Recycler View
         */
        recyclerView = (RecyclerView) findViewById(R.id.MainActivity_RecyclerView_RecyclerView);
        explorer = new DirectoryExplorer(this.getApplicationContext()); // Maybe it should be a single threaded singleton !
        adapter = new FileDescriptorRecyclerViewAdapter(this, this); // here indeed we should pass just a list !
        adapter.setData(explorer.getData());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /*
            Navigation Drawer
         */
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drwr_fragment);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerFragment.setUpDrawer(R.id.nav_drwr_fragment, drawerLayout, headerToolbar);

        headerToolbar.setTitle(explorer.getCurrentFileDescriptorName());
        headerToolbar.setSubtitle(explorer.getRoot());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        this.explorer.goTo(savedInstanceState.getString("PATH"));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("PATH", this.explorer.getCurrentPath());
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    public void goToInternalStorage(View view)
    {
        explorer.goTo(explorer.INTERNAL_STORAGE_ROOT_PATH);
        // navigationDrawerFragment close the menu
        adapter.notifyDataSetChanged();
    }

    public void goToExternalStorage(View view)
    {
        explorer.goTo(explorer.EXTERNAL_STORAGE_ROOT_PATH);
        // navigationDrawerFragment close the menu
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(FileDescriptorViewHolder view)
    {
        if(view.getCurrent().isDirectory())
        {
            explorer.goTo(view.getCurrent().getPath());
            headerToolbar.setTitle(explorer.getCurrentFileDescriptorName());
            headerToolbar.setSubtitle(explorer.getRoot());
            adapter.setData(explorer.getData());
            adapter.notifyDataSetChanged();
        }
        else
        {
            //we should try to open if it is a txt file
        }
    }

    @Override
    public void onCheckedChanged(FileDescriptorViewHolder view)
    {
        if(view.isChecked())
            explorer.pushCheckedStack(view.getCurrent().getPath());
        else
            explorer.removeFromCheckedStack(view.getCurrent().getPath());
        if(explorer.isEmptyCheckedStack())
            ((ImageView)findViewById(R.id.pasteButton)).setBackgroundResource(R.drawable.btn_paste_off);
    }

    @Override
    public void onClick(View v)
    {
        explorer.goBack();
        headerToolbar.setTitle(explorer.getCurrentFileDescriptorName());
        headerToolbar.setSubtitle(explorer.getRoot());
        adapter.setData(explorer.getData());
        adapter.notifyDataSetChanged();
    }

    /*
    Actions of the Header Toolbar
     */
    public void onClickButtonCut(View view)
    {
        explorer.setFlushCheckedStackType(explorer.FLUSH_TYPE_CUT);
        if(!explorer.isEmptyCheckedStack())
            ((ImageView)findViewById(R.id.pasteButton)).setBackgroundResource(R.drawable.btn_paste_on);
    }

    public void onClickButtonCopy(View view)
    {
        explorer.setFlushCheckedStackType(explorer.FLUSH_TYPE_COPY);
        if(!explorer.isEmptyCheckedStack())
            ((ImageView)findViewById(R.id.pasteButton)).setBackgroundResource(R.drawable.btn_paste_on);
    }

    public void onClickButtonPaste(View view)
    {
        explorer.flushCheckedStack();
        explorer.refresh();
        adapter.setData(explorer.getData());
        adapter.notifyDataSetChanged();
        ((ImageView)findViewById(R.id.pasteButton)).setBackgroundResource(R.drawable.btn_paste_off);
    }

    public void onClickButtonDelete(View view)
    {
        // Cannot delete if not on the directory with the files to be deleted !
        explorer.setFlushCheckedStackType(explorer.FLUSH_TYPE_DELETE);
        // this should be divided with a AlerdDialog !
        explorer.flushCheckedStack();
        explorer.refresh();
        adapter.setData(explorer.getData());
        adapter.notifyDataSetChanged();
    }

    public void onClickButtonNewFile(View view)
    {
        /*
        Open editing activity
        insert text blabla
        closing, return the data and explorer must create the file
         */
        Intent intent = new Intent();
    }

}
