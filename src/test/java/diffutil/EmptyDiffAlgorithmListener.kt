package diffutil

import com.github.difflib.algorithm.DiffAlgorithmListener

class EmptyDiffAlgorithmListener : DiffAlgorithmListener {
    override fun diffStart() {}
    override fun diffStep(value: Int, max: Int) {}
    override fun diffEnd() {}
}