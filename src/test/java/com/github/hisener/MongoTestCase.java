package com.github.hisener;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;

abstract class MongoTestCase {

    private String hostAddress;
    private int localPort;

    private MongodExecutable mongodExecutable;
    private MongodProcess mongod;

    @BeforeEach
    void setUp() throws IOException {
        hostAddress = Network.getLocalHost().getHostAddress();
        localPort = new ServerSocket(0).getLocalPort();

        mongodExecutable =
                MongodStarter.getDefaultInstance()
                        .prepare(
                                new MongodConfigBuilder()
                                        .version(Version.Main.V3_6)
                                        .net(new Net(hostAddress, localPort, false))
                                        .build());

        mongod = mongodExecutable.start();
    }

    @AfterEach
    void tearDown() {
        mongod.stop();
        mongodExecutable.stop();
    }

    String getUri() {
        return String.format(
                "mongodb://%s:%d/%s",
                this.hostAddress, this.localPort, UUID.randomUUID().toString());
    }
}
