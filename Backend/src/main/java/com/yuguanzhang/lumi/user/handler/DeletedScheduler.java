package com.yuguanzhang.lumi.user.handler;

import com.yuguanzhang.lumi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeletedScheduler {

    private final UserRepository userRepository;


    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteUsers() {
        userRepository.deleteAllByIsDeleted("Y");
    }
}
