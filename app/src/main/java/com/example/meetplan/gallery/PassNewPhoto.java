package com.example.meetplan.gallery;

import com.parse.ParseFile;

/** Interface that allows the add photo dialog fragment to pass back the
 * newly created photo to the gallery fragment, in order to update the adapter. */
public interface PassNewPhoto {

    /** Passes in a new photo upon creation.
     * @param file ParseFile of the photo that is being passed. */
    public void passCreatedParseFile(ParseFile file);
}
