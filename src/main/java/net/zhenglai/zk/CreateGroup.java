package net.zhenglai.zk;


import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Zhenglai on 8/3/16.
 * <p>
 * <p>
 * A program to create a znode representing a group in ZooKeeper
 */
public class CreateGroup {

    static class GroupCreator implements Watcher {
        private static final int SESSION_TIMEOUT = 5000;

        private ZooKeeper zk;

        private CountDownLatch connectedSignal = new CountDownLatch(1);

        public void connect(String hosts) throws IOException, InterruptedException {
            // the first is the host address (and optional port, which defaults to 2181)
            // this is the watcher object, to receive callback when watched event occurs
            zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
            // zk.addAuthInfo("digest", "tom:secret".getBytes());
            // When a ZooKeeper instance is created, it starts a thread to connect to the ZooKeeper service.
            // The call to the constructor returns immediately
            connectedSignal.await();
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                // block until the ZooKeeper instance is ready
                // After calling countDown() once, the counter reaches zero and the await() method returns.
                connectedSignal.countDown();
            }
        }

        public void create(String groupName) throws KeeperException, InterruptedException {
            String path = "/" + groupName;
            // OPEN_ACL_UNSAFE, which gives all permissions (except ADMIN permission) to everyone.
            String createdPath = zk.create(path, null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.printf("Created %s", createdPath);
        }

        public void close() throws InterruptedException {
            zk.close();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        GroupCreator createGroup = new GroupCreator();
        createGroup.connect(args[0]);
        createGroup.create(args[1]);
        createGroup.close();
    }
}
