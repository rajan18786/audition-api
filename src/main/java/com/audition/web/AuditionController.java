package com.audition.web;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditionController {

    private final AuditionService auditionService;

    public AuditionController(AuditionService auditionService) {
        this.auditionService = auditionService;
    }

    @Operation(summary = "Get all posts from the system", description = "Returns a list of posts ; searchable passing a userId or Title")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam(required = false) Integer userId,
        @RequestParam(required = false) String title) {

        Map<String, Object> filters = new HashMap<>();
        if (userId != null) {
            filters.put("userId", userId);
        }
        if (title != null) {
            filters.put("title", title);
        }
        return auditionService.getPosts(filters);
    }

    @Operation(summary = "Get a post based on Id", description = "Returns a posts for a particular Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostById(@PathVariable("id") final int postId) {

        if (postId < 1) {
            throw new IllegalArgumentException("postId must be greater than 0");
        }
        return auditionService.getPostById(postId);
    }

    @Operation(summary = "Get all comments for a Post from the system", description = "Returns a list of comments passing a postId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })
    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionComment> getCommentsByPostId(@PathVariable("id") final int postId) {

        if (postId < 1) {
            throw new IllegalArgumentException("postId must be greater than 0");
        }
        return auditionService.getCommentsByPostId(postId);
    }

    @Operation(summary = "Get all comments for posts from the system", description = "Returns a list of comments , also searchable passing a postId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })
    @RequestMapping(value = "comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionComment> getCommentsForAPostId(@RequestParam(required = false) Integer postId) {

        Map<String, Object> filters = new HashMap<>();
        if (postId != null) {
            filters.put("postId", postId);
        }
        return auditionService.getCommentsForAPostId(filters);
    }


}
