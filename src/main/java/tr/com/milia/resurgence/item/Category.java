package tr.com.milia.resurgence.item;

/**
 * Itemlar kullanılabilen, kullanıldıktan sonra kaybolan,
 * koruma sağlayan, NPC'ler ile alış/verişi olan, oyuncular ile alışverişi olan
 * borsası olan, special özellikleri olan itemlar (örneğin kullanıldığında EXP veren)
 * aktif/pasif edilebilen, silinebilen
 */
public interface Category {
	// todo https://milia.atlassian.net/browse/RSRGNC-20
	//  javadoc'da bulunan kategoriler tanımla
	//  kategoryler interface ile birleştirilebilir. örneğin Sellable interface'i implement eder, category'sine göre
	//  satılıp satılmadığı döner
}
