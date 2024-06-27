package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditionService {

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;


    public List<AuditionPost> getPosts(Map<String, Object> filters) {
        return auditionIntegrationClient.getPosts(filters);
    }

    public AuditionPost getPostById(final int postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    public List<AuditionComment> getCommentsByPostId(final int postId) {
        return auditionIntegrationClient.getCommentsByPostId(postId);
    }

    public List<AuditionComment> getCommentsForAPostId(Map<String, Object> filters) {
        return auditionIntegrationClient.getCommentsForAPostId(filters);
    }


}
