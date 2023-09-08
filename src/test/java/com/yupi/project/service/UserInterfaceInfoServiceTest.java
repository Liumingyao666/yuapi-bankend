package com.yupi.project.service;


import com.yupi.yuapicommon.service.InnerUserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserInterfaceInfoServiceTest {

    @Resource
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @Test
    public void invokeCount(){
        boolean b = innerUserInterfaceInfoService.invokeCount(1L, 1L);
        Assertions.assertTrue(b);
    }
}
