package com.sangria.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangria.client.ServiceClientApplication;
import com.sangria.client.Enum.PlayerTradeTypeEnum;
import com.sangria.client.dao.GameManagerMapper;
import com.sangria.client.dto.ItemMakeDTO;
import com.sangria.client.dto.PlayerAddDTO;
import com.sangria.client.dto.PlayerTradeRequestDTO;
import com.sangria.client.entity.GameManagerDO;

@SpringBootTest(classes = ServiceClientApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ServiceOperationApplicationTests {

}
