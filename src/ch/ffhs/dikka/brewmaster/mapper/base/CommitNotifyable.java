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
package ch.ffhs.dikka.brewmaster.mapper.base;

/**
 * Acts as a hook an a commit event.
 * @author David Daniel <david.daniel@stutents.ffhs.ch>
 * @since 2012-11-12
 */
public interface CommitNotifyable
{
    /**
     * Triggered after successful a commit.
     */
    public void afterCommit ();

    /**
     * Triggered after a rollback.
     */
    public void afterRollback ();
}
