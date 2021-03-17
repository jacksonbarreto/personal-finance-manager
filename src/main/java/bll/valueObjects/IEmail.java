package bll.valueObjects;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface {@code IEmail} represents an email address entity.
 * <p>
 * Ensures that a valid email is maintained throughout the execution,
 * in accordance with RFC2822 and RFC1035.
 */
public interface IEmail extends Serializable {
    Pattern REGEX_EMAIL_FORMAT = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])",
            Pattern.CASE_INSENSITIVE);
    String SEPARATOR = "@";

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
     * Indicates whether some other IEmail is "equal to" this one.
     * This is a valuable object, so it is immutable.
     * It is only the same when both objects have all their attributes equal.
     *
     * @param obj an instance of IEmail.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    boolean equals(Object obj);

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
        if (email == null)
            return false;
        Matcher matcher = REGEX_EMAIL_FORMAT.matcher(email);
        return matcher.find();
    }

    /**
     * Indicates whether an e-mail address is invalid, according to the standards of RFC2822 and RFC1035.
     *
     * @param email a email address, complete, in plain text.
     * @return {@code true} if the email address dont follows the standards of RFC2822 and RFC1035.
     */
    static boolean isPlainTextEmailInvalid(String email) {
        if (email == null)
            return true;
        Matcher matcher = REGEX_EMAIL_FORMAT.matcher(email);
        return !matcher.find();
    }
}
