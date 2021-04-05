package bll.valueObjects;

import bll.exceptions.EmptyArgumentException;
import bll.exceptions.NullArgumentException;

import javax.persistence.Embeddable;
import java.util.Objects;
@Embeddable
final public class Attachment implements IAttachment {

    private String URI;

    public Attachment(String URI) {
        if (URI == null)
            throw new NullArgumentException();
        if (URI.trim().isEmpty())
            throw new EmptyArgumentException();
        this.URI = URI.trim();
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

    @Override
    public String toString() {
        return "Attachment{" +
                "URI='" + URI + '\'' +
                '}';
    }

    @SuppressWarnings("unused")
    private void setURI(String URI) {
        this.URI = URI;
    }

    protected Attachment() {
    }


}
