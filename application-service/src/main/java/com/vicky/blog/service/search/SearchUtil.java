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
            boolean matchesName = searchBy.contains(SearchBy.ORGANIZATION_NAME) || searchBy.contains(SearchBy.ALL);
            boolean matchesProfileId = searchBy.contains(SearchBy.PROFILE_ID);

            return (matchesName && organization.getName().toLowerCase().contains(query.toLowerCase())
                    || (matchesProfileId && organization.getProfileId().toLowerCase().contains(query.toLowerCase())));
        }).collect(Collectors.toList());
    }

    List<UserDTO> searchUsers(String userId, String query, List<SearchBy> searchBy) throws AppException {
        List<UserDTO> users = userService.getUsers(userId);
        return users.stream().filter(user -> {
            boolean matchesName = searchBy.contains(SearchBy.USER_NAME) || searchBy.contains(SearchBy.ALL);
            boolean matchesEmail = searchBy.contains(SearchBy.USER_EMAIL) || searchBy.contains(SearchBy.ALL);
            boolean matchesProfileId = searchBy.contains(SearchBy.PROFILE_ID);

            return (matchesName && user.getName().toLowerCase().contains(query.toLowerCase()))
                    || (matchesEmail && user.getEmail().toLowerCase().contains(query.toLowerCase())
                            || (matchesProfileId && user.getProfileId().toLowerCase().contains(query.toLowerCase())));
        }).collect(Collectors.toList());
    }

    List<BlogDTO> searchBlogs(String userId, String query, List<SearchBy> searchBy) throws AppException {
        return blogService.getAllBlogsVisibleToUser(userId).stream().filter(blog -> {
            boolean matchesTitle = searchBy.contains(SearchBy.BLOG_TITLE);
            boolean matchesContent = searchBy.contains(SearchBy.BLOG_CONTENT);
            boolean matchesProfileId = searchBy.contains(SearchBy.PROFILE_ID);

            return (matchesTitle && blog.getTitle().toLowerCase().contains(query.toLowerCase()))
                    || (matchesContent && blog.getContent().toLowerCase().contains(query.toLowerCase()))
                    || (matchesProfileId && blog.getPostedProfileId().toLowerCase().contains(query.toLowerCase()));
        }).collect(Collectors.toList());
    }

    Optional<SearchDTO> search(String userId, String query, SearchType type, List<SearchBy> searchBy)
            throws AppException {
        List<Entity> entities = new ArrayList<>();
        SearchDTO searchDTO = new SearchDTO();
        boolean isAll = type == SearchType.ALL;
        switch (type) {
        case ALL:
        case ORGANIZATION:
            List<OrganizationDTO> organizations = searchOrganizations(userId, query, searchBy);

            entities.addAll(organizations.stream()
                    .map(organization -> searchDTO.new Entity(organization.getId(), organization.getProfileId(),
                            SearchType.ORGANIZATION, organization.getImage(), organization.getName()))
                    .collect(Collectors.toList()));
            if (!isAll) {
                break;
            }
        case USER:
            entities.addAll(searchUsers(userId, query, searchBy).stream().map(user -> searchDTO.new Entity(user.getId(),
                    user.getProfileId(), SearchType.USER, user.getImage(), user.getName()))
                    .collect(Collectors.toList()));
            if (!isAll) {
                break;
            }
        case BLOG:
            entities.addAll(searchBlogs(userId, query, searchBy).stream().map(blog -> searchDTO.new Entity(blog.getId(),
                    blog.getPostedProfileId(), SearchType.BLOG, blog.getImage(), blog.getTitle()))
                    .collect(Collectors.toList()));
            break;
        default:
            return Optional.empty();
        }
        searchDTO.setEntities(entities);
        return Optional.of(searchDTO);
    }

}
