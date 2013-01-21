/* 
 * LICENSE
 *
 * Copyright (C) 2012 DIKKA Group
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ffhs.dikka.brewmaster;

/**
 * Base exception class.
 */
public class BrewMasterException
    extends java.lang.Exception
{
    /** For the sake of it, exceptions are by definition serializable */
    public static final long serialVersionUID = 1;

    /**
     * Constructs a new exception with null as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to Throwable.initCause(java.lang.Throwable).
     */
    public BrewMasterException ()
    { super(); }

    /**
     * Constructs a new exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to Throwable.initCause(java.lang.Throwable).
     * @param message   the detail message. The detail message is saved for
     *                  later retrieval by the Throwable.getMessage() method.
     */
    public BrewMasterException (String message)
    { super (message); }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * Note that the detail message associated with cause is not automatically
     * incorporated in this exception's detail message.
     * @param message   the detail message (which is saved for later retrieval by
     *                  the Throwable.getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the
     *              Throwable.getCause() method). (A null value is permitted, and indicates
     *              that the cause is nonexistent or unknown.)
     */
    public BrewMasterException (String message, Throwable cause)
    { super (message, cause); }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of (cause==null ? null : cause.toString()) (which typically contains the
     * class and detail message of cause).
     *
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, PrivilegedActionException).
     * @param cause the cause (which is saved for later retrieval by the
     *              Throwable.getCause() method). (A null value is permitted, and indicates
     *              that the cause is nonexistent or unknown.)
     */
    public BrewMasterException (Throwable cause)
    { super (cause); }
}
