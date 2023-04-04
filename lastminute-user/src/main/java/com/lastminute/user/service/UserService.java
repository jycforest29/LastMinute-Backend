package com.lastminute.user.service;

import com.lastminute.user.domain.AccountState;
import com.lastminute.user.domain.User;
import com.lastminute.user.exception.ExceptionCode;
import com.lastminute.user.exception.UserException;
import com.lastminute.user.external.dto.CreateUserRequestDto;
import com.lastminute.user.external.dto.ReadUserResponseDto;
import com.lastminute.user.external.dto.UpdateUserRequestDto;
import com.lastminute.user.repository.ForbiddenNameRepository;
import com.lastminute.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    // Read Only
    private final UserRepository userRepository;

    // Write
    private final UserWriteFacade userWriteFacade;

    // TODO : 캐싱 처리
    private final ForbiddenNameService forbiddenNameService;

    public ReadUserResponseDto createUser(CreateUserRequestDto request) {
        validateUserName(request.getNickname());

        User newbie = request.toEntity();
        newbie = userWriteFacade.createUser(newbie);
        return ReadUserResponseDto.of(newbie);
    }

    private void validateUserName(String nickname) {
        final boolean isForbidden = forbiddenNameService.isForbiddenName(nickname);
        if (isForbidden) {
            throw new UserException(ExceptionCode.USER_NAME_NOT_ALLOWED);
        }
    }

    public ReadUserResponseDto findUser(Long userId) {
        User user = findUserInternal(userId);
        return ReadUserResponseDto.of(user);
    }

    public ReadUserResponseDto updateUser(UpdateUserRequestDto request) {
        User user = findUserInternal(request.getUserId());

        validateUserName(request.getNickname());

        user.updateProfile(request.getNickname(), request.getEmail());
        userWriteFacade.updateProfile(user);

        return ReadUserResponseDto.of(user);
    }

    public void withdrawUser(Long userId) {
        User user = findUserInternal(userId);

        if (user.getAccountState().equals(AccountState.WITHDRAWN)) {
            throw new UserException(ExceptionCode.USER_ALREADY_WITHDRAWN);
        }
        userWriteFacade.withdrawUser(user);
    }

    private User findUserInternal(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
    }

}
