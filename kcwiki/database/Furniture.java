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
public class Furniture {        
    
                //api_mst_furniture         ：家具データ
        private String  api_id=null;			//：家具ID
	private String	api_type=null;			//		：カテゴリ 0=床, 1=壁紙, 2=窓, 3=壁掛け, 4=家具, 5=机
	private String	api_no=null;			//			：カテゴリ内通し番号 0から始まる
	private String	api_title=null;			//		：家具名
	private String	api_description=null;			//	：説明
	private String	api_rarity=null;			//		：レアリティ
	private String	api_price=null;			//		：価格
	private String	api_saleflg=null;			//		：販売中?
	private String	api_season=null;			//		：
                
                //api_mst_furnituregraph
    	private String	api_filename=null;			//	：ファイル名
	private String	api_version=null;			//		：バージョン

        
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
     * @return the api_type
     */
    public String getApi_type() {
        return api_type;
    }

    /**
     * @param api_type the api_type to set
     */
    public void setApi_type(String api_type) {
        this.api_type = api_type;
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
     * @return the api_title
     */
    public String getApi_title() {
        return api_title;
    }

    /**
     * @param api_title the api_title to set
     */
    public void setApi_title(String api_title) {
        this.api_title = api_title;
    }

    /**
     * @return the api_description
     */
    public String getApi_description() {
        return api_description;
    }

    /**
     * @param api_description the api_description to set
     */
    public void setApi_description(String api_description) {
        this.api_description = api_description;
    }

    /**
     * @return the api_rarity
     */
    public String getApi_rarity() {
        return api_rarity;
    }

    /**
     * @param api_rarity the api_rarity to set
     */
    public void setApi_rarity(String api_rarity) {
        this.api_rarity = api_rarity;
    }

    /**
     * @return the api_price
     */
    public String getApi_price() {
        return api_price;
    }

    /**
     * @param api_price the api_price to set
     */
    public void setApi_price(String api_price) {
        this.api_price = api_price;
    }

    /**
     * @return the api_saleflg
     */
    public String getApi_saleflg() {
        return api_saleflg;
    }

    /**
     * @param api_saleflg the api_saleflg to set
     */
    public void setApi_saleflg(String api_saleflg) {
        this.api_saleflg = api_saleflg;
    }

    /**
     * @return the api_season
     */
    public String getApi_season() {
        return api_season;
    }

    /**
     * @param api_season the api_season to set
     */
    public void setApi_season(String api_season) {
        this.api_season = api_season;
    }

    /**
     * @return the api_filename
     */
    public String getApi_filename() {
        return api_filename;
    }

    /**
     * @param api_filename the api_filename to set
     */
    public void setApi_filename(String api_filename) {
        this.api_filename = api_filename;
    }

    /**
     * @return the api_version
     */
    public String getApi_version() {
        return api_version;
    }

    /**
     * @param api_version the api_version to set
     */
    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }
                        
}
