package com.lastminute.user.service;

import com.lastminute.user.domain.ForbiddenName;
import com.lastminute.user.exception.ExceptionCode;
import com.lastminute.user.exception.UserException;
import com.lastminute.user.external.dto.CreateForbiddenNameRequestDto;
import com.lastminute.user.external.dto.ReadForbiddenNameResponseDto;
import com.lastminute.user.repository.ForbiddenNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForbiddenNameService {

    private final ForbiddenNameRepository forbiddenNameRepository;

    public ReadForbiddenNameResponseDto createForbiddenName(CreateForbiddenNameRequestDto request) {
        if (isForbiddenName(request.getName())) {
            throw new UserException(ExceptionCode.FORBIDDEN_NAME_ALREADY_EXIST);
        }

        ForbiddenName forbiddenName = request.toEntity();
        forbiddenName = forbiddenNameRepository.save(forbiddenName);

        return ReadForbiddenNameResponseDto.of(forbiddenName);
    }

    public void deleteForbiddenName(String name) {
        ForbiddenName forbiddenName = forbiddenNameRepository.findById(name)
                .orElseThrow(() -> new UserException(ExceptionCode.FORBIDDEN_NAME_NOT_FOUND));

        forbiddenNameRepository.delete(forbiddenName);
    }

    public boolean isForbiddenName(String name) {
        return forbiddenNameRepository.findById(name).isPresent();
    }

}
