package com.jobdam.admin.repository;

import com.jobdam.admin.entity.MembertypeChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MembertypeChangeRepository extends JpaRepository<MembertypeChange, Integer> {


    List<MembertypeChange>  findByRequestAdminStatusCode_Code(String pending);

    boolean existsByUserIdAndRequestAdminStatusCode_Code(Integer userId, String pending);
}
