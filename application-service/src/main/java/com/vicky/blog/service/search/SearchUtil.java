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
import com.vicky.blog.common.dto.search.SearchDTO.Entity;
import com.vicky.blog.common.dto.search.SearchDTO.SearchBy;
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

    List<OrganizationDTO> searchOrganizations(String userId, String query, List<SearchBy> searchBy)
            throws AppException {
        List<OrganizationDTO> organizations = organizationService.getOrganizationsVisibleToUser(userId);
        return organizations.stream().filter(organization -> {
            if (searchBy.contains(SearchBy.ALL) || searchBy.contains(SearchBy.ORGANIZATION_NAME)) {
                return organization.getName().toLowerCase().contains(query.toLowerCase());
            }
            return false;
        }).collect(Collectors.toList());
    }

    List<UserDTO> searchUsers(String userId, String query, List<SearchBy> searchBy) throws AppException {
        List<UserDTO> users = userService.getUsers(userId);
        return users.stream().filter(user -> {
            if (searchBy.contains(SearchBy.USER_NAME)) {
                return user.getName().toLowerCase().contains(query.toLowerCase());
            } else if (searchBy.contains(SearchBy.USER_EMAIL)) {
                return user.getEmail().toLowerCase().contains(query.toLowerCase());
            } else if (searchBy.contains(SearchBy.ALL)) {
                return user.getName().toLowerCase().contains(query.toLowerCase())
                        || user.getEmail().toLowerCase().contains(query.toLowerCase());
            }
            return false;
        }).collect(Collectors.toList());
    }

    List<BlogDTO> searchBlogs(String userId, String query, List<SearchBy> searchBy) throws AppException {
        return blogService.getAllBlogsVisibleToUser(userId).stream().filter(blog -> {
            if (searchBy.contains(SearchBy.BLOG_TITLE)) {
                return blog.getTitle().toLowerCase().contains(query.toLowerCase());
            } else if (searchBy.contains(SearchBy.BLOG_CONTENT)) {
                return blog.getContent().toLowerCase().contains(query.toLowerCase());
            } else if (searchBy.contains(SearchBy.ALL)) {
                return blog.getTitle().toLowerCase().contains(query.toLowerCase())
                        || blog.getContent().toLowerCase().contains(query.toLowerCase());
            }
            return false;
        }).collect(Collectors.toList());
    }

    Optional<SearchDTO> search(String userId, String query, SearchType type, List<SearchBy> searchBy)
            throws AppException {
        List<Entity> entities = new ArrayList<>();
        SearchDTO searchDTO = new SearchDTO();
        switch (type) {
        case ORGANIZATION:
            List<OrganizationDTO> organizations = searchOrganizations(userId, query, searchBy);

            entities = organizations.stream()
                    .map(organization -> searchDTO.new Entity(organization.getId(), organization.getProfileId(), type))
                    .toList();
            break;
        case USER:
            entities = searchUsers(userId, query, searchBy).stream()
                    .map(user -> searchDTO.new Entity(user.getId(), user.getProfileId(), type)).toList();
            break;
        case BLOG:
            entities = searchBlogs(userId, query, searchBy).stream()
                    .map(blog -> searchDTO.new Entity(blog.getId(), blog.getPostedProfileId(), type)).toList();
            break;
        default:
            return Optional.empty();
        }
        searchDTO.setEntities(entities);
        return Optional.of(searchDTO);
    }

}
