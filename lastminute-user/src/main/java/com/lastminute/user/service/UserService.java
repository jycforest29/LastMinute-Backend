package com.lastminute.user.service;

import com.lastminute.user.domain.AccountState;
import com.lastminute.user.domain.User;
import com.lastminute.user.exception.ExceptionCode;
import com.lastminute.user.exception.UserException;
import com.lastminute.user.external.dto.UserRequestDto;
import com.lastminute.user.external.dto.UserResponseDto;
import com.lastminute.user.repository.ForbiddenNameRepository;
import com.lastminute.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserUpdateFacade userUpdateFacade;
    private final ForbiddenNameRepository forbiddenNameRepository;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validUserName(userRequestDto.getNickname());

        User newbie = userRequestDto.toEntity();
        newbie = userUpdateFacade.createUser(newbie);
        return UserResponseDto.of(newbie);
    }

    private void validUserName(String nickname) {
        final boolean isForbidden = forbiddenNameRepository.findById(nickname).isPresent();
        if (isForbidden) {
            throw new UserException(ExceptionCode.USER_NAME_NOT_ALLOWED);
        }
    }

    public UserResponseDto findUser(Long userId) {
        User user = findUserInternal(userId);
        return UserResponseDto.of(user);
    }

    public void withdrawUser(Long userId) {
        User user = findUserInternal(userId);

        if (user.getAccountState().equals(AccountState.WITHDRAWN)) {
            throw new UserException(ExceptionCode.USER_ALREADY_WITHDRAWN);
        }
        userUpdateFacade.withdrawUser(user);
    }

    private User findUserInternal(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
    }

}
