package hr.fer.rgkk.transactions;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import static org.bitcoinj.script.ScriptOpCodes.*;

/**
 * You must implement standard Pay-2-Public-Key-Hash transaction type.
 */
public class PayToPubKeyHash extends ScriptTransaction {

    private final ECKey key;

    PayToPubKeyHash(WalletKit walletKit, NetworkParameters parameters) {
        super(walletKit, parameters);
        key = getWallet().freshReceiveKey();
    }


    @Override
    public Script createLockingScript() {
        ScriptBuilder builder = new ScriptBuilder(); // Create new ScriptBuilder
        builder.op(OP_DUP);
        builder.op(OP_HASH160);
        builder.data(key.getPubKeyHash());
        builder.op(OP_EQUALVERIFY);
        builder.op(OP_CHECKSIG);
        return builder.build();
    }

    @Override
    public Script createUnlockingScript(Transaction unsignedTransaction) {
        TransactionSignature txSig = sign(unsignedTransaction, key); // Create key signature

        return new ScriptBuilder()                                   // Create new ScriptBuilder
                .data(txSig.encodeToBitcoin())                       // Add key signature to unlocking script
                .data(key.getPubKey())                               // Add public key to unlocking script
                .build();                                            // Build "<sig><pubkey>" unlocking script
    }

}
