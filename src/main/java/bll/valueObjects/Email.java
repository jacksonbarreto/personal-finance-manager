package bll.valueObjects;

import java.util.Objects;

import bll.exceptions.InvalidEmailFormatException;
import bll.exceptions.NullArgumentException;
import org.hibernate.annotations.Immutable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Implementation of the {@code IEmail} interface.
 */
@Embeddable
@Immutable
final public class Email implements IEmail {
    private String localPart;
    private String domainName;
    private boolean AdvertisingContact;

    public Email(String email, boolean advertisingContact) {
        if (email == null)
            throw new NullArgumentException();
        if (IEmail.isPlainTextEmailInvalid(email))
            throw new InvalidEmailFormatException();
        String[] parts = email.split(SEPARATOR);
        this.localPart = parts[0];
        this.domainName = parts[1];
        this.AdvertisingContact = advertisingContact;
    }

    public Email(String email) {
        this(email, true);
    }


    @Override
    public String getLocalPart() {
        return this.localPart;
    }

    @Override
    public String getDomainName() {
        return this.domainName;
    }

    @Transient
    @Override
    public String getEmail() {
        return this.localPart + "@" + this.domainName;
    }

    @Transient
    @Override
    public boolean isAuthorizedAdvertisingContact() {
        return AdvertisingContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return AdvertisingContact == email.AdvertisingContact && localPart.equals(email.localPart) && domainName.equals(email.domainName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localPart, domainName, AdvertisingContact);
    }


    @Override
    public String toString() {
        return "Email{" +
                "localPart='" + localPart + '\'' +
                ", domainName='" + domainName + '\'' +
                ", AdvertisingContact=" + AdvertisingContact +
                '}';
    }

    protected Email() {
    }

    @SuppressWarnings("unused")
    private void setLocalPart(String localPart) {
        this.localPart = localPart;
    }

    @SuppressWarnings("unused")
    private void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @SuppressWarnings("unused")
    private boolean isAdvertisingContact() {
        return AdvertisingContact;
    }

    @SuppressWarnings("unused")
    private void setAdvertisingContact(boolean advertisingContact) {
        AdvertisingContact = advertisingContact;
    }
}
