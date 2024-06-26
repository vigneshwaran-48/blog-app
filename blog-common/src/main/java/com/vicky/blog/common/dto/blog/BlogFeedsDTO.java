package com.vicky.blog.common.dto.blog;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogFeedsDTO implements Serializable {

    public enum PageStatus {
        AVAILABLE, SIGNUP, BUY_PREMIUM, NOT_AVAILABLE
    }
    
    private PageStatus status;
    private List<BlogDTO> feeds;

}
