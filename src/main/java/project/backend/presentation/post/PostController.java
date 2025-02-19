package project.backend.presentation.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.post.PostService;
import project.backend.business.post.request.PostDetailServiceRequest;
import project.backend.business.post.request.PostListServiceRequest;
import project.backend.business.post.response.CreateUpdatePostResponse;
import project.backend.business.post.response.PostCountResponse;
import project.backend.business.post.response.PostDetailResponse;
import project.backend.business.post.response.PostListResponse;
import project.backend.presentation.post.docs.PostControllerDocs;
import project.backend.presentation.post.request.SummaryUrlRequest;
import project.backend.presentation.post.request.UpdatePostRequest;
import project.backend.security.aop.AssignCurrentUserInfo;
import project.backend.security.aop.AssignOrNullCurrentUserInfo;
import project.backend.security.aop.CurrentUserInfo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostControllerDocs {

  private final PostService postService;

  @AssignCurrentUserInfo
  @GetMapping("/count")
  public ResponseEntity<PostCountResponse> getPostCount(CurrentUserInfo userInfo) {
    PostCountResponse response = postService.getTotalPostCount(userInfo.getUserId());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @GetMapping
  public ResponseEntity<PostListResponse> getPosts(CurrentUserInfo userInfo,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Long archiveId,
      @RequestParam(required = false) String search) {
    PostListServiceRequest postListServiceRequest = PostListServiceRequest.of(page, archiveId,
        search);
    PostListResponse response = postService.getPosts(userInfo.getUserId(), postListServiceRequest);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignOrNullCurrentUserInfo
  @PostMapping
  public ResponseEntity<CreateUpdatePostResponse> createPost(CurrentUserInfo userInfo,
      @Valid @RequestBody SummaryUrlRequest summaryUrlRequest) {
    CreateUpdatePostResponse response = postService.createPostDetail(
        userInfo.getUserId(), summaryUrlRequest.toServiceRequest());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignOrNullCurrentUserInfo
  @GetMapping("/{id}")
  public ResponseEntity<PostDetailResponse> getPostDetail(CurrentUserInfo userInfo,
      @PathVariable("id") Long postId, @RequestParam String status) {
    PostDetailServiceRequest request = PostDetailServiceRequest.of(postId, status);
    PostDetailResponse response = postService.getPostDetail(userInfo.getUserId(), request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @PatchMapping("/{id}")
  public ResponseEntity<CreateUpdatePostResponse> updatePost(CurrentUserInfo userInfo,
      @PathVariable("id") Long postId, @Valid @RequestBody UpdatePostRequest updatePostRequest) {
    CreateUpdatePostResponse response = postService.updatePostDetail(userInfo.getUserId(), postId,
        updatePostRequest.toServiceRequest());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(CurrentUserInfo userInfo,
      @PathVariable("id") Long postId) {
    postService.deletePostDetail(userInfo.getUserId(), postId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @PatchMapping("/{id}/summary")
  public ResponseEntity<CreateUpdatePostResponse> updateSummaryPost(CurrentUserInfo userInfo,
      @PathVariable("id") Long postId, @Valid @RequestBody SummaryUrlRequest summaryUrlRequest) {
    CreateUpdatePostResponse response = postService.updateSummaryPost(userInfo.getUserId(), postId,
        summaryUrlRequest.toServiceRequest());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
