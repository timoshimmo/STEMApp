package app.com.android.ihsteachers.providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import tm.charlie.expandabletextview.ExpandableTextView;

public class PicassoImageGetterJava implements Html.ImageGetter {

    private ExpandableTextView textView = null;
    private Context context;

    public PicassoImageGetterJava() {

    }

    public PicassoImageGetterJava(ExpandableTextView target, Context ctx) {
        textView = target;
        context = ctx;
    }

    @Override
    public Drawable getDrawable(String source) {

        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();

        if(source.contains("ihs_stemapp")) {
            String fullPath = "http://stemapp.com.ng/ihs-teachers-stem/"+source;

            Picasso.get()
                    .load(fullPath)
                    .into(drawable);
        }
        else {
            Picasso.get()
                    .load(source)
                    .into(drawable);
        }

        return drawable;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, width, height);
            setBounds(0, 0, width, height);
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(context.getResources(), bitmap));

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
