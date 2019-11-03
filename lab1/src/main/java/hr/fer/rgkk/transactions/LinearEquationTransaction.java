package hr.fer.rgkk.transactions;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.math.BigInteger;

import static org.bitcoinj.script.ScriptOpCodes.*;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIG;

/**
 * You must implement locking and unlocking script such that transaction output is locked by 2 integers x and y
 * such that they are solution to the equation system:
 * <pre>
 *     x + y = first four digits of your student id
 *     abs(x-y) = last four digits of your student id
 * </pre>
 * If needed change last digit of your student id such that x and y have same parity. This is needed so that equation
 * system has integer solutions.
 * <p>
 * My student id is 0036489244
 * So the first 4 digits are 0036, and the last are 9244
 * Solutions to the above equation system are as follows: (1490, -1454) and (-1454, 1490)
 */
public class LinearEquationTransaction extends ScriptTransaction {

    private final int x = 1490;
    private final int y = -1454;

    LinearEquationTransaction(WalletKit walletKit, NetworkParameters parameters) {
        super(walletKit, parameters);
    }

    @Override
    public Script createLockingScript() {
        ScriptBuilder builder = new ScriptBuilder();
        builder.op(OP_2DUP);
        builder.op(OP_ADD);
        builder.data(encodeToByte(x + y)); // 0036
        builder.op(OP_EQUALVERIFY);
        builder.op(OP_SUB);
        builder.data(encodeToByte(x - y)); // 2944
        builder.op(OP_EQUAL);

        return builder.build();
    }

    @Override
    public Script createUnlockingScript(Transaction unsignedTransaction) {
        ScriptBuilder builder = new ScriptBuilder();
        builder.data(encodeToByte(x));
        builder.data(encodeToByte(y));
        return builder.build();
    }

    private byte[] encodeToByte(int number) {
        return Utils.reverseBytes(Utils.encodeMPI(BigInteger.valueOf(number), false));
    }

}
