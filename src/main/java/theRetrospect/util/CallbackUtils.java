package theRetrospect.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
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
     * @param loopBody          The main loop body. Call the provided runnable to continue the loop.
     *                          Note that if you do not call the provided runnable, the loop will terminate
     *                          but loopComplete will not be called.
     * @param loopComplete      Actions to run after the whole loop exits. Can be null.
     */
    public static void ForLoop(Supplier<Boolean> continueCondition, Runnable postIteration, Consumer<Runnable> loopBody, Runnable loopComplete) {
        if (continueCondition.get()) {
            loopBody.accept(() -> {
                if (postIteration != null)
                    postIteration.run();
                ForLoop(continueCondition, postIteration, loopBody, loopComplete);
            });
        } else {
            if (loopComplete != null)
                loopComplete.run();
        }
    }

    /**
     * An async for each loop using callbacks.
     *
     * @param list     The list to iterate through.
     * @param loopBody The main loop body. Call the provided runnable to continue the loop.
     *                 Note that if you do not call the provided runnable, the loop will terminate
     *                 but loopComplete will not be called.
     * @param <T>      The type of elements in the list.
     */
    public static <T> void ForEachLoop(List<T> list, BiConsumer<T, Runnable> loopBody) {
        List<T> listCopy = new ArrayList<>(list);
        ForLoop(
                () -> listCopy.size() > 0,
                () -> listCopy.remove(0),
                next -> loopBody.accept(listCopy.get(0), next)
        );
    }

    /**
     * An async for each loop using callbacks.
     *
     * @param list         The list to iterate through.
     * @param loopBody     The main loop body. Call the provided runnable to continue the loop.
     *                     Note that if you do not call the provided runnable, the loop will terminate
     *                     but loopComplete will not be called.
     * @param loopComplete Actions to run after the whole loop exits. Can be null.
     * @param <T>          The type of elements in the list.
     */
    public static <T> void ForEachLoop(List<T> list, BiConsumer<T, Runnable> loopBody, Runnable loopComplete) {
        List<T> listCopy = new ArrayList<>(list);
        ForLoop(
                () -> listCopy.size() > 0,
                () -> listCopy.remove(0),
                next -> loopBody.accept(listCopy.get(0), next),
                loopComplete
        );
    }
}
