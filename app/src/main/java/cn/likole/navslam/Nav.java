package cn.likole.navslam;

import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.geometry.PointF;
import com.slamtec.slamware.robot.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 导航类,用于计算关键途径点
 * Created by likole on 8/26/17.
 */

public class Nav {

    private static int width=20;
    private static int height=20;
    private static int[][] map= {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };

    private static List<Node> openList = new ArrayList<Node>();// 开启列表
    private static List<Node> closeList = new ArrayList<Node>();// 关闭列表

    public void run(AbstractSlamwarePlatform slamwarePlatform, PointF dest)
    {
        Location location=slamwarePlatform.getLocation();
        int x=height-Math.round(location.getX());
        int y=width-Math.round(location.getY());
        int nx=height-Math.round(dest.getX());
        int ny=width-Math.round(dest.getY());

//        test
//        int x=height+0;
//        int y=width- 0;
//        int nx=height- 18;
//        int ny=width-(-7);


        if(x<0||x>height*2||y<0||y>width*2) return;
        if(nx<0||nx>height*2||ny<0||ny>width*2) return;

        int Max_row = height*2+1;
        int MAX_col = width*2+1;

        Node startPoint = new Node(x, y, null);
        Node endPoint = new Node(nx,
                ny, null);

        seachWay(map, startPoint, endPoint, Max_row, MAX_col,slamwarePlatform);

        //final map
//        for (int i = 0; i < Max_row; i++) {
//            for (int j = 0; j < MAX_col; j++) {
//                System.out.print(map[i][j]+" ");
//            }
//            System.out.print("\n");
//        }
    }

    /**
     * 搜寻最短路径new Nav().
     *
     * @param arr
     * @param startPoint
     * @param endPoint
     */
    private static boolean seachWay(int[][] arr, Node startPoint,
                                    Node endPoint, int row, int col,AbstractSlamwarePlatform slamwarePlatform) {
        final int CONST_HENG = 10;// 垂直方向或水平方向移动的路径评分
        final int CONST_XIE = 14;// 斜方向移动的路径评分
        Node curNode = startPoint;
        if (startPoint.x < 0 || startPoint.y > col || endPoint.x < 0
                || endPoint.y > col
//                || arr[startPoint.x][startPoint.y] == 0
                || arr[endPoint.x][endPoint.y] == 0) {
            throw new IllegalArgumentException("坐标参数错误！！");
        }

        openList.add(startPoint);
        while (!openList.isEmpty() && !openList.contains(endPoint)) {
            curNode = minList(openList);
            if (curNode.x == endPoint.x && curNode.y == endPoint.y
                    || openList.contains(endPoint)) {
                System.out.println("找到最短路径");
                List<Location> locations=new ArrayList<>();
                while(!(curNode.x==startPoint.x&&curNode.y==startPoint.y)){
//                    System.out.print("("+curNode.x+","+curNode.y+") ");
                    Location location=new Location(height-curNode.x,width-curNode.y,0);
                    System.out.print("("+location.getX()+","+location.getY()+") ");
                    locations.add(location);
                    map[curNode.x][curNode.y]=2;
                    if (curNode.parentNode!=null) {
                        curNode=curNode.parentNode;
                    }
                }
                Collections.reverse(locations);
                for (Location location:locations)
                {
                    slamwarePlatform.moveTo(location,true);
                }
//                System.out.print("("+startPoint.x+","+startPoint.y+")\n ");
                map[startPoint.x][startPoint.y]=2;
                return true;
            }
            // 上
            if (curNode.y - 1 >= 0) {
                checkPath(curNode.x, curNode.y - 1, curNode, endPoint,
                        CONST_HENG);
            }
            // 下
            if (curNode.y + 1 < col) {
                checkPath(curNode.x, curNode.y + 1, curNode, endPoint,
                        CONST_HENG);
            }
            // 左
            if (curNode.x - 1 >= 0) {
                checkPath(curNode.x - 1, curNode.y, curNode, endPoint,
                        CONST_HENG);
            }
            // 右
            if (curNode.x + 1 < row) {
                checkPath(curNode.x + 1, curNode.y, curNode, endPoint,
                        CONST_HENG);
            }
            // 左上
            if (curNode.x - 1 >= 0 && curNode.y - 1 >= 0) {
                checkPath(curNode.x - 1, curNode.y - 1, curNode, endPoint,
                        CONST_XIE);
            }
            // 左下
            if (curNode.x - 1 >= 0 && curNode.y + 1 < col) {
                checkPath(curNode.x - 1, curNode.y + 1, curNode, endPoint,
                        CONST_XIE);
            }
            // 右上
            if (curNode.x + 1 < row && curNode.y - 1 >= 0) {
                checkPath(curNode.x + 1, curNode.y - 1, curNode, endPoint,
                        CONST_XIE);
            }
            // 右下
            if (curNode.x + 1 < row && curNode.y + 1 < col) {
                checkPath(curNode.x + 1, curNode.y + 1, curNode, endPoint,
                        CONST_XIE);
            }
            openList.remove(curNode);
            closeList.add(curNode);
        }
        // if (!openList.contains(endPoint)) {
        // System.out.println("一条路径都未找到！！！");
        // return false;
        // }

        return false;

    }

    // 核心算法---检测节点是否通路
    private static boolean checkPath(int x, int y, Node preNode, Node endPoint,
                                     int c) {
        Node node = new Nav().new Node(x, y, preNode);
        // 查找地图中是否能通过
        if (map[x][y] == 0) {
            closeList.add(node);
            return false;
        }
        // 查找关闭列表中是否存在
        if (isListContains(closeList, x, y) != -1) {// 存在
            return false;
        }
        // 查找开启列表中是否存在
        int index = -1;
        if ((index = isListContains(openList, x, y)) != -1) {// 存在
            // G值是否更小，即是否更新G，F值
            if ((preNode.g + c) < openList.get(index).g) {
                countG(node, endPoint, c);
                countF(node);
                openList.set(index, node);
            }
        } else {
            // 不存在，添加到开启列表中
            node.setParentNode(preNode);
            count(node, endPoint, c);
            openList.add(node);
        }
        return true;
    }

    // 计算G,H,F值
    private static void count(Node node, Node eNode, int cost) {
        countG(node, eNode, cost);
        countH(node, eNode);
        countF(node);
    }

    // 计算G值
    private static void countG(Node node, Node eNode, int cost) {
        if (node.getParentNode() == null) {
            node.setG(cost);
        } else {
            node.setG(node.getParentNode().getG() + cost);
        }
    }

    // 计算H值
    private static void countH(Node node, Node eNode) {
        node.setF((Math.abs(node.getX() - eNode.getX()) + Math.abs(node.getY()
                - eNode.getY())) * 10);
    }

    // 计算F值
    private static void countF(Node node) {
        node.setF(node.getG() + node.getH());
    }

    // 集合中是否包含某个元素(-1：没有找到，否则返回所在的索引)
    private static int isListContains(List<Node> list, int x, int y) {
        for (int i = 0; i < list.size(); i++) {
            Node node = list.get(i);
            if (node.getX() == x && node.getY() == y) {
                return i;
            }
        }
        return -1;
    }

    // 找最小值
    private static Node minList(List<Node> list) {
        Iterator<Node> i = list.iterator();
        Node candidate = i.next();

        while (i.hasNext()) {
            Node next = i.next();
            if (next.compareTo(candidate) < 0)
                candidate = next;
        }
        return candidate;
    }

    // 节点类
    private class Node {
        private int x;// X坐标
        private int y;// Y坐标
        private Node parentNode;// 父类节点
        private int g;// 当前点到起点的移动耗费
        private int h;// 当前点到终点的移动耗费，即曼哈顿距离|x1-x2|+|y1-y2|(忽略障碍物)
        private int f;// f=g+h

        public Node(int x, int y, Node parentNode) {
            this.x = x;
            this.y = y;
            this.parentNode = parentNode;
        }

        public int compareTo(Node candidate) {
            return this.getF() - candidate.getF();
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Node getParentNode() {
            return parentNode;
        }

        public void setParentNode(Node parentNode) {
            this.parentNode = parentNode;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public int getF() {
            return f;
        }

        public void setF(int f) {
            this.f = f;
        }

        public String toString() {
            return "(" + x + "," + y + "," + f + ")";
        }
    }

    // 节点比较类
    class NodeFComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getF() - o2.getF();
        }

    }

}
