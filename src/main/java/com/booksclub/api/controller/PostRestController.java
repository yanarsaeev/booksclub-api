package com.booksclub.api.controller;

import com.booksclub.api.dto.PersonDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostRestController {

    private final PostService postService;

    private final PersonRestController personRestController;

    private final ModelMapper modelMapper;

    @GetMapping("/posts")
    public List<PostDto> getAll() {
        List<Post> postList = this.postService.findAll();
        return postList.stream().map(this::convertToPostDto).toList();
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
            Post savedPost = this.postService.save(postDto);
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
                                    BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.postService.update(postId, postDto.getTitle(), postDto.getDescription());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/api/events/{eventId}")
                            .build(Map.of("eventId", postId)))
                    .body(convertToPostDto(this.postService.findById(postId)));
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> delete(@PathVariable("postId") Long postId) {
        this.postService.delete(postId);
        return ResponseEntity.ok("Post deleted");
    }

    private PostDto convertToPostDto(Post post) {
        PersonDto personDto = this.personRestController.convertToPersonDto(post.getAuthor());
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setAuthor(personDto);
        return postDto;
    }
}
