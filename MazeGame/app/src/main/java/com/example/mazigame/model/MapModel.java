package com.example.mazigame.model;

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

//    public MapModel(int wide, int high) {
//        this.wide = wide;
//        this.high = high;
//    }

     public MapModel(int wide,int high){
        this.wide = wide;
        this.high = high;
        this.map = new int[wide][high];
        for (int i=0;i<wide;i++){
            for (int j=0;j<high;j++){
                if(i%2 == 0 && j%2 == 0){
                    this.map[i][j] = ROAD_NOT_INIT;
                }else if (i%2 == 1 && j%2 == 1){
                    this.map[i][j] = COLUMN;
                }else {
                    this.map[i][j] = WAIT;
                    //加入待定序列
                    waits.add(new CubeModel(i,j));
                }
            }
        }

        //取第一面墙作为初始起点


        Random random = new Random();
        while (waits.size()>0){
            int ranInt = random.nextInt(waits.size());
            CubeModel cubeModel = waits.get(ranInt);
            waits.remove(ranInt);

        }
    }

}
