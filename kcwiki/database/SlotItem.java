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
public class SlotItem {     
    
                //api_mst_slotitem          ：装備データ
private String          api_id=null;			//			：装備ID
private String		api_sortno=null;			//		：並べ替え順
private String		api_name=null;			//		：装備名
private String		api_type=null;			//		：装備タイプ
private String          api_taik=null;			//		：耐久(0)
private String		api_souk=null;			//		：装甲
private String		api_houg=null;			//		：火力
private String		api_raig=null;			//		：雷装
private String		api_soku=null;			//		：速力
private String		api_baku=null;			//		：爆装
private String		api_tyku=null;			//		：対空
private String		api_tais=null;			//		：対潜
private String		api_atap=null;			//		：(0)
private String		api_houm=null;			//		：命中
private String		api_raim=null;			//		：雷撃命中(0)
private String		api_houk=null;			//		：回避
private String		api_raik=null;			//		：雷撃回避(0)
private String		api_bakk=null;			//		：爆撃回避(0)
private String		api_saku=null;			//		：索敵
private String		api_sakb=null;			//		：索敵妨害(0)
private String		api_luck=null;			//		：運(0)
private String		api_leng=null;			//		：射程
private String		api_rare=null;			//		：レアリティ
private String		api_broken=null;			//		：廃棄資材
private String		api_info=null;			//		：図鑑情報
private String		api_usebull=null;			//		：

private String		api_cost=null;			//		：航空機コスト　非航空機は(ID - 1)の値と同じになる、前に航空機がなければ(2015/05/07現在 ID:1~15)存在しない
private String		api_distance=null;			//	：航空機の航続距離　上に同じ        
   
    /*                    
			[0]：大分類
				 1=砲
				 2=魚雷
				 3=艦載機
				 4=機銃・特殊弾(対空系)
				 5=偵察機・電探(索敵系)
				 6=強化
				 7=対潜装備
				 8=大発動艇・探照灯
				 9=簡易輸送部材
				10=艦艇修理施設
				11=照明弾
				12=司令部施設
				13=航空要員
				14=高射装置
				15=対地装備
				16=水上艦要員
				17=大型飛行艇
				18=戦闘糧食
				19=補給物資
				20=特型内火艇
				21=陸上攻撃機
				22=局地戦闘機

			[1]：図鑑表示
				 1=Primary Armament
				 2=Secondary Armament
				 3=Torpedo
				 4=Midget Submarine
				 5=Carrier-Based Aircraft
				 6=AA Gun
				 7=Reconnaissance
				 8=Radar
				 9=Upgrades
				10=Sonar
				14=Daihatsu
				15=Autogyro
				16=AntiSubmarine Patrol
				17=Extension Armor
				18=Searchlight
				19=Supply
				20=Machine Tools
				21=Flare
				22=Fleet Command
				23=Maintenance Team
				24=AA Director
				25=AP Shell
				26=Rocket Artillery
				27=Picket Crew
				28=AA Shell
				29=AA Rocket
				30=Damage Control
				31=Engine Upgrades
				32=Depth Charge
				33=Flying Boat
				34=Ration
				35=Supply
				36=Multi-purpose Seaplane
				37=Amphibious Vehicle
				38=Land Attacker
				39=Interceptor

				 1=主砲
				 2=副砲
				 3=魚雷
				 4=特殊潜航艇
				 5=艦上機
				 6=対空機銃
				 7=偵察機
				 8=電探
				 9=強化
				10=ソナー
				14=上陸用舟艇
				15=オートジャイロ
				16=対潜哨戒機
				17=追加装甲
				18=探照灯
				19=簡易輸送部材
				20=艦艇修理施設
				21=照明弾
				22=司令部施設
				23=航空要員
				24=高射装置
				25=対艦強化弾
				26=対地装備
				27=水上艦要員
				28=対空強化弾
				29=対空ロケットランチャー
				30=応急修理要員
				31=機関部強化
				32=爆雷
				33=大型飛行艇
				34=戦闘糧食
				35=補給物資
				36=多用途水上機/水上戦闘機
				37=特型内火艇
				38=陸上攻撃機
				39=局地戦闘機

			[2]：カテゴリ
				(api_mst_slotitem_equiptype を参照)
				 1=小口径主砲
				 2=中口径主砲
				 3=大口径主砲
				 4=副砲
				 5=魚雷
				 6=艦上戦闘機
				 7=艦上爆撃機
				 8=艦上攻撃機
				 9=艦上偵察機
				10=水上偵察機
				11=水上爆撃機
				12=小型電探
				13=大型電探
				14=ソナー
				15=爆雷
				16=追加装甲
				17=機関部強化
				18=対空強化弾
				19=対艦強化弾
				20=VT信管
				21=対空機銃
				22=特殊潜航艇
				23=応急修理要員
				24=上陸用舟艇
				25=オートジャイロ
				26=対潜哨戒機
				27=追加装甲(中型)
				28=追加装甲(大型)
				29=探照灯
				30=簡易輸送部材
				31=艦艇修理施設
				32=潜水艦魚雷
				33=照明弾
				34=司令部施設
				35=航空要員
				36=高射装置
				37=対地装備
				38=大口径主砲(II)
				39=水上艦要員
				40=大型ソナー
				41=大型飛行艇
				42=大型探照灯
				43=戦闘糧食
				44=補給物資
				45=水上戦闘機
				46=特型内火艇
				47=陸上攻撃機
				48=局地戦闘機
				93=大型電探(II)
				94=艦上偵察機(II)

			[3]：アイコンID
				 1=小口径主砲
				 2=中口径主砲
				 3=大口径主砲
				 4=副砲
				 5=魚雷
				 6=艦上戦闘機
				 7=艦上爆撃機
				 8=艦上攻撃機
				 9=艦上偵察機
				10=水上機
				11=電探
				12=対空強化弾
				13=対艦強化弾
				14=応急修理要員
				15=対空機銃
				16=高角砲
				17=爆雷
				18=ソナー
				19=機関部強化
				20=上陸用舟艇
				21=オートジャイロ
				22=対潜哨戒機
				23=追加装甲
				24=探照灯
				25=簡易輸送部材
				26=艦艇修理施設
				27=照明弾
				28=司令部施設
				29=航空要員
				30=高射装置
				31=対地装備
				32=水上艦要員
				33=大型飛行艇
				34=戦闘糧食
				35=補給物資
				36=特型内火艇
				37=陸上攻撃機
				38=局地戦闘機
    */ 


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
     * @return the api_sortno
     */
    public String getApi_sortno() {
        return api_sortno;
    }

    /**
     * @param api_sortno the api_sortno to set
     */
    public void setApi_sortno(String api_sortno) {
        this.api_sortno = api_sortno;
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
     * @return the api_taik
     */
    public String getApi_taik() {
        return api_taik;
    }

    /**
     * @param api_taik the api_taik to set
     */
    public void setApi_taik(String api_taik) {
        this.api_taik = api_taik;
    }

    /**
     * @return the api_souk
     */
    public String getApi_souk() {
        return api_souk;
    }

    /**
     * @param api_souk the api_souk to set
     */
    public void setApi_souk(String api_souk) {
        this.api_souk = api_souk;
    }

    /**
     * @return the api_houg
     */
    public String getApi_houg() {
        return api_houg;
    }

    /**
     * @param api_houg the api_houg to set
     */
    public void setApi_houg(String api_houg) {
        this.api_houg = api_houg;
    }

    /**
     * @return the api_raig
     */
    public String getApi_raig() {
        return api_raig;
    }

    /**
     * @param api_raig the api_raig to set
     */
    public void setApi_raig(String api_raig) {
        this.api_raig = api_raig;
    }

    /**
     * @return the api_soku
     */
    public String getApi_soku() {
        return api_soku;
    }

    /**
     * @param api_soku the api_soku to set
     */
    public void setApi_soku(String api_soku) {
        this.api_soku = api_soku;
    }

    /**
     * @return the api_baku
     */
    public String getApi_baku() {
        return api_baku;
    }

    /**
     * @param api_baku the api_baku to set
     */
    public void setApi_baku(String api_baku) {
        this.api_baku = api_baku;
    }

    /**
     * @return the api_tyku
     */
    public String getApi_tyku() {
        return api_tyku;
    }

    /**
     * @param api_tyku the api_tyku to set
     */
    public void setApi_tyku(String api_tyku) {
        this.api_tyku = api_tyku;
    }

    /**
     * @return the api_tais
     */
    public String getApi_tais() {
        return api_tais;
    }

    /**
     * @param api_tais the api_tais to set
     */
    public void setApi_tais(String api_tais) {
        this.api_tais = api_tais;
    }

    /**
     * @return the api_atap
     */
    public String getApi_atap() {
        return api_atap;
    }

    /**
     * @param api_atap the api_atap to set
     */
    public void setApi_atap(String api_atap) {
        this.api_atap = api_atap;
    }

    /**
     * @return the api_houm
     */
    public String getApi_houm() {
        return api_houm;
    }

    /**
     * @param api_houm the api_houm to set
     */
    public void setApi_houm(String api_houm) {
        this.api_houm = api_houm;
    }

    /**
     * @return the api_raim
     */
    public String getApi_raim() {
        return api_raim;
    }

    /**
     * @param api_raim the api_raim to set
     */
    public void setApi_raim(String api_raim) {
        this.api_raim = api_raim;
    }

    /**
     * @return the api_houk
     */
    public String getApi_houk() {
        return api_houk;
    }

    /**
     * @param api_houk the api_houk to set
     */
    public void setApi_houk(String api_houk) {
        this.api_houk = api_houk;
    }

    /**
     * @return the api_raik
     */
    public String getApi_raik() {
        return api_raik;
    }

    /**
     * @param api_raik the api_raik to set
     */
    public void setApi_raik(String api_raik) {
        this.api_raik = api_raik;
    }

    /**
     * @return the api_bakk
     */
    public String getApi_bakk() {
        return api_bakk;
    }

    /**
     * @param api_bakk the api_bakk to set
     */
    public void setApi_bakk(String api_bakk) {
        this.api_bakk = api_bakk;
    }

    /**
     * @return the api_saku
     */
    public String getApi_saku() {
        return api_saku;
    }

    /**
     * @param api_saku the api_saku to set
     */
    public void setApi_saku(String api_saku) {
        this.api_saku = api_saku;
    }

    /**
     * @return the api_sakb
     */
    public String getApi_sakb() {
        return api_sakb;
    }

    /**
     * @param api_sakb the api_sakb to set
     */
    public void setApi_sakb(String api_sakb) {
        this.api_sakb = api_sakb;
    }

    /**
     * @return the api_luck
     */
    public String getApi_luck() {
        return api_luck;
    }

    /**
     * @param api_luck the api_luck to set
     */
    public void setApi_luck(String api_luck) {
        this.api_luck = api_luck;
    }

    /**
     * @return the api_leng
     */
    public String getApi_leng() {
        return api_leng;
    }

    /**
     * @param api_leng the api_leng to set
     */
    public void setApi_leng(String api_leng) {
        this.api_leng = api_leng;
    }

    /**
     * @return the api_rare
     */
    public String getApi_rare() {
        return api_rare;
    }

    /**
     * @param api_rare the api_rare to set
     */
    public void setApi_rare(String api_rare) {
        this.api_rare = api_rare;
    }

    /**
     * @return the api_broken
     */
    public String getApi_broken() {
        return api_broken;
    }

    /**
     * @param api_broken the api_broken to set
     */
    public void setApi_broken(String api_broken) {
        this.api_broken = api_broken;
    }

    /**
     * @return the api_info
     */
    public String getApi_info() {
        return api_info;
    }

    /**
     * @param api_info the api_info to set
     */
    public void setApi_info(String api_info) {
        this.api_info = api_info;
    }

    /**
     * @return the api_usebull
     */
    public String getApi_usebull() {
        return api_usebull;
    }

    /**
     * @param api_usebull the api_usebull to set
     */
    public void setApi_usebull(String api_usebull) {
        this.api_usebull = api_usebull;
    }

    /**
     * @return the api_cost
     */
    public String getApi_cost() {
        return api_cost;
    }

    /**
     * @param api_cost the api_cost to set
     */
    public void setApi_cost(String api_cost) {
        this.api_cost = api_cost;
    }

    /**
     * @return the api_distance
     */
    public String getApi_distance() {
        return api_distance;
    }

    /**
     * @param api_distance the api_distance to set
     */
    public void setApi_distance(String api_distance) {
        this.api_distance = api_distance;
    }
                        

                                
}
