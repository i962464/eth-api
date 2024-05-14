package org.pundi.util;

import java.util.List;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 22:03:00
 */
@Slf4j
public class HDWallet {

  private static final String PREFIX = "0x";

  private static final byte[] SEED = null;

  private static final String PASSPHRASE = "";

  private static final Long CREATIONTIMESECONDS = 0L;

  private static final String wordList = "ketchup unfold gaze genuine bulk afford pear essay want put network stadium average law loop";

  private static final MainNetParams mainnetParams = MainNetParams.get();

  public static void main(String[] args) throws Exception {

    DeterministicSeed deterministicSeed = new DeterministicSeed(wordList, SEED, PASSPHRASE, CREATIONTIMESECONDS);

    /**生成根私钥 root private key*/
    DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(deterministicSeed.getSeedBytes());

    /**根私钥进行 priB58编码*/
    String priv = rootPrivateKey.serializePrivB58(mainnetParams);
    log.info("BIP32 extended private key:{}", priv);
//    /**由根私钥生成HD钱包*/
//    DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(rootPrivateKey);
//    /**定义父路径*/
//    List<ChildNumber> parsePath = HDUtils.parsePath("44H/60H/0H");
//
//    DeterministicKey accountKey0 = deterministicHierarchy.get(parsePath, true, true);
//
//    /**由父路径,派生出第一个子私钥*/
//    DeterministicKey childKey0 = HDKeyDerivation.deriveChildKey(accountKey0, 0);
//    log.info("BIP32 extended 0 private key:{}", childKey0.serializePrivB58(mainnetParams));
//    log.info("BIP32 extended 0 public key:{}", childKey0.serializePubB58(mainnetParams));
//    log.info("0 private key:{}", childKey0.getPrivateKeyAsHex());
//    log.info("0 public key:{}", childKey0.getPublicKeyAsHex());
//    ECKeyPair childEcKeyPair0 = ECKeyPair.create(childKey0.getPrivKeyBytes());
//    log.info("0 address:{}", PREFIX + Keys.getAddress(childEcKeyPair0));
  }


}
