package com.audition.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
class AuditionIntegrationClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetPostsById(){
        AuditionPost aud = new AuditionPost(1, 1, "Mock title", "Mock description body");
        when(restTemplate.getForEntity(anyString(), eq(AuditionPost.class))).thenReturn(new ResponseEntity<>(aud,
            HttpStatus.OK));
        AuditionPost posts = auditionIntegrationClient.getPostById(1);
        assertThat(posts).isNotNull();
        assertEquals(posts.getTitle(), aud.getTitle());
        assertEquals(posts.getId(), aud.getId());
        assertEquals(posts.getBody(), aud.getBody());
        assertEquals(posts.getUserId(), aud.getUserId());
    }

    @Test
    public void testGetPostsById_Not_Found(){
        when(restTemplate.getForEntity(anyString(), eq(AuditionPost.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById(1);
        });
    }

    @Test
    public void testGetPosts() {
        AuditionPost aud = new AuditionPost(1, 1, "Mock title", "Mock description body");
        AuditionPost[] posts = new AuditionPost[1];
        posts[0] = aud;
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionPost[].class))).thenReturn(
            new ResponseEntity<>(posts,
                HttpStatus.OK));
        List<AuditionPost> postsList = auditionIntegrationClient.getPosts(null);
        assertThat(postsList).isNotNull();
    }

    @Test
    public void testGetPostsReturningNull() {
        new AuditionPost(1, 1, "Mock title", "Mock description body");
        AuditionPost[] posts = null;
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionPost[].class))).thenReturn(
            new ResponseEntity<>(posts,
                HttpStatus.OK));
        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts(null));

    }

    @Test
    public void testGetPostsForAUserId() {
        AuditionPost aud = new AuditionPost(1, 1, "Mock title", "Mock description body");
        AuditionPost[] posts = new AuditionPost[1];
        posts[0] = aud;
        Map<String, Object> filters = new HashMap<>();
        filters.put("userId", 1);
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionPost[].class))).thenReturn(
            new ResponseEntity<>(posts,
                HttpStatus.OK));
        List<AuditionPost> postsList = auditionIntegrationClient.getPosts(filters);
        assertThat(postsList).isNotNull();
    }

    @Test
    public void testGetPostsForAUserId_Not_Found(){
        Map<String, Object> filters = new HashMap<>();
        filters.put("userId", 1);
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionPost[].class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts(filters));
    }

    @Test
    public void testGetPostsForAUserId_SystemException(){
        Map<String, Object> filters = new HashMap<>();
        filters.put("userId", 1);
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionPost[].class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        Throwable exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts(filters));
        assertTrue(exception.getMessage().contains("Posts"));
    }

    @Test
    public void testGetPostsForAUserId_BadGateway(){
        Map<String, Object> filters = new HashMap<>();
        filters.put("userId", 1);
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionPost[].class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY));
        Throwable exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts(filters));
        assertTrue(exception.getMessage().contains("502 BAD_GATEWAY"));
    }


    @Test
    public void testGetCommentsByPostId() {
        AuditionComment aud = new AuditionComment(1, 1, "Mock title", "mock@mock.com", "Mock description body");
        AuditionComment[] comments = new AuditionComment[1];
        comments[0] = aud;
        when(restTemplate.getForEntity(anyString(), eq(AuditionComment[].class))).thenReturn(
            new ResponseEntity<>(comments,
                HttpStatus.OK));
        List<AuditionComment> list = auditionIntegrationClient.getCommentsByPostId(1);
        assertThat(list).isNotNull();
        assertEquals(list.get(0).getPostId(), aud.getPostId());
        assertEquals(list.get(0).getId(), aud.getId());
        assertEquals(list.get(0).getBody(), aud.getBody());
        assertEquals(list.get(0).getName(), aud.getName());
        assertEquals(list.get(0).getEmail(), aud.getEmail());
    }

    @Test
    public void testGetCommentsForPostId() {
        AuditionComment aud = new AuditionComment(1, 1, "Mock title", "mock@mock.com", "Mock description body");
        AuditionComment[] comments = new AuditionComment[1];
        Map<String, Object> filters = new HashMap<>();
        filters.put("postId", 1);
        comments[0] = aud;
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionComment[].class))).thenReturn(
            new ResponseEntity<>(comments,
                HttpStatus.OK));
        List<AuditionComment> list = auditionIntegrationClient.getCommentsForAPostId(filters);
        assertThat(list).isNotNull();
    }

    @Test
    public void testGetCommentsForPostId_BadGateway(){
        Map<String, Object> filters = new HashMap<>();
        filters.put("postId", 1);
        when(restTemplate.getForEntity(ArgumentMatchers.any(), eq(AuditionComment[].class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY));
        Throwable exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsForAPostId(filters));
        assertTrue(exception.getMessage().contains("502 BAD_GATEWAY"));
    }

    @Test
    public void testGetCommentsByPostId_BadGateway(){
        when(restTemplate.getForEntity(anyString(), eq(AuditionComment[].class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY));
        Throwable exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(1));
        assertTrue(exception.getMessage().contains("502 BAD_GATEWAY"));
    }

    @Test
    public void testGetCommentsByUnknownPostId() {
        AuditionComment[] comments = null;
        when(restTemplate.getForEntity(anyString(), eq(AuditionComment[].class))).thenReturn(
            new ResponseEntity<>(comments,
                HttpStatus.OK));
        assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(-1));
    }
}
