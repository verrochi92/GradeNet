// UserWrapper.java
// Cursor wrapper for User objects
// By Nicholas Verrochi
// Last Modified: 5/3/18

package com.bhcc.nick.gradenet.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bhcc.nick.gradenet.User;

import com.bhcc.nick.gradenet.database.Schema.UserTable;

import java.util.UUID;

public class UserWrapper extends CursorWrapper {

    public UserWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(UserTable.Cols.UUID)));
        String firstName = getString(getColumnIndex(UserTable.Cols.FIRST_NAME));
        String lastName = getString(getColumnIndex(UserTable.Cols.LAST_NAME));
        String username = getString(getColumnIndex(UserTable.Cols.USERNAME));
        String password = getString(getColumnIndex(UserTable.Cols.PASSWORD));
        int userLevel = Integer.parseInt(getString(getColumnIndex(UserTable.Cols.USER_LEVEL)));

        User user = new User();
        user.setUUID(uuid);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setUserLevel(userLevel);

        return user;
    }
}
