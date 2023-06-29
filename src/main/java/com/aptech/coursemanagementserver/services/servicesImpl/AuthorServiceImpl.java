package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AuthorDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.models.Author;
import com.aptech.coursemanagementserver.repositories.AuthorRepository;
import com.aptech.coursemanagementserver.services.AuthorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public AuthorDto findById(long id) {
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        "This author with authorId: [" + id + "] is not exist."));

        return toDto(author);
    }

    @Override
    public List<AuthorDto> findAll() {
        List<Author> authors = authorRepository.findAll();

        List<AuthorDto> authorDtos = new ArrayList<>();

        for (Author author : authors) {
            AuthorDto authorDto = toDto(author);
            authorDtos.add(authorDto);
        }

        return authorDtos;
    }

    @Override
    public void save(AuthorDto authorDto) {
        Author author = new Author();
        if (authorDto.getId() > 0) {
            author = authorRepository.findById(authorDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This author with authorId: [" + authorDto.getId() + "] is not exist."));
        }
        if (authorDto.getId() == 0
                && findAll().stream().map(authorr -> authorr.getName()).toList().contains(authorDto.getName())) {
            throw new IsExistedException(authorDto.getName());
        }
        author.setName(authorDto.getName());
        author.setImage(authorDto.getImage());
        authorRepository.save(author);
    }

    @Override
    public void saveAll(List<AuthorDto> authorDtos) {
        List<Author> authors = new ArrayList<>();

        for (AuthorDto authorDto : authorDtos) {
            Author author = new Author();
            author.setName(authorDto.getName());
            author.setImage(authorDto.getImage());
            authors.add(author);
        }
        authorRepository.saveAll(authors);
    }

    @Override
    public void deleteAuthor(long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> new NoSuchElementException("This author with authorId: [" + authorId + "] is not exist."));
        if (author.getCourses().size() > 0) {
            throw new BadRequestException("Cannot delete author who has already taught courses.");
        }
        authorRepository.delete(author);
    }

    private AuthorDto toDto(Author author) {
        AuthorDto authorDto = AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .image(author.getImage())
                .created_at(author.getCreated_at())
                .build();
        return authorDto;
    }

}
