package com.example.mazigame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapModel{
    private int[][] map;
    public int wide;
    public int high;
    public static final int ROAD = 0;
    public static final int ROAD_NOT_INIT = -1;
    public static final int WAIT = 1;
    public static final int COLUMN = 2;
    public static final int MOVE_OF_LEFT = 1;
    public static final int MOVE_OF_RIGHT = 2;
    public static final int MOVE_OF_TOP = 3;
    public static final int MOVE_OF_BOTTOM = 4;

    private List<CubeModel> waits;

    public MapModel(int[][] map){
        this.map = map;
        this.wide = map.length;
        this.high = map[0].length;
    }

     public MapModel(int wide,int high){
         this.wide = wide*2+1;
         this.high = high*2+1;
         init();
     }
    private void init(){
        this.map = new int[wide][high];
        waits = new ArrayList<>();
        initMap(wide, high);
    }

    public void setMap(int[][] map){
        this.map = map;
    }

    public int[][] getMap(){
         return map;
    }
    private void initMap(int wide, int high) {
        for (int i=1;i<wide-1;i++){
            for (int j=1;j<high-1;j++){
                if(i%2 == 1 && j%2 == 1){
                    this.map[i][j] = ROAD_NOT_INIT;
                }else if (i%2 == 0 && j%2 == 0){
                    this.map[i][j] = COLUMN;
                }else {
                    this.map[i][j] = WAIT;
                }
            }
        }
        for(int i=0;i<wide;i++){
            this.map[i][0] = COLUMN;
            this.map[i][high-1] = COLUMN;
        }
        for(int j=0;j<high;j++){
            this.map[0][j] = COLUMN;
            this.map[wide-1][j] = COLUMN;
        }

        //取第一g格作为初始起点，并将其邻墙加入待定序列中
        map[1][1] = ROAD;
        waits.add(new CubeModel(1,2));
        waits.add(new CubeModel(2,1));
        Random random = new Random();
        while (!waits.isEmpty()){
            int ranInt = random.nextInt(waits.size());
            CubeModel cube = waits.get(ranInt);
            waits.remove(ranInt);
            if(cube.getWeighe()%2 == 1){
                if(map[cube.getWeighe()][cube.getHeighe()-1] == ROAD_NOT_INIT){
                    map[cube.getWeighe()][cube.getHeighe()] = ROAD;
                    addWaite(cube.getWeighe(),cube.getHeighe()-1);
                }else if(map[cube.getWeighe()][cube.getHeighe()+1] == ROAD_NOT_INIT){
                    map[cube.getWeighe()][cube.getHeighe()] = ROAD;
                    addWaite(cube.getWeighe(),cube.getHeighe()+1);
                }else {
                    map[cube.getWeighe()][cube.getHeighe()] = COLUMN;
                }
            }
            if(cube.getHeighe()%2 == 1){
                if(map[cube.getWeighe()-1][cube.getHeighe()] == ROAD_NOT_INIT){
                    map[cube.getWeighe()][cube.getHeighe()] = ROAD;
                    addWaite(cube.getWeighe()-1,cube.getHeighe());
                }else if (map[cube.getWeighe()+1][cube.getHeighe()] == ROAD_NOT_INIT){
                    map[cube.getWeighe()][cube.getHeighe()] = ROAD;
                    addWaite(cube.getWeighe()+1,cube.getHeighe());
                }else {
                    map[cube.getWeighe()][cube.getHeighe()] = COLUMN;
                }
            }
        }
    }

    private void addWaite(int hori,int vert){
         map[hori][vert] = ROAD;
         if(vert-1>0)   if (map[hori][vert-1] == WAIT) waits.add(new CubeModel(hori,vert-1));
         if(vert+1<high)if (map[hori][vert+1] == WAIT) waits.add(new CubeModel(hori,vert+1));
         if(hori-1>0)   if (map[hori-1][vert] == WAIT) waits.add(new CubeModel(hori-1,vert));
         if(hori+1<wide)if (map[hori+1][vert] == WAIT) waits.add(new CubeModel(hori+1,vert));
    }

    public static String mapToString(int[][] map){
         StringBuilder result = new StringBuilder();
         if(map == null)
             return result.toString();
         for (int i = 0;i<map.length;i++){
             for (int j=0;j<map[0].length;j++){
                 switch (map[i][j]){
                     case ROAD: {
                         result.append( "R");
                         break;
                     }
                     case COLUMN:{
                         result.append( "C");
                         break;
                     }
                     default:{
                         result.append( "X");
                     }
                 }
             }
             result.append( "N");
         }
         return result.toString();
    }

    public static int[][] stringToMap(String strMap){
        String[] temp = strMap.split("N");
        int[][] map = new int[temp.length][temp[0].length()];
        for (int i = 0; i < temp.length; i++){
            char[] charTemp = temp[i].toCharArray();
            for (int j = 0; j < charTemp.length; j++){
                switch (charTemp[j]){
                    case 'R': {
                        map[i][j] = ROAD;
                        break;
                    }
                    case 'C':{
                        map[i][j] = COLUMN;
                        break;
                    }
                }
            }
        }
         return map;
    }

    /**
     * 递归算法求解路径，自行设计的算法，
     * 只适用于单路迷宫，如非迷宫求解路径非最短路径
     * @param map
     * @param roadList
     * @param people
     * @param nowCube
     * @return
     */
    public static boolean promptRoad(int[][] map,List<CubeModel> roadList,CubeModel people,CubeModel nowCube){

         if(people.getWeighe() == nowCube.getWeighe() && people.getHeighe() == nowCube.getHeighe()){
             roadList.add(nowCube);
             return true;
         }
         //向4个方向查找路径
        if(map[nowCube.getWeighe()-1][nowCube.getHeighe()] == ROAD){
            map[nowCube.getWeighe()-1][nowCube.getHeighe()] = COLUMN;
            CubeModel newCube = new CubeModel(nowCube.getWeighe()-2,nowCube.getHeighe());
            if(promptRoad(map,roadList,people,newCube)){
                roadList.add(nowCube);
                map[nowCube.getWeighe()-1][nowCube.getHeighe()] = ROAD;
                return true;
            }
            map[nowCube.getWeighe()-1][nowCube.getHeighe()] = ROAD;
        }
        if(map[nowCube.getWeighe()+1][nowCube.getHeighe()] == ROAD){
            map[nowCube.getWeighe()+1][nowCube.getHeighe()] = COLUMN;
            CubeModel newCube = new CubeModel(nowCube.getWeighe()+2,nowCube.getHeighe());
            if(promptRoad(map,roadList,people,newCube)){
                roadList.add(nowCube);
                map[nowCube.getWeighe()+1][nowCube.getHeighe()] = ROAD;
                return true;
            }
            map[nowCube.getWeighe()+1][nowCube.getHeighe()] = ROAD;
        }
        if(map[nowCube.getWeighe()][nowCube.getHeighe()-1] == ROAD){
            map[nowCube.getWeighe()][nowCube.getHeighe()-1] = COLUMN;
            CubeModel newCube = new CubeModel(nowCube.getWeighe(),nowCube.getHeighe()-2);
            if(promptRoad(map,roadList,people,newCube)){
                map[nowCube.getWeighe()][nowCube.getHeighe()-1] = ROAD;
                roadList.add(nowCube);
                return true;
            }
            map[nowCube.getWeighe()][nowCube.getHeighe()-1] = ROAD;
        }
        if(map[nowCube.getWeighe()][nowCube.getHeighe()+1] == ROAD){
            map[nowCube.getWeighe()][nowCube.getHeighe()+1] = COLUMN;
            CubeModel newCube = new CubeModel(nowCube.getWeighe(),nowCube.getHeighe()+2);
            if(promptRoad(map,roadList,people,newCube)){
                roadList.add(nowCube);
                map[nowCube.getWeighe()][nowCube.getHeighe()+1] = ROAD;
                return true;
            }
            map[nowCube.getWeighe()][nowCube.getHeighe()+1] = ROAD;
        }
         return false;
    }
}
