package model;

import Exception.invalidEmailFormatException;

/**
 * Interface {@code IEmail} represents an email address entity.
 * <p>
 * Ensures that a valid email is maintained throughout the execution,
 * in accordance with RFC2822 and RFC1035.
 */
public interface IEmail {
    String REGEX_EMAIL_FORMAT =
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    /**
     * Returns the local part of the email address.
     *
     * @return the local part of the email address.
     */
    String getLocalPart();

    /**
     * Returns the domain name of the email address.
     *
     * @return the domain name of the email address.
     */
    String getDomainName();

    /**
     * Returns a string with the full email address.
     * In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     *         LocalPart + '@' + DomainName
     * </pre></blockquote>
     *
     * @return a string with the full email address.
     */
    String getEmail();

    /**
     * Returns whether advertising is allowed to be sent to this email address.
     *
     * @return {@code true} if sending e-mail has been authorized.
     */
    boolean isAuthorizedAdvertisingContact();

    /**
     * Change the permission to send advertising emails.
     *
     * @param newCondition {@code true} to authorize receipt or {@code false} to deny receipt.
     */
    void updateAuthorizedAdvertisingContact(boolean newCondition);

    /**
     * Change the email address.
     * An email address, complete and in plain text, follows the following format:
     * <blockquote>
     * <pre>
     *         "localPart@domainName"
     *     </pre></blockquote>
     *
     * @param newEmail new email address, complete, in plain text.
     * @throws invalidEmailFormatException if the email address does not meet the standards of RFC2822 and RFC1035.
     */
    void updateEmail(String newEmail);

    /**
     * Indicates whether some other IEmail is "equal to" this one.
     * This method evaluates whether localPart and DomainPart are the same.
     *
     * @param email an instance of IEmail.
     * @return {@code true} if this object is the same as the obj
     */
    boolean equals(IEmail email);

    /**
     * Indicates whether some other IEmail is "equal to" this one.
     * This method checks whether all attributes are in the same state on both objects.
     *
     * @param email an instance of IEmail.
     * @return {@code true} if this object is the same as the obj
     */
    boolean isDeepEqual(IEmail email);

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     */
    IEmail clone();

    /**
     * Returns a string representation of the email object.
     *
     * @return a string representation of the email object.
     */
    String toString();

    /**
     * Indicates whether an e-mail address is valid, according to the standards of RFC2822 and RFC1035.
     *
     * @param email a email address, complete, in plain text.
     * @return {@code true} if the email address follows the standards of RFC2822 and RFC1035.
     */
    static boolean isPlainTextEmailValid(String email) {
        return email.matches(REGEX_EMAIL_FORMAT);
    }
}
