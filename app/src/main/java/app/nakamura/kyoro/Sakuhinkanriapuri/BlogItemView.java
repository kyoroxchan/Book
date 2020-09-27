package app.nakamura.kyoro.Sakuhinkanriapuri;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * View for a {@link Blog} model.
 */
public class BlogItemView extends RelativeLayout {
    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.nameTextView)
    TextView nameTextView;

    @Bind(R.id.tagTextView)
    TextView tagTextView;

    @Bind(R.id.dateTextView)
    TextView dateTextView;

    @Bind(R.id.imageView)
    ImageView imageView;

    public BlogItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.list_item, this);
        ButterKnife.bind(this);
    }

    public void bind(final Book book, final View.OnClickListener listener) {
        container.setOnClickListener(listener);
        nameTextView.setText(book.getName());
        tagTextView.setText(book.getTag());
        dateTextView.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE).format(book.getCreatedAt()));
        imageView.setImageAlpha(book.getImageId());
    }
}