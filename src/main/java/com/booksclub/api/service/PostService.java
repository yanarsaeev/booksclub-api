package com.booksclub.api.service;

import com.booksclub.api.entities.Post;
import com.booksclub.api.exception.ElementNotFoundException;
import com.booksclub.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    public Post findById(Long id) {
        return this.postRepository.findById(id).orElseThrow(
                () -> new ElementNotFoundException("Post with id %s not found".formatted(id)));
    }

    @Transactional
    public void delete(Long id) {
        this.postRepository.deleteById(id);
    }

    @Transactional
    public void save(Post post) {
        this.postRepository.saveAndFlush(post);
    }

    @Transactional
    public void update(Long postId, String title, String description) {
        this.postRepository.findById(postId)
                .ifPresentOrElse(post -> {
                    post.setTitle(title);
                    post.setDescription(description);
                }, () -> {
                    throw new ElementNotFoundException("Post with id %s not found".formatted(postId));
                });
    }
}
