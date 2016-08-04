package net.zhenglai.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;

/**
 * Created by Zhenglai on 8/3/16.
 */
public class JoinGroup {

    private static class GroupJoiner extends ConnectionWatcher {
        public void join(String groupName, String memberName) throws KeeperException, InterruptedException {
            String path = "/" + groupName + "/" + memberName;
            String createdPath = zk.create(path, null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.printf("Created: %s", createdPath);
        }
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        GroupJoiner joinGroup = new GroupJoiner();
        joinGroup.connect(args[0]);
        joinGroup.join(args[1], args[2]);

        // stay alive until process is killed or thread is interrupted
        Thread.sleep(Long.MAX_VALUE);

        // you will see that upon termination, the ephemeral znode is removed by ZooKeeper.
    }
}
