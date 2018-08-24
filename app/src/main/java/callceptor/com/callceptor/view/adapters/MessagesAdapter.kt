package callceptor.com.callceptor.view.adapters

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import callceptor.com.callceptor.R
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.domain.listeners.OnMessagesItemClicked

/**
 * Created by Tom on 24.8.2018..
 */
class MessagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    lateinit var context: Context
    lateinit var messages: ArrayList<Message>
    lateinit var onMessagesItemClicked: OnMessagesItemClicked
//    private var isLoadingAdded: Boolean = false
//    private lateinit var onListItemClicked: OnResultItemClicked

    constructor()

    constructor(context: Context, messages: ArrayList<Message>, onMessagesItemClicked: OnMessagesItemClicked) : super() { // , onListItemClicked: OnResultItemClicked
        this.context = context
        this.messages = messages
        this.onMessagesItemClicked = onMessagesItemClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == ITEM) {
        return ResultItemHolder(LayoutInflater.from(context).inflate(R.layout.item_messages, parent, false))
//        } else {
//            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_git_result_loading, parent, false))
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        var viewType = getItemViewType(position)
//
//        if (viewType == ITEM) {
        holder as ResultItemHolder


        holder.itemMessagesTvTime.text = messages[position].date
        holder.itemMessagesTvNumber.text = messages[position].number
        holder.itemMessagesTvMessage.text = messages[position].body

        holder.messagesItem.setOnClickListener { onMessagesItemClicked.onMessageClicked(position) }

    }

//    override fun getItemViewType(position: Int): Int {
//        return if (repos[position].createdAt != null) {
//            ITEM
//        } else {
//            LOADING
//        }
//    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ResultItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.itemMessagesMainRelativeLayout)
        lateinit var messagesItem: RelativeLayout

        @BindView(R.id.itemMessagesTvMessage)
        lateinit var itemMessagesTvMessage: TextView

        @BindView(R.id.itemMessagesTvNumber)
        lateinit var itemMessagesTvNumber: TextView

        @BindView(R.id.itemMessagesTvTime)
        lateinit var itemMessagesTvTime: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

    }

//    protected inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    fun add(repo: RepoObject) {
//        repos.add(repo)
//        notifyItemInserted(repos.size - 1)
//    }
//
//    fun addAll(repoList: List<RepoObject>) {
//        for (rl in repoList) {
//            add(rl)
//        }
//    }
//
//    fun remove(repo: RepoObject) {
//        val position = repos.indexOf(repo)
//        if (position > -1) {
//            repos.removeAt(position)
//            notifyItemRemoved(position)
//        }
//    }
//
//    fun clear() {
//        isLoadingAdded = false
//        while (itemCount > 0) {
//            remove(getItem(0))
//        }
//    }
//
//    fun isEmpty(): Boolean {
//        return itemCount == 0
//    }
//
//    fun addLoadingFooter() {
//        isLoadingAdded = true
//        add(RepoObject())
//    }
//
//    fun removeLoadingFooter() {
//        isLoadingAdded = false
//
//        val position = if (repos.size > 0) repos.size - 1 else 0
//
//        repos.removeAt(position)
//        notifyItemRemoved(position)
//
//    }
//
//    fun getItem(position: Int): RepoObject {
//        return repos[position]
//    }
}