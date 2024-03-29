/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.mod;

import vidis.data.var.vars.AVariable;

/**
 * Basic module component interface that all user implemented modules must implement.
 * <p>
 * The function {@link #execute()} is called by the simulator at each simulation step.
 * </p>
 * @author Dominik
 *
 */
public interface IUserComponent {
    /**
     * Retrieves a user-scope set variable from the Simulator.
     * <p>
     * <i>Hint: If you specified a variable in the .msim configuration file you will
     * retrieve it here as a String</i>
     * </p>
     * @param <T> generic return type
     * @param clazz the expected return class-type
     * @param identifier the identifier of the requested variable
     * @throws NullPointerException thrown if not variable found
     * @throws ClassCastException thrown if expected type (clazz) does not match the variable class type
     * @return null or object
     */
    public AVariable getVariable(String identifier)
	    throws NullPointerException, ClassCastException;

    /**
     * Checks if a variable exists in the user-scope.
     * @param identifier the identifier of the variable
     * @return true or false
     */
    public boolean hasVariable(String identifier);

    /**
     * Is executed on each simulation step.
     * <p>
     * <ul>
     * <li>Hint: it is impossible to tell which of the components executes first!</li>
     * <li>Hint: do NOT use Thread.sleep() or something like that - it will only hang the simulator</li>
     * <li>Hint: use built-in sleep() and interrupt() functions!</li>
     * </ul>
     * </p>
     */
    public void execute();

    /**
     * Causes this user component to sleep for some execution steps.
     * @param steps simulator steps to sleep
     */
    public void sleep(int steps);

    /**
     * Interrupts a previously called sleep of this component.
     * <p>
     * <i>
     * Hint: if this component does not sleep, no action is done.
     * </i>
     * </p>
     * @see #sleep(int)
     */
    public void interrupt();
    
}
