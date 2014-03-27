package com.tanzeer.editimage.utils;

public class CommonConstants {

	public static String APP_PATH = "/sdcard/editimage/";
	public static String CACHE_PATH = APP_PATH + ".cache/";
	public static String TMPFILE_PATH = CACHE_PATH + "tmp.jpg";
	
	
    public static String LOGCAT_PATH = "/sdcard/mustache_logcat/";
    
	public static String PICTURE_FILE_EXT = ".jpg";
	
	public static boolean ENABLE_LOGGING = false;//true;
	
	/**
	 * Request code for returning image capture data from camera intent.
	 */
	public static final int REQ_TAKE_PICTURE = 1;
	public static final int REQ_OPEN_PICTURE = 2;
	public static final int REQ_ADD_MUSTACHE = 3;
	public static final int REQ_EDIT_MUSTACHE = 4;
	public static final int REQ_PICK_FRIENDS = 5;
	public static final int REQ_BILLING = 6;
	
	//Face detection
	public static final int MAX_FACES = 5;
	
	
	
	public static final String KEY_MUSTACHE_PATH = "mustachePath";
	public static final String KEY_MUSTACHE_BASE_NAME = "mustacheBaseName";
	public static final String KEY_FB_PROFILE_PIC_URL = "facebookProfilePic";
	public static final String KEY_PICTURE_SOURCE = "PICTURE_SOURCE";
	
	
	public static final String MUSTACHE_FOLDER = "/mustache";
	public static final String MUSTACHE_FILE_EXT = ".png";
	
	public static final String DEFAULT_PACK = "Free mustaches";
	
	public static final int LOAD_PICTURE_FROM_GALLERY = 1;
	public static final int TAKE_PICTURE_FROM_CAMERA = 2;
	public static final int GET_PICTURE_FROM_FACEBOOK = 3;
	
	public static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkQfRCys0zkvjJ24TsCz7NTkFVig6zzPC2nMA/V+M556IKVrsJUmYwjY7sYz0VcpF75fY0peKYK3aX/CTJFFRWktALT9RsDeSadMvLHs0v8kw02Kd1cLmClqcjyWHtZR5Yfjja1TrHuoaqLwUZ/FC/gBdsJ6I7uQwgLmIohcTJyrgQgxrBqQ66tWcwh5q17H2WtveSePayqzVOxvEqBqJnu6yZid04cOuXRx/W89UWkUgNCRROBuMJ5iljQrLVbKfuOUNPWrgJwnvivw/Vc6lLOZXTVzraAlgkySNv3ayv+bzh48DeuARJ83vhvS+IZ2mqqLXkaoPdQGPgQtfZgZo3wIDAQAB";
	
	public static final String  BILLING_TEST_PRODUCT_ID = "android.test.canceled";
	public static final String  COSTUME_PACK_PRODUCT_ID = "com.brightnewt.mustachebash.costume_pack";
	public static final String  TENNESSEE_PACK_PRODUCT_ID = "com.brightnewt.mustachebash.tennessee_pack";
	public static final String  CELEBRITY_PACK_PRODUCT_ID = "com.brightnewt.mustachebash.celebrity_pack";
	public static final String  COMIC_BOOK_PACK_PRODUCT_ID = "com.brightnewt.mustachebash.comic_book_pack";
	public static final String  DA_VINCI_PACK_PRODUCT_ID = "com.brightnewt.mustachebash.da_vinci_pack";
	public static final String  SOLID_GOLD_PACK_PRODUCT_ID = "com.brightnewt.mustachebash.solid_gold_pack";
	public static final String  REMOVE_BANNER_AD_PRODUCT_ID = "com.brightnewt.mustachebash.remove_banner_ad";
	public static final String  UNLOCK_ALL_PACKS_PRODUCT_ID = "com.brightnewt.mustachebash.unlock_all_packs";
	public static final String[] ALL_PACKS = new String[]{COSTUME_PACK_PRODUCT_ID,TENNESSEE_PACK_PRODUCT_ID,CELEBRITY_PACK_PRODUCT_ID,COMIC_BOOK_PACK_PRODUCT_ID,DA_VINCI_PACK_PRODUCT_ID,SOLID_GOLD_PACK_PRODUCT_ID};
}
