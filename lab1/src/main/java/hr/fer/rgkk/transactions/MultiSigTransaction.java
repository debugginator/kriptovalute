package hr.fer.rgkk.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.script.Script;

/**
 * You must implement locking and unlocking script such that transaction output is locked by one mandatory authority
 * (e.g. bank) and at least 1 of 3 other authorities (e.g. bank associates).
 */
public class MultiSigTransaction extends ScriptTransaction {

    public MultiSigTransaction(WalletKit walletKit, NetworkParameters parameters) {
        super(walletKit, parameters);
    }

    @Override
    public Script createLockingScript() {
        // TODO: Create Locking script
        throw new UnsupportedOperationException();
    }

    @Override
    public Script createUnlockingScript(Transaction unsignedTransaction) {
        // TODO: Please be aware of the OP_CHECK_MULTISIG bug!
        // TODO: Create Unlocking script
        throw new UnsupportedOperationException();
    }
}
