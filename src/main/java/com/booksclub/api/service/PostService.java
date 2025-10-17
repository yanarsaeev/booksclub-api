package com.booksclub.api.service;

import com.booksclub.api.dto.PostDto;
import com.booksclub.api.entities.Person;
import com.booksclub.api.entities.Post;
import com.booksclub.api.exception.NotFoundException;
import com.booksclub.api.repository.PersonRepository;
import com.booksclub.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PersonRepository personRepository;

    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    public Post findById(Long id) {
        return this.postRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Post with id %s not found".formatted(id)));
    }

    public void delete(Long id) {
        if (this.postRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Post with id %s not found".formatted(id));
        }

        this.postRepository.deleteById(id);
    }

    @Transactional
    public Post save(PostDto postDto) {
        Post savedPost = new Post();
        savedPost.setTitle(postDto.getTitle());
        savedPost.setDescription(postDto.getDescription());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.findPersonByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        savedPost.setAuthor(person);
        person.getPosts().add(savedPost);
        savedPost.setCreatedAt(LocalDateTime.now());

        return this.postRepository.save(savedPost);
    }

    @Transactional
    public void update(Long id, String title, String description) {
        this.postRepository.findById(id)
                .ifPresentOrElse(post -> {
                    post.setTitle(title);
                    post.setDescription(description);
                }, () -> {
                    throw new NotFoundException("Post with id %s not found".formatted(id));
                });
    }
}
