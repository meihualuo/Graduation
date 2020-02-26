package com.example.mazigame.model;

public class MapModel{
    public int[][] map;
    public int wide;
    public int high;
    public final int ROAD = 0;
    public final int WALL = 1;
    public final int COLUMN = 2;

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
                    this.map[i][j] = ROAD;
                }else if (i%2 == 1 && j%2 == 1){
                    this.map[i][j] = COLUMN;
                }else {
                    this.map[i][j] = WALL;
                }
            }
        }
    }

}
