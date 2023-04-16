package org.example.CMS.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.CMS.database.ContactDao;
import org.example.CMS.dto.Contact;
import org.example.CMS.dto.ContactList;
import org.example.CMS.errorhandler.ErrorCode;
import org.example.CMS.errorhandler.InvalidInputException;
import org.example.CMS.util.CSMUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContactService {

    private final ContactDao contactDao;

    @Autowired
    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public ResponseEntity<HttpStatus> createContact(Contact contact) {
        validateContactDetail(contact);
        Long contactId = contactDao.createContact(contact);
        log.info("A new contact has been created with id : {}", contactId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void validateContactDetail(Contact contact) {
        if (contactDao.contactExists(contact.getPhoneNumber(), contact.getEmail())) {
            throw new InvalidInputException(ErrorCode.CMS100);
        }
    }

    public ResponseEntity<HttpStatus> updateContact(Contact contact) {
        validateContactDetail(contact);
        Long rowUpdated = contactDao.updateContactDetails(contact);
        if (rowUpdated <= 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        log.info("Contact details updated successfully");
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    public ContactList getContact(Long id,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  int pageNo,
                                  int pageSize) {
        int start = (pageNo - 1) * pageSize;

        Long totalCount = contactDao.getContactListCountByFilters(StringUtils.lowerCase(firstName), CSMUtil.isBlank(firstName),
                StringUtils.lowerCase(lastName), CSMUtil.isBlank(lastName),
                StringUtils.lowerCase(email), CSMUtil.isBlank(email),
                id, (id == null || id == 0));
        if (noFilter(id, firstName, lastName, email)) {
            return ContactList.builder()
                    .contacts(contactDao.getContactList(start, pageSize))
                    .totalCount(totalCount)
                    .build();
        }

        return ContactList.builder()
                .contacts(contactDao.getContactListByFilters(StringUtils.lowerCase(firstName), CSMUtil.isBlank(firstName),
                        StringUtils.lowerCase(lastName), CSMUtil.isBlank(lastName),
                        StringUtils.lowerCase(email), CSMUtil.isBlank(email),
                        id, (id == null || id == 0),
                        start, pageSize))
                .totalCount(totalCount)
                .build();
    }

    public boolean noFilter(Long id,
                            String firstName,
                            String lastName,
                            String email) {
        return CSMUtil.isBlank(firstName) && CSMUtil.isBlank(lastName) && CSMUtil.isBlank(email) && (id == null || id == 0);
    }

    public ResponseEntity<HttpStatus> deleteContact(Long contactId) {
        Long rowUpdated = contactDao.deleteContactById(contactId);
        if (rowUpdated > 0) {
            log.info("Contact deleted successfully with id : {}", contactId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
