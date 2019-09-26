package diffutil

import com.github.difflib.DiffUtils
import com.github.difflib.algorithm.jgit.HistogramDiff
import diffutil.callback.DefaultDiffUtilCallback
import diffutil.callback.ExpandableListDiffCallback
import org.junit.Test
import kotlin.system.measureTimeMillis

private const val LARGE_AMOUNT_OF_ITEMS = 5000

class DiffUtilRunnerKtTest {

    @Test
    fun groupie_diffutil_callback_should_be_faster_than_default_diffutil_callback_for_large_amounts_of_items() {
        val oldList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("Hello")) }
        val newList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("World")) }
        val oldListTransformed = oldList.flatMap { listOf(it.summary) + it.items }
        val newListTransformed = newList.flatMap { listOf(it.summary) + it.items }

        assert(measureTimeMillis {
            calculateDiffUtilAndDispatch(ExpandableListDiffCallback(oldListTransformed, newListTransformed, oldList.map { it.items.size }, newList.map { it.items.size }), EmptyUpdateCallback())
        } < measureTimeMillis {
            calculateDiffUtilAndDispatch(DefaultDiffUtilCallback(oldListTransformed, newListTransformed, EqualsDiffCallback()), EmptyUpdateCallback())
        })
    }

    @Test
    fun myers_android_implementation_is_faster_than_java_diff_utils_one_for_large_amounts_of_items() {
        val oldList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("Hello")) }
        val newList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("World")) }

        assert(measureTimeMillis {
            calculateDiffUtilAndDispatch(DefaultDiffUtilCallback(oldList, newList, EqualsDiffCallback()), EmptyUpdateCallback())
        } < measureTimeMillis {
            DiffUtils.diff(oldList, newList)
        })
    }

    @Test
    fun histogram_is_faster_than_default_diffcallback_myers_algorithm() {
        val oldList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("Hello")) }
        val newList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("World")) }

        assert(measureTimeMillis {
            calculateDiffUtilAndDispatch(DefaultDiffUtilCallback(oldList, newList, EqualsDiffCallback()), EmptyUpdateCallback())
        } > measureTimeMillis {
            HistogramDiff<Nested>().computeDiff(oldList, newList, EmptyDiffAlgorithmListener())
        })
    }

    @Test
    fun histogram_is_faster_than_custom_gropie_diffutil_callback_myers_algorithm() {
        val oldList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("Hello")) }
        val newList = (1..LARGE_AMOUNT_OF_ITEMS).map { Nested("Summary $it", listOf("World")) }
        val oldListTransformed = oldList.flatMap { listOf(it.summary) + it.items }
        val newListTransformed = newList.flatMap { listOf(it.summary) + it.items }

        assert(measureTimeMillis {
            calculateDiffUtilAndDispatch(ExpandableListDiffCallback(oldListTransformed, newListTransformed, oldList.map { it.items.size }, newList.map { it.items.size }), EmptyUpdateCallback())
        } > measureTimeMillis {
            HistogramDiff<Nested>().computeDiff(oldList, newList, EmptyDiffAlgorithmListener())
        })
    }
}