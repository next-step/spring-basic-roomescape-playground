package roomescape.member.exception;

import roomescape.global.exception.BusinessException;
import roomescape.global.response.code.ErrorCode;

public class MemberException extends BusinessException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
