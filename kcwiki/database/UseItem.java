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
public class UseItem {      
    
                //api_mst_useitem           ：アイテムデータ
private String          api_id=null;			//			：アイテムID
private String		api_usetype=null;			//		：使用形態　0=使用不可, 1=高速修復材, 2=高速建造材, 3=開発資材, 4=使用可能, etc.
private String		api_category=null;			//	：カテゴリ
private String		api_name=null;			//		：アイテム名
private String		api_description=null;			//	：[0]=アイテムの説明, [1]=(家具箱のみ)入手できる家具コインの枚数
private String		api_price=null;			//		：


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
     * @return the api_usetype
     */
    public String getApi_usetype() {
        return api_usetype;
    }

    /**
     * @param api_usetype the api_usetype to set
     */
    public void setApi_usetype(String api_usetype) {
        this.api_usetype = api_usetype;
    }

    /**
     * @return the api_category
     */
    public String getApi_category() {
        return api_category;
    }

    /**
     * @param api_category the api_category to set
     */
    public void setApi_category(String api_category) {
        this.api_category = api_category;
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
