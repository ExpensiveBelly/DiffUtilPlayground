package diffutil

import diffutil.callback.DefaultDiffUtilCallback
import diffutil.callback.ExpandableListDiffCallback
import org.junit.Test
import kotlin.system.measureTimeMillis

private const val AMOUNT_OF_ITEMS = 5000

class DiffUtilRunnerKtTest {

    @Test
    fun groupie_diffutil_callback_should_be_faster_than_default_diffutil_callback() {
        val oldList = (1..AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("Hello")) }
        val newList = (1..AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("World")) }
        val oldListTransformed = oldList.flatMap { listOf(it.summary) + it.items }
        val newListTransformed = newList.flatMap { listOf(it.summary) + it.items }

        assert(measureTimeMillis {
            calculateDiffUtilAndDispatch(ExpandableListDiffCallback(oldListTransformed, newListTransformed, oldList.map { it.items.size }, newList.map { it.items.size }), EmptyUpdateCallback())
        } < measureTimeMillis {
            calculateDiffUtilAndDispatch(DefaultDiffUtilCallback(oldListTransformed, newListTransformed, EqualsDiffCallback()), EmptyUpdateCallback())
        })
    }
}