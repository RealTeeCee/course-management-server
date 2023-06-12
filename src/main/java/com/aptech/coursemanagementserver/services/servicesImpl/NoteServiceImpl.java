package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.NoteDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.LessonTrackingId;
import com.aptech.coursemanagementserver.models.Note;
import com.aptech.coursemanagementserver.repositories.NoteRepository;
import com.aptech.coursemanagementserver.services.NoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public NoteDto loadNote(NoteDto noteDto) {
        try {
            Note note = noteRepository.findAllNotesByEnrollmentIdAndCourseId(
                    noteDto.getEnrollmentId(), noteDto.getCourseId());
            if (note == null) {
                return new NoteDto();
            }
            NoteDto returnNoteDto = new NoteDto();
            returnNoteDto.setEnrollmentId(noteDto.getEnrollmentId());
            returnNoteDto.setCourseId(note.getTrackId().getCourse_id());
            returnNoteDto.setSectionId(note.getTrackId().getSection_id());
            returnNoteDto.setLessonId(note.getTrackId().getLession_id());
            returnNoteDto.setVideoId(note.getTrackId().getVideo_id());
            returnNoteDto.setResumePoint(note.getResumePoint());
            returnNoteDto.setDescription(note.getDescription());
            return returnNoteDto;

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public boolean saveNote(NoteDto noteDto) {
        try {
            LessonTrackingId trackId = setTrackId(noteDto);
            boolean isUpdated = noteRepository.findByTrackId(trackId).isPresent();
            addNote(noteDto, isUpdated);

            return true;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    private void addNote(NoteDto noteDto, boolean isUpdated) {
        LessonTrackingId trackId = setTrackId(noteDto);

        Note note = isUpdated ? noteRepository.findByTrackId(trackId).orElseThrow(
                () -> new NoSuchElementException(
                        "The note with trackId:[" + trackId.toString() + "] is not exist."))
                : new Note();

        note.setTrackId(trackId)
                .setResumePoint(noteDto.getResumePoint())
                .setDescription(noteDto.getDescription());

        noteRepository.save(note);
    }

    private LessonTrackingId setTrackId(NoteDto noteDto) {
        LessonTrackingId trackId = new LessonTrackingId();

        trackId.setEnrollment_id(noteDto.getEnrollmentId())
                .setCourse_id(noteDto.getCourseId())
                .setSection_id(noteDto.getSectionId())
                .setLession_id(noteDto.getLessonId())
                .setVideo_id(noteDto.getVideoId());
        return trackId;
    }

}
