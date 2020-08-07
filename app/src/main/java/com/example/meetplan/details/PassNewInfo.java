package com.example.meetplan.details;

/** Interface to pass changed information about meetup to the details
 * fragment, in order to save the information to the selected meetup. */
public interface PassNewInfo {

    /** Stores the key and value for the changed information.
     * @param type the key for the changed field in the Parse database
     * @param info the value of the changed field*/
    public void passMeetupInformation(String type, String info);
}
