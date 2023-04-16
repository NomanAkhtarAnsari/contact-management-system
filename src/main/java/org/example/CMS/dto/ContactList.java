package org.example.CMS.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContactList {

    private Long totalCount;

    private List<Contact> contacts;
}
