package com.javaprac;

public enum Permission {

    READ(1), WRITE(2), EDIT(3);

    private int id;

    private Permission(int id)
    {
        this.id = id;
    }

    public boolean allow_write()
    {
        return this.id >= WRITE.id;
    }

    public boolean allow_edit()
    {
        return this.id >= EDIT.id;
    }
}
