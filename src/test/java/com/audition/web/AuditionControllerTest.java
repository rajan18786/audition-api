package com.audition.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AuditionControllerTest {

    @Mock
    private AuditionService auditionService;

    @InjectMocks
    private AuditionController auditionController;

    @Test
    public void testGetPostsById(){
        AuditionPost aud = new AuditionPost(1, 1, "Mock title", "Mock description body");
        when(auditionService.getPostById(1)).thenReturn(aud);
        AuditionPost posts = auditionController.getPostById(1);
        assertThat(posts).isNotNull();
    }

   @Test
    public void testGetPosts() {
        AuditionPost aud = new AuditionPost();
        aud.setBody("Mock description body");
        aud.setId(1);
        aud.setUserId(1);
        aud.setTitle("Mock title");
        when(auditionService.getPosts(null)).thenReturn(List.of(aud));
        List<AuditionPost> postsList = auditionController.getPosts(null,null);
        assertThat(postsList).isNotNull();
    }

    @Test
    public void testGetPostsForUserIdAndTitle() {
        AuditionPost aud = new AuditionPost();
        aud.setBody("Mock description body");
        aud.setId(1);
        aud.setUserId(1);
        aud.setTitle("Mock title");
        when(auditionService.getPosts(null)).thenReturn(List.of(aud));
        List<AuditionPost> postsList = auditionController.getPosts(1,"Mock title");
        assertThat(postsList).isNotNull();
    }

    @Test
    public void testGetCommentsByPostId() {
        AuditionComment aud = new AuditionComment(1, 1, "Mock title", "mock@mock.com", "Mock description body");
        when(auditionService.getCommentsByPostId(1)).thenReturn(List.of(aud));
        List<AuditionComment> list = auditionController.getCommentsByPostId(1);
        assertThat(list).isNotNull();
    }

    @Test
    public void testGetCommentsForAllPosts() {
        AuditionComment aud = new AuditionComment();
        aud.setBody("Mock description body");
        aud.setId(1);
        aud.setPostId(1);
        aud.setName("Mock title");
        aud.setEmail("mock@mock.com");
        AuditionComment[] comments = new AuditionComment[1];
        Map<String, Object> filters = new HashMap<>();
        filters.put("postId", 1);
        comments[0] = aud;
        when(auditionService.getCommentsForAPostId(filters)).thenReturn(List.of(comments));
        //No postid passed
        List<AuditionComment> list = auditionController.getCommentsForAPostId(null);
        assertThat(list).isNotNull();
    }

    @Test
    public void testGetCommentsForPostId() {
        AuditionComment aud = new AuditionComment();
        aud.setBody("Mock description body");
        aud.setId(1);
        aud.setPostId(1);
        aud.setName("Mock title");
        aud.setEmail("mock@mock.com");
        AuditionComment[] comments = new AuditionComment[1];
        Map<String, Object> filters = new HashMap<>();
        filters.put("postId", 1);
        comments[0] = aud;
        when(auditionService.getCommentsForAPostId(filters)).thenReturn(List.of(comments));
        List<AuditionComment> list = auditionController.getCommentsForAPostId(1);
        assertThat(list).isNotNull();
    }
}
