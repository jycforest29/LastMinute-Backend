package com.lastminute.user.repository;

import com.lastminute.user.domain.ForbiddenName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForbiddenNameRepository extends JpaRepository<ForbiddenName, String> {
}
