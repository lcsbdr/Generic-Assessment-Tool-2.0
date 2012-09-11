/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */


// $Id: NullLocation.java,v 1.1 2012-01-21 10:58:44 turnjoke Exp $
package net.infonode.docking.location;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.internalutil.InternalDockingUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author $Author: turnjoke $
 * @version $Revision: 1.1 $
 */
public class NullLocation implements WindowLocation {
  public static final NullLocation INSTANCE = new NullLocation();

  private NullLocation() {
  }

  public boolean set(DockingWindow window) {
    RootWindow rootWindow = window.getRootWindow();

    if (rootWindow == null)
      return false;

    InternalDockingUtil.addToRootWindow(window, rootWindow);
    return true;
  }

  public void write(ObjectOutputStream out) throws IOException {
    out.writeInt(LocationDecoder.NULL);
  }
}
