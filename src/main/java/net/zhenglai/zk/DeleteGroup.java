package net.zhenglai.zk;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

public class DeleteGroup {

    private static class GroupDeleter extends ConnectionWatcher {
        private void delete(String groupName) throws KeeperException, InterruptedException {
            String path = "/" + groupName;

            try {
                List<String> children = zk.getChildren(path, false);
                for (String child : children) {
                    zk.delete(path + "/" + child, -1);
                }

                zk.delete(path, -1);
            } catch (KeeperException.NoNodeException e) {
                System.out.printf("Group %s does not exist", groupName);
                System.exit(1);
            }
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        GroupDeleter deleteGroup = new GroupDeleter();
        deleteGroup.connect(args[0]);
        deleteGroup.delete(args[1]);
        deleteGroup.close();
    }
}
