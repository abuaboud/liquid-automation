package org.liquidbot.bot.client.security;

import org.liquidbot.bot.Configuration;

import java.io.FileDescriptor;
import java.security.AccessControlContext;
import java.security.Permission;

/*
 * Created on 8/3/14
 */
public class LSecurityManager extends java.lang.SecurityManager {

    private final String[] allowedExtensions = {"ini", "png", "jpg", "tmp", "txt", "jpeg","json"};

    @Override
    public void checkPermission(final Permission perm) {
    }

    @Override
    public void checkPermission(final Permission perm, final Object context) {
        if (context instanceof AccessControlContext) {
            AccessControlContext accessControlContext = ((AccessControlContext) context);
            accessControlContext.checkPermission(perm);
        } else {
            throw new SecurityException();
        }
    }

    @Override
    public void checkWrite(FileDescriptor fd) {

    }

    @Override
    public void checkWrite(String file) {
        if (!hasAccess()) {
            boolean allowed = false;
            for (String extension : allowedExtensions) {
                if (file.endsWith("." + extension)) {
                    allowed = true;
                }
            }
            if (!allowed)
                throw new SecurityException("Denied Access " + file);
        }

    }

    public boolean hasAccess() {
        return !Thread.currentThread().equals(Configuration.getInstance().getScriptHandler().getScriptThread());
    }
}
