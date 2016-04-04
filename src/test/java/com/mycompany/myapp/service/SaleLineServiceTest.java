package com.mycompany.myapp.service;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleLineRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jaherrera on 29/02/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class SaleLineServiceTest {

    @Inject
    private SaleLineRepository saleLineRepository;

    @Test
    public void testFind () {


        List<SaleLine> saleLineList = saleLineRepository.findByarticle_id(new Long(1));

        assertThat(saleLineList.size() > 0);
    }
}
