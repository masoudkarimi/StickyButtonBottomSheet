package mkn.stickybuttonbuttomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import mkn.stickybuttonbuttomsheet.utils.*

class StickyButtonBottomSheet(private val context: Context) {

    private val screenHeight = context.getScreenHeight()
    private val startHeight = screenHeight.times(0.55f).toInt()
    private val emptySpaceHeight = screenHeight - startHeight

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonContainer: FrameLayout
    private lateinit var btnSubmit: MaterialButton

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetDialog: BottomSheetDialog?
    private val mBaseView: FrameLayout

    private var buttonClickListener = {}
    private var bottomSheetFullScreenProgress: (Float) -> Unit = {}

    private val items = arrayListOf<String>()

    private var adapterPositionOffset = 0
    private val adapter: Adapter by lazy {
        Adapter(
            emptySpaceHeight,
            ::onItemClick,
        )
    }

    init {
        mBaseView = createView()
        mBottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
    }

    private fun createView(): FrameLayout {
        return with(context) {
            return@with frameLayout {
                recyclerView = recycler {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    clipToPadding = false
                    setPadding(0, 0, 0, 96.dp)
                }

                addView(recyclerView, matchWidthMatchHeight {})

                btnSubmit = MaterialButton(context, null, R.attr.borderlessButtonStyle).apply {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    typeface = FontUtils.getBoldFont(context)
                    backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.teal_700))
                    text = "Submit"
                    setOnClickListener {
                        dismiss()
                        buttonClickListener()
                    }
                }


                buttonContainer = frameLayout {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                    addView(btnSubmit, matchWidthCustomHeight(60) {
                        gravity = Gravity.CENTER
                        rightMargin = 24.dp
                        leftMargin = 24.dp
                    })
                }

                addView(buttonContainer, matchWidthCustomHeight(96) {
                    gravity = Gravity.BOTTOM
                })
            }
        }
    }

    private fun dismiss() {
        mBottomSheetDialog?.let {
            it.dismiss()
            mBottomSheetDialog = null
        }
    }

    private fun onItemClick(position: Int, view: View) {
        if (adapter[position] is Row.EmptySpace) {
            dismiss()
            return
        }

        if (adapter[position] is Row.TextItem) {
            dismiss()
            val item = adapter[position] as Row.TextItem
            // do something here
            // real position of clicked item -> position - adapterPositionOffset
        }
    }

    fun setItems(data: List<String>): StickyButtonBottomSheet {
        items.clear()
        items.addAll(data)
        return this
    }

    fun setBottomSheetFullScreenProgressCallback(callback: (Float) -> Unit): StickyButtonBottomSheet {
        bottomSheetFullScreenProgress = callback
        return this
    }

    fun show() {
        setUpViews()

        mBottomSheetDialog?.setContentView(mBaseView)
        bottomSheetBehavior = BottomSheetBehavior.from(mBaseView.parent as View)
        bottomSheetBehavior?.peekHeight = startHeight
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss()
                }
            }
        })

        mBottomSheetDialog?.show()
    }

    private fun setUpViews() {
        recyclerView.adapter = adapter

        adapter.addItem(Row.EmptySpace)
        adapterPositionOffset++
        adapter.addItem(Row.TopCorner)
        adapterPositionOffset++
        adapter.addItem(Row.Title("Simple Title"))
        adapterPositionOffset++

        adapter.addItems(items.map {
            Row.TextItem(it)
        })

        val itemDecorator = StickyHeaderItemDecorator()
        recyclerView.addItemDecoration(itemDecorator)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val topPosition = lm.findFirstVisibleItemPosition()

                if (topPosition == 0) {
                    val view = lm.findViewByPosition(topPosition) ?: return

                    val top = view.top
                    val bottom = view.bottom
                    val height = view.measuredHeight
                    val offset = height.minus(height.times(0.80f)).toInt()

                    if (bottom in 0..offset) {
                        val progress = 1f - (bottom.toFloat() / offset) // 0f ... 1f
                        bottomSheetFullScreenProgress.invoke(progress)
                    } else if (bottom < 0) {
                        if (lm.itemCount >= 3) {
                            bottomSheetFullScreenProgress.invoke(1f)
                        }
                    } else if (bottom > offset) {
                        if (lm.itemCount >= 3) {
                            bottomSheetFullScreenProgress.invoke(0f)
                        }
                    }
                } else {
                    if (lm.itemCount >= 3) {
                        bottomSheetFullScreenProgress.invoke(1f)
                    }
                }

            }
        })

    }

    sealed class Row {
        object EmptySpace : Row()
        object TopCorner : Row()
        data class Title(val title: String) : Row()
        data class TextItem(val text: String) : Row()
    }

    class Adapter(
        private val emptySpaceHeight: Int,
        private val onItemClick: ((position: Int, view: View) -> Unit)? = null,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var type = 0

        private val typeEmptySpace = ++type
        private val typeTopCorner = ++type
        private val typeTitle = ++type
        private val typeTextItem = ++type

        private val items = arrayListOf<Row>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                typeEmptySpace -> {
                    val view = View(parent.context).apply {
                        layoutParams = RecyclerView.LayoutParams(
                            RecyclerView.LayoutParams.MATCH_PARENT,
                            emptySpaceHeight
                        )
                    }

                    EmptyViewHolder(
                        view,
                        onItemClick
                    )
                }

                typeTopCorner -> {
                    val view = TopCornerView(parent.context)
                    object : RecyclerView.ViewHolder(view) {}
                }

                typeTitle -> {
                    val view = BottomSheetTitleView(parent.context).apply {
                        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                        layoutParams = RecyclerView.LayoutParams(
                            RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT
                        )
                    }


                    TitleViewHolder(view)
                }

                typeTextItem -> {
                    val view = TextView(parent.context).apply {
                        gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                        setPadding(16, 0, 16, 0)
                        setTextColor(Color.DKGRAY)
                        typeface = FontUtils.getRegularFont(context)
                        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                        layoutParams = RecyclerView.LayoutParams(
                            RecyclerView.LayoutParams.MATCH_PARENT,
                            52.dp
                        )

                        background = rippleDrawable {
                            backColor = ContextCompat.getColor(context, R.color.white)
                            rippleColor = Color.argb(15, 0, 0, 0)
                            preLollipopPressedColor = Color.argb(15, 0, 0, 0)
                            maskDrawable = materialShape {
                                setCornerSize(16.dpf)
                            }
                        }
                    }

                    TextItemViewHolder(view, onItemClick)
                }

                else -> throw IllegalArgumentException("viewType is invalid!")
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun getItemViewType(position: Int): Int {
            return when (items[position]) {
                Row.EmptySpace -> typeEmptySpace
                Row.TopCorner -> typeTopCorner
                is Row.Title -> typeTitle
                is Row.TextItem -> typeTextItem
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (getItemViewType(position) == typeTextItem) {
                (holder as TextItemViewHolder).bind(items[position] as Row.TextItem)
            }

            if (getItemViewType(position) == typeTitle) {
                (holder as TitleViewHolder).bind(items[position] as Row.Title)
            }
        }

        fun addItem(row: Row) {
            items.add(row)
            notifyItemInserted(items.lastIndex)
        }

        fun addItems(data: List<Row>) {
            items.addAll(data)
            notifyDataSetChanged()
        }

        operator fun get(position: Int) = items[position]


        class TitleViewHolder(
            private val root: BottomSheetTitleView
        ) : RecyclerView.ViewHolder(root as View) {
            fun bind(item: Row.Title) {
                root.bind(item.title)
            }
        }

        class TextItemViewHolder(
            private val view: TextView,
            onItemClick: ((position: Int, view: View) -> Unit)?
        ) : RecyclerView.ViewHolder(view) {

            init {
                view.setOnClickListener {
                    onItemClick?.invoke(adapterPosition, view)
                }
            }

            fun bind(item: Row.TextItem) {
                view.text = item.text
            }

        }

        class EmptyViewHolder(
            private val view: View,
            private val onItemClick: ((position: Int, view: View) -> Unit)? = null
        ) : RecyclerView.ViewHolder(view) {

            init {
                view.setOnClickListener {
                    onItemClick?.invoke(adapterPosition, view)
                }
            }

        }
    }


    class TopCornerView(context: Context) : View(context) {
        private val bg: MaterialShapeDrawable
        private val handle: MaterialShapeDrawable

        init {

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                24.dp
            )

            bg = materialShape {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                shapeAppearanceModel = ShapeAppearanceModel.Builder()
                    .setTopRightCornerSize(20.dpf)
                    .setTopLeftCornerSize(20.dpf)
                    .build()
            }

            handle = materialShape {
                setCornerSize(2.dpf)
                fillColor = ColorStateList.valueOf(Color.LTGRAY)
            }
        }


        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            bg.setBounds(0, 8.dp, w, h)
            val handleHorizontalPosition = (w - 32.dp).div(2)
            handle.setBounds(
                handleHorizontalPosition,
                20.dp,
                handleHorizontalPosition + 32.dp,
                24.dp
            )
        }


        override fun onDraw(canvas: Canvas) {
            bg.draw(canvas)
            handle.draw(canvas)
        }
    }

    class StickyHeaderItemDecorator : RecyclerView.ItemDecoration() {
        private var header: BottomSheetTitleView? = null
        private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 1.dpf
            style = Paint.Style.STROKE
            color = Color.LTGRAY
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
            val layoutManager = parent.layoutManager ?: return
            if (layoutManager.itemCount >= 3) {
                val headerView = layoutManager.findViewByPosition(2)
                if (header == null && headerView is BottomSheetTitleView) {
                    header = headerView
                }


                val top: Int = if (headerView is BottomSheetTitleView) headerView.top else 0
                if (top <= 0) {
                    c.save()
                    c.translate(0f, 0f)
                    header!!.draw(c)
                    c.restore()
                }

                if (top < 24.dp && header != null) {
                    val progress = if (top >= 0) (24.dp - top.coerceAtLeast(0)) / 24.dpf else 1f
                    c.save()
                    c.translate(0f, header!!.height.toFloat())
                    linePaint.alpha = (255f * progress).toInt()
                    c.drawLine(0f, 0f, c.width.toFloat(), 0f, linePaint)
                    c.restore()
                }
            }

        }
    }

    @SuppressLint("ViewConstructor")
    class BottomSheetTitleView(context: Context) :
        FrameLayout(context) {

        private val textView = TextView(context).apply {
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.teal_700))
            typeface = FontUtils.getBoldFont(context)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
        }

        init {
            addView(textView, wrapWidthCustomHeight(30) {
                gravity = Gravity.CENTER
                rightMargin = 16.dp
                leftMargin = 16.dp
            })
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(72.dp, MeasureSpec.EXACTLY)
            )
        }


        fun bind(text: String) {
            textView.text = text
        }

    }

}