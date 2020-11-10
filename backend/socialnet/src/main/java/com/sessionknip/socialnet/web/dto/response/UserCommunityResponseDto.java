package com.sessionknip.socialnet.web.dto.response;

import com.sessionknip.socialnet.web.dto.community.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class UserCommunityResponseDto {
    private List<UserDto> communityElements;

    public UserCommunityResponseDto() {
    }

    public void setCommunityElements(List<UserDto> communityElements) {
        this.communityElements = communityElements;
    }

}
