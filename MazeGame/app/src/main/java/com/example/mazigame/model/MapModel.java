package com.example.mazigame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapModel{
    public int[][] map;
    public int wide;
    public int high;
    public final int ROAD = 0;
    public final int ROAD_NOT_INIT = -1;
    public final int WAIT = 1;
    public final int COLUMN = 2;

    private List<CubeModel> waits;

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

}
