package com.yuguanzhang.lumi.user.service.deleted;

import com.yuguanzhang.lumi.user.dto.deleted.DeletedRequestDto;
import com.yuguanzhang.lumi.user.dto.deleted.DeletedResponseDto;

public interface DeletedService {
    DeletedResponseDto deleted(DeletedRequestDto requestDto);
}
