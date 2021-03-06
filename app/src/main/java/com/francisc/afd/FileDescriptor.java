package com.francisc.afd;

/**
 * Created by franc on 1/7/2017.
 */

public class FileDescriptor
{
    // TO-DO : Make a display nameTextView and detailsTextView triming !
    private String path;
    private String name;
    private String details;
    private boolean isSelected;
    private Type type;

    enum Type
    {
        File,
        Directory
    }

    public FileDescriptor(String path, String name, String details, boolean isSelected, Type type)
    {
        this.path = path;
        this.name = name;
        this.details = details;
        this.isSelected = isSelected;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isSelected() { return isSelected; }

    public void setIsSelected(boolean isSelected) { this.isSelected = isSelected; }

    public boolean isDirectory()
    {
        return (this.type == Type.Directory);
    }
}
