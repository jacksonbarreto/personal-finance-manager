package bll.valueObjects;

import bll.exceptions.EmptyArgumentException;
import bll.exceptions.NullArgumentException;

import java.io.Serializable;

public interface IAttachment extends Serializable {


    /**
     * Returns the URI of the attachment
     *
     * @return the URI of the attachment.
     * @throws EmptyArgumentException if the URI is empty.
     * @throws NullArgumentException  if a URI is not sent as a parameter.
     */
    String getURI();

    /**
     * Returns a string representation of the Category object.
     *
     * @return a string representation of the Category object.
     */
    String toString();
}
