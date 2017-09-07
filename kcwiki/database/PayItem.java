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
public class PayItem {      
    
                //api_mst_payitem           ：アイテム屋さんデータ
private String          api_id=null;			//			：ID
private String		api_type=null;			//		：
private String		api_name=null;			//		：商品名
private String		api_description=null;			//	：商品説明
private String		api_item=null;			//		：入手アイテム量　[0]=燃料, [1]=弾薬, [2]=鋼材, [3]=ボーキサイト, [4]=高速建造材, [5]=高速修復材, [6]=開発資材, [7]=ドック解放キー
private String		api_price=null;			//		：価格


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
                        
}
