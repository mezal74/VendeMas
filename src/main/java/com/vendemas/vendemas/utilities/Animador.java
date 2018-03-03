package com.vendemas.vendemas.utilities;

import android.support.annotation.Nullable;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by Rick on 27/11/2016.
 */

public class Animador {

    public static void fade (View target, long duracion, long delay, float valorInicio, float valorFinal, @Nullable Animator.AnimatorListener listener){
        target.setAlpha(valorInicio);
        ObjectAnimator animacion = new ObjectAnimator();
        animacion.setTarget(target);
        animacion.setPropertyName("alpha");
        animacion.setDuration(duracion);
        animacion.setFloatValues(valorFinal);
        animacion.setStartDelay(delay);
        if(listener != null)
            animacion.addListener(listener);
        animacion.start();
    }

    public static void shrink (View target, String axis, long duracion, long delay, float valorInicio, float valorFinal, @Nullable Animator.AnimatorListener listener) {
        ObjectAnimator animacion = new ObjectAnimator();
        animacion.setTarget(target);
        animacion.setPropertyName("scale"+axis);
        animacion.setDuration(duracion);
        animacion.setFloatValues(valorInicio, valorFinal);
        animacion.setStartDelay(delay);
        if(listener != null)
            animacion.addListener(listener);
        animacion.start();
    }

    public static void rotate (View target, boolean sense /*T=right; F=left*/, long duracion, long delay, float valorInicio, float valorFinal, @Nullable Animator.AnimatorListener listener){
        ObjectAnimator animacion = new ObjectAnimator();
        animacion.setTarget(target);
        animacion.setPropertyName("rotation");
        animacion.setDuration(duracion);
        animacion.setFloatValues(valorInicio, valorFinal);
        animacion.setStartDelay(delay);
        if(listener != null)
            animacion.addListener(listener);
        animacion.start();
    }

    public static void move(View target, String axis, int duracion, int delay, @Nullable Animator.AnimatorListener listener) {
        ObjectAnimator animacion = new ObjectAnimator();
        animacion.setTarget(target);
        animacion.setPropertyName("translation"+axis);
        animacion.setDuration(duracion);
        animacion.setFloatValues(-target.getHeight());
        animacion.setStartDelay(delay);
        if(listener != null)
            animacion.addListener(listener);
        animacion.start();
    }

    public static void moveDown(View target, String axis, int duracion, int delay, @Nullable Animator.AnimatorListener listener) {
        ObjectAnimator animacion = new ObjectAnimator();
        animacion.setTarget(target);
        animacion.setPropertyName("translation"+axis);
        animacion.setDuration(duracion);
        animacion.setFloatValues(0);
        animacion.setStartDelay(delay);
        if(listener != null)
            animacion.addListener(listener);
        animacion.start();
    }

    public static void moveTo(View target, String axis, int duracion, int delay, float valorInicio, float valorFinal, @Nullable Animator.AnimatorListener listener) {
        ObjectAnimator animacion = new ObjectAnimator();
        animacion.setTarget(target);
        animacion.setPropertyName("translation"+axis);
        animacion.setDuration(duracion);
        animacion.setFloatValues(valorInicio, valorFinal);
        animacion.setStartDelay(delay);
        if(listener != null)
            animacion.addListener(listener);
        animacion.start();
    }
}
