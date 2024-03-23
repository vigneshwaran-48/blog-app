package com.vicky.blog.notificationservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.vicky.blog.common.dto.organization.OrganizationPermissionResponse;
import com.vicky.blog.common.dto.organization.OrganizationResponseData;

@HttpExchange("/api/v1/app/organization")
public interface OrganizationServiceClient {
    
    @GetExchange("/{organizationId}")
    public OrganizationResponseData getOrganization(@PathVariable String organizationId);

    @GetExchange("/{organizationId}/notification/access")
    public OrganizationPermissionResponse isUserHasNotificationAccess(@PathVariable String organizationId);

}
