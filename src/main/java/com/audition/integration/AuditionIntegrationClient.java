package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuditionIntegrationClient {

    private final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
    private final RestTemplate restTemplate;

    public AuditionIntegrationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<AuditionPost> getPosts(Map<String, Object> filters) {

        URI uri = buildUriWithFilters(POSTS_URL, filters);
        try {
            ResponseEntity<AuditionPost[]> responseEntity = restTemplate.getForEntity(uri, AuditionPost[].class);
            AuditionPost[] auditionpost = responseEntity.getBody();
            if (auditionpost != null && auditionpost.length > 0) {
                return List.of(auditionpost);
            } else {
                throw new SystemException("Cannot find any posts", "SYSEXP-MEDIBANK-1", 404);
            }
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find any Posts", 404);
            } else {
                throw new SystemException(e.getMessage(), "SYSEXP-MEDIBANK-2", e.getStatusCode().value(),
                    e.getRootCause());
            }
        }

    }

    public AuditionPost getPostById(final int id) throws SystemException {

        String url = POSTS_URL + "/" + id;
        try {
            ResponseEntity<AuditionPost> responseEntity = restTemplate.getForEntity(url, AuditionPost.class);
            return responseEntity.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                throw new SystemException(e.getMessage(), "SYSEXP-MEDIBANK-3", e.getStatusCode().value(),
                    e.getRootCause());
            }
        }
    }

    public List<AuditionComment> getCommentsByPostId(final int postId) {

        String url = POSTS_URL + "/" + postId + "/comments";
        try {
            ResponseEntity<AuditionComment[]> responseEntity = restTemplate.getForEntity(url, AuditionComment[].class);
            AuditionComment[] auditionComment = Optional.ofNullable(responseEntity.getBody())
                .map(body -> {
                    if (body.length > 0) {
                        return body;
                    } else {
                        throw new SystemException("Cannot find comments with Post id " + postId, "SYSEXP-MEDIBANK-4",
                            404);
                    }
                })
                .orElseThrow(
                    () -> new SystemException("Cannot find comments with Post id " + postId, "SYSEXP-MEDIBANK-5", 404));
            return List.of(auditionComment);
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find comments with Post id " + postId, "Resource Not Found", 404);
            } else {
                throw new SystemException(e.getMessage(), "SYSEXP-MEDIBANK-6", e.getStatusCode().value(),
                    e.getRootCause());
            }
        }
    }

    public List<AuditionComment> getCommentsForAPostId(Map<String, Object> filters) {

        try {
            String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";
            URI uri = buildUriWithFilters(COMMENTS_URL, filters);
            ResponseEntity<AuditionComment[]> responseEntity = restTemplate.getForEntity(uri, AuditionComment[].class);
            AuditionComment[] auditionComment = responseEntity.getBody();
            if (auditionComment != null && auditionComment.length > 0) {
                return List.of(auditionComment);
            } else {
                throw new SystemException("Cannot find comments with Post id ", "SYSEXP-MEDIBANK-7", 404);
            }

        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find comments Resource Not Found", 404);
            } else {
                throw new SystemException(e.getMessage(), "SYSEXP-MEDIBANK-8", e.getStatusCode().value(),
                    e.getRootCause());
            }
        }
    }

    private URI buildUriWithFilters(String baseUrl, Map<String, Object> filters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        // Add parameters from the map if present
        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        return builder.build().toUri();
    }
}
