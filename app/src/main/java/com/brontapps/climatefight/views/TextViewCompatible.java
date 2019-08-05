package com.brontapps.climatefight.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.brontapps.climatefight.R;

/**
 * Creadito con cariño por alexb el día 04/01/2018.
 */

public class TextViewCompatible extends AppCompatTextView {
    public TextViewCompatible(Context context) {
        super(context);
    }
    public TextViewCompatible(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }
    public TextViewCompatible(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs, R.styleable.TextViewCompatible);

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableLeft = attributeArray.getDrawable(R.styleable.TextViewCompatible_drawableLeftCompat);
                drawableRight = attributeArray.getDrawable(R.styleable.TextViewCompatible_drawableRightCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.TextViewCompatible_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.TextViewCompatible_drawableTopCompat);

            } else {
                final int drawableLeftId = attributeArray.getResourceId(R.styleable.TextViewCompatible_drawableLeftCompat, -1);
                final int drawableRightId = attributeArray.getResourceId(R.styleable.TextViewCompatible_drawableRightCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.TextViewCompatible_drawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.TextViewCompatible_drawableTopCompat, -1);

                if (drawableLeftId != -1)
                    drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
                if (drawableRightId != -1)
                    drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }

            int color = attributeArray.getColor(R.styleable.TextViewCompatible_drawableTintCompat, -1);
            if(color != -1) {
                if (drawableLeft != null){
                    drawableLeft = DrawableCompat.wrap(drawableLeft).mutate();
                    DrawableCompat.setTint(drawableLeft, color);
                }
                if (drawableRight != null){
                    drawableRight = DrawableCompat.wrap(drawableRight).mutate();
                    DrawableCompat.setTint(drawableRight, color);
                }
                if (drawableTop != null){
                    drawableTop = DrawableCompat.wrap(drawableTop).mutate();
                    DrawableCompat.setTint(drawableTop, color);
                }
                if (drawableBottom != null){
                    drawableBottom = DrawableCompat.wrap(drawableBottom).mutate();
                    DrawableCompat.setTint(drawableBottom, color);
                }
            }
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);

            attributeArray.recycle();
        }
    }
}