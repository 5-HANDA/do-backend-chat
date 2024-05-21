package hobbiedo.base.exception;

import hobbiedo.base.code.BaseErrorCode;
import hobbiedo.base.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
	private BaseErrorCode errorCode;

	public ErrorReasonDto getErrorReason() {
		return errorCode.getReason();
	}

	public ErrorReasonDto getErrorReasonHttpStatus() {
		return errorCode.getReasonHttpStatus();
	}
}
