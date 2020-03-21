package com.modernfactions.util;

import java.util.HashMap;

public class LanguageUtil {

    private static HashMap<String, String> languages = new HashMap<>();
    static {
        languages.put("af_za","Afrikaans");
        languages.put("ar_sa","Arabic");
        languages.put("ast_es","Asturian");
        languages.put("az_az","Azerbaijani");
        languages.put("ba_ru","Bashkir");
        languages.put("bar_de","Bavarian");
        languages.put("be_by","Belarusian");
        languages.put("bg_bg","Bulgarian");
        languages.put("br_fr","Breton");
        languages.put("brb","Brabantian");
        languages.put("bs_BA","Bosnian");
        languages.put("ca_es","Catalan");
        languages.put("cs_cz","Czech");
        languages.put("cy_gb","Welsh");
        languages.put("da_dk","Danish");
        languages.put("de_at","Austrian German");
        languages.put("de_ch","Swiss German");
        languages.put("de_de","German");
        languages.put("el_gr","Greek");
        languages.put("en_au","Australian English");
        languages.put("en_ca","Canadian English");
        languages.put("en_gb","British English");
        languages.put("en_nz","New Zealand English");
        languages.put("en_7s","Pirate English");
        languages.put("en_ud","British English (upside down)");
        languages.put("en_us","American English");
        languages.put("enp","English puristic");
        languages.put("en_ws","Early Modern English (Wikipedia)");
        languages.put("eo_uy","Esperanto");
        languages.put("es_ar","Argentinian Spanish");
        languages.put("es_cl","Chilean Spanish");
        languages.put("es_es","Spanish");
        languages.put("es_mx","Mexican Spanish");
        languages.put("es_uy","Uruguayan Spanish");
        languages.put("es_ve","Venezuelan Spanish");
        languages.put("and","Andalusian");
        languages.put("et_ee","Estonian");
        languages.put("eu_es","Basque");
        languages.put("fa_ir","Persian");
        languages.put("fi_fi","Finnish");
        languages.put("fil_ph","Filipino");
        languages.put("fo_fo","Faroese");
        languages.put("fr_ca","Canadian French");
        languages.put("fr_fr","French");
        languages.put("vmf_de","East Franconian");
        languages.put("fy_nl","Frisian");
        languages.put("ga_ie","Irish");
        languages.put("gd_gb","Scottish Gaelic");
        languages.put("gl_es","Galician");
        languages.put("got","Gothic");
        languages.put("gv_im","Manx");
        languages.put("haw","Hawaiian");
        languages.put("he_il","Hebrew");
        languages.put("hi_in","Hindi");
        languages.put("hr_hr","Croatian");
        languages.put("hu_hu","Hungarian");
        languages.put("hy_am","Armenian");
        languages.put("id_id","Indonesian");
        languages.put("ig_ng","Igbo");
        languages.put("io_en","Ido");
        languages.put("is_is","Icelandic");
        languages.put("isv","Interslavic");
        languages.put("it_it","Italian");
        languages.put("ja_jp","Japanese");
        languages.put("jbo","Lojban");
        languages.put("ka_ge","Georgian");
        languages.put("kab_dz","Kabyle");
        languages.put("kk_kz","Kazakh");
        languages.put("kn_in","Kannada");
        languages.put("ko_kr","Korean");
        languages.put("ksh_de","Kölsch/Ripuarian");
        languages.put("kw_gb","Cornish");
        languages.put("la_va","Latin");
        languages.put("lb_lu","Luxembourgish");
        languages.put("li_li","Limburgish");
        languages.put("lol_aa","LOLCAT");
        languages.put("lt_lt","Lithuanian");
        languages.put("lv_lv","Latvian");
        languages.put("mi_nz","Māori");
        languages.put("mk_mk","Macedonian");
        languages.put("mn_mn","Mongolian");
        languages.put("moh_us","Mohawk");
        languages.put("ms_my","Malay");
        languages.put("mt_mt","Maltese");
        languages.put("nds_de","Low German");
        languages.put("nl_be","Dutch, Flemish");
        languages.put("nl_nl","Dutch");
        languages.put("nn_no","Norwegian Nynorsk");
        languages.put("no_no","Norwegian Bokmål");
        languages.put("nb_no","Norwegian Bokmål");
        languages.put("nuk","Nuu-chah-nulth");
        languages.put("oc_fr","Occitan");
        languages.put("oj_ca","Ojibwe");
        languages.put("ovd_se","Elfdalian");
        languages.put("pl_pl","Polish");
        languages.put("pt_br","Brazilian Portuguese");
        languages.put("pt_pt","Portuguese");
        languages.put("qya_aa","Quenya (Form of Elvish from LOTR)");
        languages.put("ro_ro","Romanian");
        languages.put("ru_ru","Russian");
        languages.put("scn","Sicilian");
        languages.put("sme","Northern Sami");
        languages.put("sk_sk","Slovak");
        languages.put("sl_si","Slovenian");
        languages.put("so_so","Somali");
        languages.put("sq_al","Albanian");
        languages.put("sr_sp","Serbian");
        languages.put("sv_se","Swedish");
        languages.put("swg","Allgovian German");
        languages.put("sxu","Upper Saxon German");
        languages.put("szl","Silesian");
        languages.put("ta_IN","Tamil");
        languages.put("th_th","Thai");
        languages.put("tlh_aa","Klingon");
        languages.put("tr_tr","Turkish");
        languages.put("tt_ru","Tatar");
        languages.put("tzl_tzl","Talossan");
        languages.put("uk_ua","Ukrainian");
        languages.put("ca-val_es","Valencian");
        languages.put("vec_it","Venetian");
        languages.put("vi_vn","Vietnamese");
        languages.put("yi_de","Yiddish");
        languages.put("yo_ng","Yoruba");
        languages.put("zh_cn","Chinese Simplified (China)");
        languages.put("zh_tw","Chinese Traditional (Taiwan)");
    }

    public static String getLanguageName(String languageCode) {
        if (languageCode == null) {
            return "English";
        } else {
            return languages.get(languageCode);
        }
    }
}