package vn.edu.husc.kltn.huynn.digitalsignatureqrcode;

import android.util.Log;

import java.math.BigInteger;

import javax.xml.bind.DatatypeConverter;

public class SignVerify {

    SHA256 sha256 = new SHA256();
    RSA rsa = new RSA();
    BigInteger[] sk = new BigInteger[2];//Secure key = private key
    BigInteger[] pk = new BigInteger[2];//Public key
    BigInteger D = new BigInteger("23242930525595733134491908184679306654637030869002044313501113087128600027465821100042755502614340090312548142631338704931992994075983643268843095994821036513643929296438460872221385417740219241358905316147704877440780404901118591767290445442318370149961862201935317549233276220019405142460976021470727893981101032945530324225898535384960962416828820104421547501188025853693634305834268233088205190028602727929904309704012685261662685033524578911752053639025894405139719090068824121843318201646938963235005361296809983009933128044676706322149577012089517314614308374414728389277251220591905563870010629447786048119999");
    BigInteger E = new BigInteger("13012297390627839203544309463280698142785233893419180025719388993159750572222525953909022167818100952618213123728751854365731149408863330817692961170991991");
    BigInteger N = new BigInteger("23471696714241035610793541632238232393830968335236692335792964638920996399821432133573484319841680559526766366576024032982843949213339397699743873368930703310686584661081276428387904699502358309378501517966531358475462121829238022852477239699489579645416616002432381418176158701297364168477724893793891147619196286921902109713740691946825216717800679764228080561786545761730768071608351573932942363103237797201579896765418644076202575693829662834579405707474125709325786065210222858170765546448318890324007419174356293706224889688438236729690989754770253476618290277302819849970487927579533404650534830357442345428407");

    public String sign(String message, BigInteger privateKeyD, BigInteger privateKeyN) {
        sk[0] = D;
        if (BigInteger.valueOf(0).compareTo(privateKeyD) < 0) {
            sk[0] = privateKeyD;
        }

        sk[1] = N;
        if (BigInteger.valueOf(0).compareTo(privateKeyN) < 0) {
            sk[1] = privateKeyN;
        }

        byte[] msgBytes = message.getBytes();
        byte[] bobsHash = sha256.hash(msgBytes);
        String bobsHashStr = DatatypeConverter.printHexBinary(bobsHash);

        byte[] encrypted = rsa.encrypt(sk[0], sk[1], bobsHashStr.getBytes());
        return DatatypeConverter.printHexBinary(encrypted);
    }

    public boolean verify(byte[] encrypted, String message, BigInteger publicKeyE, BigInteger publicKeyN) {
        pk[0] = E;
        if (BigInteger.valueOf(0).compareTo(publicKeyE) < 0) {
            pk[0] = publicKeyE;
        }

        pk[1] = N;
        if (BigInteger.valueOf(0).compareTo(publicKeyN) < 0) {
            pk[1] = publicKeyN;
        }

        byte[] rMsgBytes = message.getBytes();
        byte[] rHash = sha256.hash(rMsgBytes);
        String rHashStr = DatatypeConverter.printHexBinary(rHash);

        byte[] decrypted = rsa.decrypt(pk[0], pk[1], encrypted);

        Log.i("Decrypted", new String(decrypted));
        Log.i("rHashStr", rHashStr);

        if (new String(decrypted).equals(rHashStr)) {
            return true;
        }
        return false;
    }
}