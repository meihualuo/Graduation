package com.example.mazigame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapModel{
    private int[][] map;
    public int wide;

    public static final int ROAD = 0;
    public static final int ROAD_NOT_INIT = -1;
    public static final int WAIT = 1;
    public static final int COLUMN = 2;
    public static final int TERMINAL = 3;
    public static final int STAIR_IN = 4;
    public static final int STAIR_OUT = 5;

    private List<CubeModel> waits;

    public MapModel(int[][] map){
        this.map = map;
        this.wide = map.length;
    }

     public MapModel(int wide){
         this.wide = wide*2+1;
         init();
         //取第一g格作为初始起点，并将其邻墙加入待定序列中
         this.map[1][1] = ROAD;
         addWaite(1,1);
         initColumn();
     }

     public MapModel(int wide,CubeModel statr1,CubeModel statr2){
         this.wide = wide*2+1;
         init();
         addWaite(statr1.getWeighe(),statr1.getHeighe());
         addWaite(statr2.getWeighe(),statr2.getHeighe());
         this.map[statr1.getWeighe()][statr1.getHeighe()] = STAIR_IN;
         this.map[statr2.getWeighe()][statr2.getHeighe()] = STAIR_IN;
         initColumn();
     }

     public void addStairOut(CubeModel statr){
         this.map[statr.getWeighe()][statr.getHeighe()] = STAIR_OUT;
     }

     public void setTerminal(){
         this.map[this.wide-2][this.wide-2] = TERMINAL;
     }

     private void init(){
         this.map = new int[wide][wide];
         this.waits = new ArrayList<>();
         initMap(wide);
     }

    public int[][] getMap(){
         return this.map;
    }
    private void initMap(int wide) {
        for (int i=1;i<wide-1;i++){
            for (int j=1;j<wide-1;j++){
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
            this.map[i][wide-1] = COLUMN;
        }
        for(int j=0;j<wide;j++){
            this.map[0][j] = COLUMN;
            this.map[wide-1][j] = COLUMN;
        }
    }

    private void initColumn(){
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
         if(vert+1<wide)if (map[hori][vert+1] == WAIT) waits.add(new CubeModel(hori,vert+1));
         if(hori-1>0)   if (map[hori-1][vert] == WAIT) waits.add(new CubeModel(hori-1,vert));
         if(hori+1<wide)if (map[hori+1][vert] == WAIT) waits.add(new CubeModel(hori+1,vert));
    }
}
