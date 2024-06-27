package com.audition.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AuditionServiceTest {

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    @InjectMocks
    private AuditionService auditionService;

    @Test
    public void testGetPostsById(){
        AuditionPost aud = new AuditionPost(1, 1, "Mock title", "Mock description body");
        when(auditionIntegrationClient.getPostById(1)).thenReturn(aud);
        AuditionPost posts = auditionService.getPostById(1);
        assertThat(posts).isNotNull();
    }

   @Test
    public void testGetPosts() {
        AuditionPost aud = new AuditionPost(1, 1, "Mock title", "Mock description body");
        when(auditionIntegrationClient.getPosts(null)).thenReturn(List.of(aud));
        List<AuditionPost> postsList = auditionService.getPosts(null);
        assertThat(postsList).isNotNull();
    }

    @Test
    public void testGetCommentsByPostId() {
        AuditionComment aud = new AuditionComment(1, 1, "Mock title", "mock@mock.com", "Mock description body");
        when(auditionIntegrationClient.getCommentsByPostId(1)).thenReturn(List.of(aud));
        List<AuditionComment> list = auditionService.getCommentsByPostId(1);
        assertThat(list).isNotNull();
    }

    @Test
    public void testGetCommentsForPostId() {
        AuditionComment aud = new AuditionComment(1, 1, "Mock title", "mock@mock.com", "Mock description body");
        AuditionComment[] comments = new AuditionComment[1];
        Map<String, Object> filters = new HashMap<>();
        filters.put("postId", 1);
        comments[0] = aud;
        when(auditionIntegrationClient.getCommentsForAPostId(filters)).thenReturn(List.of(comments));
        List<AuditionComment> list = auditionService.getCommentsForAPostId(filters);
        assertThat(list).isNotNull();
    }
}
