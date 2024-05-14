package org.pundi.util;

import java.math.BigInteger;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 14:32:00
 */
public class XPubWalletUtil {


  public static void main(String[] args) throws Exception {

//    // 设置根种子
//    String mnemonicCode = "your twelve word mnemonic here";
//    String passphrase = "";
//    DeterministicSeed seed = new DeterministicSeed(mnemonicCode, null, passphrase, 1409478661L);
//
//    // 从种子派生主密钥
//    Wallet wallet = Wallet.fromSeed(null, seed);
//    DeterministicKey masterPrivateKey = wallet.freshReceiveKey();
//
//    // 设置Derivation Path
//    String derivationPath = "m/44'/60'/0'/0"; // BIP44路径
//
//    // 派生子密钥
//    List<ChildNumber> path = HDUtils.parsePath(derivationPath);
//    DeterministicHierarchy hierarchy = new DeterministicHierarchy(masterPrivateKey);
//    DeterministicKey childKey = hierarchy.deriveChild(path, true, true, new ChildNumber(0));
//
//    // 输出ETH地址
//    System.out.println("ETH Address: " + EthAddress.fromPublicKey(childKey.getPubKey()));

    // Generate a new Ethereum address
    ECKeyPair keyPair = Keys.createEcKeyPair();
    BigInteger privateKey = keyPair.getPrivateKey();
    BigInteger publicKey = keyPair.getPublicKey();

    // Get the hexadecimal representation of the keys
    String privateKeyHex = privateKey.toString(16);
    String publicKeyHex = publicKey.toString(16);

    // Generate the Ethereum address from the public key
    String address = Keys.getAddress(publicKeyHex);

    System.out.println("Private Key: " + privateKeyHex);
    System.out.println("Public Key: " + publicKeyHex);
    System.out.println("Ethereum Address: " + address);

  }
}

