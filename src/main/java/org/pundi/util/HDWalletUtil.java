package org.pundi.util;

import java.math.BigInteger;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description 根据助记词管理多个地址
 * @createTime 2024年05月11日 22:03:00
 */
@Slf4j
public class HDWalletUtil {


  public static String mnemonic = "swamp border inhale call name hungry hero cereal economy sunset west income";

  // 1. 根据助记词生成根私钥、根公钥
  public static Bip32ECKeyPair generateRootKeyPair(String mnemonic) {
    byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
    return Bip32ECKeyPair.generateKeyPair(seed);
  }

  // 2. 根据index生成派生地址
  public static String deriveAddress(Bip32ECKeyPair rootKeyPair, int index) {
    int[] derivationPath = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, index};
    Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(rootKeyPair, derivationPath);
    Credentials credentials = Credentials.create(derivedKeyPair);
    return credentials.getAddress();
  }

  // 3. 根据index获取派生地址的私钥
  public static String derivePrivateKey(Bip32ECKeyPair rootKeyPair, int index) {
    int[] derivationPath = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, index};
    Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(rootKeyPair, derivationPath);
    Credentials credentials = Credentials.create(derivedKeyPair);
    return credentials.getEcKeyPair().getPrivateKey().toString(16);
  }


  //打印根私钥、根公钥和种子
  public static void printRootKeyPair(Bip32ECKeyPair rootKeyPair, String mnemonic) {
    String rootPrivateKey = rootKeyPair.getPrivateKey().toString(16);
    String rootPublicKey = rootKeyPair.getPublicKey().toString(16);
    byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
    String seedString = new BigInteger(1, seed).toString(16);
    System.out.println("Root Private Key: " + rootPrivateKey);
    System.out.println("Root Public Key: " + rootPublicKey);
    System.out.println("Seed: " + seedString);
  }


  /**
   * 根据助记词推导地址
   * @param mnemonic
   * @param index
   */
  public static String createAccount(String mnemonic, Integer index){
    // 生成根私钥、根公钥
    Bip32ECKeyPair rootKeyPair = generateRootKeyPair(mnemonic);
    return deriveAddress(rootKeyPair, index);
  }


  // 4. 测试方法 https://bip39.onekey.so/
  public static void main(String[] args) {
    // 替换为你的助记词
    String mnemonic = "swamp border inhale call name hungry hero cereal economy sunset west income";

    // 生成根私钥、根公钥
    Bip32ECKeyPair rootKeyPair = generateRootKeyPair(mnemonic);
    printRootKeyPair(rootKeyPair, mnemonic);

    String a = EthereumUtil.deriveAddressFromExtendPubKey(
        "c5c87c1389ed5468128cbfaf78f31c9d5ab6b5dcf6557761da0fb14cb87211227d39da0e90865808752023ee498471db425d162f8ffae9c24ef6eb6531922a8", 0);
    System.out.println(">>>>a=" + a);
    // 测试派生地址
    int index = 0;
    String derivedAddress = deriveAddress(rootKeyPair, index);
    System.out.println("Derived Address at index " + index + ": " + derivedAddress);

    // 测试派生地址的私钥
    String derivedPrivateKey = derivePrivateKey(rootKeyPair, index);
    System.out.println("Private Key for Derived Address at index " + index + ": " + derivedPrivateKey);
  }


}
