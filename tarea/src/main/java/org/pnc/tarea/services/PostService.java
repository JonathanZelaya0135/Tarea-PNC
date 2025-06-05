package org.pnc.tarea.services;

import org.pnc.tarea.entities.Post;
import org.pnc.tarea.entities.User;
import org.pnc.tarea.entities.dto.CreatePostDto;
import org.pnc.tarea.entities.dto.UserPostsDto;
import org.pnc.tarea.repositories.PostRepository;
import org.pnc.tarea.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public void createPost(CreatePostDto request){
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }

        Post post = new Post();
        post.setAuthor(optionalUser.get());
        post.setMessage(request.getContent());
        post.setComments(new ArrayList<>());
        postRepository.save(post);
    }

    public List<UserPostsDto> getUserPosts(UUID userId){
        List<UserPostsDto> response = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        List<Post> userPost = postRepository.findAllByAuthor(optionalUser.get());
        userPost.forEach(post -> {
            response.add(new UserPostsDto(post.getId(), post.getMessage()));
        });

        return response;
    }

    public void likePost(UUID userId, UUID postId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()){
            throw new RuntimeException("Post not found");
        }
        optionalPost.get().getLikes().add(optionalUser.get());
        postRepository.save(optionalPost.get());
    }

    public List<Post> getLikedPosts(UUID userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return optionalUser.get().getLikes();
    }

}
