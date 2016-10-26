/*
 * Copyright 2011-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lambdaworks.redis.support;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;

import org.junit.Test;

import com.lambdaworks.redis.RedisURI;

/**
 * @author Mark Paluch
 */
public class RedisClusterClientFactoryBeanTest {

    private RedisClusterClientFactoryBean sut = new RedisClusterClientFactoryBean();

    @Test(expected = IllegalArgumentException.class)
    public void invalidUri() throws Exception {

        sut.setUri(URI.create("http://www.web.de"));
        sut.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sentinelUri() throws Exception {

        sut.setUri(URI.create(RedisURI.URI_SCHEME_REDIS_SENTINEL + "://www.web.de"));
        sut.afterPropertiesSet();
    }

    @Test
    public void validUri() throws Exception {

        sut.setUri(URI.create(RedisURI.URI_SCHEME_REDIS + "://password@host"));
        sut.afterPropertiesSet();
        assertThat(sut.getRedisURI().getHost()).isEqualTo("host");
        assertThat(sut.getRedisURI().getPassword()).isEqualTo("password".toCharArray());
    }

    @Test
    public void validUriPasswordOverride() throws Exception {

        sut.setUri(URI.create(RedisURI.URI_SCHEME_REDIS + "://password@host"));
        sut.setPassword("thepassword");

        sut.afterPropertiesSet();
        assertThat(sut.getRedisURI().getHost()).isEqualTo("host");
        assertThat(sut.getRedisURI().getPassword()).isEqualTo("thepassword".toCharArray());
    }
    
    @Test
    public void supportsSsl() throws Exception {

        sut.setUri(URI.create(RedisURI.URI_SCHEME_REDIS_SECURE + "://password@host"));
        sut.afterPropertiesSet();
        assertThat(sut.getRedisURI().getHost()).isEqualTo("host");
        assertThat(sut.getRedisURI().getPassword()).isEqualTo("password".toCharArray());
        assertThat(sut.getRedisURI().isVerifyPeer()).isFalse();
        assertThat(sut.getRedisURI().isSsl()).isTrue();
    }
}
