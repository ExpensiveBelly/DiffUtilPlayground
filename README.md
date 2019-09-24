# DiffUtilPlayground

- What's DiffUtil?

https://developer.android.com/reference/android/support/v7/util/DiffUtil

- But by looking at the implementation, I see it's coupled with 
Android (import androidx.recyclerview.widget.RecyclerView.Adapter;), how do I test it?

That's the reason I created this repo.

- How do I run DiffUtil?

Open `DiffUtilRunner.kt` and run `main()`. Change the lists as needed.

#### Objectives of this project

1. Ability to add tests for DiffUtil implementations
2. Possibility of tweaking the algorithm to make it more efficient/performant
3. Custom implementations of DiffUtil.Callback. In this repo, a custom implementation used
for [Groupie]((https://github.com/lisawray/groupie)) is showcased.



