package bll.valueObjects;

import bll.exceptions.EmptyArgumentException;

import java.util.Objects;

import static bll.entities.Utilities.isNullArgument;

final public class Attachment implements IAttachment {

    private String URI;

    public Attachment(String URI) {
        isNullArgument(URI);
        if (URI.isEmpty())
            throw new EmptyArgumentException();
        this.URI = URI;
    }

    /**
     * Returns the URI of the attachment
     *
     * @return the URI of the attachment.
     */
    @Override
    public String getURI() {
        return this.URI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return URI.equals(that.URI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(URI);
    }

    private void setURI(String URI) {
        this.URI = URI;
    }

    private Attachment() {
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "URI='" + URI + '\'' +
                '}';
    }
}
