package com.aptech.coursemanagementserver.services;

import com.aptech.coursemanagementserver.dtos.NoteDto;

public interface NoteService {
    NoteDto loadNote(NoteDto noteDto);

    boolean saveNote(NoteDto noteDto);

}
