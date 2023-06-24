package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.AuthorDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
@Tag(name = "Author Endpoints")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "[ANORNYMOUS] - GET All Authors")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        try {
            List<AuthorDto> authorDtos = authorService.findAll();
            return ResponseEntity.ok(authorDtos);
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "[ANY ROLE] - GET Author By Id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable("id") long id) {
        try {
            AuthorDto authorDto = authorService.findById(id);
            return ResponseEntity.ok(authorDto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    @Operation(summary = "[ADMIN, MANAGER, EMPLOYEE] - Create / Update Author")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> create(@RequestBody AuthorDto authorDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            authorService.save(authorDto);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message(
                            (authorDto.getId() == 0 ? "Create new" : "Update") + " author successfully.").build(),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "[ADMIN, MANAGER, EMPLOYEE] - Delete Author")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> delete(long authorId) {
        try {
            authorService.deleteAuthor(authorId);
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message("Delete author successfully.").build(),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
