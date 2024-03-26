package com.javaprac.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Permission {
    private Levels level;

    public enum Levels {
        READ, WRITE, EDIT
    }

    public Permission(Levels level)
    {
        this.level = level;
    }

    public Levels getLevel()
    {
        return level;
    }

    public boolean allow_write()
    {
        if (level == Levels.READ) {
            return false;
        } else {
            return true;
        }
    }

    public boolean allow_edit()
    {
        if (level == Levels.EDIT) {
            return true;
        } else {
            return false;
        }
    }
}
