package net.zhenglai.zk;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Zhenglai on 8/3/16.
 */
public class ListGroup {

    private static class GroupLister extends ConnectionWatcher {
        public void list(String groupName) throws InterruptedException, KeeperException {
            String path = "/" + groupName;

            try {
                List<String> children = zk.getChildren(path, false);
                if (children.isEmpty()) {
                    System.out.printf("No members in group %s\n", groupName);
                    System.exit(1);
                }

                for (String child : children) {
                    System.out.println(child);
                }
            } catch (KeeperException.NoNodeException e) {
                System.out.printf("Group %s does not exist\n", groupName);
                System.exit(1);
            }
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        GroupLister listGroup = new GroupLister();
        listGroup.connect(args[0]);
        listGroup.list(args[1]);
        listGroup.close();
    }
}
