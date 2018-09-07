package org.gbutil.fsm;


import org.apache.logging.log4j.LogManager;
import org.gbutil.fsm.node.ISMConnectedNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.function.Function;

public class FiniteStateMachine<L extends Enum<L>, S extends IState> implements Function<S, Boolean> {
    ISMConnectedNode<L, S> mInitialNode;
    String mName;

    FiniteStateMachine(String name, ISMConnectedNode<L, S> initialNode) {
        mInitialNode = initialNode;
        mName = name;
    }

    @SuppressWarnings("unchecked")
    public static <L extends Enum<L>, S extends IState> FiniteStateMachine<L, S> importFSM(String path) {
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;
        try {
            fileIn = new FileInputStream(path);
            objIn = new ObjectInputStream(fileIn);

            return (FiniteStateMachine<L, S>) objIn.readObject();
        } catch (FileNotFoundException e) {
            LogManager.getRootLogger().error("FSM resource file at '" + path + "' not found");
        } catch (IOException e) {
            LogManager.getRootLogger().error("Unexpected error occurred while trying to open resource file", e);
        } catch (ClassNotFoundException | ClassCastException e) {
            LogManager.getRootLogger().error("Unexpected class found in resource file", e);
        } finally {
            try {
                if (fileIn != null) fileIn.close();
                if (objIn != null) objIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Boolean apply(S state) {
        return mInitialNode.execute(state);
    }

    @Override
    public String toString() {
        return String.format("FiniteStateMachine{mInitialNode=%s, mName='%s'}", mInitialNode, mName);
    }
}
