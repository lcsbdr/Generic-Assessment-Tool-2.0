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


// $Id: ImmutablePropertyException.java,v 1.1 2012-01-21 10:58:36 turnjoke Exp $
package net.infonode.properties.base.exception;

import net.infonode.properties.base.Property;

/**
 * Exception thrown when trying to modify an immutable property.
 *
 * @author $Author: turnjoke $
 * @version $Revision: 1.1 $
 */
public class ImmutablePropertyException extends PropertyException {
  /**
   * Constructor.
   *
   * @param property the property that triggered this exception
   */
  public ImmutablePropertyException(Property property) {
    super(property, "Can't modify immutable property '" + property.getName() + "'!");
  }

}
