package com.peploleum.insight.yummy;

import com.peploleum.insight.yummy.dto.entities.insight.Biographics;
import com.peploleum.insight.yummy.service.SearchService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YummySearchTests {
    private final Logger log = LoggerFactory.getLogger(YummySearchTests.class);

    private final static Biographics BIOGRAPHICS = new Biographics();
    private final static String BIO_ID = "12345";
    private final static String BIO_NAME = "azertyuiop";
    private final static String BIO_FIRSTNAME = "qsdfghjklm";

    @Autowired
    private SearchService searchService;

    @Before
    public void initData() {
        BIOGRAPHICS.setId(BIO_ID);
        BIOGRAPHICS.setBiographicsName(BIO_NAME);
        BIOGRAPHICS.setBiographicsFirstname(BIO_FIRSTNAME);

        this.searchService.save(BIOGRAPHICS);
    }

    @After
    public void removeData() {
        this.searchService.delete(BIOGRAPHICS);
    }

    @Test
    public void searchBiographics() throws IllegalAccessException{
        Biographics searchObj = new Biographics();
        searchObj.setBiographicsName(BIO_NAME);
        Object result = this.searchService.searchObjectByName(searchObj);

        assertThat(result instanceof Biographics);
        assertThat(BIO_ID.equals(((Biographics) result).getId()));
    }
}
