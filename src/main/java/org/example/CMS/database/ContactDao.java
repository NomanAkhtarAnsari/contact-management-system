package org.example.CMS.database;

import org.example.CMS.dto.Contact;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ContactDao {

    @SqlUpdate("INSERT INTO contact(first_name, last_name, email, phone_number) " +
            "VALUES(:firstName, :lastName, :email, :phoneNumber)")
    @GetGeneratedKeys
    Long createContact(@BindBean Contact contact);

    @SqlQuery("SELECT EXISTS(SELECT 1 FROM contact WHERE phone_number = :phoneNumber OR email = :email)")
    boolean contactExists(String phoneNumber,
                          String email);

    @SqlUpdate("UPDATE contact SET first_name = coalesce(:firstName, first_name), last_name = coalesce(:lastName, last_name), " +
            "email = coalesce(:email, email), phone_number = coalesce(:phoneNumber, phone_number) WHERE id = :id")
    @GetGeneratedKeys
    Long updateContactDetails(@BindBean Contact contact);

    @SqlQuery("SELECT first_name, last_name, email, phone_number FROM contact " +
            "WHERE (:skipFirstNameFilter OR LOWER(first_name) = :firstName) " +
            "AND (:skipLastNameFilter OR LOWER(last_name) = :lastName) " +
            "AND (:skipEmailFilter OR LOWER(email) = :email) " +
            "AND (:skipIdFilter OR id = :id) " +
            "ORDER BY id DESC " +
            "OFFSET :start " +
            "LIMIT :count")
    @RegisterBeanMapper(Contact.class)
    List<Contact> getContactListByFilters(String firstName,
                                          boolean skipFirstNameFilter,
                                          String lastName,
                                          boolean skipLastNameFilter,
                                          String email,
                                          boolean skipEmailFilter,
                                          Long id,
                                          boolean skipIdFilter,
                                          int start,
                                          int count);

    @SqlQuery("SELECT COUNT(1) FROM contact " +
            "WHERE (:skipFirstNameFilter OR LOWER(first_name) = :firstName) " +
            "AND (:skipLastNameFilter OR LOWER(last_name) = :lastName) " +
            "AND (:skipEmailFilter OR LOWER(email) = :email) " +
            "AND (:skipIdFilter OR id = :id)")
    Long getContactListCountByFilters(String firstName,
                                      boolean skipFirstNameFilter,
                                      String lastName,
                                      boolean skipLastNameFilter,
                                      String email,
                                      boolean skipEmailFilter,
                                      Long id,
                                      boolean skipIdFilter);

    @SqlQuery("SELECT first_name, last_name, email, phone_number " +
            "FROM contact " +
            "ORDER BY id DESC " +
            "OFFSET :start " +
            "LIMIT :count")
    @RegisterBeanMapper(Contact.class)
    List<Contact> getContactList(int start, int count);

    @SqlUpdate("DELETE FROM contact WHERE id = :id")
    @GetGeneratedKeys
    Long deleteContactById(Long id);
}
