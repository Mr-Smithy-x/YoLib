import com.mrsmyx.yorehab.YoParser;
import com.mrsmyx.yorehab.callbacks.YoSearchListener;
import com.mrsmyx.yorehab.models.YoItem;
import com.mrsmyx.yorehab.utils.AESEncryption;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        YoParser.SearchAsync("witch", new YoSearchListener() {
            @Override
            public void OnItemSearchFound(List<YoItem> yoItemList) {
                for (YoItem y : yoItemList){
                    System.out.println(y.getItem_name());
                }
            }

            @Override
            public void OnItemSearchError(String message) {
                System.err.println(message);
            }
        });
    }

}
