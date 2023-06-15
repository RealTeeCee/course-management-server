// package com.aptech.coursemanagementserver.services;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.NoSuchElementException;
// import java.util.stream.Collectors;

// import org.springframework.http.codec.ServerSentEvent;
// import org.springframework.stereotype.Service;

// import com.aptech.coursemanagementserver.enums.NotificationType;
// import com.aptech.coursemanagementserver.models.Comment;
// import com.aptech.coursemanagementserver.models.Notification;
// import com.aptech.coursemanagementserver.models.Post;
// import com.aptech.coursemanagementserver.models.User;
// import com.aptech.coursemanagementserver.repositories.PostRepository;
// import com.aptech.coursemanagementserver.services.authServices.UserService;

// import lombok.extern.slf4j.Slf4j;
// import reactor.core.publisher.Flux;
// import reactor.core.scheduler.Schedulers;

// @Service
// @Slf4j
// public class PostService {

// private final PostRepository postRepository;
// private final UserService userService;
// private final NotificationService notificationService;

// public PostService(PostRepository postRepository,
// UserService userService, NotificationService notificationService) {
// this.postRepository = postRepository;
// this.userService = userService;

// this.notificationService = notificationService;
// }

// public Post create(Post post) {
// User user = userService.findByEmail(post.getUser().getEmail()).orElseThrow(()
// -> new NoSuchElementException(
// "The Email with Email: [" + post.getUser().getEmail() + "] is not exist."));
// post.setUser(user);
// post.setComments(new ArrayList<>());
// post.setLikedUsers(new ArrayList<>());
// return postRepository.save(post);
// }

// public List<Post> getAll() {
// return postRepository.findAll();
// }

// public Post getPostByID(int postID) {
// /*
// * Query query = new Query().addCriteria(Criteria.where("id").is(new
// * ObjectId(postID)));
// * log.info(int.valueOf(mongoTemplate.find(query, Post.class)));
// */
// return postRepository.findPostById(postID).orElseThrow(() -> new
// RuntimeException("post not found!"));
// }

// public void deletePostByID(int postID) {
// var post = postRepository.findPostById(postID).orElseThrow(() -> new
// RuntimeException("post not found!"));
// postRepository.delete(post);
// }

// public Post addComment(CommentRequest commentRequest) {
// Post post = getPostByID(commentRequest.getPost().getId());
// Comment comment = commentRequest.getComment();
// // it's checking users at the same time
// User commentUser =
// userService.findByEmail(comment.getUser().getEmail()).orElseThrow(() -> new
// NoSuchElementException(
// "The Email with Email: [" + post.getUser().getEmail() + "] is not exist."));
// comment.setUser(commentUser);

// User postUser =
// userService.findByEmail(post.getUser().getEmail()).orElseThrow(() -> new
// NoSuchElementException(
// "The Email with Email: [" + post.getUser().getEmail() + "] is not exist."));
// post.setUser(postUser);
// post.getComments().add(comment);
// notificationService.createNotification(Notification.builder()
// .delivered(false)
// .content("new comment from " + commentUser.getUsername())
// .notificationType(NotificationType.COMMENT)
// .userFrom(commentUser)
// .userTo(postUser).build());
// return postRepository.save(post);
// }

// public Post removeComment(CommentRequest commentRequest) {
// Post post = getPostByID(commentRequest.getPost().getId());
// Comment comment = commentRequest.getComment();
// // it's checking users at the same time
// User commentUser =
// userService.findByEmail(comment.getUser().getEmail()).orElseThrow(() -> new
// NoSuchElementException(
// "The Email with Email: [" + post.getUser().getEmail() + "] is not exist."));
// comment.setUser(commentUser);

// User postUser =
// userService.findByEmail(post.getUser().getEmail()).orElseThrow(() -> new
// NoSuchElementException(
// "The Email with Email: [" + post.getUser().getEmail() + "] is not exist."));
// post.setUser(postUser);

// var updatedComments = post.getComments()
// .stream()
// .filter(x -> !Objects.equals(x.getId(), comment.getId()))
// .collect(Collectors.toList());
// post.setComments(updatedComments);
// return postRepository.save(post);
// }

// public Post addLike(LikeRequest likeRequest) {
// Post post = getPostByID(likeRequest.getPost().getId());
// User postOfUser = post.getUser();
// // it's checking users at the same time
// User likedUser =
// userService.getUserByUsername(likeRequest.getUser().getUsername());
// if (post.getLikedUsers() != null
// && userService.isUserContains(post.getLikedUsers(),
// likeRequest.getUser().getUsername())) {
// log.info("call remove like: " + likeRequest.getUser().getUsername());
// return removeLike(likeRequest);
// }
// post.getLikedUsers().add(likedUser);
// notificationService.createNotification(Notification.builder()
// .delivered(false)
// .content("like from " + likedUser.getUsername())
// .notificationType(NotificationType.LIKE)
// .userFrom(likedUser)
// .userTo(postOfUser).build());
// return postRepository.save(post);
// }

// public Post removeLike(LikeRequest likeRequest) {
// log.info("Removelike: " + likeRequest.getUser().getUsername());
// Post post = getPostByID(likeRequest.getPost().getId());
// User requestUser = likeRequest.getUser();
// // it's checking users at the same time
// User unLikedUser = userService.getUserByUsername(requestUser.getUsername());
// var updatedLikes = post.getLikedUsers()
// .stream()
// .filter(x -> !x.getUsername().equals(unLikedUser.getUsername()))
// .collect(Collectors.toList());
// post.setLikedUsers(updatedLikes);
// return postRepository.save(post);
// }

// public List<Comment> getCommentsByPostID(int postID) {
// Post post = getPostByID(postID);
// return post.getComments();
// }

// public Flux<ServerSentEvent<List<Post>>> streamPosts() {
// return Flux.interval(Duration.ofSeconds(2))
// .publishOn(Schedulers.boundedElastic())
// .map(sequence ->
// ServerSentEvent.<List<Post>>builder().id(String.valueOf(sequence))
// .event("post-list-event").data(getAll())
// .build());

// }
// }
