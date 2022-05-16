package theRetrospect.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CallbackUtils {

    /**
     * An async for loop using callbacks.
     *
     * @param continueCondition This condition must return true before a new iteration can be started.
     * @param iteration         The main loop body. Call the provided runnable to continue the loop.
     *                          Note that if you do not call the provided runnable, the loop will terminate
     *                          but loopComplete will not be called.
     */
    public static void ForLoop(Supplier<Boolean> continueCondition, Consumer<Runnable> iteration) {
        ForLoop(continueCondition, null, iteration, null);
    }

    /**
     * An async for loop using callbacks.
     *
     * @param continueCondition This condition must return true before a new iteration can be started.
     * @param postIteration     Actions to run after an iteration is complete. Can be null.
     * @param iteration         The main loop body. Call the provided runnable to continue the loop.
     *                          Note that if you do not call the provided runnable, the loop will terminate
     *                          but loopComplete will not be called.
     */
    public static void ForLoop(Supplier<Boolean> continueCondition, Runnable postIteration, Consumer<Runnable> iteration) {
        ForLoop(continueCondition, postIteration, iteration, null);
    }

    /**
     * An async for loop using callbacks.
     *
     * @param continueCondition This condition must return true before a new iteration can be started.
     * @param postIteration     Actions to run after an iteration is complete. Can be null.
     * @param iteration         The main loop body. Call the provided runnable to continue the loop.
     *                          Note that if you do not call the provided runnable, the loop will terminate
     *                          but loopComplete will not be called.
     * @param loopComplete      Actions to run after the whole loop exits. Can be null.
     */
    public static void ForLoop(Supplier<Boolean> continueCondition, Runnable postIteration, Consumer<Runnable> iteration, Runnable loopComplete) {
        if (continueCondition.get()) {
            iteration.accept(() -> {
                if (postIteration != null)
                    postIteration.run();
                ForLoop(continueCondition, postIteration, iteration);
            });
        } else {
            if (loopComplete != null)
                loopComplete.run();
        }
    }
}
