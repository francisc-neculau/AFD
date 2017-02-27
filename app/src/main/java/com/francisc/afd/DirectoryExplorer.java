package com.francisc.afd;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by franc on 1/7/2017.
 */

public class DirectoryExplorer
{
    private Context context;

    private static final String PATH_SEPARATOR = "/";
    private static final String EMPTY_PATH = "";
    public static final String INTERNAL_STORAGE_ROOT_PATH = "/storage/emulated/0";
    public static final String TEMPORARY_DIRECTORY_PATH = "/storage/emulated/0/afd_tmp";
    public static final String EXTERNAL_STORAGE_ROOT_PATH = "/storage/sdcard1"; // -> Make sure you can access it !!! also it should be possible to implement some sort of listener to know when it's available

    public static final String FLUSH_TYPE_CUT    = "ct";
    public static final String FLUSH_TYPE_COPY   = "cp";
    public static final String FLUSH_TYPE_DELETE = "d";
    // make a memory navigationHistoryStack to know how the user moves through the filesistem
    // when you go back to a file that has been moved it should kill the rest of the navigationHistoryStack
    private  ArrayList<FileDescriptor> list;
    private HashMap<Integer, FileDescriptor> checked;
    private String root;
    private ArrayDeque<String> checkedStack;
    private String checkedStackDirectoryPath;
    private String flushType;
    private ArrayDeque<String> navigationHistoryStack;


    public DirectoryExplorer(Context context)
    {
        this.context = context;
        checkedStack = new ArrayDeque<String>();
        checkedStackDirectoryPath = EMPTY_PATH;
        flushType = "";
        navigationHistoryStack = new ArrayDeque<String>();
        list  = new ArrayList<FileDescriptor>();
        root = INTERNAL_STORAGE_ROOT_PATH;
        goTo(root);
    }

    private void setUpTemporaryDirectory()
    {
        File tmp = new File(TEMPORARY_DIRECTORY_PATH);
        if(tmp.exists())
            deleteFile(tmp);
        else
            createDirectory(TEMPORARY_DIRECTORY_PATH);
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

    // first you should check if we can go in there !
    // and throw an exception
    public void goTo(String path)
    {
        File root = new File(path);//Environment.getDataDirectory();
        list.clear();
        for (File f : root.listFiles())
        {
            if(f.isDirectory())
                list.add(new FileDescriptor(f.getAbsolutePath(), f.getName(), "", false, FileDescriptor.Type.Directory));
            else
                list.add(new FileDescriptor(f.getAbsolutePath(), f.getName(), "", false, FileDescriptor.Type.File));
        }
        navigationHistoryStack.push(path);
    }

    public void goBack()
    {
        navigationHistoryStack.poll(); // eliminate the current location
        String path = navigationHistoryStack.poll(); // eliminate also the parent location, because it will be put back in the goTo method
        goTo(path);
    }

    public void canGo(){}

    public void setFlushCheckedStackType(String flushType)
    {
        // FIXME: This should be an enum.
        this.flushType = flushType;
    }

    public void removeFromCheckedStack(String filePath)
    {
        if(checkedStack.size() == 1)
            checkedStackDirectoryPath = EMPTY_PATH;
        checkedStack.remove(filePath);
//        for(int index = 0; index < checked.size(); index++)
//            if(checked.get(index).equals(filePath))
//                checked.remove(index);
    }

    public void pushCheckedStack(String filePath)
    {
        /*
                If the parent directory of the file is the same,
            add it to the stack otherwise clear the stack and add
            it. Clearing the stack happens because the user has
            changed the directory and now wants to copy/cut files
            from this directory.
         */
        String directoryPath = filePath.substring(0, filePath.lastIndexOf(PATH_SEPARATOR));
        if(directoryPath.equals(checkedStackDirectoryPath))
            checkedStack.push(filePath);
        else
        {
            checkedStack.clear();
            checkedStackDirectoryPath = directoryPath;
            checkedStack.push(filePath);
        }
    }

    public void flushCheckedStack()
    {
        FileChannel in;
        FileChannel out;
        boolean isCompleted = true;
        File sourceFile, destinationFile;
        String newFilePath;
        int numberOfFilesSuccessfulProcessed = 0;
        switch (flushType)
        {
            case FLUSH_TYPE_CUT:
                for (String filePath : checkedStack)
                {
                    sourceFile = new File(filePath);
                    newFilePath = (navigationHistoryStack.peek() + filePath.substring(filePath.lastIndexOf(PATH_SEPARATOR)));
                    if(newFilePath.contains(filePath))
                    {
                        setUpTemporaryDirectory();
                        copyAll(sourceFile, new File(TEMPORARY_DIRECTORY_PATH + PATH_SEPARATOR + sourceFile.getName()));
                        copyAll(new File(TEMPORARY_DIRECTORY_PATH + PATH_SEPARATOR + sourceFile.getName()), new File(newFilePath));
                        deleteAll(new File(TEMPORARY_DIRECTORY_PATH));
                    }
                    else
                        copyAll(new File(filePath), new File(newFilePath));
//                    if(!isCompleted)
//                        Toast.makeText(context, "File cut failed : " + filePath, Toast.LENGTH_SHORT).show();
                    deleteAll(new File(filePath));
                    numberOfFilesSuccessfulProcessed++;
                }
                Toast.makeText(context, "Moved : " + numberOfFilesSuccessfulProcessed + " files", Toast.LENGTH_SHORT).show();
                break;
            case FLUSH_TYPE_COPY:
                for (String filePath : checkedStack)
                {
                    sourceFile = new File(filePath);
                    newFilePath = (navigationHistoryStack.peek() + filePath.substring(filePath.lastIndexOf(PATH_SEPARATOR)));
                    if(newFilePath.contains(filePath))
                    {
                        setUpTemporaryDirectory();
                        copyAll(sourceFile, new File(TEMPORARY_DIRECTORY_PATH + PATH_SEPARATOR + sourceFile.getName()));
                        copyAll(new File(TEMPORARY_DIRECTORY_PATH + PATH_SEPARATOR + sourceFile.getName()), new File(newFilePath));
                        deleteAll(new File(TEMPORARY_DIRECTORY_PATH));
                    }
                    else
                        copyAll(new File(filePath), new File(newFilePath));
//                    if(!isCompleted)
//                        Toast.makeText(context, "File copy failed : " + filePath, Toast.LENGTH_SHORT).show();
                    numberOfFilesSuccessfulProcessed++;
                }
                Toast.makeText(context, "Copied : " + numberOfFilesSuccessfulProcessed + " files", Toast.LENGTH_SHORT).show();
                break;
            case FLUSH_TYPE_DELETE:
                for (String filePath : checkedStack)
                {
                    isCompleted = deleteAll(new File(filePath));
                    if(!isCompleted)
                        Toast.makeText(context, "File deletion failed : " + filePath, Toast.LENGTH_SHORT).show();
                    numberOfFilesSuccessfulProcessed++;
                }
                Toast.makeText(context, "Deleted : " + numberOfFilesSuccessfulProcessed + " files", Toast.LENGTH_SHORT).show();
                break;
        }
        checkedStack.clear();
        checkedStackDirectoryPath = this.EMPTY_PATH;
    }

    private boolean deleteFile(File f)
    {
        try
        {
            f.delete();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    private boolean deleteAll(File f)
    {
        if (f.isDirectory())
        {
            String[] children = f.list();
            for (int i = 0; i < children.length; i++)
            {
                if(new File(f, children[i]).isDirectory())
                    deleteAll(new File(f, children[i]));
                else
                    new File(f, children[i]).delete();
            }
            f.delete();
        }
        else
            deleteFile(f);
        return true;
    }

    /*
    in  - File or Folder
    out - Folder
     */
    private int copyAll(File in, File out)
    {
        int counter = 0;
        if (in.isDirectory())
        {
            if (!out.exists())
                out.mkdir();

            String[] children = in.list();
            for (int i = 0; i < in.listFiles().length; i++) {
                if (new File(in, children[i]).isDirectory())
                {
                    copyAll(new File(in, children[i]), new File(out + PATH_SEPARATOR + children[i])); // a/b/c x/y/a
                }
                else
                {
                    copyFile(new File(in, children[i]), new File(out, children[i]));
                }
                counter++;
            }
        }
        else
        {
            if(copyFile(in, out))
                counter++;
        }
        return counter;
    }

    /*
    in  - File
    out - File
     */
    private boolean copyFile(File in, File out)
    {
        FileChannel inChannel;
        FileChannel outChannel;

        try { inChannel = new FileInputStream(in).getChannel(); }
        catch (IOException e)
        { return false; }

        try { outChannel = new FileOutputStream(out).getChannel(); }
        catch (IOException e)
        { return false; }

        try { inChannel.transferTo(0, inChannel.size(), outChannel); }
        catch (IOException e)
        { return false;  }
        finally
        {
            if (inChannel != null)
                try { inChannel.close(); }
                catch (IOException e) { /* */ };
            if (outChannel != null)
                try { outChannel.close(); }
                catch (IOException e) { /* */ };
        }
        return true;
    }

    public void refresh()
    {
        File root = new File(navigationHistoryStack.peek());//Environment.getDataDirectory();
        list.clear();
        for (File f : root.listFiles())
        {
            if(f.isDirectory())
                list.add(new FileDescriptor(f.getAbsolutePath(), f.getName(), "", false, FileDescriptor.Type.Directory));
            else
                list.add(new FileDescriptor(f.getAbsolutePath(), f.getName(), "", false, FileDescriptor.Type.File));
        }
    }

    private boolean createDirectory(String directoryPath)
    {
        File folder = new File(directoryPath);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return true;
    }

    public String getCurrentPath()
    {
        return navigationHistoryStack.peek();
    }

    public String getCurrentFileDescriptorName()
    {
        return navigationHistoryStack.peek().substring(navigationHistoryStack.peek().lastIndexOf("/"));
    }

    public String getRoot()
    {
        switch (root)
        {
            case INTERNAL_STORAGE_ROOT_PATH:
                return "Internal";
            default:
                return "External";
        }
    }

    public void setRoot(String root)
    {
        this.root = root;
        this.goTo(root);
    }

    public boolean isEmptyCheckedStack()
    {
        return this.checkedStack.isEmpty();
    }

    public void writeNewFile(String path)
    {
        // write the file
    }

}
