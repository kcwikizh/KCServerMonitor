/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.database;

/**
 *
 * @author VEP
 */
public class Mission {
                            //遠征データ
    private String		api_id=null;			//		：遠征ID
    private String		api_maparea_id=null;			//		：海域カテゴリID
    private String		api_name=null;			//		：遠征名
    private String		api_details=null;			//		：遠征詳細
    private String		api_time=null;			//		：時間　分単位
    private String		api_difficulty=null;			//		：難易度
    private String		api_use_fuel=null;			//		：燃料消費割合
    private String		api_use_bull=null;			//		：弾薬消費割合
    private String		api_win_item1=null;			//		：獲得アイテム1　[0]=アイテムID, [1]=入手個数
    private String		api_win_item2=null;			//		：獲得アイテム2
    private String		api_return_flag=null;			//		：遠征中止可否

    /**
     * @return the api_id
     */
    public String getApi_id() {
        return api_id;
    }

    /**
     * @param api_id the api_id to set
     */
    public void setApi_id(String api_id) {
        this.api_id = api_id;
    }

    /**
     * @return the api_maparea_id
     */
    public String getApi_maparea_id() {
        return api_maparea_id;
    }

    /**
     * @param api_maparea_id the api_maparea_id to set
     */
    public void setApi_maparea_id(String api_maparea_id) {
        this.api_maparea_id = api_maparea_id;
    }

    /**
     * @return the api_name
     */
    public String getApi_name() {
        return api_name;
    }

    /**
     * @param api_name the api_name to set
     */
    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    /**
     * @return the api_details
     */
    public String getApi_details() {
        return api_details;
    }

    /**
     * @param api_details the api_details to set
     */
    public void setApi_details(String api_details) {
        this.api_details = api_details;
    }

    /**
     * @return the api_time
     */
    public String getApi_time() {
        return api_time;
    }

    /**
     * @param api_time the api_time to set
     */
    public void setApi_time(String api_time) {
        this.api_time = api_time;
    }

    /**
     * @return the api_difficulty
     */
    public String getApi_difficulty() {
        return api_difficulty;
    }

    /**
     * @param api_difficulty the api_difficulty to set
     */
    public void setApi_difficulty(String api_difficulty) {
        this.api_difficulty = api_difficulty;
    }

    /**
     * @return the api_use_fuel
     */
    public String getApi_use_fuel() {
        return api_use_fuel;
    }

    /**
     * @param api_use_fuel the api_use_fuel to set
     */
    public void setApi_use_fuel(String api_use_fuel) {
        this.api_use_fuel = api_use_fuel;
    }

    /**
     * @return the api_use_bull
     */
    public String getApi_use_bull() {
        return api_use_bull;
    }

    /**
     * @param api_use_bull the api_use_bull to set
     */
    public void setApi_use_bull(String api_use_bull) {
        this.api_use_bull = api_use_bull;
    }

    /**
     * @return the api_win_item1
     */
    public String getApi_win_item1() {
        return api_win_item1;
    }

    /**
     * @param api_win_item1 the api_win_item1 to set
     */
    public void setApi_win_item1(String api_win_item1) {
        this.api_win_item1 = api_win_item1;
    }

    /**
     * @return the api_win_item2
     */
    public String getApi_win_item2() {
        return api_win_item2;
    }

    /**
     * @param api_win_item2 the api_win_item2 to set
     */
    public void setApi_win_item2(String api_win_item2) {
        this.api_win_item2 = api_win_item2;
    }

    /**
     * @return the api_return_flag
     */
    public String getApi_return_flag() {
        return api_return_flag;
    }

    /**
     * @param api_return_flag the api_return_flag to set
     */
    public void setApi_return_flag(String api_return_flag) {
        this.api_return_flag = api_return_flag;
    }
    
    
    
}
