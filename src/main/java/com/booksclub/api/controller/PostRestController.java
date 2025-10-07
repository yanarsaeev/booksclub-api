package com.booksclub.api.controller;

import com.booksclub.api.dto.PostDto;
import com.booksclub.api.entities.Post;
import com.booksclub.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostRestController {

    private final PostService postService;

    private final ModelMapper modelMapper;

    @GetMapping("/posts")
    public List<PostDto> getAll() {
        return this.postService.findAll().stream().map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/posts/{postId}")
    public PostDto get(@PathVariable("postId") Long postId) {
        return convertToPostDto(this.postService.findById(postId));
    }

    @PostMapping("/posts")
    public ResponseEntity<?> create(@RequestBody @Valid PostDto postDto, BindingResult bindingResult,
                                    UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Post savedPost = convertToPost(postDto);
            this.postService.save(savedPost);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/api/posts/{postId}")
                            .build(Map.of("postId", savedPost.getId())))
                    .body(savedPost);
        }
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<?> update(@PathVariable("postId") Long postId,
                                    @RequestBody @Valid PostDto postDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.postService.update(postId, postDto.getTitle(), postDto.getDescription());
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") Long postId) {
        this.postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/users/{userId}/posts")
//    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable("userId") Long userId) {
//        if ()
//    }

    private PostDto convertToPostDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }

    private Post convertToPost(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }
}
