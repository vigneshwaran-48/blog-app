package com.vicky.blog.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.search.SearchDTO;
import com.vicky.blog.common.dto.search.SearchDTO.SearchType;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.UserService;

@Component
class SearchUtil {
    
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    List<OrganizationDTO> searchOrganizations(String userId, String query) throws AppException {
        List<OrganizationDTO> organizations = organizationService.getOrganizationsVisibleToUser(userId);
        return organizations.stream()
                            .filter(organization -> organization.getName()
                                                                .toLowerCase()
                                                                .contains(query.toLowerCase()))
                            .collect(Collectors.toList());                       
    }

    List<UserDTO> searchUsers(String userId, String query) throws AppException {
        List<UserDTO> users = userService.getUsers(userId);
        return users.stream()
                    .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
    }

    List<BlogDTO> searchBlogs(String userId, String query) throws AppException {
        return blogService.getAllBlogsVisibleToUser(userId)
                            .stream()
                            .filter(blog -> blog.getTitle().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());
    }

    Optional<SearchDTO> search(String userId, String query, SearchType type) throws AppException {
        List<String> ids = new ArrayList<>();
        switch (type) {
            case ORGANIZATION:
                List<OrganizationDTO> organizations = searchOrganizations(userId, query);
                ids = organizations.stream()
                                        .map(organization -> organization.getId())
                                        .toList();
                break;
            case USER:
                ids = searchUsers(userId, query).stream()
                                                .map(user -> user.getId())
                                                .toList();
                break;
            case BLOG:
                ids = searchBlogs(userId, query).stream()
                                                .map(user -> user.getId())
                                                .toList();
                break;
            default:
                return Optional.empty();
        }
        return Optional.of(new SearchDTO(type, ids));
    }
    
}
