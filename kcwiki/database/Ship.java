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
public class Ship {     
                
                //api_mst_ship          ：艦船データ(艦娘)　衣替え艦娘のデータはshipgraphにしか存在しない
private String          api_id=null;			//			：艦娘固有ID
private String		api_sortno=null;			//		：図鑑番号
private String		api_name=null;			//		：艦娘名
private String		api_yomi=null;			//		：艦娘名読み
private String		api_stype=null;			//		：艦種
private String		api_afterlv=null;			//		：改装Lv
private String		api_aftershipid=null;			//	：改装後ID 文字列 "0"=なし
private String		api_taik=null;			//		：耐久 [0]=初期値, [1]=最大値
private String		api_souk=null;			//		：装甲
private String		api_houg=null;			//		：火力
private String		api_raig=null;			//		：雷装
private String		api_tyku=null;			//		：対空
private String		api_luck=null;			//		：運
private String		api_soku=null;			//		：速力　0=陸上基地, 5=低速, 10=高速
private String		api_leng=null;			//		：射程 0=無, 1=短, 2=中, 3=長, 4=超長
private String		api_slot_num=null;			//	：スロット数
private String		api_maxeq=null;			//		：艦載機搭載数
private String		api_buildtime=null;			//	：建造時間 分単位
private String		api_broken=null;			//		：解体資材
private String		api_powup=null;			//		：近代化改修強化値
private String		api_backs=null;			//		：レアリティ
private String		api_getmes=null;			//		：取得時台詞
private String		api_afterfuel=null;			//	：改装鋼材
private String		api_afterbull=null;			//	：改装弾薬
private String		api_fuel_max=null;			//	：消費燃料
private String		api_bull_max=null;			//	：消費弾薬
private String		api_voicef=null;			//		：ボイス設定フラグ ビットフラグ; 1=放置ボイス, 2=時報, 4=特殊放置ボイス
            						//放置ボイスは5分おきに発声する(cond>=50でかつ特殊放置ボイス利用可能ならそれを発声)
                                                                
                //api_mst_shipgraph	：艦船画像設定                                           
private String          api_filename=null;			//	：ファイル名
private String		api_version=null;			//		：ファイルのバージョン [グラフィック, ボイス, 母港ボイス]
private String		api_boko_n=null;			//		：母港での表示座標(無傷)
private String		api_boko_d=null;			//		：〃(中破)
private String		api_kaisyu_n=null;			//	：近代化改修
private String		api_kaisyu_d=null;			//	：
private String		api_kaizo_n=null;			//		：改装
private String		api_kaizo_d=null;			//		：
private String		api_map_n=null;			//		：出撃中
private String		api_map_d=null;			//		：
private String		api_ensyuf_n=null;			//	：演習(自軍側)
private String		api_ensyuf_d=null;			//	：
private String		api_ensyue_n=null;			//	：演習(敵軍側)
private String		api_battle_n=null;			//	：戦闘
private String		api_battle_d=null;			//	：
private String		api_weda=null;			//		：ケッコンカッコカリの顔枠の左上?
private String		api_wedb=null;			//		：〃右下?        
                                                                    
                //api_mst_shipupgrade	：特殊改装設定                                                
private String          api_current_ship_id=null;			//		：改装前の艦船ID　0=なし
private String		api_original_ship_id=null;			//	：初期改装の艦船ID
private String		api_upgrade_type=null;			//		：1
private String		api_upgrade_level=null;			//		：改装回数
private String		api_drawing_count=null;			//		：要改装設計図
private String		api_catapult_count=null;			//		：要試製甲板カタパルト                                        
    /*
                api_mst_stype		：艦船カテゴリ
		api_id			：カテゴリID(艦船のstypeに対応)
		api_sortno		：並べ替え順
		api_name		：艦種名(英語は図鑑内での表示)
			 1=海防艦		Escort Ship
			 2=駆逐艦		Destroyer
			 3=軽巡洋艦		Light Cruiser
			 4=重雷装巡洋艦	Torpedo Cruiser
			 5=重巡洋艦		Heavy Cruiser
			 6=航空巡洋艦	Aircraft Cruiser
			 7=軽空母		Light Aircraft Carrier
			 8=巡洋戦艦		Battleship		(実データ中では"戦艦"だが混同を防ぐため)
			 9=戦艦			Battleship
			10=航空戦艦		Aviation Battleship
			11=正規空母		Aircraft Carrier
			12=超弩級戦艦	Super Dreadnoughts
			13=潜水艦		Submarine
			14=潜水空母		Aircraft Carrying Submarine
			15=補給艦						(敵のほう)
			16=水上機母艦	Seaplane Carrier
			17=揚陸艦		Amphibious Assault Ship
			18=装甲空母		Aircraft Carrier
			19=工作艦		Repair Ship
			20=潜水母艦		Submarine Tender
			21=練習巡洋艦	Training Cruiser
			22=補給艦		Fleet Oiler		(味方のほう)
		api_scnt		：入渠時間係数(本体ソース内では[slotCount]とある)
		api_kcnt		：建造時のシルエット
		api_equip_type	：装備可能カテゴリのフラグ
			＊データと直接的関係はないが、このオブジェクトの子は"1"といった数字から始まる名前を持っている。
			　この名前は規約違反であるため、正規のjsonパーサでは正常に読み込むことができないので注意すること。                                                        
                                                        
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
     * @return the api_yomi
     */
    public String getApi_yomi() {
        return api_yomi;
    }

    /**
     * @param api_yomi the api_yomi to set
     */
    public void setApi_yomi(String api_yomi) {
        this.api_yomi = api_yomi;
    }

    /**
     * @return the api_stype
     */
    public String getApi_stype() {
        return api_stype;
    }

    /**
     * @param api_stype the api_stype to set
     */
    public void setApi_stype(String api_stype) {
        this.api_stype = api_stype;
    }

    /**
     * @return the api_afterlv
     */
    public String getApi_afterlv() {
        return api_afterlv;
    }

    /**
     * @param api_afterlv the api_afterlv to set
     */
    public void setApi_afterlv(String api_afterlv) {
        this.api_afterlv = api_afterlv;
    }

    /**
     * @return the api_aftershipid
     */
    public String getApi_aftershipid() {
        return api_aftershipid;
    }

    /**
     * @param api_aftershipid the api_aftershipid to set
     */
    public void setApi_aftershipid(String api_aftershipid) {
        this.api_aftershipid = api_aftershipid;
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
     * @return the api_slot_num
     */
    public String getApi_slot_num() {
        return api_slot_num;
    }

    /**
     * @param api_slot_num the api_slot_num to set
     */
    public void setApi_slot_num(String api_slot_num) {
        this.api_slot_num = api_slot_num;
    }

    /**
     * @return the api_maxeq
     */
    public String getApi_maxeq() {
        return api_maxeq;
    }

    /**
     * @param api_maxeq the api_maxeq to set
     */
    public void setApi_maxeq(String api_maxeq) {
        this.api_maxeq = api_maxeq;
    }

    /**
     * @return the api_buildtime
     */
    public String getApi_buildtime() {
        return api_buildtime;
    }

    /**
     * @param api_buildtime the api_buildtime to set
     */
    public void setApi_buildtime(String api_buildtime) {
        this.api_buildtime = api_buildtime;
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
     * @return the api_powup
     */
    public String getApi_powup() {
        return api_powup;
    }

    /**
     * @param api_powup the api_powup to set
     */
    public void setApi_powup(String api_powup) {
        this.api_powup = api_powup;
    }

    /**
     * @return the api_backs
     */
    public String getApi_backs() {
        return api_backs;
    }

    /**
     * @param api_backs the api_backs to set
     */
    public void setApi_backs(String api_backs) {
        this.api_backs = api_backs;
    }

    /**
     * @return the api_getmes
     */
    public String getApi_getmes() {
        return api_getmes;
    }

    /**
     * @param api_getmes the api_getmes to set
     */
    public void setApi_getmes(String api_getmes) {
        this.api_getmes = api_getmes;
    }

    /**
     * @return the api_afterfuel
     */
    public String getApi_afterfuel() {
        return api_afterfuel;
    }

    /**
     * @param api_afterfuel the api_afterfuel to set
     */
    public void setApi_afterfuel(String api_afterfuel) {
        this.api_afterfuel = api_afterfuel;
    }

    /**
     * @return the api_afterbull
     */
    public String getApi_afterbull() {
        return api_afterbull;
    }

    /**
     * @param api_afterbull the api_afterbull to set
     */
    public void setApi_afterbull(String api_afterbull) {
        this.api_afterbull = api_afterbull;
    }

    /**
     * @return the api_fuel_max
     */
    public String getApi_fuel_max() {
        return api_fuel_max;
    }

    /**
     * @param api_fuel_max the api_fuel_max to set
     */
    public void setApi_fuel_max(String api_fuel_max) {
        this.api_fuel_max = api_fuel_max;
    }

    /**
     * @return the api_bull_max
     */
    public String getApi_bull_max() {
        return api_bull_max;
    }

    /**
     * @param api_bull_max the api_bull_max to set
     */
    public void setApi_bull_max(String api_bull_max) {
        this.api_bull_max = api_bull_max;
    }

    /**
     * @return the api_voicef
     */
    public String getApi_voicef() {
        return api_voicef;
    }

    /**
     * @param api_voicef the api_voicef to set
     */
    public void setApi_voicef(String api_voicef) {
        this.api_voicef = api_voicef;
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

    /**
     * @return the api_boko_n
     */
    public String getApi_boko_n() {
        return api_boko_n;
    }

    /**
     * @param api_boko_n the api_boko_n to set
     */
    public void setApi_boko_n(String api_boko_n) {
        this.api_boko_n = api_boko_n;
    }

    /**
     * @return the api_boko_d
     */
    public String getApi_boko_d() {
        return api_boko_d;
    }

    /**
     * @param api_boko_d the api_boko_d to set
     */
    public void setApi_boko_d(String api_boko_d) {
        this.api_boko_d = api_boko_d;
    }

    /**
     * @return the api_kaisyu_n
     */
    public String getApi_kaisyu_n() {
        return api_kaisyu_n;
    }

    /**
     * @param api_kaisyu_n the api_kaisyu_n to set
     */
    public void setApi_kaisyu_n(String api_kaisyu_n) {
        this.api_kaisyu_n = api_kaisyu_n;
    }

    /**
     * @return the api_kaisyu_d
     */
    public String getApi_kaisyu_d() {
        return api_kaisyu_d;
    }

    /**
     * @param api_kaisyu_d the api_kaisyu_d to set
     */
    public void setApi_kaisyu_d(String api_kaisyu_d) {
        this.api_kaisyu_d = api_kaisyu_d;
    }

    /**
     * @return the api_kaizo_n
     */
    public String getApi_kaizo_n() {
        return api_kaizo_n;
    }

    /**
     * @param api_kaizo_n the api_kaizo_n to set
     */
    public void setApi_kaizo_n(String api_kaizo_n) {
        this.api_kaizo_n = api_kaizo_n;
    }

    /**
     * @return the api_kaizo_d
     */
    public String getApi_kaizo_d() {
        return api_kaizo_d;
    }

    /**
     * @param api_kaizo_d the api_kaizo_d to set
     */
    public void setApi_kaizo_d(String api_kaizo_d) {
        this.api_kaizo_d = api_kaizo_d;
    }

    /**
     * @return the api_map_n
     */
    public String getApi_map_n() {
        return api_map_n;
    }

    /**
     * @param api_map_n the api_map_n to set
     */
    public void setApi_map_n(String api_map_n) {
        this.api_map_n = api_map_n;
    }

    /**
     * @return the api_map_d
     */
    public String getApi_map_d() {
        return api_map_d;
    }

    /**
     * @param api_map_d the api_map_d to set
     */
    public void setApi_map_d(String api_map_d) {
        this.api_map_d = api_map_d;
    }

    /**
     * @return the api_ensyuf_n
     */
    public String getApi_ensyuf_n() {
        return api_ensyuf_n;
    }

    /**
     * @param api_ensyuf_n the api_ensyuf_n to set
     */
    public void setApi_ensyuf_n(String api_ensyuf_n) {
        this.api_ensyuf_n = api_ensyuf_n;
    }

    /**
     * @return the api_ensyuf_d
     */
    public String getApi_ensyuf_d() {
        return api_ensyuf_d;
    }

    /**
     * @param api_ensyuf_d the api_ensyuf_d to set
     */
    public void setApi_ensyuf_d(String api_ensyuf_d) {
        this.api_ensyuf_d = api_ensyuf_d;
    }

    /**
     * @return the api_ensyue_n
     */
    public String getApi_ensyue_n() {
        return api_ensyue_n;
    }

    /**
     * @param api_ensyue_n the api_ensyue_n to set
     */
    public void setApi_ensyue_n(String api_ensyue_n) {
        this.api_ensyue_n = api_ensyue_n;
    }

    /**
     * @return the api_battle_n
     */
    public String getApi_battle_n() {
        return api_battle_n;
    }

    /**
     * @param api_battle_n the api_battle_n to set
     */
    public void setApi_battle_n(String api_battle_n) {
        this.api_battle_n = api_battle_n;
    }

    /**
     * @return the api_battle_d
     */
    public String getApi_battle_d() {
        return api_battle_d;
    }

    /**
     * @param api_battle_d the api_battle_d to set
     */
    public void setApi_battle_d(String api_battle_d) {
        this.api_battle_d = api_battle_d;
    }

    /**
     * @return the api_weda
     */
    public String getApi_weda() {
        return api_weda;
    }

    /**
     * @param api_weda the api_weda to set
     */
    public void setApi_weda(String api_weda) {
        this.api_weda = api_weda;
    }

    /**
     * @return the api_wedb
     */
    public String getApi_wedb() {
        return api_wedb;
    }

    /**
     * @param api_wedb the api_wedb to set
     */
    public void setApi_wedb(String api_wedb) {
        this.api_wedb = api_wedb;
    }

    /**
     * @return the api_current_ship_id
     */
    public String getApi_current_ship_id() {
        return api_current_ship_id;
    }

    /**
     * @param api_current_ship_id the api_current_ship_id to set
     */
    public void setApi_current_ship_id(String api_current_ship_id) {
        this.api_current_ship_id = api_current_ship_id;
    }

    /**
     * @return the api_original_ship_id
     */
    public String getApi_original_ship_id() {
        return api_original_ship_id;
    }

    /**
     * @param api_original_ship_id the api_original_ship_id to set
     */
    public void setApi_original_ship_id(String api_original_ship_id) {
        this.api_original_ship_id = api_original_ship_id;
    }

    /**
     * @return the api_upgrade_type
     */
    public String getApi_upgrade_type() {
        return api_upgrade_type;
    }

    /**
     * @param api_upgrade_type the api_upgrade_type to set
     */
    public void setApi_upgrade_type(String api_upgrade_type) {
        this.api_upgrade_type = api_upgrade_type;
    }

    /**
     * @return the api_upgrade_level
     */
    public String getApi_upgrade_level() {
        return api_upgrade_level;
    }

    /**
     * @param api_upgrade_level the api_upgrade_level to set
     */
    public void setApi_upgrade_level(String api_upgrade_level) {
        this.api_upgrade_level = api_upgrade_level;
    }

    /**
     * @return the api_drawing_count
     */
    public String getApi_drawing_count() {
        return api_drawing_count;
    }

    /**
     * @param api_drawing_count the api_drawing_count to set
     */
    public void setApi_drawing_count(String api_drawing_count) {
        this.api_drawing_count = api_drawing_count;
    }

    /**
     * @return the api_catapult_count
     */
    public String getApi_catapult_count() {
        return api_catapult_count;
    }

    /**
     * @param api_catapult_count the api_catapult_count to set
     */
    public void setApi_catapult_count(String api_catapult_count) {
        this.api_catapult_count = api_catapult_count;
    }
}
