package com.shengxian.common.util;

/**
 * 全局变量
 * @author luxing
 */
public class Global {

    /**
     * 商家二维码保存路径
     */
    public static final  String path ="http://www.bxy8888.com/uploadFile/business/";

    /*系统异常时提示*/
    public static final String ERROR="哎呀，出了点小问题";

    /*密码加密密钥*/
    public static final String passwordKey="662381";

    /*********支付宝参数*********/
    public final static String APP_ID="2017102709548714";
    //商户应用私钥
    public final static String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGlv54L5CStDWtKCI1SUmo4h6LA/FzRK4tyYCjj0G3x7dXJkCTBXPT/Ar0mxmRm6AuDF+Ohqq0kkZ531/XcGGlJX9p7iNKpKvimX6iGJAFi/zzdiCONZTqSASAPWkJRYfCcba2CnyWWeaWbmrn1iCeqM6pAX7y7Y6F4SuKVR7AtsGcK4OXdWaX9VDSevcka+raPDq3tlFBBRulx4y+nPJD7SX4IIKb12iXLVANYwXjxExntC/9LgkJN3O/Zj4uZGsUOiwMKs1kvQq4vuxEbQx3QcrtdQNtxJEK4ipLx2OptskNzPeMaS1SxJ4FNLyBbD7iU0HtHqF0/hRHpVSsaTTPAgMBAAECggEAKylXGAHg8jsaj5o0RjRni+Jf+yLblqSq2wIo38r8P0yr7iXVRZDVEWFdvg3TSE/JMZtENbfYHKpgMYtB+BwRJF3/G2CELtbaTIP/CUqpPS4kg4sZa6GhTPSlhN6sxY1nuSKwfnakd68C9LEaVZjkwxzNkYNqS8uFDkgkp2sevpwhNBfYv90MjtxvKxVIett7bq8DUVEPXAAiuVrbng64kh4Fen3xk6qJtjEEDTy9naXPKPE9B+/UBJXlZ4pLMTwgO+uCwnkwbT+DM0Pzq0pWKkspbwGSrMssCsJNc3zte3bkJJ/ZTvrHWIMeNA7yhRX9LJfgQ4WBMbPvLRVRFZ0YoQKBgQD3vN2pPillCH1xiuqk3+V70f5AWkjP0PPAKireefvNbqnto0hN5aWGni9D9C3VREw48xqp4FrW6rVL+vggzjhKTkYnWY1arbwmyCLoppdn2TqakxJ90nHT5CxeM7qpgbFrv4ooNvLggR1VUSvd6dnvNcml/JsqJ+uOrdLBAtuHMQKBgQCLFBgjaWPThM9wMr3Ye/KIfNd6ZDIRcs1qz/GLHajYt+ZrvVMhwFDHw2OIfvq2bBkkgug2Ar9886uU7LcRVbSRzw4jMrs7HgDlqPkvOF60uX3Le7w8a874kbA0KuzQd2jfaarepZ8mX5apMinAB89Iwq5olMi9muSKoZmYBhl7/wKBgB9lzmoOuMxXQR7MzmRLBBinSqP5TVUZVGs8bOZ5a1Jj/iFiGl3VBx+CmNbx8YUf/NES7oQtLRYj9QpGKoBt4nB4bye5j9K4RipcHyCBTJsayvSgQy2zKD4EoyLlR3kIOOZBBk+Gd03Az2jquraLVtWcaYwh8cBsAsy4dweDRzNhAoGBAIGt9uPt/PrZN2LyrToSOI8zeOfSTS0f0pHp2ttQiIOq1dtX6Q/rdmcNtN0tKTC3kqoMteDGpy4nNrHiFsC/HWNnoVMIlrQEweliqRZyhTwWSUlW0qm0DISRs66w56dp8AuBR6Xa4I6zSpPxBiTcI1dAz8cMFDFXIApzq/eomFKZAoGAIzJBUzkxpPTG+G5AXpS7/aH6Z+zFpUSpl74uQMxeK/glmPvpkm3n3dA1mLce/O5gHCm2KzuQtQwqBdLzDp8IpUk+1WTvBOtG7kp9xe9Z4gHyiOm241/kOUOpqmz2YQ/bv+Ritng3RL1fHktnHkaSfoCYUWxzAFBlaTyKPB0dz5Y=";
    public final static String CHARSET="utf-8";
    //支付宝公钥
    public final static String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjBB4ZKuR+TmrG6y+iOmUi2GX/IFxfNVs14QFRsTvHE2Xe4lCPzszhEftd5vZhXW9xeZkFg6nmhNZYW2pB8kv7RQqCf2XPEmjTd8YdR0ahD+UauzIJRKJjsppB7knWA3FqhMEZ5Pi9kw2fqfujP5TVbW/7Kaap4ScEqiW1Ox/WLchrbb3FhNSz0bvhuGsj+lXfUgfx1AW286Bk7zX9Ry9OPH3IPctAHsgbEc+vAfJPfpL9hGp4TqWt8pd185TKNfCEnWTYfW9Fk92klIn2/kJdmgEq2uoof+QjPm85+kjSgKSXmVV7xyiC6hsS1wzkMPxGQjKxV6AjrK6tbJWg0DERwIDAQAB";
    public final static String APLIPAY_NOTIFY_URL="http://apijava.gdswlw.com/meilan/Alipay/payResult.do";
    public final static String APLIPAY_DEPOSIT_NOTIFY_URL="http://apijava.gdswlw.com/meilan/deposit/payResult.do";//礼物支付回调
    public final static String APLIPAY_WALLET_NOTIFY_URL="http://apijava.gdswlw.com/meilan/Alipay/payResult2.do";//钱包充值支付回调
    /****************************/

    /**************微信参数******************/
    public final static String APPID="wx484bcccad1570f32";
    public final static String MCH_ID="1494724642";//商户号
    public final static String API_KEY="MLSXIANGGANGMEILANZHANJITUAN1369";//商户平台密钥key
    public final static String APPSECRET="478a389f645f1acd984cc878100de2e7";
    public final static String NOTIFY_URL="http://apijava.gdswlw.com/meilan/weixin/notify.do";//订单支付回调
    public final static String DEPOSIT_NOTIFY_URL="http://apijava.gdswlw.com/meilan/weixin/notifyDeposit.do";//缴纳押金回调
    public final static String WALLET_NOTIFY_URL="http://apijava.gdswlw.com/meilan/weixin/notifyWallet.do";//钱包充值回调
    /*****************************************/

}
