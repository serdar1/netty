/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel.epoll;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.testsuite.transport.TestsuitePermutation;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Collections;
import java.util.List;

final class EpollTestUtils {

    private static final int BOSSES = 2;
    private static final int WORKERS = 3;

    private static final EventLoopGroup epollBossGroup =
            new EpollEventLoopGroup(BOSSES, new DefaultThreadFactory("testsuite-epoll-boss", true));
    private static final EventLoopGroup epollWorkerGroup =
            new EpollEventLoopGroup(WORKERS, new DefaultThreadFactory("testsuite-epoll-worker", true));

    static List<TestsuitePermutation.BootstrapComboFactory<ServerBootstrap, Bootstrap>> newFactories() {
        return Collections.<TestsuitePermutation.BootstrapComboFactory<ServerBootstrap, Bootstrap>>singletonList(
                new TestsuitePermutation.BootstrapComboFactory<ServerBootstrap, Bootstrap>() {
            @Override
            public ServerBootstrap newServerInstance() {
                return new ServerBootstrap().group(
                        epollBossGroup, epollWorkerGroup).channel(EpollServerSocketChannel.class);
            }

            @Override
            public Bootstrap newClientInstance() {
                return new Bootstrap().group(epollWorkerGroup).channel(EpollSocketChannel.class);
            }
        });
    }

    private EpollTestUtils() {
        // utility class
    }
}