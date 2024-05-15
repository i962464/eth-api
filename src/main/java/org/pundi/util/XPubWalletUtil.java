package org.pundi.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.Keys;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月15日 14:42:00
 */
public class XPubWalletUtil {

  // 1. 根据助记词打印seed明文，打印xpub和xprivate
  public static Pair<String, String> getSeedXpubXprivate(String mnemonicCode, String passphrase) throws UnreadableWalletException {
    DeterministicSeed seed = new DeterministicSeed(mnemonicCode, null, passphrase, System.currentTimeMillis() / 1000L);
    System.out.println("Seed (明文): " + seed.getMnemonicCode());

    // 创建钱包密钥
    DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
    List<ChildNumber> keyPath = new ArrayList<>();
    DeterministicKey key = chain.getKeyByPath(keyPath, true);

    // 打印xpub
    String xpub = key.serializePubB58(NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
    System.out.println("Xpub: " + xpub);

    // 打印xprivate
    String xprivate = key.serializePrivB58(NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
    System.out.println("Xprivate: " + xprivate);

    return Pair.of(xpub, xprivate);
  }

  // 2. 根据xpub eth地址
  public static String generateEthAddresses(String xpub, int index) {
    DeterministicKey parentKey = DeterministicKey.deserializeB58(xpub, MainNetParams.get());
    DeterministicKey childKey = HDKeyDerivation.deriveChildKey(parentKey, index);
    ECKey uncompressedChildKey = childKey.decompress();
    // 以太坊需要把前缀去掉（0x04前缀表示未压缩）
    String hexKey = uncompressedChildKey.getPublicKeyAsHex().substring(2);
    String addr = Keys.getAddress(hexKey);

    return Keys.toChecksumAddress(addr);
  }

  // 3. 根据xpub或者xprivate推导生成地址的私钥
  public static String derivePrivateKey(String xpub, int index) {
    return null;
  }

  public static void main(String[] args) throws UnreadableWalletException {
    String mnemonicCode = "swamp border inhale call name hungry hero cereal economy sunset west income";
    String passphrase = "";
    // 1. 打印seed明文，xpub和xprivate
    Pair<String, String> rootXpubAndPrv = getSeedXpubXprivate(mnemonicCode, passphrase);

    // 2. 根据xpub生成eth地址
    String xpub = rootXpubAndPrv.getLeft();
    String ethAddresses = generateEthAddresses(xpub, 0);
    System.out.println("Eth Addresses: " + ethAddresses);

    // 3. 根据xpub推导生成地址的私钥
    int index = 0;
    String derivedPrivateKey = derivePrivateKey(xpub, index);
    System.out.println("Private Key for Address at index " + index + ": " + derivedPrivateKey);
  }


}
