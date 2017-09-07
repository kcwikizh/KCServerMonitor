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
public class MapInfo {      
    
                //api_mst_mapinfo           ：海域詳細データ(鎮守府正面海域, 東部オリョール海, etc.)
    private String              api_id=null;			//			：海域ID
    private String              api_maparea_id=null;			//	：海域カテゴリID
    private String              api_no=null;			//			：海域カテゴリ内番号
    private String		api_name=null;			//		：海域名
    private String		api_level=null;			//		：難易度
    private String		api_opetext=null;			//		：作戦名
    private String		api_infotext=null;			//	：作戦情報
    private String		api_item=null;			//		：獲得アイテム
    private String		api_max_maphp=null;			//	：海域HPゲージ最大値？夏イベ海域は全て300, 他はnull ここに載っているHPはダミー？
    private String		api_required_defeat_count=null;			//	：攻略に必要な撃破数 nullあり
    private String		api_sally_flag=null;			//	：出撃艦隊編成フラグ
                        /*
			(夏イベ時)通常海域, MI海域は[ 0, 1 ], AL海域, 本土防衛は[ 1, 0 ]
			(2014/09現在)すべて[ 1, 0 ]
			(秋イベ時)通常海域/E-2は[ 1, 0 ]、E-1/3は[ 0, 2 ], E-4は[ 0, 3 ]
			{ 通常艦隊出撃フラグ, 連合艦隊出撃フラグ } と予想(それぞれビットフラグ)
			'15 秋イベ: 通常艦隊出撃は [ 1, 0 ]、連合艦隊出撃は [ 0, n ] ( n : 1=機動部隊, 2=水上部隊, 4=輸送部隊 でビットOR )
                        */
                        
                //api_mst_mapbgm        
    private String              api_map_bgm;			//		：通常戦闘BGMID　[0]=昼戦, [1]=夜戦
    private String		api_boss_bgm;			//	：ボス戦闘BGMID

    
        public void additem(){
            
        }
    
    
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
     * @return the api_no
     */
    public String getApi_no() {
        return api_no;
    }

    /**
     * @param api_no the api_no to set
     */
    public void setApi_no(String api_no) {
        this.api_no = api_no;
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
     * @return the api_level
     */
    public String getApi_level() {
        return api_level;
    }

    /**
     * @param api_level the api_level to set
     */
    public void setApi_level(String api_level) {
        this.api_level = api_level;
    }

    /**
     * @return the api_opetext
     */
    public String getApi_opetext() {
        return api_opetext;
    }

    /**
     * @param api_opetext the api_opetext to set
     */
    public void setApi_opetext(String api_opetext) {
        this.api_opetext = api_opetext;
    }

    /**
     * @return the api_infotext
     */
    public String getApi_infotext() {
        return api_infotext;
    }

    /**
     * @param api_infotext the api_infotext to set
     */
    public void setApi_infotext(String api_infotext) {
        this.api_infotext = api_infotext;
    }

    /**
     * @return the api_item
     */
    public String getApi_item() {
        return api_item;
    }

    /**
     * @param api_item the api_item to set
     */
    public void setApi_item(String api_item) {
        this.api_item = api_item;
    }

    /**
     * @return the api_max_maphp
     */
    public String getApi_max_maphp() {
        return api_max_maphp;
    }

    /**
     * @param api_max_maphp the api_max_maphp to set
     */
    public void setApi_max_maphp(String api_max_maphp) {
        this.api_max_maphp = api_max_maphp;
    }

    /**
     * @return the api_required_defeat_count
     */
    public String getApi_required_defeat_count() {
        return api_required_defeat_count;
    }

    /**
     * @param api_required_defeat_count the api_required_defeat_count to set
     */
    public void setApi_required_defeat_count(String api_required_defeat_count) {
        this.api_required_defeat_count = api_required_defeat_count;
    }

    /**
     * @return the api_sally_flag
     */
    public String getApi_sally_flag() {
        return api_sally_flag;
    }

    /**
     * @param api_sally_flag the api_sally_flag to set
     */
    public void setApi_sally_flag(String api_sally_flag) {
        this.api_sally_flag = api_sally_flag;
    }

    /**
     * @return the api_map_bgm
     */
    public String getApi_map_bgm() {
        return api_map_bgm;
    }

    /**
     * @param api_map_bgm the api_map_bgm to set
     */
    public void setApi_map_bgm(String api_map_bgm) {
        this.api_map_bgm = api_map_bgm;
    }

    /**
     * @return the api_boss_bgm
     */
    public String getApi_boss_bgm() {
        return api_boss_bgm;
    }

    /**
     * @param api_boss_bgm the api_boss_bgm to set
     */
    public void setApi_boss_bgm(String api_boss_bgm) {
        this.api_boss_bgm = api_boss_bgm;
    }
         
    
    
}
