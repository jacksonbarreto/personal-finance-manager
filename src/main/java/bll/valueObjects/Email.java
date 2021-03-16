package bll.valueObjects;

import java.util.Objects;

import bll.exceptions.InvalidEmailFormatException;

/**
 * Implementation of the {@code IEmail} interface.
 */
public class Email implements IEmail {
    private final String localPart;
    private final String domainName;
    private final boolean AdvertisingContact;

    public Email(String email, boolean advertisingContact) {
        if (!IEmail.isPlainTextEmailValid(email))
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

    @Override
    public String getEmail() {
        return this.localPart + "@" + this.domainName;
    }

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
}
