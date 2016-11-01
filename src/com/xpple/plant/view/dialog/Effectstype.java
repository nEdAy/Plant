package com.xpple.plant.view.dialog;

import com.xpple.plant.view.dialog.effects.BaseEffects;
import com.xpple.plant.view.dialog.effects.DialogCancel;
import com.xpple.plant.view.dialog.effects.FadeIn;
import com.xpple.plant.view.dialog.effects.Fall;
import com.xpple.plant.view.dialog.effects.FlipH;
import com.xpple.plant.view.dialog.effects.FlipV;
import com.xpple.plant.view.dialog.effects.NewsPaper;
import com.xpple.plant.view.dialog.effects.RotateBottom;
import com.xpple.plant.view.dialog.effects.RotateLeft;
import com.xpple.plant.view.dialog.effects.Shake;
import com.xpple.plant.view.dialog.effects.SideFall;
import com.xpple.plant.view.dialog.effects.SlideBottom;
import com.xpple.plant.view.dialog.effects.SlideLeft;
import com.xpple.plant.view.dialog.effects.SlideRight;
import com.xpple.plant.view.dialog.effects.SlideTop;
import com.xpple.plant.view.dialog.effects.Slit;

/**
 * Created by lee on 2014/7/30.
 */
public enum Effectstype {
	DialogCancel(DialogCancel.class), Fadein(FadeIn.class), Slideleft(
			SlideLeft.class), Slidetop(SlideTop.class), SlideBottom(
			SlideBottom.class), Slideright(SlideRight.class), Fall(Fall.class), Newspager(
			NewsPaper.class), Fliph(FlipH.class), Flipv(FlipV.class), RotateBottom(
			RotateBottom.class), RotateLeft(RotateLeft.class), Slit(Slit.class), Shake(
			Shake.class), Sidefill(SideFall.class);
	private Class<? extends BaseEffects> effectsClazz;

	private Effectstype(Class<? extends BaseEffects> mclass) {
		effectsClazz = mclass;
	}

	public BaseEffects getAnimator() {
		BaseEffects bEffects = null;
		try {
			bEffects = effectsClazz.newInstance();
		} catch (ClassCastException e) {
			throw new Error("Can not init animatorClazz instance");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new Error("Can not init animatorClazz instance");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new Error("Can not init animatorClazz instance");
		}
		return bEffects;
	}
}
