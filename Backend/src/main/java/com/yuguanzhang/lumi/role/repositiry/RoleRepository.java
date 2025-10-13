package com.yuguanzhang.lumi.role.repositiry;

import com.yuguanzhang.lumi.role.entity.Role;
import com.yuguanzhang.lumi.role.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    //role(ex RoleName.TUTOR) 이름으로 Role 객체 조회
    Optional<Role> findByRoleName(RoleName roleName);

}
